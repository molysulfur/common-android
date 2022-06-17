package com.awonar.app.ui.order.leverageselector.adpater

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemStepBinding
import com.awonar.app.widget.StepView
import timber.log.Timber

class LeverageStepViewHolder constructor(private val binding: AwonarItemStepBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LeverageSelectorAdapter.StepViewData, onChecked: ((Int) -> Unit)?) {
        with(binding.awonarItemStepHolder) {
            label = item.label
            setChecked(item.isCheck)
            if (item.start) {
                viewState = StepView.STATE.START
            }
            if (item.end) {
                viewState = StepView.STATE.END
            }
            this.onChecked = {
                onChecked?.invoke(item.value)
            }
        }

    }
}