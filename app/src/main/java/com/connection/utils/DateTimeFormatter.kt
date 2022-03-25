package com.connection.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatter {

    private val hourMinuteFormat = SimpleDateFormat("hh:mm", Locale.US)
    private val weekDayFormat = SimpleDateFormat("EEEE", Locale.US)

    fun formatMessageDateTime(timestamp: Long): String = hourMinuteFormat.format(timestamp)

    fun formatWeekDay(timestamp: Long): String = weekDayFormat.format(timestamp)
}