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
import timber.log.Timber
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

        }
        is OrderPortfolioItem.CopierPortfolioItem -> item.copier.let { copy ->
            Timber.e("${copy.user}")
            view.setImage(copy.user.picture ?: "")
            view.setTitle(copy.user.username ?: "")
        }
        else -> {
        }
    }
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