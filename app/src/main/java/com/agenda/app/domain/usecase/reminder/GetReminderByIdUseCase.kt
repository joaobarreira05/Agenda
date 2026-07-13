package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.repository.ReminderRepository
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(id: String): Reminder? {
        return reminderRepository.getById(id)
    }
}
