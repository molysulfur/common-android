package com.awonar.app.ui.setting.experience.adapter.holder

import android.annotation.SuppressLint
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Answer
import com.awonar.android.model.experience.QuestionAnswer
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionRadioGroupBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import timber.log.Timber

@SuppressLint("ResourceType")
class RadioQuestionViewHolder constructor(private val binding: AwonarItemQuestionRadioGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ExperienceItem.RadioQuestion, onAnswer: ((String?, Answer?) -> Unit)?) {
        addOption(item.option ?: emptyList(), item.questionAnswer)
        binding.awonarItemRadioQuestionRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val view: RadioButton =
                binding.awonarItemRadioQuestionRadioGroup.getChildAt(checkedId) as RadioButton
            val optionId: String = view.tag.toString()
            if (optionId.isNotBlank()) {
                onAnswer?.invoke(item.questionId, Answer(id = optionId, answer = null))
            }
        }
    }

    private fun addOption(options: List<QuestionOption?>, questionAnswer: QuestionAnswer?) {
        options.forEachIndexed { index, option ->
            val radioButton = RadioButton(binding.root.context).apply {
                id = index
                tag = option?.id ?: ""
                setPadding(0, 16, 16, 8)
                text = option?.text
                isChecked = questionAnswer?.answers?.find { it.id == option?.id } != null
            }
            binding.awonarItemRadioQuestionRadioGroup.addView(radioButton)
        }

    }

}