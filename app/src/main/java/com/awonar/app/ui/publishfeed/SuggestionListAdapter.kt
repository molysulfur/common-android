package com.awonar.app.ui.publishfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible

class SuggestionListAdapter constructor(private val itemLists: MutableList<Suggestible>) :
    RecyclerView.Adapter<SuggestionListAdapter.SuggestionItemViewHolder>() {

    var onClick: ((MentionSuggestion) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionItemViewHolder =
        SuggestionItemViewHolder(
            AwonarItemButtonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SuggestionItemViewHolder, position: Int) {
        holder.bind(itemLists[position] as MentionSuggestion, onClick)
    }

    override fun getItemCount(): Int = itemLists.size

    class SuggestionItemViewHolder(private val binding: AwonarItemButtonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: MentionSuggestion, onClick: ((MentionSuggestion) -> Unit)?) {
            binding.text = item.suggestion
            with(binding.awonarButtonItemButtonList) {
                setOnClickListener {
                    onClick?.invoke(item)
                }
            }
        }

    }
}