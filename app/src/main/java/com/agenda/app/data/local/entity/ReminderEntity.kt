package com.agenda.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.agenda.app.domain.model.ReminderStatus
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("categoryId")]
)
data class ReminderEntity(
    @PrimaryKey val id: String,
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
