package com.awonar.app.ui.deposit

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.payment.MethodPayment
import com.awonar.app.ui.deposit.adapter.DepositAdapter
import com.awonar.app.ui.deposit.adapter.DepositItem
import timber.log.Timber


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
            }
        }
    }
    val itemList: List<DepositItem> = payments.map {
        Timber.e("$it")
        DepositItem.MethodItem("", it.paymentChannel ?: "")
    }
    (recycler.adapter as DepositAdapter).itemList = itemList.toMutableList()

}