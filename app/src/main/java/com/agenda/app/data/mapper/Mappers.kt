package com.agenda.app.data.mapper

import com.agenda.app.data.local.entity.CategoryEntity
import com.agenda.app.data.local.entity.ReminderEntity
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.model.Reminder

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    color = color,
    icon = icon,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    color = color,
    icon = icon,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ReminderEntity.toDomain() = Reminder(
    id = id,
    categoryId = categoryId,
    description = description,
    recognizedText = recognizedText,
    dateTime = dateTime,
    status = status,
    audioFilePath = audioFilePath,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)

fun Reminder.toEntity() = ReminderEntity(
    id = id,
    categoryId = categoryId,
    description = description,
    recognizedText = recognizedText,
    dateTime = dateTime,
    status = status,
    audioFilePath = audioFilePath,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)
