package com.agenda.app.domain.usecase.category

import com.agenda.app.domain.model.Category
import com.agenda.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAll()
    }
}
