package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.AppError
import com.agenda.app.domain.model.AppResult
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.repository.ReminderRepository
import com.agenda.app.domain.service.AlarmSchedulerContract
import javax.inject.Inject

class UpdateReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmSchedulerContract
) {
    suspend operator fun invoke(reminder: Reminder): AppResult<Unit> {
        return try {
            val oldReminder = reminderRepository.getById(reminder.id)
            reminderRepository.update(reminder)
            
            if (oldReminder?.dateTime != reminder.dateTime || oldReminder.status != reminder.status) {
                alarmScheduler.cancel(reminder.id)
                alarmScheduler.schedule(reminder)
            }
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
