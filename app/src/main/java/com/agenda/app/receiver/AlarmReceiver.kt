package com.agenda.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receives alarm intents from [AlarmManager] and displays a notification
 * for the corresponding reminder.
 *
 * Implemented fully in Phase 9 (Alarmes) and Phase 10 (Notificações).
 */
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.agenda.app.domain.usecase.reminder.GetReminderByIdUseCase
import com.agenda.app.presentation.notification.NotificationHelper

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getReminderByIdUseCase: GetReminderByIdUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("EXTRA_REMINDER_ID") ?: return
        
        val pendingResult = goAsync()
        
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val reminder = getReminderByIdUseCase(reminderId)
                if (reminder != null && reminder.status != com.agenda.app.domain.model.ReminderStatus.COMPLETED) {
                    notificationHelper.showReminderNotification(reminder)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
