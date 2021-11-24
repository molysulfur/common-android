package com.awonar.android.shared.utils

import kotlin.math.min

object ConverterOrderUtil {

    fun getExposure(
        leverage: Int,
        minLeverage: Int,
        amount: Float
    ) = if (leverage < minLeverage) amount.times(leverage) else amount
}