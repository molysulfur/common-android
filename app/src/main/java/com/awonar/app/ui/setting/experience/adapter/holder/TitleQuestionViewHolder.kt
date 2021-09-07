package com.awonar.app.ui.setting.experience.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem

class TitleQuestionViewHolder constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(titleItem: ExperienceItem.Title) {
        binding.text = titleItem.title
    }
}