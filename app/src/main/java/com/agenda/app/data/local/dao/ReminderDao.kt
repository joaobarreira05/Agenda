package com.agenda.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.agenda.app.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY dateTime ASC")
    fun getAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE categoryId = :categoryId ORDER BY dateTime ASC")
    fun getByCategory(categoryId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE status = 'PENDING' AND dateTime > :now ORDER BY dateTime ASC LIMIT :limit")
    fun getUpcoming(limit: Int, now: Long): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE status = 'PENDING' ORDER BY dateTime ASC")
    fun getPending(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getById(id: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity)

    @Update
    suspend fun update(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE reminders SET status = 'COMPLETED', completedAt = :completedAt WHERE id = :id")
    suspend fun markCompleted(id: String, completedAt: Long)
}
