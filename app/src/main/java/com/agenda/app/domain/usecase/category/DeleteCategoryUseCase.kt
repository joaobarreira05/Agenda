package com.agenda.app.domain.usecase.category

import com.agenda.app.domain.model.AppError
import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: String): AppResult<Unit> {
        return try {
            categoryRepository.delete(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
