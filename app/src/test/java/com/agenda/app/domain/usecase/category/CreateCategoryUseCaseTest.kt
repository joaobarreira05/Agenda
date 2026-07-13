package com.agenda.app.domain.usecase.category

import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.repository.CategoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CreateCategoryUseCaseTest {

    private val repository: CategoryRepository = mockk(relaxed = true)
    private val useCase = CreateCategoryUseCase(repository)

    @Test
    fun `invoke should return error when name is empty`() = runTest {
        val category = Category("1", "   ", "#FFF", null, 0, LocalDateTime.now(), LocalDateTime.now())
        val result = useCase(category)
        assertTrue(result is AppResult.Error)
    }

    @Test
    fun `invoke should insert category when valid`() = runTest {
        val category = Category("1", "Trabalho", "#FFF", null, 0, LocalDateTime.now(), LocalDateTime.now())
        val result = useCase(category)
        coVerify { repository.insert(category) }
        assertTrue(result is AppResult.Success)
    }
}
