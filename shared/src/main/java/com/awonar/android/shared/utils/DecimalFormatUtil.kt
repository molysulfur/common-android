package com.awonar.android.shared.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object DecimalFormatUtil {

    fun convert(value: Float, format: String = "#.##"): Float {
        val df = DecimalFormat(format)
        return df.format(value).toFloat()
    }
}