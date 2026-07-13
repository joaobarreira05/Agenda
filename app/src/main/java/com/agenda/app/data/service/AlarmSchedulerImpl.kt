package com.agenda.app.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.agenda.app.domain.model.Reminder
import com.agenda.app.domain.service.AlarmSchedulerContract
import com.agenda.app.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmSchedulerContract {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(reminder: Reminder) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_REMINDER_ID", reminder.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = reminder.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                // Should ask for permission, fall back to inexact
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    override fun cancel(reminderId: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_REMINDER_ID", reminderId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun rescheduleAll(reminders: List<Reminder>) {
        reminders.forEach { schedule(it) }
    }
}
