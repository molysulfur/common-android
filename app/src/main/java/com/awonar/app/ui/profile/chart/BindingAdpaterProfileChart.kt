package com.awonar.app.ui.profile.chart

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartAdapter
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import com.awonar.app.ui.user.UserViewModel
import timber.log.Timber

@BindingAdapter("setProfileChart", "viewModel", "userViewModel")
fun setChartPortfolio(
    recycler: RecyclerView,
    items: MutableList<PositionChartItem>,
    viewModel: ProfileChartViewModel,
    userViewModel: UserViewModel,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            adapter = PositionChartAdapter().apply {
                onPieClick = {
                    viewModel.getInsideChart(userViewModel.userState.value, it)
                }
                onExposure = {
                    viewModel.changeType("exposure")
                    viewModel.getExposure(userViewModel.userState.value)
                }
                onAllocate = {
                    viewModel.changeType("allocate")
                    viewModel.getAllocate(userViewModel.userState.value)
                }
            }
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    (recycler.adapter as PositionChartAdapter).apply {
        itemLists = items
    }
}