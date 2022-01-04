package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.databinding.AwonarWidgetCopiesCardViewBinding
import com.molysulfur.library.widget.BaseViewGroup

class CopiesCardView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCopiesCardViewBinding


    var title: String? = null
        set(value) {
            titleRes = 0
            field = value
            updateTitle()
        }

    var titleRes: Int = 0
        set(value) {
            title = null
            field = value
            updateTitle()
        }

    private fun updateTitle() {
        when {
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = context.getString(titleRes)
        }
    }

    var subTitle: String? = null
        set(value) {
            subTitleRes = 0
            field = value
            updateSubTitle()
        }

    var subTitleRes: Int = 0
        set(value) {
            subTitle = null
            field = value
            updateSubTitle()
        }

    private fun updateSubTitle() {
        when {
            subTitle != null -> binding.subTitle = subTitle
            subTitleRes > 0 -> binding.subTitle = context.getString(subTitleRes)
        }
    }

    var description: String? = null
        set(value) {
            descriptionRes = 0
            field = value
            updateDescription()
        }

    var descriptionRes: Int = 0
        set(value) {
            description = null
            field = value
            updateDescription()
        }

    private fun updateDescription() {
        when {
            description != null -> binding.description = description
            descriptionRes > 0 -> binding.description = context.getString(descriptionRes)
        }
    }

    var change: Float = 0f
        set(value) {
            field = value
            updateChange()
        }

    private fun updateChange() {
        binding.change = "%.2f%s".format(change, "%")
    }

    var risk: Int = 0
        set(value) {
            field = value
            updateRisk()
        }

    private fun updateRisk() {
        binding.risk = "%s".format(risk)
    }


    override fun setup() {
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCopiesCardViewBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        return state
    }

    override fun restoreInstanceState(state: Parcelable) {
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