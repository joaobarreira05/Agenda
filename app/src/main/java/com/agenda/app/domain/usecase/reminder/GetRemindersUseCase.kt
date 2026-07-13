package com.agenda.app.domain.usecase.reminder

import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(categoryId: String? = null): Flow<List<Reminder>> {
        return if (categoryId != null) {
            reminderRepository.getByCategory(categoryId)
        } else {
            reminderRepository.getAll()
        }
    }
}
