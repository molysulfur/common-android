package com.awonar.app.ui.search.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSearchItemBinding
import com.awonar.app.ui.marketprofile.setAvatar
import com.awonar.app.ui.search.adapter.SearchItem

class SearchItemViewHolder constructor(private val binding: AwonarItemSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(
        listItem: SearchItem.ListItem,
        onTradeClick: ((String?) -> Unit)?,
        onFollowClick: ((Boolean) -> Unit)?,
        onItemClick: ((String?, Boolean) -> Unit)?,
    ) {

        with(binding.awonarItemSearchItemList) {
            setOnClickListener {
                onItemClick?.invoke(listItem.data?.id, listItem.isMarket)
            }
            listItem.data?.apply {
                setTitle(title ?: "")
                setAvatar(picture ?: "")
                setSubTitle(description ?: "")
                setButtonText(description ?: "")
                isButtonSelected(isFollow)
            }
            when {
                listItem.isMarket -> {
                    setButtonText("Trade")
                }
                listItem.data?.isFollow == true -> {
                    setButtonText("Following")
                }
                else -> {
                    setButtonText("Follow")
                }
            }
            onButtonClick = {
                when (listItem.isMarket) {
                    true -> onTradeClick?.invoke(listItem.data?.id)
                    else -> onFollowClick?.invoke(listItem.data?.isFollow != true)
                }
            }
        }

    }
}