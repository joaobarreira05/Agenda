package com.agenda.app.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.usecase.category.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.agenda.app.domain.usecase.category.CreateCategoryUseCase
import com.agenda.app.domain.usecase.category.DeleteCategoryUseCase
import java.time.LocalDateTime
import java.util.UUID

import com.agenda.app.domain.usecase.category.UpdateCategoryUseCase

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        loadCategories()
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

    fun addCategory(name: String, color: String) {
        viewModelScope.launch {
            val maxSortOrder = _categories.value.maxOfOrNull { it.sortOrder } ?: 0
            val newCategory = Category(
                id = UUID.randomUUID().toString(),
                name = name,
                color = color,
                icon = "ic_folder",
                sortOrder = maxSortOrder + 1,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            createCategoryUseCase(newCategory)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            deleteCategoryUseCase(category.id)
        }
    }

    fun updateCategory(category: Category, newName: String, newColor: String) {
        viewModelScope.launch {
            val updated = category.copy(
                name = newName,
                color = newColor,
                updatedAt = LocalDateTime.now()
            )
            updateCategoryUseCase(updated)
        }
    }
}
