package com.awonar.app.ui.setting.experience.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Answer
import com.awonar.android.model.experience.QuestionOption
import com.awonar.app.databinding.AwonarItemQuestionRadioGroupBinding
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import com.awonar.app.widget.ImageCheckBoxView
import timber.log.Timber

class CheckBoxQuestionViewHolder constructor(private val binding: AwonarItemQuestionRadioGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var checkedList = arrayListOf<Answer>()

    fun bind(item: ExperienceItem.CheckBoxQuestion, onAnswer: ((String?, List<Answer>) -> Unit)?) {
        item.option?.forEachIndexed { index, option ->
            val radioButton = ImageCheckBoxView(binding.root.context).apply {
                setText(option?.text ?: "")
                id = index
                tag = option?.id ?: "NONE"
                setEnable(true)
                option?.image?.let {
                    setIcon(it)
                }
                setOnCheckChangeListener = { isChecked ->
                    if (this.tag == "NONE") {
                        checkedList = arrayListOf()
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
                    } else {
                        if (checkedList.findLast { it.id == tag } == null) {
                            checkedList.add(Answer(id = tag.toString()))
                        } else {
                            checkedList = checkedList.filter { it.id != tag } as ArrayList<Answer>
                        }
                    }
                    onAnswer?.invoke(option?.questionId, checkedList)
                }
            }
            binding.awonarItemRadioQuestionRadioGroup.addView(radioButton)
        }
    }

}