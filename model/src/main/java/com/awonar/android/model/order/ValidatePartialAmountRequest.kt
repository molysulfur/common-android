package com.awonar.android.model.order

data class ValidatePartialAmountRequest(
    val amount: Float,
    val inputAmount: Float,
    val pl: Float,
    val units: Float,
    val leverage: Int,
    val id: Int
)


val pl = 0f
val leverage = 0
val value = 0f
val amountToReduct = 0f
val unit = 0f