package com.agenda.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Agenda app.
 *
 * Serves as the Hilt entry point and initializes app-wide configurations
 * such as notification channels.
 */
@HiltAndroidApp
class AgendaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    /**
     * Creates notification channels required by the app.
     *
     * This is idempotent — calling it multiple times is safe because Android
     * ignores channel creation if the channel already exists.
     *
     * Channels:
     * - [CHANNEL_REMINDERS]: High-importance channel for user reminders (sound + vibration + heads-up)
     * - [CHANNEL_GENERAL]: Default-importance channel for informational notifications
     */
    private fun createNotificationChannels() {
        val reminderChannel = NotificationChannel(
            CHANNEL_REMINDERS,
            getString(R.string.notification_channel_reminders_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.notification_channel_reminders_description)
            enableVibration(true)
            setShowBadge(true)
        }

        val generalChannel = NotificationChannel(
            CHANNEL_GENERAL,
            getString(R.string.notification_channel_general_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notification_channel_general_description)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannels(listOf(reminderChannel, generalChannel))
    }

    companion object {
        const val CHANNEL_REMINDERS = "channel_reminders"
        const val CHANNEL_GENERAL = "channel_general"
    }
}
