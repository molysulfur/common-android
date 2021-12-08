package com.awonar.android.model.order

data class ValidatePartialAmountRequest(
    val amount: Float,
    val inputAmount: Float,
    val pl: Float,
    val units: Float,
    val leverage: Int,
    val id: Int
)