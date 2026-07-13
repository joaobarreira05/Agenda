package com.agenda.app.data.repository

import com.agenda.app.data.local.dao.CategoryDao
import com.agenda.app.data.mapper.toDomain
import com.agenda.app.data.mapper.toEntity
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: String): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun insert(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(id: String) {
        categoryDao.delete(id)
    }

    override suspend fun updateSortOrders(categories: List<Category>) {
        categories.forEach { categoryDao.update(it.toEntity()) }
    }
}
