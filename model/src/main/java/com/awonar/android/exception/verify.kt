package com.awonar.android.exception

import java.lang.Exception


class VerifyPersonalException(
    override val message: String?
) : Exception(message)