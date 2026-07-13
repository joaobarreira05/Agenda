package com.agenda.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.usecase.reminder.GetPendingRemindersUseCase
import com.agenda.app.domain.usecase.reminder.CompleteReminderUseCase
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.usecase.category.GetCategoriesUseCase
import com.agenda.app.domain.usecase.category.CreateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPendingRemindersUseCase: GetPendingRemindersUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val completeReminderUseCase: CompleteReminderUseCase
) : ViewModel() {

    private val _allReminders = MutableStateFlow<List<Reminder>>(emptyList())
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    init {
        viewModelScope.launch {
            initializeCategoriesIfEmpty()
            loadCategories()
            loadReminders()
            
            launch {
                kotlinx.coroutines.flow.combine(_allReminders, _selectedCategoryId) { all, selectedId ->
                    if (selectedId == null) all else all.filter { it.categoryId == selectedId }
                }.collect { filtered ->
                    _reminders.value = filtered
                }
            }
        }
    }
    
    fun selectCategory(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    private suspend fun initializeCategoriesIfEmpty() {
        val categories = getCategoriesUseCase().firstOrNull()
        if (categories.isNullOrEmpty()) {
            val defaults = listOf(
                Category(UUID.randomUUID().toString(), "Trabalho", "#1E88E5", null, 0, LocalDateTime.now(), LocalDateTime.now()),
                Category(UUID.randomUUID().toString(), "Pessoal", "#43A047", null, 1, LocalDateTime.now(), LocalDateTime.now()),
                Category(UUID.randomUUID().toString(), "Compras", "#EF6C00", null, 2, LocalDateTime.now(), LocalDateTime.now())
            )
            defaults.forEach { createCategoryUseCase(it) }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { /* Handle error */ }
                .collect { list ->
                    _categories.value = list
                }
        }
    }

    private fun loadReminders() {
        viewModelScope.launch {
            getPendingRemindersUseCase()
                .catch { /* Handle error */ }
                .collect { list ->
                    _allReminders.value = list
                }
        }
    }

    private val _selectedReminderIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedReminderIds: StateFlow<Set<String>> = _selectedReminderIds

    fun toggleReminderSelection(reminderId: String) {
        val current = _selectedReminderIds.value.toMutableSet()
        if (current.contains(reminderId)) {
            current.remove(reminderId)
        } else {
            current.add(reminderId)
        }
        _selectedReminderIds.value = current
    }

    fun getSelectedRemindersWithFutureAlarms(): Int {
        val now = LocalDateTime.now()
        return _allReminders.value.count { it.id in _selectedReminderIds.value && it.dateTime.isAfter(now) }
    }

    fun completeSelectedReminders() {
        viewModelScope.launch {
            _selectedReminderIds.value.forEach { id ->
                completeReminderUseCase(id)
            }
            _selectedReminderIds.value = emptySet()
        }
    }
}
