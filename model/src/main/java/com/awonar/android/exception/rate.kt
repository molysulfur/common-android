package com.awonar.android.exception

import java.lang.Exception

class MinRateException(
    override val message: String?,
    val minRate: Float
) : Exception(message)