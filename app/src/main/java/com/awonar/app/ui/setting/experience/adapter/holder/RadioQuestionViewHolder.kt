package com.awonar.app.ui.setting.experience.adapter.holder

import android.annotation.SuppressLint
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionRadioBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem

@SuppressLint("ResourceType")
class RadioQuestionViewHolder constructor(private val binding: AwonarItemQuestionRadioBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ExperienceItem.RadioQuestion) {
        binding.title = item.title
        binding.subTitle = item.subTitle
        addOption(item.option)
    }

    private fun addOption(options: List<QuestionOption?>) {
        options.forEach { option ->
            val radioButton = RadioButton(binding.root.context).apply {
                text = option?.text
            }
            binding.awonarItemRadioQuestionRadioGroupAnswer.addView(radioButton)
        }
    }

}