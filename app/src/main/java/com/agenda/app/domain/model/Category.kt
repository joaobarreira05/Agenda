package com.agenda.app.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a Category.
 */
data class Category(
    val id: String,
    val name: String,
    val color: String,
    val icon: String?,
    val sortOrder: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
