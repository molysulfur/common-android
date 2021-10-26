package com.awonar.app.ui.portfolio

import android.app.Activity
import android.graphics.Canvas
import android.os.Build
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.ui.portfolio.activedadapter.ActivedColumnAdapter
import com.awonar.app.ui.portfolio.adapter.ColumnValue
import com.awonar.app.ui.portfolio.adapter.ColumnValueType
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.widget.InstrumentOrderView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

@BindingAdapter("updateQuoteList")
fun updateQuoteList(
    recycler: RecyclerView,
    quote: Array<Quote>
) {
    if (recycler.adapter != null) {
        val adapter = recycler.adapter as OrderPortfolioAdapter
        adapter.quote = quote
    }
}

@BindingAdapter("setOrderPortfolio")
fun setAdapterOrderPortfolio(
    recycler: RecyclerView,
    items: MutableList<OrderPortfolioItem>
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderPortfolioAdapter()
        }
        val callback = PortfolioListItemTouchHelperCallback(recycler.context)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                callback.onDraw(c)
            }
        })
    }
    val adapter = recycler.adapter as OrderPortfolioAdapter
    adapter.itemLists = items


}

@BindingAdapter("setPositionOrder", "updateQuote")
fun setPositionOrderPortfolio(
    view: InstrumentOrderView,
    item: OrderPortfolioItem.InstrumentPortfolioItem,
    quote: Quote?
) {
    item.position.let { position ->
        view.setImage(position.instrument.logo ?: "")
        view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
        val convertedDate = SimpleDateFormat("dd-MM-yyyy").parse(position.openDateTime)
        view.setDescription("$convertedDate")
    }
    view.setTextColumnOne(
        getColumnValue(
            position = item.position,
            value = item.value1,
            quote = quote,
            conversion = item.conversionRate
        )
    )
    view.setTextColumnTwo(
        getColumnValue(
            position = item.position,
            value = item.value2,
            quote = quote,
            conversion = item.conversionRate
        )
    )
    view.setTextColumnThree(
        getColumnValue(
            position = item.position,
            value = item.value3,
            quote = quote,
            conversion = item.conversionRate
        )
    )
    view.setTextColumnFour(
        getColumnValue(
            position = item.position,
            value = item.value4,
            quote = quote,
            conversion = item.conversionRate
        )
    )
}

private fun getColumnValue(
    position: Position,
    value: ColumnValue?,
    quote: Quote?,
    conversion: Float
): String {
    quote?.let {
        return when (value?.type) {
            ColumnValueType.COLUMN_CURRENT -> {
                "%.${position.instrument.digit}f".format(if (position.isBuy) quote.bid else quote.ask)
            }
            ColumnValueType.PROFITLOSS -> {
                "$%.2f".format(
                    PortfolioUtil.getProfitOrLoss(
                        current = if (position.isBuy) quote.bid else quote.ask,
                        openRate = position.openRate,
                        unit = position.units,
                        rate = 1f,
                        isBuy = position.isBuy
                    )
                )
            }
            ColumnValueType.COLUMN_PIP_CHANGE -> {
                "${
                    PortfolioUtil.pipChange(
                        current = if (position.isBuy) quote.bid else quote.ask,
                        openRate = position.openRate,
                        isBuy = position.isBuy,
                        digit = position.instrument.digit
                    )
                }"
            }
            ColumnValueType.COLUMN_VALUE -> {
                val pl = PortfolioUtil.getProfitOrLoss(
                    current = if (position.isBuy) quote.bid else quote.ask,
                    openRate = position.openRate,
                    unit = position.units,
                    rate = conversion,
                    isBuy = position.isBuy
                )
                "%.2f".format(PortfolioUtil.getValue(pl, position.amount))
            }
            ColumnValueType.PROFITLOSS_PERCENT -> {
                val pl = PortfolioUtil.getProfitOrLoss(
                    current = if (position.isBuy) quote.bid else quote.ask,
                    openRate = position.openRate,
                    unit = position.units,
                    rate = conversion,
                    isBuy = position.isBuy
                )
                "%.2f%s".format(pl.times(100).div(position.amount), "%")
            }
            else -> "${value?.value ?: 0f}"
        }
    }
    return "${value?.value ?: 0f}"
}

@BindingAdapter("setActivedColumn", "viewModel")
fun setActivedColumn(
    recycler: RecyclerView,
    activedList: List<String>,
    viewModel: PortFolioViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = ActivedColumnAdapter().apply {
                onClick = { text ->
                    viewModel.activedColumnChange(text)
                }
            }
        }
    }
    (recycler.adapter as ActivedColumnAdapter).itemList = activedList
}

@BindingAdapter("setActivedColumnToolbar")
fun setActivedColumnToolbar(toolbar: MaterialToolbar, viewModel: PortFolioViewModel) {
    toolbar.setNavigationOnClickListener {
        (toolbar.context as Activity).finish()
    }
    toolbar.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.awonar_toolbar_actived_column_save -> viewModel.saveActivedColumn()
            R.id.awonar_toolbar_actived_column_reset -> viewModel.resetActivedColumn()
        }
        (toolbar.context as Activity).finish()
        false
    }
}