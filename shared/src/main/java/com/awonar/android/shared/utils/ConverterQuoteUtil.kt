package com.awonar.android.shared.utils

import com.awonar.android.model.market.Quote

object ConverterQuoteUtil {

    fun getCurrentPrice(
        quote: Quote, leverage: Int,
        isBuy: Boolean,
    ) = when (isBuy) {
        false -> {
            if (leverage > 1) quote.bid else quote.bidSpread
        }
        else -> {
            quote.askSpread
        }
    }

    fun change(close: Float, previous: Float): Float {
        return close.minus(previous)
    }

    fun percentChange(oldPrice: Float, newPrice: Float): Float {
        return ((newPrice.minus(oldPrice)).div(oldPrice)).times(100)
    }

}