package com.awonar.android.exception

import java.lang.Exception

class PortfolioNotFoundException(
    override val message: String?
) : Exception(message)