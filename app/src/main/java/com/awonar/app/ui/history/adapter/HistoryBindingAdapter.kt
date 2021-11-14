package com.awonar.app.ui.history.adapter

import androidx.databinding.BindingAdapter
import com.awonar.android.model.history.History
import com.awonar.app.widget.InstrumentOrderView

@BindingAdapter("setHistory")
fun setHistory(
    view: InstrumentOrderView,
    history: History?
) {
    history?.let {
        view.setTitle("${if (history.position?.isBuy == true) "BUY" else "SELL"} ${history.position?.instrument?.symbol}")
        view.setTextColumnOne("$%.2f".format(history.amount))
        view.setTextColumnTwo("%s".format(history.position?.openRate))
        view.setTextColumnThree("$%s".format(history.createdAt))
        view.setTextColumnFour("$%.2f".format(history.position?.netProfit))
    }
}