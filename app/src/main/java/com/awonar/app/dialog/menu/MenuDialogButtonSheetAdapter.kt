package com.awonar.app.dialog.menu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding

@SuppressLint("NotifyDataSetChanged")
class MenuDialogButtonSheetAdapter(private val onClick: ((MenuDialog) -> Unit)?) :
    RecyclerView.Adapter<MenuDialogButtonSheetViewHolder>() {

    var itemList: List<MenuDialog> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MenuDialogButtonSheetViewHolder =
        MenuDialogButtonSheetViewHolder(AwonarItemButtonItemBinding.inflate(LayoutInflater.from(
            parent.context), parent, false))

    override fun onBindViewHolder(holder: MenuDialogButtonSheetViewHolder, position: Int) {
        holder.bind(itemList[position], onClick)
    }

    override fun getItemCount(): Int = itemList.size
}