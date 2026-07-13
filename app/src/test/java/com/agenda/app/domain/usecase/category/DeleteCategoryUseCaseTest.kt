package com.agenda.app.domain.usecase.category

import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.repository.CategoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeleteCategoryUseCaseTest {

    private val repository: CategoryRepository = mockk(relaxed = true)
    private val useCase = DeleteCategoryUseCase(repository)

    @Test
    fun `invoke should delete category`() = runTest {
        val id = "cat_1"
        val result = useCase(id)
        coVerify { repository.delete(id) }
        assertTrue(result is AppResult.Success)
    }
}
