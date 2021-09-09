package com.awonar.app.ui.setting.experience.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.setting.experience.ExperienceViewModel

@BindingAdapter("setQuestions", "viewModel")
fun setQuestionAdapter(
    recycler: RecyclerView,
    items: List<ExperienceItem?>,
    viewModel: ExperienceViewModel
) {
    var adapter: QuestionAdapter? = recycler.adapter as? QuestionAdapter
    if (adapter == null) {
        adapter = QuestionAdapter()
        (adapter as QuestionAdapter).apply {
            onAnswer = { questionId, answer ->
                viewModel.addAnswer(questionId = questionId, answer = answer)
            }
            onCheckboxChange = { questionId, answers ->
                viewModel.addAnswer(questionId = questionId, answer = answers)
            }
        }
        recycler.apply {
            this.adapter = adapter
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    adapter.itemList = items

}