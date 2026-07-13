package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.repository.ReminderRepository
import com.agenda.app.domain.service.AlarmSchedulerContract
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CompleteReminderUseCaseTest {

    private val repository: ReminderRepository = mockk(relaxed = true)
    private val alarmScheduler: AlarmSchedulerContract = mockk(relaxed = true)
    private val useCase = CompleteReminderUseCase(repository, alarmScheduler)

    @Test
    fun `invoke should mark as completed and cancel alarm`() = runTest {
        val id = "rem_1"

        val result = useCase(id)

        coVerify { repository.markCompleted(id) }
        verify { alarmScheduler.cancel(id) }
        assertTrue(result is AppResult.Success)
    }
}
