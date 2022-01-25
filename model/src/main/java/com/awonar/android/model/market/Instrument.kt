package com.awonar.android.model.market

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstrumentResponse(
    @SerializedName("instruments") val instruments: List<Instrument>,
) : Parcelable

@Entity(tableName = "instruments")
@Parcelize
data class Instrument(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("exchangeId")
    @ColumnInfo(name = "exchangeId")
    val exchangeId: String?,
    @SerializedName("symbol")
    @ColumnInfo(name = "symbol")
    val symbol: String?,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,
    @SerializedName("companyName")
    @ColumnInfo(name = "companyName")
    val companyName: String?,
    @SerializedName("type")
    @ColumnInfo(name = "type")
    val type: String?,
    @SerializedName("sector")
    @ColumnInfo(name = "sector")
    val sector: String?,
    @SerializedName("assetBase")
    @ColumnInfo(name = "assetBase")
    val assetBase: String?,
    @SerializedName("assetQuote")
    @ColumnInfo(name = "assetQuote")
    val assetQuote: String?,
    @SerializedName("digit")
    @ColumnInfo(name = "digit")
    val digit: Int,
    @SerializedName("categories")
    @ColumnInfo(name = "categories")
    val categories: List<String>,
    @SerializedName("picture")
    @ColumnInfo(name = "logo")
    val logo: String?,
    @SerializedName("industry")
    @ColumnInfo(name = "industry")
    val industry: String?,
    @SerializedName("recommend")
    @ColumnInfo(name = "recommend")
    val recommend: Boolean
) : Parcelable

