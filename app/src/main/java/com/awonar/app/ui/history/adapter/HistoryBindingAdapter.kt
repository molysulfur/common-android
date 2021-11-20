package com.awonar.app.ui.history.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.DateUtils
import com.awonar.app.widget.InstrumentOrderView

@BindingAdapter("setHistoryAdapter")
fun setHistoryAdapter(
    recycler: RecyclerView,
    history: MutableList<HistoryItem>
) {
    if (recycler.adapter == null) {
        recycler.apply {
            adapter = HistoryAdapter()
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    if (!recycler.isComputingLayout) {
        (recycler.adapter as HistoryAdapter).itemLists = history
    }
}

@BindingAdapter("setHistory", "column1", "column2", "column3", "column4")
fun setHistory(
    view: InstrumentOrderView,
    history: HistoryItem.PositionItem?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    history?.let {
        when (it.transactionType) {
            2 -> {
                view.setTitle("Deposit")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            3 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Processed")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            4 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Processing")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            5 -> {
                view.setTitle("Reverse Withdrawal")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            6 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Canceled")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            7 -> {
                view.setTitle("Compensation")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            11 -> {
                view.setTitle(it.detail ?: "")
                view.setDescription(it.master?.firstName ?: "")
                view.setTextColumnOne("$%.2f".format(it.invested))
            }
            else -> setPositionHistoryItem(
                view,
                history,
                column1 ?: "",
                column2 ?: "",
                column3 ?: "",
                column4 ?: ""
            )
        }
    }
}

private fun setPositionHistoryItem(
    view: InstrumentOrderView,
    history: HistoryItem.PositionItem,
    column1: String,
    column2: String,
    column3: String,
    column4: String,
) {
    // set text
    view.setTitle("${history.detail}")
    view.setTextColumnOne(setColumnPositionHistory(column1, history))
    view.setTextColumnTwo(setColumnPositionHistory(column2, history))
    view.setTextColumnThree(setColumnPositionHistory(column3, history))
    view.setTextColumnFour(setColumnPositionHistory(column4, history))
    // set color
    view.setTextColorColumnOne(getColumnColor(column1, history))
    view.setTextColorColumnTwo(getColumnColor(column2, history))
    view.setTextColorColumnThree(getColumnColor(column3, history))
    view.setTextColorColumnFour(getColumnColor(column4, history))
}

@BindingAdapter("historyColumns")
fun setHistoryColumns(
    recycler: RecyclerView,
    columns: List<String>?,
) {
    columns?.let {
        if (recycler.adapter != null) {
            (recycler.adapter as HistoryAdapter).apply {
                this.columns = columns
            }
        }
    }
}

private fun getColumnColor(column: String, history: HistoryItem.PositionItem?): Int =
    when (column.lowercase()) {
        "p/l", "p/l%" -> ColorChangingUtil.getTextColorChange(history?.pl ?: 0f)
        else -> R.color.awonar_color_text_primary
    }

private fun setColumnPositionHistory(column: String, history: HistoryItem.PositionItem?): String =
    when (column.lowercase()) {
        "invested" -> "$%.2f".format(history?.invested)
        "open" -> "$%.2f".format(history?.history?.position?.openRate)
        "close" -> "$%.2f".format(history?.history?.position?.closeRate)
        "p/l" -> "$%.2f".format(history?.pl)
        "units" -> "$%.2f".format(history?.history?.position?.units)
        "open time" -> "%s".format(DateUtils.getDate(history?.history?.position?.openDateTime))
        "close time" -> "%s".format(DateUtils.getDate(history?.history?.position?.closeDateTime))
        "p/l%" -> "%.2f%s".format(
            history?.plPercent, "%"
        )
        else -> ""
    }