package com.awonar.app.utils

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

    fun getDate(dateString: String?, pattern: String, format: String = "dd-MM-yyyy HH:mm"): String {
        val parser = SimpleDateFormat(pattern, Locale.getDefault())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        dateString?.let {
            val date = parser.parse(dateString)
            return formatter.format(date)
        }
        return ""
    }
}