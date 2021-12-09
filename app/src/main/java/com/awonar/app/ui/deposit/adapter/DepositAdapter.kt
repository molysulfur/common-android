package com.awonar.app.ui.deposit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.deposit.adapter.holder.PaymentListViewHolder

class DepositAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<DepositItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            DepositItemType.DEPOSITION_METHOD_TYPE -> PaymentListViewHolder(
                AwonarItemListBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PaymentListViewHolder -> holder.bind(itemList[position] as DepositItem.MethodItem,onItemClick)
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun getItemCount(): Int = itemList.size
}