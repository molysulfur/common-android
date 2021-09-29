package com.awonar.app.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.awonar.app.R

object ColorChangingUtil {


    fun getTextColorChange(context: Context, change: Float): Int = when {
        change < 0f -> ContextCompat.getColor(
            context,
            R.color.awonar_color_orange
        )
        change == 0f -> ContextCompat.getColor(
            context,
            R.color.awonar_color_gray
        )
        change > 0f -> ContextCompat.getColor(
            context,
            R.color.awonar_color_green
        )
        else -> 0
    }
}