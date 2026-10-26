package com.bytheour.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun calculateHourType(timeString: String, dateString: String): Int {
    val bookingDate = LocalDate.parse(dateString)
    val startTimeString = timeString.split(" - ")[0].trim()
    val bookingTime = parseTime12Hour(startTimeString)
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate = currentDateTime.date
    val bookingDateTime = LocalDateTime(
        date = bookingDate,
        time = bookingTime
    )
    val currentInstant = currentDateTime.toInstant(TimeZone.currentSystemDefault())
    val bookingInstant = bookingDateTime.toInstant(TimeZone.currentSystemDefault())
    val minutesDifference = bookingInstant.minus(currentInstant).inWholeMinutes
    return when {
        bookingDate > currentDate -> 3

        bookingDate == currentDate -> {
            when {
                minutesDifference > 120 -> 3  // More than 2 hours
                minutesDifference in 30..120 -> 2  // Between 30 mins and 2 hours
                minutesDifference < 30 -> 1  // Less than 30 minutes
                else -> 1
            }
        }
        else -> 1
    }
}

fun getCancelMessageByTimeAndDate(timeString: String, dateString: String): String {
    val bookingDate = LocalDate.parse(dateString)

    val startTimeString = timeString.split(" - ")[0].trim()
    val bookingTime = parseTime12Hour(startTimeString)
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate = currentDateTime.date
    val bookingDateTime = LocalDateTime(date = bookingDate, time = bookingTime)
    val currentInstant = currentDateTime.toInstant(TimeZone.currentSystemDefault())
    val bookingInstant = bookingDateTime.toInstant(TimeZone.currentSystemDefault())
    val minutesDifference = bookingInstant.minus(currentInstant).inWholeMinutes

    return when {
        bookingDate > currentDate -> "Are you sure you want to cancel this booking?"
        bookingDate == currentDate -> when {
            minutesDifference > 120 -> "Are you sure you want to cancel this booking?"
            minutesDifference in 30..120 -> "Cancelling the service within 2 hours of the start time will incur a 20 percent charge."
            minutesDifference < 30 -> "Cancelling the service within 30 minutes of the start time will incur a 30 percent charge."
            else -> "Are you sure you want to cancel this booking?"
        }

        else -> "Are you sure you want to cancel this booking?"
    }
}

private fun parseTime12Hour(timeStr: String): LocalTime {
    val parts = timeStr.trim().split(" ")
    val timePart = parts[0] // "6:00"
    val period = parts[1] // "PM" or "AM"

    val timeComponents = timePart.split(":")
    var hour = timeComponents[0].toInt()
    val minute = timeComponents[1].toInt()

    // Convert to 24-hour format
    hour = when {
        period.equals("AM", ignoreCase = true) && hour == 12 -> 0
        period.equals("PM", ignoreCase = true) && hour != 12 -> hour + 12
        else -> hour
    }

    return LocalTime(hour, minute)
}