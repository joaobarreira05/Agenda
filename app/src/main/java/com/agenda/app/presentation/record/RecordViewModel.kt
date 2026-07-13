package com.agenda.app.presentation.record

import androidx.lifecycle.ViewModel
import com.agenda.app.domain.service.AudioRecorderContract
import androidx.lifecycle.viewModelScope
import com.agenda.app.domain.model.Category
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.model.ReminderStatus
import com.agenda.app.domain.parser.TextParserContract
import com.agenda.app.domain.usecase.category.GetCategoriesUseCase
import com.agenda.app.domain.usecase.reminder.CreateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val audioRecorder: AudioRecorderContract,
    private val textParser: TextParserContract,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createReminderUseCase: CreateReminderUseCase
) : ViewModel() {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording
    
    private val _saveStatus = MutableStateFlow<Boolean?>(null)
    val saveStatus: StateFlow<Boolean?> = _saveStatus

    private val _transcriptionResult = MutableStateFlow<String?>(null)
    val transcriptionResult: StateFlow<String?> = _transcriptionResult

    fun setRecordingState(isRecording: Boolean) {
        _isRecording.value = isRecording
    }

    fun processRecordingAndSave(transcribedText: String) {
        // Now instead of saving, we just emit the text to navigate to ConfirmationFragment
        _transcriptionResult.value = transcribedText
    }
    
    fun clearTranscriptionResult() {
        _transcriptionResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        if (isRecording.value) {
            audioRecorder.stopRecording()
        }
    }
}
