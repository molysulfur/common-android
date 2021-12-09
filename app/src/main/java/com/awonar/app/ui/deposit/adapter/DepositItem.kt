package com.awonar.app.ui.deposit.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DepositItem(val type: Int) : Parcelable {

    @Parcelize
    data class MethodItem(val logo: String, val name: String) :
        DepositItem(DepositItemType.DEPOSITION_METHOD_TYPE)
}