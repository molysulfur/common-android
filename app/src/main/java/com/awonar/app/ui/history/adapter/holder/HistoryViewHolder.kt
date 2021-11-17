package com.awonar.app.ui.history.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.history.History
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.ui.history.adapter.HistoryItem

class HistoryViewHolder constructor(private val binding: AwonarItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HistoryItem.ManualItem, columns: List<String>, onClick: ((History) -> Unit)?) {
        val history = item.history
        binding.history = history
        setupImageWithTransaction(history)
        if (columns.size >= 4) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
        binding.awonarInsturmentOrderItem.setOnClickListener {
            onClick?.invoke(history)
        }
    }

    private fun setupImageWithTransaction(history: History) {
        val view = binding.awonarInsturmentOrderItem
        when (history.transactionType) {
            2 -> view.setImage(R.drawable.awonar_ic_deposit)
            3 -> view.setImage(R.drawable.awonar_ic_deposit_success)
            4 -> view.setImage(R.drawable.awonar_ic_deposit_success)
            5 -> view.setImage(R.drawable.awoanr_ic_deposit_reverse)
            6 -> view.setImage(R.drawable.awoanr_ic_deposit_cenceled)
            7 -> view.setImage(R.drawable.awonar_ic_deposit_compensation)
            11 -> view.setImage(history.master?.picture ?: "")
            else -> setupImageWithCloseType(history)
        }
    }

    private fun setupImageWithCloseType(history: History) {
        val view = binding.awonarInsturmentOrderItem
        when (history.position?.closeType) {
            2 -> view.setImage(R.drawable.awonar_ic_tp)
            3 -> view.setImage(R.drawable.awonar_ic_sl)
            4 -> view.setImage(R.drawable.awonar_ic_admin_close)
            5 -> view.setImage(R.drawable.awonar_ic_part_close)
            6 -> view.setImage(R.drawable.awonar_ic_rollover)
            else -> view.setImage(history.position?.instrument?.logo ?: "")
        }
    }
}