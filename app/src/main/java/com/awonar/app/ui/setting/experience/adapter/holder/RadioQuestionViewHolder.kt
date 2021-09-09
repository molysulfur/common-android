package com.awonar.app.ui.setting.experience.adapter.holder

import android.annotation.SuppressLint
import android.view.View
import android.widget.RadioButton
import androidx.core.view.allViews
import androidx.core.view.children
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Answer
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionRadioGroupBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("ResourceType")
class RadioQuestionViewHolder constructor(private val binding: AwonarItemQuestionRadioGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ExperienceItem.RadioQuestion, onAnswer: ((String?, Answer?) -> Unit)?) {
        addOption(item.option ?: emptyList())
        binding.awonarItemRadioQuestionRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val view: RadioButton =
                binding.awonarItemRadioQuestionRadioGroup.getChildAt(checkedId) as RadioButton
            val optionId: String = view.tag.toString()
            if (optionId.isNotBlank()) {
                onAnswer?.invoke(item.questionId, Answer(id = optionId, answer = null))
            }
        }
    }

    private fun addOption(options: List<QuestionOption?>) {
        options.forEachIndexed { index, option ->
            val radioButton = RadioButton(binding.root.context).apply {
                id = index
                tag = option?.id ?: ""
                setPadding(0, 16, 16, 8)
                text = option?.text
            }
            binding.awonarItemRadioQuestionRadioGroup.addView(radioButton)
        }

    }

}