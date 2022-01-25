package com.awonar.app.ui.socialtrade.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemHorizontalWarpperBinding
import com.awonar.app.ui.socialtrade.adapter.holder.HorizontalWrapperViewHolder

class SocialTradeHorizontalWrapperAdapter constructor(
    private val adapter: SocialTradeRecommendedAdapter
) : RecyclerView.Adapter<HorizontalWrapperViewHolder>() {

    private var lastScrollX = 0

    companion object {
        private const val KEY_SCROLL_X = "horizontal.wrapper.adapter.key_scroll_x"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWrapperViewHolder {
        return HorizontalWrapperViewHolder(
            AwonarItemHorizontalWarpperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HorizontalWrapperViewHolder, position: Int) {
        (holder).bind(adapter, lastScrollX) { x ->
            lastScrollX = x
        }
    }

    override fun getItemCount(): Int = 1


    fun onSaveState(outState: Bundle) {
        outState.putInt(KEY_SCROLL_X, lastScrollX)
    }

    fun onRestoreState(savedState: Bundle) {
        lastScrollX = savedState.getInt(KEY_SCROLL_X)
    }
}
