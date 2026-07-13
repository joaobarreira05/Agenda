package com.agenda.app.presentation.record.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class VoiceWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF1E88E5")
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    private var amplitudes = FloatArray(30)
    private var isRecording = false

    fun startAnimation() {
        isRecording = true
        invalidate()
    }

    fun stopAnimation() {
        isRecording = false
        amplitudes = FloatArray(30)
        invalidate()
    }

    fun updateAmplitude(amplitude: Float) {
        if (!isRecording) return
        
        // Shift amplitudes left
        for (i in 0 until amplitudes.size - 1) {
            amplitudes[i] = amplitudes[i + 1]
        }
        
        // Normalize amplitude (assuming max is around 32767)
        val normalized = (amplitude / 32767f).coerceIn(0f, 1f)
        amplitudes[amplitudes.size - 1] = normalized
        
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerY = height / 2f
        val spacing = width / amplitudes.size.toFloat()
        
        for (i in amplitudes.indices) {
            val x = i * spacing + spacing / 2
            // Randomly animate if recording to give life when no amplitude is fed,
            // or just use the actual amplitude data
            val h = if (isRecording && amplitudes[i] == 0f) {
                Random.nextFloat() * 20f + 10f
            } else {
                amplitudes[i] * height / 2f + 10f
            }
            canvas.drawLine(x, centerY - h, x, centerY + h, paint)
        }
        
        if (isRecording) {
            postInvalidateDelayed(50) // roughly 20fps for random default animation
        }
    }
}
