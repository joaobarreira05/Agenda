package com.agenda.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String,
    val icon: String?,
    val sortOrder: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
