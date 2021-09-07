package com.awonar.app.ui.setting.experience.adapter.holder

import android.annotation.SuppressLint
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionCheckboxBinding
import com.awonar.app.databinding.AwonarItemQuestionRadioBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem

class CheckBoxQuestionViewHolder constructor(private val binding: AwonarItemQuestionCheckboxBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ExperienceItem.CheckBoxQuestion) {
        binding.title = item.title
        binding.subTitle = item.subTitle
        addOption(item.option)
    }

    private fun addOption(options: List<QuestionOption?>) {
        options.forEach { option ->
            val radioButton = CheckBox(binding.root.context).apply {
                text = option?.text
            }
            binding.awonarItemCheckboxQuestionRadioGroup.addView(radioButton)
        }
    }

}