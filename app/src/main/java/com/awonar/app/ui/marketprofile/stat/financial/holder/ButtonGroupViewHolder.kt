package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemButtonGroupBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem

class ButtonGroupViewHolder constructor(private val binding: AwonarItemButtonGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        marketItem: FinancialMarketItem.ButtonGroupItem,
        onFiscalChange: ((String?) -> Unit)?,
    ) {
        binding.button1 = marketItem.button1
        binding.button2 = marketItem.button2
//        val id = if (marketItem.default == marketItem.button2) {
//            R.id.awonar_item_button_group_button_second
//        } else {
//            R.id.awonar_item_button_group_button_first
//        }
//        binding.awonarItemButtonGroupContainer.check(id)
        binding.awonarItemButtonGroupContainer.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_item_button_group_button_first -> onFiscalChange?.invoke("annual")
                    R.id.awonar_item_button_group_button_second -> onFiscalChange?.invoke("quarter")
                }
            }
        }
    }
}