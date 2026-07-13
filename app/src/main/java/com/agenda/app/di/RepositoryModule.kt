package com.agenda.app.di

import com.agenda.app.data.repository.CategoryRepositoryImpl
import com.agenda.app.data.repository.ReminderRepositoryImpl
import com.agenda.app.domain.repository.CategoryRepository
import com.agenda.app.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindReminderRepository(
        reminderRepositoryImpl: ReminderRepositoryImpl
    ): ReminderRepository
}
