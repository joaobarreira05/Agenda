package com.agenda.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receives [Intent.ACTION_BOOT_COMPLETED] and re-schedules all pending alarms.
 *
 * Android clears all alarms when the device reboots. This receiver ensures
 * that all pending reminders are re-scheduled via [AlarmSchedulerContract].
 *
 * Implemented fully in Phase 9 (Alarmes).
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // TODO: Phase 9 — Re-schedule all pending alarms
        }
    }
}
