package com.agenda.app.data.service

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.agenda.app.domain.service.AudioRecorderContract
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioRecorderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioRecorderContract {

    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var currentFilePath: String? = null

    override fun startRecording(filePath: String) {
        currentFilePath = filePath
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(filePath)
            prepare()
            start()
        }
        isRecording = true
    }

    override fun stopRecording(): String? {
        if (!isRecording) return null
        
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            isRecording = false
            currentFilePath
        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            isRecording = false
            null
        }
    }

    override fun isRecording(): Boolean = isRecording
}
