package com.agenda.app.presentation.alarm

import android.app.KeyguardManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agenda.app.databinding.ActivityAlarmBinding
import com.agenda.app.domain.model.ReminderStatus
import com.agenda.app.domain.usecase.reminder.CompleteReminderUseCase
import com.agenda.app.domain.usecase.reminder.GetReminderByIdUseCase
import com.agenda.app.domain.usecase.category.GetCategoriesUseCase
import com.agenda.app.presentation.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import com.agenda.app.domain.usecase.reminder.UpdateReminderUseCase
import com.agenda.app.domain.service.AlarmSchedulerContract

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    @Inject
    lateinit var getReminderByIdUseCase: GetReminderByIdUseCase

    @Inject
    lateinit var updateReminderUseCase: UpdateReminderUseCase

    @Inject
    lateinit var completeReminderUseCase: CompleteReminderUseCase

    @Inject
    lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var alarmScheduler: AlarmSchedulerContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        turnScreenOnAndKeyguardOff()

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reminderId = intent.getStringExtra("EXTRA_REMINDER_ID")
        if (reminderId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            val reminder = getReminderByIdUseCase(reminderId)
            if (reminder == null || reminder.status == ReminderStatus.COMPLETED) {
                finish()
                return@launch
            }

            val categories = getCategoriesUseCase().firstOrNull() ?: emptyList()
            val category = categories.find { it.id == reminder.categoryId }

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            binding.tvAlarmTime.text = reminder.dateTime.format(timeFormatter)
            binding.tvAlarmTitle.text = reminder.description
            binding.tvAlarmCategory.text = category?.name?.uppercase() ?: "LEMBRETE"
            if (category != null) {
                try {
                    binding.tvAlarmCategory.setTextColor(android.graphics.Color.parseColor(category.color))
                } catch (e: Exception) {
                    // Ignore color parsing error
                }
            }

            binding.btnDismiss.setOnClickListener {
                lifecycleScope.launch {
                    stopAlarm()
                    finish()
                }
            }

            binding.btnSnooze.setOnClickListener {
                lifecycleScope.launch {
                    val snoozedReminder = reminder.copy(dateTime = reminder.dateTime.plusMinutes(10))
                    updateReminderUseCase(snoozedReminder)
                    alarmScheduler.schedule(snoozedReminder)
                    stopAlarm()
                    finish()
                }
            }

            startAlarm()
        }
    }

    private fun turnScreenOnAndKeyguardOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun startAlarm() {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@AlarmActivity, alarmUri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            val pattern = longArrayOf(0, 500, 500)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        vibrator?.cancel()
        
        val reminderId = intent.getStringExtra("EXTRA_REMINDER_ID")
        if (reminderId != null) {
            notificationHelper.cancelNotification(reminderId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }
}
