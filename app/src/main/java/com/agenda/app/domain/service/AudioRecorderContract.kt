package com.agenda.app.domain.service

/**
 * Contract for recording audio input.
 */
interface AudioRecorderContract {
    /** Starts recording and saves to the given [filePath]. */
    fun startRecording(filePath: String)

    /** Stops recording and returns the path to the saved file, or null if failed. */
    fun stopRecording(): String?

    /** Returns whether a recording is currently in progress. */
    fun isRecording(): Boolean
}
