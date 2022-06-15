package com.awonar.app.ui.order.leverageselector.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemStepBinding

class LeverageSelectorAdapter : RecyclerView.Adapter<LeverageStepViewHolder>() {

    var itemList: MutableList<StepViewData> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onChecked: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeverageStepViewHolder =
        LeverageStepViewHolder(
            AwonarItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: LeverageStepViewHolder, position: Int) {
        holder.bind(itemList[position], onChecked)
    }

    override fun getItemCount(): Int = itemList.size

    data class StepViewData(
        val label: String? = null,
        val start: Boolean = false,
        val end: Boolean = false,
        val isCheck: Boolean = false
    )
}