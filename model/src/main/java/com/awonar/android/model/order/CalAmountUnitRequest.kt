package com.awonar.android.model.order

data class CalAmountUnitRequest(
    val instrumentId: Int,
    val leverage: Int,
    val price : Float,
    var amount: Float,
    var unit: Int
)