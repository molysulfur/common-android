package com.awonar.app.ui.portfolio

import android.app.Activity
import android.graphics.Canvas
import android.os.Build
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.portfolio.Position
import com.awonar.app.R
import com.awonar.app.ui.portfolio.activedadapter.ActivedColumnAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.widget.InstrumentOrderView
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat

@BindingAdapter("setOrderPortfolio")
fun setAdapterOrderPortfolio(recycler: RecyclerView, items: List<Position>) {
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

@BindingAdapter("setColumnListToolbar")
fun setColumnListToolbar(toolbar: MaterialToolbar, viewModel: PortFolioViewModel) {
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