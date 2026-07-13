package com.agenda.app.data.repository

import com.agenda.app.data.local.dao.ReminderDao
import com.agenda.app.data.mapper.toDomain
import com.agenda.app.data.mapper.toEntity
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getAll(): Flow<List<Reminder>> {
        return reminderDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getByCategory(categoryId: String): Flow<List<Reminder>> {
        return reminderDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override fun getUpcoming(limit: Int): Flow<List<Reminder>> {
        val nowMillis = Instant.now().toEpochMilli()
        return reminderDao.getUpcoming(limit, nowMillis).map { list -> list.map { it.toDomain() } }
    }

    override fun getPending(): Flow<List<Reminder>> {
        return reminderDao.getPending().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: String): Reminder? {
        return reminderDao.getById(id)?.toDomain()
    }

    override suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder.toEntity())
    }

    override suspend fun update(reminder: Reminder) {
        reminderDao.update(reminder.toEntity())
    }

    override suspend fun delete(id: String) {
        reminderDao.delete(id)
    }

    override suspend fun markCompleted(id: String) {
        val nowMillis = Instant.now().toEpochMilli()
        reminderDao.markCompleted(id, nowMillis)
    }
}
