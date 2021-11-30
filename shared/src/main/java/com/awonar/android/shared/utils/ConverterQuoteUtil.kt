package com.awonar.android.shared.utils

import com.awonar.android.model.market.Quote

object ConverterQuoteUtil {

    fun getCurrentPrice(quote: Quote, leverage: Int, isBuy: Boolean) = when (isBuy) {
        true -> {
            if (leverage > 1) quote.ask else quote.askSpread
        }
        else -> {
            quote.bidSpread
        }
    }

    fun change(close: Float, previous: Float): Float {
        return close.minus(previous)
    }

    fun percentChange(oldPrice: Float, newPrice: Float): Float {
        return ((newPrice.minus(oldPrice)).div(oldPrice)).times(100)
    }

}