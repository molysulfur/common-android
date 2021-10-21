package com.awonar.app.ui.portfolio

import android.os.Build
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.widget.InstrumentOrderView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@BindingAdapter("setOrderPortfolio")
fun setAdapterOrderPortfolio(recycler: RecyclerView, items: List<Position>) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderPortfolioAdapter()
        }
    }
    val itemList = mutableListOf<OrderPortfolioItem>()
    items.forEach {
        itemList.add(OrderPortfolioItem.InstrumentPortfolioItem(it))
    }
    val adapter = recycler.adapter as OrderPortfolioAdapter
    adapter.itemLists = itemList

}

@BindingAdapter("setPositionOrder")
fun setPositionOrderPortfolio(view: InstrumentOrderView, position: Position?) {
    position?.let {
        view.setImage(position.instrument.logo ?: "")
        view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
        val convertedDate = SimpleDateFormat("dd-MM-yyyy").parse(position.openDateTime)
        view.setDescription("$convertedDate")
    }

}
