package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(): Flow<List<Reminder>> {
        return reminderRepository.getPending()
    }
}
