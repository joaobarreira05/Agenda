package com.agenda.app.domain.service

import com.agenda.app.domain.model.Reminder

/**
 * Contract for scheduling alarms for reminders.
 */
interface AlarmSchedulerContract {
    /** Schedules an exact alarm for the given [reminder]. */
    fun schedule(reminder: Reminder)

    /** Cancels an existing alarm for the given [reminderId]. */
    fun cancel(reminderId: String)

    /** Re-schedules alarms for all given [reminders]. Typically used after reboot. */
    fun rescheduleAll(reminders: List<Reminder>)
}
