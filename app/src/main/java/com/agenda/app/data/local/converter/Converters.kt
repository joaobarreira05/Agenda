package com.agenda.app.data.local.converter

import androidx.room.TypeConverter
import com.agenda.app.domain.model.ReminderStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun fromReminderStatus(status: ReminderStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toReminderStatus(value: String?): ReminderStatus? {
        return value?.let { ReminderStatus.valueOf(it) }
    }
}
