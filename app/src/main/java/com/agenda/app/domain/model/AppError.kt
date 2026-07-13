package com.agenda.app.domain.model

/**
 * A sealed class representing a domain error.
 */
sealed class AppError {
    data class DatabaseError(val message: String) : AppError()
    data class VoiceRecognitionError(val code: Int) : AppError()
    data class ParsingError(val field: String, val rawText: String) : AppError()
    data class AlarmError(val message: String) : AppError()
    data class PermissionDenied(val permission: String) : AppError()
    data class Unknown(val throwable: Throwable) : AppError()
}
