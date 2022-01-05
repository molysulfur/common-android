package com.awonar.app.ui.socialtrade.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.socialtrade.Trader
import com.awonar.app.databinding.AwonarItemCopiesRecommendedBinding
import com.awonar.app.ui.socialtrade.adapter.holder.RecommendedViewHolder

class SocialTradeRecommendedAdapter : RecyclerView.Adapter<RecommendedViewHolder>() {

    var itemList: MutableList<Trader> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        return RecommendedViewHolder(
            AwonarItemCopiesRecommendedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}