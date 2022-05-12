package com.awonar.app.ui.market.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderDialog
import com.facebook.shimmer.ShimmerFrameLayout

@BindingAdapter("startLoading")
fun setLoading(progress: ShimmerFrameLayout, isStart: Boolean) {
    progress.showShimmer(isStart)
    if (isStart) {
        progress.startShimmer()
    } else {
        progress.hideShimmer()
    }
}