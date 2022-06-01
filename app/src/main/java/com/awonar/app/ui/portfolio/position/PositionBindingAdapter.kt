package com.awonar.app.ui.portfolio.position

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.awonar.app.ui.portfolio.adapter.PortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import timber.log.Timber

@BindingAdapter("activedColumn", "viewModel")
fun setAdapter(
    recycler: RecyclerView,
    activedColumn: List<String> = emptyList(),
    viewModel: PositionViewModel,
) {
    if (activedColumn.size > 3) {
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = PortfolioAdapter().apply {
                onClick = { it, type ->
                    viewModel.navigateInstrumentInside(it, type)
                }
            }
        }
        val adapter = recycler.adapter as PortfolioAdapter
        adapter.apply {
            columns = activedColumn
        }
    }

}

@BindingAdapter("setPositionAdapter")
fun setPositionAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
) {
    if (recycler.adapter != null) {
        val adapter = recycler.adapter as PortfolioAdapter
        adapter.apply {
            itemLists = items
        }
    }
}