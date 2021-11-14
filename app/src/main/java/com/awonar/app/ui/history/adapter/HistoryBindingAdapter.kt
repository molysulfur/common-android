package com.awonar.app.ui.history.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.history.History
import com.awonar.app.utils.DateUtils
import com.awonar.app.widget.InstrumentOrderView

@BindingAdapter("setHistory", "column1", "column2", "column3", "column4")
fun setHistory(
    view: InstrumentOrderView,
    history: History?,
    column1: String,
    column2: String,
    column3: String,
    column4: String,
) {
    history?.let {
        when (it.transactionType) {
            2 -> {
                view.setTitle("Deposit")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            3 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Processed")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            4 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Processing")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            5 -> {
                view.setTitle("Reverse Withdrawal")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            6 -> {
                view.setTitle("Withdrawal")
                view.setDescription("Canceled")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            7 -> {
                view.setTitle("Compensation")
                view.setDescription("")
                view.setTextColumnOne("$%.2f".format(it.amount))
            }
            else -> setPositionHistoryItem(view, history, column1, column2, column3, column4)
        }
    }
}


private fun setPositionHistoryItem(
    view: InstrumentOrderView,
    history: History,
    column1: String,
    column2: String,
    column3: String,
    column4: String,
) {
    view.setTitle("${if (history.position?.isBuy == true) "BUY" else "SELL"} ${history.position?.instrument?.symbol}")
    view.setTextColumnOne(setColumnPositionHistory(column1, history))
    view.setTextColumnTwo(setColumnPositionHistory(column2, history))
    view.setTextColumnThree(setColumnPositionHistory(column3, history))
    view.setTextColumnFour(setColumnPositionHistory(column4, history))
}

@BindingAdapter("historyColumns")
fun setHistoryColumns(
    recycler: RecyclerView,
    columns: List<String>
) {
    if (recycler.adapter != null) {
        (recycler.adapter as HistoryAdapter).apply {
            this.columns = columns
        }
    }
}

private fun setColumnPositionHistory(column: String, history: History?): String =
    when (column.lowercase()) {
        "invested" -> "$%.2f".format(history?.amount)
        "open" -> "$%.2f".format(history?.position?.openRate)
        "close" -> "$%.2f".format(history?.position?.closeRate)
        "p/l" -> "$%.2f".format(history?.position?.netProfit)
        "units" -> "$%.2f".format(history?.position?.units)
        "open time" -> "$%.2f".format(DateUtils.getDate(history?.position?.openDateTime))
        "close time" -> "$%.2f".format(DateUtils.getDate(history?.position?.closeDateTime))
        "p/l(%)" -> "$%.2f".format(
            history?.position?.netProfit?.times(100)?.div(history.amount)
        )
        else -> ""
    }