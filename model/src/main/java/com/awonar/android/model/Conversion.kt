package com.awonar.android.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "conversions")
@Parcelize
data class Conversion constructor(
    @PrimaryKey
    @SerializedName("instrumentId")
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("pair")
    @ColumnInfo(name = "name")
    val name: String,
    @SerializedName("conversionRateAsk")
    @ColumnInfo(name = "rateAsk")
    val rateAsk: Float,
    @SerializedName("conversionRateBid")
    @ColumnInfo(name = "rateBid")
    val rateBid: Float
) : Parcelable