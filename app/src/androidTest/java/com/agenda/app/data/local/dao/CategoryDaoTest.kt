package com.agenda.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agenda.app.data.local.AgendaDatabase
import com.agenda.app.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var database: AgendaDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AgendaDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.categoryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetCategory() = runTest {
        val category = CategoryEntity("1", "Trabalho", "#FFF", null, 0, LocalDateTime.now(), LocalDateTime.now())
        dao.insert(category)

        val retrieved = dao.getById("1")
        assertEquals("Trabalho", retrieved?.name)
    }
}
