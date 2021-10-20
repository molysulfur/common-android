package com.awonar.android.shared.utils

object ConverterQuoteUtil {

    fun change(close: Float, previous: Float): Float {
        return close.minus(previous)
    }

    fun percentChange(oldPrice: Float, newPrice: Float): Float {
        return ((newPrice.minus(oldPrice)).div(oldPrice)).times(100)
    }

}