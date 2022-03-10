package com.awonar.app.ui.marketprofile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.app.ui.marketprofile.about.MarketAboutAdapter
import com.awonar.app.ui.marketprofile.about.MarketAboutItem
import com.awonar.app.utils.loadImage


@BindingAdapter("setMarketAbout")
fun setMarketAbout(
    recycler: RecyclerView,
    instrumentInfo: InstrumentProfile,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = MarketAboutAdapter()
        }
    }
    (recycler.adapter as MarketAboutAdapter).apply {
        itemList = convertToItem(instrumentInfo)
    }
}

fun convertToItem(instrumentInfo: InstrumentProfile): MutableList<MarketAboutItem> {
    val itemList = mutableListOf<MarketAboutItem>()
    instrumentInfo.shortDescription.takeIf {
        !it.isNullOrBlank()
    }?.let {
        itemList.add(MarketAboutItem.TitleItem("About"))
        itemList.add(MarketAboutItem.DescriptionItem(it))
    }
    instrumentInfo.sector.takeIf {
        !it.isNullOrBlank()
    }?.let {
        itemList.add(MarketAboutItem.TitleItem("Sector"))
        itemList.add(MarketAboutItem.DescriptionItem(it))
    }
    instrumentInfo.industry.takeIf {
        !it.isNullOrBlank()
    }?.let {
        itemList.add(MarketAboutItem.TitleItem("Industry"))
        itemList.add(MarketAboutItem.DescriptionItem(it))
    }
    instrumentInfo.ceo.takeIf {
        !it.isNullOrBlank()
    }?.let {
        itemList.add(MarketAboutItem.TitleItem("CEO"))
        itemList.add(MarketAboutItem.DescriptionItem(it))
    }
    instrumentInfo.employees.takeIf {
        it > 0
    }?.let {
        itemList.add(MarketAboutItem.TitleItem("Employee"))
        itemList.add(MarketAboutItem.DescriptionItem("$it"))
    }
    return itemList
}

@BindingAdapter("marketProfileAvatar")
fun setAvatar(image: ImageView, url: String?) {
    if (url != null)
        image.loadImage(url)
}