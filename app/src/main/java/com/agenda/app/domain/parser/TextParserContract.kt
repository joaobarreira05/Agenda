package com.agenda.app.domain.parser

import com.agenda.app.domain.model.Category
import com.agenda.app.domain.model.ParsedVoiceInput

/**
 * Contract for parsing natural language voice input into structured data.
 */
interface TextParserContract {
    /**
     * Parses the given [text] to extract category, date/time, and description.
     * Uses the provided [categories] for fuzzy matching.
     */
    fun parse(text: String, categories: List<Category>): ParsedVoiceInput
}
