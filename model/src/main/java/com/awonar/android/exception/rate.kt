package com.awonar.android.exception

import java.lang.Exception

class RateException(
    override val message: String?,
    val rate: Float
) : Exception(message)