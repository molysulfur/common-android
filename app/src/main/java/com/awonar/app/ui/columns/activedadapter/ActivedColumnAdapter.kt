package com.awonar.app.ui.columns.activedadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding

class ActivedColumnAdapter : RecyclerView.Adapter<ColumnItemListViewHolder>() {

    var itemList: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnItemListViewHolder =
        ColumnItemListViewHolder(
            AwonarItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ColumnItemListViewHolder, position: Int) {
        holder.bind(itemList[position], onClick)
    }

    override fun getItemCount(): Int = itemList.size

}