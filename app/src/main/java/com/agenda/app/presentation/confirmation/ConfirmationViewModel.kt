package com.agenda.app.presentation.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.model.ReminderStatus
import com.agenda.app.domain.parser.TextParserContract
import com.agenda.app.domain.service.AlarmSchedulerContract
import com.agenda.app.domain.usecase.category.GetCategoriesUseCase
import com.agenda.app.domain.usecase.reminder.CreateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import com.agenda.app.domain.usecase.reminder.GetReminderByIdUseCase
import com.agenda.app.domain.usecase.reminder.UpdateReminderUseCase

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val textParser: TextParserContract,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createReminderUseCase: CreateReminderUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val alarmScheduler: AlarmSchedulerContract
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfirmationUiState>(ConfirmationUiState.Loading)
    val uiState: StateFlow<ConfirmationUiState> = _uiState
    
    private var reminderIdToEdit: String? = null

    fun loadData(transcribedText: String?, reminderId: String?) {
        viewModelScope.launch {
            val categories = getCategoriesUseCase().firstOrNull() ?: emptyList()
            
            if (reminderId != null) {
                reminderIdToEdit = reminderId
                val reminder = getReminderByIdUseCase(reminderId)
                if (reminder != null) {
                    _uiState.value = ConfirmationUiState.Success(
                        transcribedText = reminder.recognizedText ?: "",
                        description = reminder.description,
                        categoryId = reminder.categoryId,
                        categories = categories,
                        dateTime = reminder.dateTime
                    )
                }
            } else if (transcribedText != null) {
                val parsed = textParser.parse(transcribedText, categories)
                val matchedCategoryId = categories.find { it.name.equals(parsed.categoryName, ignoreCase = true) }?.id 
                    ?: categories.firstOrNull()?.id ?: UUID.randomUUID().toString()

                _uiState.value = ConfirmationUiState.Success(
                    transcribedText = transcribedText,
                    description = parsed.description,
                    categoryId = matchedCategoryId,
                    categories = categories,
                    dateTime = parsed.dateTime ?: LocalDateTime.now().plusHours(1)
                )
            }
        }
    }

    fun saveReminder(description: String, categoryId: String, dateTime: LocalDateTime) {
        viewModelScope.launch {
            val currentState = _uiState.value as? ConfirmationUiState.Success ?: return@launch
            
            if (reminderIdToEdit != null) {
                val existing = getReminderByIdUseCase(reminderIdToEdit!!)
                if (existing != null) {
                    val updated = existing.copy(
                        description = description,
                        categoryId = categoryId,
                        dateTime = dateTime,
                        updatedAt = LocalDateTime.now()
                    )
                    updateReminderUseCase(updated)
                    alarmScheduler.schedule(updated)
                }
            } else {
                val reminder = Reminder(
                    id = UUID.randomUUID().toString(),
                    categoryId = categoryId,
                    description = description,
                    recognizedText = currentState.transcribedText,
                    dateTime = dateTime,
                    status = ReminderStatus.PENDING,
                    audioFilePath = null,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    completedAt = null
                )
                
                createReminderUseCase(reminder)
                alarmScheduler.schedule(reminder) // Ensure the alarm gets scheduled
            }
            
            _uiState.value = ConfirmationUiState.Saved
        }
    }
}

sealed class ConfirmationUiState {
    object Loading : ConfirmationUiState()
    data class Success(
        val transcribedText: String,
        val description: String,
        val categoryId: String,
        val categories: List<Category>,
        val dateTime: LocalDateTime
    ) : ConfirmationUiState()
    object Saved : ConfirmationUiState()
}
