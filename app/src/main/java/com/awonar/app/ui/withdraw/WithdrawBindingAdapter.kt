package com.awonar.app.ui.withdraw

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.payment.MethodPayment
import com.awonar.app.ui.deposit.DepositViewModel
import com.awonar.app.ui.deposit.adapter.DepositAdapter
import com.awonar.app.ui.deposit.adapter.DepositItem


@BindingAdapter("setMethodAdapter", "viewModel")
fun setMethodAdapter(
    recycler: RecyclerView,
    payments: List<MethodPayment>,
    viewModel: WithdrawViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = DepositAdapter().apply {
                onItemClick = {
                    viewModel.navigate(it)
                }
            }
        }
    }
    val itemList: List<DepositItem> = payments.map {
        DepositItem.MethodItem(it.id ?: "", "", it.paymentChannel ?: "")
    }
    (recycler.adapter as DepositAdapter).itemList = itemList.toMutableList()
}