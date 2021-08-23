package com.awonar.android.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserRequest(val needFresh: Boolean) : Parcelable