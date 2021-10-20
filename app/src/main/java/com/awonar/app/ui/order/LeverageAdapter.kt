package com.awonar.app.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding

class LeverageAdapter : RecyclerView.Adapter<LeverageViewHolder>() {

    var leverageString: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeverageViewHolder =
        LeverageViewHolder(AwonarItemButtonItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: LeverageViewHolder, position: Int) {
        holder.bind(leverageString[position], onClick)
    }

    override fun getItemCount(): Int = leverageString.size
}