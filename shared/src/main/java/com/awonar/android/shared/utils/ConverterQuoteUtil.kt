package com.awonar.android.shared.utils

object ConverterQuoteUtil {

    fun percentChange(oldPrice: Float, newPrice: Float): Float {
        return ((newPrice - oldPrice) / oldPrice) * 100
    }

}