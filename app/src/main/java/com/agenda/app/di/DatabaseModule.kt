package com.agenda.app.di

import android.content.Context
import androidx.room.Room
import com.agenda.app.data.local.AgendaDatabase
import com.agenda.app.data.local.dao.CategoryDao
import com.agenda.app.data.local.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AgendaDatabase {
        return Room.databaseBuilder(
            context,
            AgendaDatabase::class.java,
            AgendaDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCategoryDao(database: AgendaDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideReminderDao(database: AgendaDatabase): ReminderDao = database.reminderDao()
}
