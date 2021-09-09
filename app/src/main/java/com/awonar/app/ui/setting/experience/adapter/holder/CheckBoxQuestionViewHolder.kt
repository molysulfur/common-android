package com.awonar.app.ui.setting.experience.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Answer
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionRadioGroupBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import com.awonar.app.widget.ImageCheckBoxView

class CheckBoxQuestionViewHolder constructor(private val binding: AwonarItemQuestionRadioGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ExperienceItem.CheckBoxQuestion, onAnswer: ((String?, Answer?) -> Unit)?) {
        addOption(item.option ?: emptyList(), onAnswer, item.topicId)
    }

    private fun addOption(
        options: List<QuestionOption?>,
        onAnswer: ((String?, Answer?) -> Unit)?,
        topicId: String
    ) {
        options.forEach { option ->
            val radioButton = ImageCheckBoxView(binding.root.context).apply {
                setText(option?.text ?: "")
                tag = option?.id ?: "NONE"
                setEnable(true)
                option?.image?.let {
                    setIcon(it)
                }
                setOnCheckChangeListener = { isChecked ->
                    if (this.tag == "NONE") {
                        val childCount = binding.awonarItemRadioQuestionRadioGroup.childCount
                        for (i in 0 until childCount - 1) {
                            (binding.awonarItemRadioQuestionRadioGroup.getChildAt(i) as ImageCheckBoxView)
                                .apply {
                                    isChecked(false)
                                    setEnable(
                                        !isChecked
                                    )
                                }
                        }
                    }
                    if (option?.id == null) {
                        onAnswer?.invoke(topicId, Answer(id = null))
                    } else {
                        option.id?.let {
                            onAnswer?.invoke(topicId, Answer(id = it))
                        }
                    }
                }
            }
            binding.awonarItemRadioQuestionRadioGroup.addView(radioButton)
        }
    }

}