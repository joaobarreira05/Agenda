package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.AppError
import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.repository.ReminderRepository
import com.agenda.app.domain.service.AlarmSchedulerContract
import javax.inject.Inject

class CompleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmSchedulerContract
) {
    suspend operator fun invoke(id: String): AppResult<Unit> {
        return try {
            reminderRepository.markCompleted(id)
            alarmScheduler.cancel(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
