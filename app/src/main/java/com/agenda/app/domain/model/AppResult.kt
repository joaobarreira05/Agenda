package com.agenda.app.domain.model

/**
 * A generic class that holds a value with its loading status.
 * Used for domain layer results.
 */
sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
}
