package com.agenda.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Handles the "Snooze" action from a notification.
 *
 * Re-schedules the reminder alarm for +10 minutes and dismisses
 * the current notification.
 *
 * Implemented fully in Phase 10 (Notificações).
 */
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.agenda.app.domain.usecase.reminder.GetReminderByIdUseCase
import com.agenda.app.domain.usecase.reminder.UpdateReminderUseCase
import com.agenda.app.domain.service.AlarmSchedulerContract
import com.agenda.app.presentation.notification.NotificationHelper

@AndroidEntryPoint
class SnoozeActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getReminderByIdUseCase: GetReminderByIdUseCase

    @Inject
    lateinit var updateReminderUseCase: UpdateReminderUseCase

    @Inject
    lateinit var alarmScheduler: AlarmSchedulerContract

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("EXTRA_REMINDER_ID") ?: return
        
        notificationHelper.cancelNotification(reminderId)
        
        val pendingResult = goAsync()
        
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val reminder = getReminderByIdUseCase(reminderId)
                if (reminder != null && reminder.status != com.agenda.app.domain.model.ReminderStatus.COMPLETED) {
                    val snoozedReminder = reminder.copy(dateTime = reminder.dateTime.plusMinutes(10))
                    updateReminderUseCase(snoozedReminder)
                    alarmScheduler.schedule(snoozedReminder)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
