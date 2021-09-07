package com.awonar.app.ui.setting.experience.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDescriptionBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import timber.log.Timber

class SubTitleQuestionViewHolder constructor(private val binding: AwonarItemDescriptionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(titleItem: ExperienceItem.SubTitle) {
        binding.text = titleItem.subTitle
    }
}