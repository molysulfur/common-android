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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    item: OrderPortfolioItem?,
    quote: Quote?
) {
    (item as? OrderPortfolioItem.InstrumentPortfolioItem)?.position?.let { position ->
        view.setImage(position.instrument.logo ?: "")
        view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = parser.parse(position.openDateTime)
        date?.let {
            view.setDescription(formatter.format(date))
        }

        val (format1, value1) = getItemWithColumnType(
            position = item.position,
            value = item.value1,
            quote = quote,
            conversion = item.conversionRate
        )
        view.setTextColumnOne(
            format1.format(value1)
        )
        when (item.value1?.type) {
            ColumnValueType.PROFITLOSS, ColumnValueType.COLUMN_PIP_CHANGE, ColumnValueType.PROFITLOSS_PERCENT -> view.setTextColorColumnOne(
                if (value1 >= 0) R.color.awonar_color_green else R.color.awonar_color_orange
            )
            else -> {
            }
        }

        val (format2, value2) = getItemWithColumnType(
            position = item.position,
            value = item.value2,
            quote = quote,
            conversion = item.conversionRate
        )
        view.setTextColumnTwo(
            format2.format(value2)
        )
        when (item.value2?.type) {
            ColumnValueType.PROFITLOSS, ColumnValueType.COLUMN_PIP_CHANGE, ColumnValueType.PROFITLOSS_PERCENT -> view.setTextColorColumnTwo(
                if (value2 >= 0) R.color.awonar_color_green else R.color.awonar_color_orange
            )
            else -> {
            }
        }

        val (format3, value3) = getItemWithColumnType(
            position = item.position,
            value = item.value3,
            quote = quote,
            conversion = item.conversionRate
        )
        when (item.value3?.type) {
            ColumnValueType.PROFITLOSS, ColumnValueType.COLUMN_PIP_CHANGE, ColumnValueType.PROFITLOSS_PERCENT -> view.setTextColorColumnThree(
                if (value3 >= 0) R.color.awonar_color_green else R.color.awonar_color_orange
            )
            else -> {
            }
        }
        view.setTextColumnThree(
            format3.format(value3)
        )
        val (format4, value4) = getItemWithColumnType(
            position = item.position,
            value = item.value4,
            quote = quote,
            conversion = item.conversionRate
        )
        when (item.value4?.type) {
            ColumnValueType.PROFITLOSS, ColumnValueType.COLUMN_PIP_CHANGE, ColumnValueType.PROFITLOSS_PERCENT -> view.setTextColorColumnFour(
                if (value4 >= 0) R.color.awonar_color_green else R.color.awonar_color_orange
            )
            else -> {
            }
        }
        view.setTextColumnFour(
            format4.format(value4)
        )
    }
}

private fun getItemWithColumnType(
    position: Position,
    value: ColumnValue?,
    quote: Quote?,
    conversion: Float
): Pair<String, Float> {
    quote?.let {
        return when (value?.type) {
            ColumnValueType.COLUMN_CURRENT -> {
                Pair(
                    "%.${position.instrument.digit}f",
                    if (position.isBuy) quote.bid else quote.ask
                )
            }
            ColumnValueType.PROFITLOSS -> {
                Pair(
                    "$%.2f", PortfolioUtil.getProfitOrLoss(
                        current = if (position.isBuy) quote.bid else quote.ask,
                        openRate = position.openRate,
                        unit = position.units,
                        rate = 1f,
                        isBuy = position.isBuy
                    )
                )
            }
            ColumnValueType.COLUMN_PIP_CHANGE -> {
                Pair(
                    "%s", PortfolioUtil.pipChange(
                        current = if (position.isBuy) quote.bid else quote.ask,
                        openRate = position.openRate,
                        isBuy = position.isBuy,
                        digit = position.instrument.digit
                    ).toFloat()
                )
            }
            ColumnValueType.COLUMN_VALUE -> {
                val pl = PortfolioUtil.getProfitOrLoss(
                    current = if (position.isBuy) quote.bid else quote.ask,
                    openRate = position.openRate,
                    unit = position.units,
                    rate = conversion,
                    isBuy = position.isBuy
                )
                Pair("$%.2f", PortfolioUtil.getValue(pl, position.amount))
            }
            ColumnValueType.PROFITLOSS_PERCENT -> {
                val pl = PortfolioUtil.getProfitOrLoss(
                    current = if (position.isBuy) quote.bid else quote.ask,
                    openRate = position.openRate,
                    unit = position.units,
                    rate = conversion,
                    isBuy = position.isBuy
                )
                Pair("%.2f\\%", pl.times(100).div(position.amount))
            }
            else -> Pair("%s", value?.value ?: 0f)
        }
    }
    return Pair("%s", value?.value ?: 0f)
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