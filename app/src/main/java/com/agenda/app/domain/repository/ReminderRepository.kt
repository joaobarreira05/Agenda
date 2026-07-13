package com.agenda.app.domain.repository

import com.agenda.app.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for managing [Reminder] data.
 */
interface ReminderRepository {
    fun getAll(): Flow<List<Reminder>>
    fun getByCategory(categoryId: String): Flow<List<Reminder>>
    fun getUpcoming(limit: Int): Flow<List<Reminder>>
    suspend fun getById(id: String): Reminder?
    fun getPending(): Flow<List<Reminder>>
    suspend fun insert(reminder: Reminder)
    suspend fun update(reminder: Reminder)
    suspend fun delete(id: String)
    suspend fun markCompleted(id: String)
}
