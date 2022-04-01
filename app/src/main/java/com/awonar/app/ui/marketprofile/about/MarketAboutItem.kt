package com.awonar.app.ui.marketprofile.about

import android.os.Parcelable
import com.awonar.android.model.market.InstrumentProfile
import kotlinx.parcelize.Parcelize

sealed class MarketAboutItem(val type: Int) : Parcelable {

    @Parcelize
    data class TitleItem(
        val title: String?,
    ) : MarketAboutItem(MarketAboutType.TITLE)

    @Parcelize
    data class DescriptionItem(
        val description: String?,
    ) : MarketAboutItem(MarketAboutType.DESCRIPTION)


    companion object {

        fun convertToItem(instrumentInfo: InstrumentProfile): MutableList<MarketAboutItem> {
            val itemList = mutableListOf<MarketAboutItem>()
            instrumentInfo.shortDescription.takeIf {
                !it.isNullOrBlank()
            }?.let {
                itemList.add(TitleItem("About"))
                itemList.add(DescriptionItem(it))
            }
            instrumentInfo.sector.takeIf {
                !it.isNullOrBlank()
            }?.let {
                itemList.add(TitleItem("Sector"))
                itemList.add(DescriptionItem(it))
            }
            instrumentInfo.industry.takeIf {
                !it.isNullOrBlank()
            }?.let {
                itemList.add(TitleItem("Industry"))
                itemList.add(DescriptionItem(it))
            }
            instrumentInfo.ceo.takeIf {
                !it.isNullOrBlank()
            }?.let {
                itemList.add(TitleItem("CEO"))
                itemList.add(DescriptionItem(it))
            }
            instrumentInfo.employees.takeIf {
                it > 0
            }?.let {
                itemList.add(TitleItem("Employee"))
                itemList.add(DescriptionItem("$it"))
            }
            return itemList
        }
    }
}