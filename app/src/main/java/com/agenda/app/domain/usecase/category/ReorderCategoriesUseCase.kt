package com.agenda.app.domain.usecase.category

import com.agenda.app.domain.model.AppError
import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.repository.CategoryRepository
import javax.inject.Inject

class ReorderCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categories: List<Category>): AppResult<Unit> {
        return try {
            categoryRepository.updateSortOrders(categories)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
