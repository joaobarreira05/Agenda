package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.AppError
import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.repository.ReminderRepository
import com.agenda.app.domain.service.AlarmSchedulerContract
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmSchedulerContract
) {
    suspend operator fun invoke(id: String): AppResult<Unit> {
        return try {
            alarmScheduler.cancel(id)
            reminderRepository.delete(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
