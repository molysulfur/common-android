package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemHorizontalWarpperBinding
import com.awonar.app.ui.socialtrade.adapter.SocialTradeRecommendedAdapter

class HorizontalWrapperViewHolder(
    private val binding: AwonarItemHorizontalWarpperBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adapter: SocialTradeRecommendedAdapter, lastScrollX: Int, onScrolled: (Int) -> Unit) {
        val context = binding.root.context
        binding.awonarHorizontalWrapperContainer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.awonarHorizontalWrapperContainer.adapter = adapter
        binding.awonarHorizontalWrapperContainer.doOnPreDraw {
            binding.awonarHorizontalWrapperContainer.scrollBy(lastScrollX, 0)
        }
        binding.awonarHorizontalWrapperContainer.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onScrolled(recyclerView.computeHorizontalScrollOffset())
            }
        })
    }
}