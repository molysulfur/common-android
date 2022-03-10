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
        itemList = MarketAboutItem.convertToItem(instrumentInfo)
    }
}


@BindingAdapter("marketProfileAvatar")
fun setAvatar(image: ImageView, url: String?) {
    if (url != null)
        image.loadImage(url)
}