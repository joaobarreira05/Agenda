package com.agenda.app.domain.repository

import com.agenda.app.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for managing [Category] data.
 */
interface CategoryRepository {
    fun getAll(): Flow<List<Category>>
    suspend fun getById(id: String): Category?
    suspend fun insert(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(id: String)
    suspend fun updateSortOrders(categories: List<Category>)
}
