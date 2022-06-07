package com.awonar.app.ui.portfolio.position

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.portfolio.adapter.PortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import timber.log.Timber

@BindingAdapter("setPositionAdapter", "activedColumn", "viewModel", "positionStyle")
fun setPositionAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
    activedColumn: List<String> = emptyList(),
    viewModel: PositionViewModel,
    style: String?
) {
    if (activedColumn.size <= 3 && style != "card" && style != "chart") {
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
                onPieClick = { type ->
                    type?.let {
                        viewModel.updateChartType(it)
                    }
                }
                onExposure = {
                    viewModel.updateChartType("exposure")
                }
                onAllocate = {
                    viewModel.updateChartType("allocate")
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