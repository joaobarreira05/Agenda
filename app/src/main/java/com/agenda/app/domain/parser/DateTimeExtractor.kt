package com.agenda.app.domain.parser

import java.time.LocalDateTime

/**
 * Contract for extracting date and time from natural language text.
 */
interface DateTimeExtractor {
    /**
     * Extracts date and time from the [text].
     * @return A Pair containing the extracted [LocalDateTime] (if any) and the remaining text.
     */
    fun extract(text: String): Pair<LocalDateTime?, String>
}
