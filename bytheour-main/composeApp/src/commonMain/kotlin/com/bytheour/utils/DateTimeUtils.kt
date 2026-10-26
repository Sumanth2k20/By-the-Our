package com.bytheour.utils

import com.bytheour.utils.DateTimeUtils.YYYY_MM_DD_HH_MM_SS
import com.bytheour.utils.DateTimeUtils.YYYY_MM_DD_HH_MM_SS_AM_PM
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val YYYY_MM_DD_HH_MM_SS_AM_PM = "yyyy-MM-dd hh:mm a"

}

fun getCurrentDateTimeIn24hrs(): String {
    val now = Clock.System.now() // current Instant (UTC)
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    return localDateTime.formatAs24hrsString(YYYY_MM_DD_HH_MM_SS)
}

fun LocalDateTime.formatAs24hrsString(pattern: String): String {
    return pattern
        .replace("yyyy", year.toString())
        .replace("MM", monthNumber.toString().padStart(2, '0'))
        .replace("dd", dayOfMonth.toString().padStart(2, '0'))
        .replace("HH", hour.toString().padStart(2, '0'))
        .replace("mm", minute.toString().padStart(2, '0'))
        .replace("ss", second.toString().padStart(2, '0'))
}

fun getCurrentDateTimeString(): String {
    val now = Clock.System.now() // current Instant (UTC)
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.formatAsString(YYYY_MM_DD_HH_MM_SS_AM_PM)
}



fun LocalDateTime.formatAsString(pattern: String): String {
    var result = pattern

    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val amPm = if (hour < 12) "AM" else "PM"

    result = result.replace("yyyy", year.toString())
        .replace("MM", monthNumber.toString().padStart(2, '0'))
        .replace("dd", dayOfMonth.toString().padStart(2, '0'))
        .replace("HH", hour.toString().padStart(2, '0'))
        .replace("hh", hour12.toString().padStart(2, '0'))
        .replace("mm", minute.toString().padStart(2, '0'))
        .replace("ss", second.toString().padStart(2, '0'))
        .replace("a", amPm)

    return result
}



fun formatTo12Hour(dateTime: LocalDateTime): String {
    val hour = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
    val amPm = if (dateTime.hour < 12) "AM" else "PM"

    val hourStr = hour.toString().padStart(2, '0')
    val minuteStr = dateTime.minute.toString().padStart(2, '0')

    return "${dateTime.date} $hourStr:$minuteStr $amPm"
}

fun formatTo24Hour(dateTime: LocalDateTime): String {
    val hourStr = dateTime.hour.toString().padStart(2, '0')
    val minuteStr = dateTime.minute.toString().padStart(2, '0')
    val secondStr = dateTime.second.toString().padStart(2, '0')

    return "${dateTime.date} $hourStr:$minuteStr:$secondStr"
}



fun parse12HourToLocalDateTime(dateTimeString: String): LocalDateTime {
    val parts = dateTimeString.trim().split(" ")

    if (parts.size < 2) {
        throw IllegalArgumentException("Invalid date format: $dateTimeString")
    }

    val datePart = parts[0]
    val timePart = parts[1]
    val amPm = parts.getOrNull(2)

    val (year, month, day) = datePart.split("-").map { it.toInt() }
    val (hourInput, minute) = timePart.split(":").map { it.toInt() }

    val hour24 = when {
        amPm == null -> hourInput
        amPm.equals("PM", true) && hourInput != 12 -> hourInput + 12
        amPm.equals("AM", true) && hourInput == 12 -> 0
        else -> hourInput
    }

    return LocalDateTime(year, month, day, hour24, minute)
}


fun formatServerDateTimeTo12Hour(dateTimeString: String): String {
    val cleaned = dateTimeString.replace("T", " ")

    val parts = cleaned.split(" ")

    if (parts.size == 1) {
        return parts[0]
    }

    val datePart = parts[0]
    val timePart = parts[1]

    val dateSplit = datePart.split("-", "/")
    if (dateSplit.size < 3) return dateTimeString

    val year = dateSplit[0].toInt()
    val month = dateSplit[1].toInt()
    val day = dateSplit[2].toInt()

    val timeSplit = timePart.split(":")
    val hour = timeSplit.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = timeSplit.getOrNull(1)?.toIntOrNull() ?: 0
    val second = timeSplit.getOrNull(2)?.toIntOrNull() ?: 0

    val local = LocalDateTime(year, month, day, hour, minute, second)

    return local.formatAsString(DateTimeUtils.YYYY_MM_DD_HH_MM_SS_AM_PM)
}
