package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.model.ReminderStatus
import com.agenda.app.domain.repository.ReminderRepository
import com.agenda.app.domain.service.AlarmSchedulerContract
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CreateReminderUseCaseTest {

    private val repository: ReminderRepository = mockk(relaxed = true)
    private val alarmScheduler: AlarmSchedulerContract = mockk(relaxed = true)
    private val useCase = CreateReminderUseCase(repository, alarmScheduler)

    @Test
    fun `invoke should insert reminder and schedule alarm`() = runTest {
        val reminder = Reminder(
            "1", "cat1", "Test", "Test", LocalDateTime.now(), 
            ReminderStatus.PENDING, null, LocalDateTime.now(), LocalDateTime.now(), null
        )

        val result = useCase(reminder)

        coVerify { repository.insert(reminder) }
        verify { alarmScheduler.schedule(reminder) }
        assertTrue(result is AppResult.Success)
    }
}
