package com.awonar.android.model.history

import android.os.Parcelable
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.user.Master
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryResponse(
    @SerializedName("histories") val histories: List<History>?,
    @SerializedName("masters") val masters: List<Master>?,
    @SerializedName("meta") val meta: HistoryMeta
) : Parcelable

@Parcelize
data class History(
    @SerializedName("amount") val amount: Float,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("depositId") val depositId: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("id") val id: String,
    @SerializedName("masterId") val masterId: String,
    @SerializedName("position") val position: Position?,
    @SerializedName("positionId") val positionId: String,
    @SerializedName("transactionNo") val transactionNo: String,
    @SerializedName("transactionType") val transactionType: Int,
    @SerializedName("withdrawId") val withdrawId: String,
) : Parcelable

@Parcelize
data class HistoryMeta(
    @SerializedName("hasMore") val hasMore: Boolean,
    @SerializedName("limit") val limit: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("totalCount") val totalCount: Int,
) : Parcelable
