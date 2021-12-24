package com.awonar.app.ui.user.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemIconBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.user.adapter.UserInfoItem
import com.awonar.app.utils.ImageUtil
import javax.inject.Inject

class SocialViewHolder @Inject constructor(private val binding: AwonarItemIconBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserInfoItem.SocialItem) {
        when {
            item.icon != null -> ImageUtil.loadImage(binding.awonarItemIconImageIcon, item.icon)
            item.iconRes > 0 -> binding.awonarItemIconImageIcon.setImageResource(item.iconRes)
        }

    }
}