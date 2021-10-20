package com.awonar.android.model.order

data class DefaultStopLossRequest(
    val instrumentId: Int,
    val amount: Float,
    val unit: Float,
    val price: Float
)