package com.agenda.app.data.parser

import com.agenda.app.domain.parser.DateTimeExtractor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class DateTimeExtractorImpl @Inject constructor() : DateTimeExtractor {

    override fun extract(text: String): Pair<LocalDateTime?, String> {
        var resultText = text.lowercase()
        var date: LocalDate? = null
        var time: LocalTime? = null

        // Extraction of days
        if (resultText.contains("amanhã")) {
            date = LocalDate.now().plusDays(1)
            resultText = resultText.replace("amanhã", "")
        } else if (resultText.contains("hoje")) {
            date = LocalDate.now()
            resultText = resultText.replace("hoje", "")
        }

        // Smart time extraction: "às 14", "às 14h30", "das 11:30", "por volta das 11:30", "11:30"
        val timeRegex = "(?:por volta das|por volta de|às|das|pelas)?\\s*(\\d{1,2})[:h](\\d{2})|(?:por volta das|por volta de|às|das|pelas)\\s+(\\d{1,2})".toRegex()
        val timeMatch = timeRegex.find(resultText)
        if (timeMatch != null) {
            val hourStr = timeMatch.groupValues[1].takeIf { it.isNotBlank() } ?: timeMatch.groupValues[3]
            val hour = hourStr.toInt()
            val minuteStr = timeMatch.groupValues[2]
            val minute = if (minuteStr.isNotBlank()) minuteStr.toInt() else 0
            time = LocalTime.of(hour, minute)
            resultText = resultText.replace(timeMatch.value, "")
        }

        val dateTime = if (date != null || time != null) {
            LocalDateTime.of(date ?: LocalDate.now(), time ?: LocalTime.of(9, 0))
        } else {
            null
        }

        return Pair(dateTime, resultText.trim().replace("\\s+".toRegex(), " "))
    }
}
