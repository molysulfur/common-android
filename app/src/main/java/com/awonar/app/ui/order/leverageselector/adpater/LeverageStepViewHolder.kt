package com.awonar.app.ui.order.leverageselector.adpater

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemStepBinding
import com.awonar.app.widget.StepView

class LeverageStepViewHolder constructor(private val binding: AwonarItemStepBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LeverageSelectorAdapter.StepViewData, onChecked: ((Boolean) -> Unit)?) {
        with(binding.awonarItemStepHolder) {
            label = item.label
            isChecked = item.isCheck
            if (item.start) {
                viewState = StepView.STATE.START
            }
            if (item.end) {
                viewState = StepView.STATE.END
            }
            this.onChecked = {
                onChecked?.invoke(it)
            }
        }

    }
}