package com.awonar.app.ui.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemDividerBlankBinding
import com.awonar.app.databinding.AwonarItemLoadingBinding
import com.awonar.app.databinding.AwonarItemPaymentHistoryBinding
import com.awonar.app.ui.history.adapter.holder.LoadMoreViewHolder
import com.awonar.app.ui.payment.adapter.holder.BlankViewHolder
import com.awonar.app.ui.payment.adapter.holder.HistoryCardViewHolder
import com.awonar.app.ui.payment.adapter.holder.HistoryViewHolder
import com.awonar.app.ui.payment.adapter.holder.PaymentListViewHolder

class DepositAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<DepositItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((String) -> Unit)? = null
    var onHistoryClick: (() -> Unit)? = null
    var onLoadMore: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            DepositItemType.DEPOSITION_METHOD_TYPE -> PaymentListViewHolder(
                AwonarItemButtonItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DepositItemType.DEPOSITION_BLANK_TYPE -> BlankViewHolder(
                AwonarItemDividerBlankBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DepositItemType.DEPOSITION_HISTORY_TYPE -> HistoryCardViewHolder(
                AwonarItemPaymentHistoryBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DepositItemType.DEPOSITION_HISTORY_BUTTON_TYPE -> HistoryViewHolder(
                AwonarItemButtonItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DepositItemType.DEPOSITION_LOADMORE -> LoadMoreViewHolder(
                AwonarItemLoadingBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PaymentListViewHolder -> holder.bind(
                itemList[position] as DepositItem.MethodItem,
                onItemClick
            )
            is HistoryViewHolder -> holder.bind(onHistoryClick)
            is HistoryCardViewHolder -> holder.bind(item = itemList[position] as DepositItem.HistoryItem)
            is LoadMoreViewHolder -> {
                onLoadMore?.invoke()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun getItemCount(): Int = itemList.size
}