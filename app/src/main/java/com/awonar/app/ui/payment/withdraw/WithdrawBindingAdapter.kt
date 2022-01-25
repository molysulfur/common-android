package com.awonar.app.ui.payment.withdraw

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.payment.Deposit
import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.model.payment.Withdraw
import com.awonar.app.ui.payment.adapter.DepositAdapter
import com.awonar.app.ui.payment.adapter.DepositItem
import com.awonar.app.ui.payment.deposit.DepositViewModel

@BindingAdapter("desinationText")
fun setDesinationText(
    textView: AppCompatTextView,
    text: String?,
) {
    if (text == null) {
        textView.visibility = View.GONE
    } else {
        var html = "<p>Your verification code will be sent to</p>"
        html += "<font color=\"#1A4C8E\">%s</font>".format(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(html,
                Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(html)
        }
        textView.text = text
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("withDrawHistory", "page", "viewModel")
fun setWithdrawHistoryAdapter(
    recycler: RecyclerView,
    withdraw: List<Withdraw>,
    page: Int,
    viewModel: WithdrawViewModel,
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = DepositAdapter().apply {
                onLoadMore = {
                    viewModel.getHistory()
                }
            }
        }
    }
    val itemList: MutableList<DepositItem> = withdraw.map {
        DepositItem.HistoryItem(
            "${it.withdrawNo}",
            it.createdAt,
            it.paymentMethod?.name,
            it.status,
            it.dollarAmount.times(-1)
        )
    }.toMutableList()
    if (page > 0) {
        itemList.add(DepositItem.LoadMoreItem())
    }
    (recycler.adapter as DepositAdapter).itemList = itemList

}


@BindingAdapter("setMethodAdapter", "viewModel")
fun setMethodAdapter(
    recycler: RecyclerView,
    payments: List<MethodPayment>,
    viewModel: WithdrawViewModel,
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = DepositAdapter().apply {
                onItemClick = {
                    viewModel.navigate(it)
                }
                onHistoryClick = {
                    viewModel.navigate("history")
                }
            }
        }
    }
    val itemList: MutableList<DepositItem> = payments.map {
        DepositItem.MethodItem(it.id ?: "", "", it.paymentChannel ?: "")
    }.toMutableList()

    itemList.add(DepositItem.BlankItem())
    itemList.add(DepositItem.HistoryButtonItem())
    (recycler.adapter as DepositAdapter).itemList = itemList.toMutableList()
}