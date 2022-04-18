package com.awonar.app.utils

import android.text.format.DateUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDate(dateString: String?, format: String = "dd-MM-yyyy HH:mm"): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        dateString?.let {
            val date = parser.parse(dateString)
            return formatter.format(date)
        }
        return ""
    }

    fun getLongDate(dateString: String?, format: String = "dd-MM-yyyy HH:mm"): Long {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        dateString?.let {
            val date = parser.parse(dateString)
            return date.time
        }
        return 0L
    }

    fun getDate(timestamp: Long, format: String = "dd-MM-yyyy HH:mm"): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        timestamp.let {
            return formatter.format(timestamp)
        }
    }

    fun getDate(dateString: String?, pattern: String, format: String = "dd-MM-yyyy HH:mm"): String {
        val parser = SimpleDateFormat(pattern, Locale.getDefault())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        dateString?.let {
            val date = parser.parse(dateString)
            return formatter.format(date)
        }
        return ""
    }

    fun getTimeAgo(dateString: String?): String {
        if (dateString.isNullOrBlank()) return ""
        val time = getLongDate(dateString = dateString)
        return DateUtils.getRelativeTimeSpanString(time).toString()
    }
}