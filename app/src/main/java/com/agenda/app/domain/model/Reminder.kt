package com.agenda.app.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a Reminder.
 */
data class Reminder(
    val id: String,
    val categoryId: String,
    val description: String,
    val recognizedText: String,
    val dateTime: LocalDateTime,
    val status: ReminderStatus,
    val audioFilePath: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val completedAt: LocalDateTime?
)
