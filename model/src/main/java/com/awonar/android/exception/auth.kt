package com.awonar.android.exception

import java.lang.Exception

class UnAuthenticationException(override val message: String?) : Exception(message)

class UnAuthenticationIsExistEmailException(
    override val message: String?,
    val email: String? = null
) : Exception(message)