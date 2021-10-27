package com.awonar.app.ui.portfolio

import android.app.Activity
import android.graphics.Canvas
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.R
import com.awonar.app.ui.portfolio.activedadapter.ActivedColumnAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.widget.InstrumentOrderView
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("updateQuote")
fun updateQuoteList(
    recycler: RecyclerView,
    quote: Array<Quote>
) {
    if (recycler.adapter != null) {
        val adapter = recycler.adapter as OrderPortfolioAdapter
        adapter.quote = quote
    }
}


@BindingAdapter("setOrderPortfolio", "activedColumn")
fun setAdapterOrderPortfolio(
    recycler: RecyclerView,
    items: MutableList<OrderPortfolioItem>,
    activedColumn: List<String>
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
    adapter.columns = activedColumn
    adapter.itemLists = items
}

@BindingAdapter("setPositionOrder", "column1", "column2", "column3", "column4")
fun setItemPositionOrderPortfolio(
    view: InstrumentOrderView,
    item: OrderPortfolioItem?,
    column1: String,
    column2: String,
    column3: String,
    column4: String,
) {
    when (item) {
        is OrderPortfolioItem.InstrumentPortfolioItem -> item.position.let { position ->
            view.setImage(position.instrument.logo ?: "")
            view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = parser.parse(position.openDateTime)
            date?.let {
                view.setDescription(formatter.format(date))
            }
            view.setTextColumnOne(
                getPositionValueByColumn(
                    item,
                    column1
                )
            )
            view.setTextColumnTwo(
                getPositionValueByColumn(
                    item,
                    column2
                )
            )
            view.setTextColumnThree(
                getPositionValueByColumn(
                    item,
                    column3
                )
            )
            view.setTextColumnFour(
                getPositionValueByColumn(
                    item,
                    column4
                )
            )
        }
        is OrderPortfolioItem.CopierPortfolioItem -> item.copier.let { copy ->
            view.setImage(copy.user.picture ?: "")
            view.setTitle(copy.user.username ?: "")
            view.setTextColumnOne(
                getCopierValueByCoumn(
                    item,
                    column1
                )
            )
            view.setTextColumnTwo(
                getCopierValueByCoumn(
                    item,
                    column2
                )
            )
            view.setTextColumnThree(
                getCopierValueByCoumn(
                    item,
                    column3
                )
            )
            view.setTextColumnFour(
                getCopierValueByCoumn(
                    item,
                    column4
                )
            )
        }
        else -> {
        }
    }
}

private fun getCopierValueByCoumn(
    item: OrderPortfolioItem.CopierPortfolioItem,
    column: String
): String = when (column) {
    "Units" -> "%.2f".format(item.units)
    "Avg. Open" -> "%s".format(item.avgOpen)
    "Invested" -> "$%.2f".format(item.invested)
    "S/L($)" -> "$%.2f".format(item.profitLoss)
    "S/L(%)" -> "%s%s".format(item.profitLossPercent, "%")
    "Value" -> "$%s".format(item.value)
    "Fee" -> "$%s".format(item.fees)
    "Leverage" -> "%s".format(item.leverage)
    "Current" -> ""
    "Net Invest" -> "$%.2f".format(item.netInvested)
    "CSL" -> "%s".format(item.copyStopLoss)
    "CSL(%)" -> "%.2f%s".format(item.copyStopLossPercent, "%")
    else -> ""
}

private fun getPositionValueByColumn(
    item: OrderPortfolioItem.InstrumentPortfolioItem,
    column: String
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "Units" -> "%.2f".format(item.units)
    "Open" -> "%s".format(item.open)
    "Current" -> "%s".format(item.current)
    "S/L($)" -> "$%.2f".format(item.profitLoss)
    "S/L(%)" -> "%s%s".format(item.profitLossPercent, "%")
    "Pip Change" -> "%s".format(item.pipChange)
    "Leverage" -> "%s".format(item.leverage.toFloat())
    "Value" -> "$%s".format(item.value)
    "Fee" -> "$%s".format(item.fees)
    "Execute at" -> "$%s".format(item.invested)
    "SL" -> "%s".format(item.stopLoss)
    "TP" -> "%s".format(item.takeProfit)
    "SL($)" -> "$%.2f".format(item.amountStopLoss)
    "TP($)" -> "$%.2f".format(item.amountTakeProfit)
    "SL(%)" -> "%s%s".format(item.stopLossPercent, "%")
    "TP(%)" -> "%s%s".format(item.takeProfitPercent, "%")
    else -> ""
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