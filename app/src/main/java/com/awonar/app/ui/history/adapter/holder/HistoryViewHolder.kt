package com.awonar.app.ui.history.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.history.History
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.DefaultDispatcher
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.market.MarketViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class HistoryViewHolder constructor(
    private val binding: AwonarItemHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var job: CoroutineScope? = null
    private var data: HistoryItem.PositionItem? = null

    fun bind(
        item: HistoryItem.PositionItem,
        columns: List<String>,
        onClick: ((History) -> Unit)?,
        onShowInsideInstrument: ((String, String?) -> Unit)?,
    ) {
        data = item
        setupJob()
        binding.position = item
        setupImageWithTransaction(item)
        if (columns.size >= 4) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
        binding.awonarInsturmentOrderItem.setOnClickListener {
            when (item.positionType?.lowercase()) {
                "manual" -> item.history?.let { history ->
                    onClick?.invoke(history)
                }
                "market " -> {
                    item.detail?.let { detail ->
                        onShowInsideInstrument?.invoke(detail, item.positionType)
                    }
                }
                "user" -> item.master?.username?.let { username ->
                    onShowInsideInstrument?.invoke(username, item.positionType)
                }
                "user2" -> item.id?.let { id ->
                    onShowInsideInstrument?.invoke(id, item.positionType)
                }
                else -> item.detail?.let { detail ->
                    onShowInsideInstrument?.invoke(detail, item.positionType)
                }
            }

        }
    }

    private fun setupJob() {
        job = CoroutineScope(Dispatchers.IO)
        job?.launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val quote = quotes[data?.history?.position?.instrument?.id]
                quote?.let {
                    data?.buy = quote.bid
                    data?.sell = quote.ask
                }
                binding.position = data
            }
        }
    }

    private fun setupImageWithTransaction(history: HistoryItem.PositionItem) {
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

    private fun setupImageWithCloseType(history: HistoryItem.PositionItem) {
        val view = binding.awonarInsturmentOrderItem
        when (history.history?.position?.closeType) {
            2 -> view.setImage(R.drawable.awonar_ic_tp)
            3 -> view.setImage(R.drawable.awonar_ic_sl)
            4 -> view.setImage(R.drawable.awonar_ic_admin_close)
            5 -> view.setImage(R.drawable.awonar_ic_part_close)
            6 -> view.setImage(R.drawable.awonar_ic_rollover)
            else -> view.setImage(history.picture ?: "")
        }
    }
}