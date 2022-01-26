package com.awonar.android.shared.utils

import org.json.JSONObject

object JsonUtil {

    fun getError(message: String?): String = JSONObject(message ?: "").get("message").toString()
}