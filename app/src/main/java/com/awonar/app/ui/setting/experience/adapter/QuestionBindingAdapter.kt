package com.awonar.app.ui.setting.experience.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.experience.Topic
import timber.log.Timber

@BindingAdapter("setQuestions")
fun setQuestionAdapter(recycler: RecyclerView, items: List<ExperienceItem?>) {
    var adapter: QuestionAdapter? = recycler.adapter as? QuestionAdapter
    if (adapter == null) {
        adapter = QuestionAdapter()
        recycler.apply {
            this.adapter = adapter
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    adapter.itemList = items

}