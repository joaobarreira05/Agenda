package com.agenda.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Handles the "Complete" action from a notification.
 *
 * Marks the reminder as completed and dismisses the notification
 * without opening the app.
 *
 * Implemented fully in Phase 10 (Notificações).
 */
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.agenda.app.domain.usecase.reminder.CompleteReminderUseCase
import com.agenda.app.presentation.notification.NotificationHelper

@AndroidEntryPoint
class CompleteActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var completeReminderUseCase: CompleteReminderUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("EXTRA_REMINDER_ID") ?: return
        
        notificationHelper.cancelNotification(reminderId)
        
        val pendingResult = goAsync()
        
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                completeReminderUseCase(reminderId)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
