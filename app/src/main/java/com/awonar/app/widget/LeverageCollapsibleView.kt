package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetCollapsibleLeverageBinding
import com.molysulfur.library.widget.BaseViewGroup

class LeverageCollapsibleView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCollapsibleLeverageBinding

    private var title: String? = null
    private var titleRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0
    private var expanded = false
    private val toggle: Transition = TransitionInflater.from(context)
        .inflateTransition(R.transition.awonar_transition_list_toggle)


    override fun setup() {
        binding.linearLayoutCompat.setOnClickListener {
            toggleExpanded()
        }
    }

    fun setDescription(descriptionRes: Int) {
        this.descriptionRes = descriptionRes
        description = null
        updateDescription()
    }

    fun setDescription(description: String) {
        this.description = description
        descriptionRes = 0
        updateDescription()
    }

    private fun updateDescription() {
        when{
            description != null -> binding.awonarLeverageCollapsibleTextDescription.text = description
            descriptionRes >0 -> binding.awonarLeverageCollapsibleTextDescription.setText(descriptionRes)
        }
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
        title = null
        updateTitle()
    }


    fun setTitle(title: String) {
        this.title = title
        titleRes = 0
        updateTitle()
    }

    private fun updateTitle() {
        when {
            title != null -> binding.awonarLeverageCollapsibleTextTitle.text = title
            titleRes >0 -> binding.awonarLeverageCollapsibleTextTitle.setText(titleRes)
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        binding.awonarLeverageCollapsibleRecyclerItem.layoutManager =
            GridLayoutManager(context, 4)
        binding.awonarLeverageCollapsibleRecyclerItem.adapter = adapter

    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCollapsibleLeverageBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }


    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        return state
    }

    override fun restoreInstanceState(state: Parcelable) {
    }

    private fun toggleExpanded() {
        expanded = !expanded
        toggle.duration = if (expanded) 300L else 200L
        TransitionManager.beginDelayedTransition(binding.root.parent as ViewGroup, toggle)
        binding.awonarLeverageCollapsibleRecyclerItem.visibility =
            if (expanded) View.VISIBLE else View.GONE
        binding.awonarLeverageCollapsibleImageIconExpand.rotationX = if (expanded) 180f else 0f
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


}