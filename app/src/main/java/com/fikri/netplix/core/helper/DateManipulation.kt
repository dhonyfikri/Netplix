package com.fikri.netplix.core.helper

import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateManipulation {
    fun String.withDateFormat(
        type: Int = DateFormat.DEFAULT,
        pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    ): String {
        var result = this
        val format = SimpleDateFormat(pattern, Locale.US)
        val date = format.parse(this) as Date
        try {
            result = DateFormat.getDateInstance(type).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss.SSS"): Date {
        val format = SimpleDateFormat(pattern, Locale.US)
        return format.parse(this) as Date
    }

    fun getStringDate(dayDelta: Int = 0): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var current = LocalDateTime.now()
            if (dayDelta != 0) {
                current = current.plusDays(dayDelta.toLong())
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
            current.format(formatter)
        } else {
            val current = Calendar.getInstance()
            if (dayDelta != 0) {
                current.add(Calendar.DAY_OF_YEAR, dayDelta)
            }
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
            formatter.format(current.time)
        }
    }

    fun getDayDiff(date1: Date, date2: Date): Int {
        val diff: Long = date2.time - date1.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days.toInt()
    }
}