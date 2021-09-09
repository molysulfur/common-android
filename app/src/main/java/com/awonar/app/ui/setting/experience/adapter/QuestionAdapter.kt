package com.awonar.app.ui.setting.experience.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Answer
import com.awonar.app.databinding.*
import com.awonar.app.ui.setting.experience.adapter.holder.*

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: List<ExperienceItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var onAnswer: ((String?, Answer?) -> Unit)? = null
    var onCheckboxChange: ((String?, List<Answer>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ExperienceItemType.EXPERIENCE_TITLE ->
                TitleQuestionViewHolder(
                    AwonarItemTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            ExperienceItemType.EXPERIENCE_SUBTITLE ->
                SubTitleQuestionViewHolder(
                    AwonarItemDescriptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            ExperienceItemType.EXPERIENCE_RADIO ->
                RadioQuestionViewHolder(
                    AwonarItemQuestionRadioGroupBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            ExperienceItemType.EXPERIENCE_CHECKBOX ->
                CheckBoxQuestionViewHolder(
                    AwonarItemQuestionRadioGroupBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            ExperienceItemType.EXPERIENCE_BLANK -> BlankViewHolder(
                AwonarItemDividerBlankBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("Exception is not found $viewType")

        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleQuestionViewHolder -> holder.bind(item as ExperienceItem.Title)
            is SubTitleQuestionViewHolder -> holder.bind(item as ExperienceItem.SubTitle)
            is RadioQuestionViewHolder -> holder.bind(
                item as ExperienceItem.RadioQuestion,
                onAnswer
            )
            is CheckBoxQuestionViewHolder -> holder.bind(
                item as ExperienceItem.CheckBoxQuestion,
                onCheckboxChange
            )
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position]?.type ?: -1

    override fun getItemCount(): Int = itemList.size
}