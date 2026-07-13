package com.agenda.app.presentation.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.agenda.app.R
import com.agenda.app.domain.model.Reminder
import com.agenda.app.presentation.MainActivity
import com.agenda.app.receiver.CompleteActionReceiver
import com.agenda.app.receiver.SnoozeActionReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showReminderNotification(reminder: Reminder) {
        // Intent to open app
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Add deep link or extra to open specific reminder
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            reminder.id.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Snooze Action
        val snoozeIntent = Intent(context, SnoozeActionReceiver::class.java).apply {
            putExtra("EXTRA_REMINDER_ID", reminder.id)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode() + 1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Complete Action
        val completeIntent = Intent(context, CompleteActionReceiver::class.java).apply {
            putExtra("EXTRA_REMINDER_ID", reminder.id)
        }
        val completePendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode() + 2,
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenIntent = Intent(context, com.agenda.app.presentation.alarm.AlarmActivity::class.java).apply {
            putExtra("EXTRA_REMINDER_ID", reminder.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            reminder.id.hashCode() + 3,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, com.agenda.app.AgendaApplication.CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history) // Use a proper icon
            .setContentTitle("Lembrete: ${reminder.categoryName ?: ""}".trim())
            .setContentText(reminder.description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(0, "Adiar (10 min)", snoozePendingIntent)
            .addAction(0, "Concluir", completePendingIntent)
            .build()

        notificationManager.notify(reminder.id.hashCode(), notification)
    }

    fun cancelNotification(reminderId: String) {
        notificationManager.cancel(reminderId.hashCode())
    }
}

// Extension to get categoryName cleanly if needed (we can pass it differently later)
val Reminder.categoryName: String?
    get() = null // Simplified for now
