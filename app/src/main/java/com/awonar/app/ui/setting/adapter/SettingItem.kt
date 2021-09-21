package com.awonar.app.ui.setting.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingItem(
    val buttonText: String? = null,
    val buttonTextRes: Int = 0,
    val isAlert: Boolean = false,
    val navigation: Class<*>?
) : Parcelable