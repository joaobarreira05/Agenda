package com.agenda.app.domain.model

import java.time.LocalDateTime

/**
 * Represents the parsed output from the user's voice input.
 */
data class ParsedVoiceInput(
    val categoryName: String?,
    val description: String,
    val dateTime: LocalDateTime?,
    val rawText: String
)
