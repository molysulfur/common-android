package com.awonar.app.ui.payment.deposit

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.payment.Deposit
import com.awonar.android.model.payment.MethodPayment
import com.awonar.app.ui.payment.adapter.DepositAdapter
import com.awonar.app.ui.payment.adapter.DepositItem

@BindingAdapter("depositHistory", "page", "viewModel")
fun setDepositHistoryAdapter(
    recycler: RecyclerView,
    deposits: List<Deposit>,
    page: Int,
    viewModel: DepositViewModel
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
    val itemList: MutableList<DepositItem> = deposits.map {
        DepositItem.HistoryItem(
            "${it.depositNo}",
            it.createdAt,
            it.paymentMethod?.name,
            it.status,
            it.dollarAmount
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
    viewModel: DepositViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = DepositAdapter().apply {
                onItemClick = {
                    viewModel.navigateDepositFragment(it)
                }

                onHistoryClick = {
                    viewModel.navigateDepositFragment("history")
                }
            }
        }
    }
    val itemList: MutableList<DepositItem> = payments.map {
        DepositItem.MethodItem(it.id ?: "", "", it.paymentChannel ?: "")
    }.toMutableList()
    itemList.add(DepositItem.BlankItem())
    itemList.add(DepositItem.HistoryButtonItem())
    (recycler.adapter as DepositAdapter).itemList = itemList
}