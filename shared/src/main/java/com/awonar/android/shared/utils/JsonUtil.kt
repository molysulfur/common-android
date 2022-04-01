package com.awonar.android.shared.utils

import com.google.gson.Gson
import org.json.JSONObject

object JsonUtil {

    fun <T> gsonToJson(data: T): String {
        return Gson().toJson(data) ?: "{}"
    }

    fun getError(message: String?): String = JSONObject(message ?: "").get("message").toString()
}