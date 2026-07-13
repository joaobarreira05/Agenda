package com.agenda.app.data.parser

import com.agenda.app.domain.model.Category
import com.agenda.app.domain.model.ParsedVoiceInput
import com.agenda.app.domain.parser.DateTimeExtractor
import com.agenda.app.domain.parser.TextParserContract
import javax.inject.Inject

class TextParserImpl @Inject constructor(
    private val dateTimeExtractor: DateTimeExtractor,
    private val categoryMatcher: CategoryMatcher
) : TextParserContract {

    override fun parse(text: String, categories: List<Category>): ParsedVoiceInput {
        // 1. Extract Date and Time
        val (dateTime, textAfterDateTime) = dateTimeExtractor.extract(text)

        // 2. Extract Category
        val (category, textAfterCategory) = categoryMatcher.matchCategory(textAfterDateTime, categories)

        // 3. Clean up the remaining text for the description
        var description = textAfterCategory
            .replace("lembra-me de", "", ignoreCase = true)
            .replace("lembrar de", "", ignoreCase = true)
            .replace("para", "", ignoreCase = true) // Common stopword left over
            .trim()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        if (description.isBlank()) {
            description = "Lembrete sem descrição"
        }

        return ParsedVoiceInput(
            categoryName = category?.name,
            description = description,
            dateTime = dateTime,
            rawText = text
        )
    }
}
