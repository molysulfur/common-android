package com.awonar.app.utils

import java.util.regex.Pattern

object PatternUtil {

    fun checkPatternPassword(password: String): Boolean {
        val regex: Pattern = Pattern.compile("[^A-Za-z0-9]")
        return regex.matcher(password).find()
    }
}