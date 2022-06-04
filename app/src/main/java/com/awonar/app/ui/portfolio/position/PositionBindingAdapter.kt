package com.awonar.app.ui.portfolio.position

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.portfolio.adapter.PortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

@BindingAdapter("setPositionAdapter", "activedColumn", "viewModel")
fun setPositionAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
    activedColumn: List<String> = emptyList(),
    viewModel: PositionViewModel,
) {
    if (activedColumn.size <= 3) {
        return
    }
    if (activedColumn.size > 3 && recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = PortfolioAdapter().apply {
                onIntrumentClick = { position ->
                    viewModel.navigateInstrumentInside(position)
                }
                onCopierClick = { position ->
                    viewModel.navigateInstrumentCopier(position)
                }
            }
        }
    }
    val adapter = recycler.adapter as PortfolioAdapter
    adapter.apply {
        columns = activedColumn
        itemLists = items
    }
}