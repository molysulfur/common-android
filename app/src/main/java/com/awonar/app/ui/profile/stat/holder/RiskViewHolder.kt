package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class RiskViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.RiskItem) {
        with(binding.awonarItemListText) {
            setTitle("Average Risk Scroe of The Last 7 Day")
            setStartIcon(riskIcon(item.risk))
        }
    }

    private fun riskIcon(risk: Int): Int = when (risk) {
        1 -> R.drawable.awonar_ic_risk_1
        2 -> R.drawable.awonar_ic_risk_2
        3 -> R.drawable.awonar_ic_risk_3
        4 -> R.drawable.awonar_ic_risk_4
        5 -> R.drawable.awonar_ic_risk_5
        6 -> R.drawable.awonar_ic_risk_6
        7 -> R.drawable.awonar_ic_risk_7
        8 -> R.drawable.awonar_ic_risk_8
        9 -> R.drawable.awonar_ic_risk_9
        10 -> R.drawable.awonar_ic_risk_10
        else -> 0
    }
}