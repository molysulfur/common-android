package com.awonar.app.ui.payment.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DepositItem(val type: Int) : Parcelable {


    @Parcelize
    class HistoryItem(
        val id: String?,
        val date: String?,
        val description: String?,
        val status: String?,
        val amount: Float
    ) :
        DepositItem(DepositItemType.DEPOSITION_HISTORY_TYPE)


    @Parcelize
    class BlankItem : DepositItem(DepositItemType.DEPOSITION_BLANK_TYPE)

    @Parcelize
    class LoadMoreItem : DepositItem(DepositItemType.DEPOSITION_LOADMORE)

    @Parcelize
    class HistoryButtonItem : DepositItem(DepositItemType.DEPOSITION_HISTORY_BUTTON_TYPE)

    @Parcelize
    data class MethodItem(val id: String, val logo: String, val name: String) :
        DepositItem(DepositItemType.DEPOSITION_METHOD_TYPE)
}