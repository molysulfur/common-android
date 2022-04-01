package com.awonar.app.ui.profile.stat

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatisticLayoutManager(context: Context, adapter: RecyclerView.Adapter<*>, spanCount: Int) :
    GridLayoutManager(context, spanCount) {
    init {
        spanSizeLookup = StatisticLookup(adapter, spanCount)
    }

}
