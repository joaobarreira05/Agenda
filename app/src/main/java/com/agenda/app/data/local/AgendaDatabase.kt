package com.agenda.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agenda.app.data.local.converter.Converters
import com.agenda.app.data.local.dao.CategoryDao
import com.agenda.app.data.local.dao.ReminderDao
import com.agenda.app.data.local.entity.CategoryEntity
import com.agenda.app.data.local.entity.ReminderEntity

@Database(
    entities = [CategoryEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AgendaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        const val DATABASE_NAME = "agenda_database"
    }
}
