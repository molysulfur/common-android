package com.awonar.app.widget

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.databinding.AwonarWidgetInfoBinding
import com.molysulfur.library.widget.BaseViewGroup


class InfoView : BaseViewGroup {

    private var title: String? = null
    private var titleRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0


    fun setTitle(title: String) {
        this.title = title
        titleRes = 0
        updateTitle()
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
        title = null
        updateTitle()
    }

    private fun updateTitle() {
        when {
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = context.getString(titleRes)
        }
    }

    fun setDescription(description: String) {
        this.description = description
        descriptionRes = 0
        updateDescription()
    }

    fun setDescription(descriptionRes: Int) {
        this.descriptionRes = descriptionRes
        description = null
        updateDescription()
    }

    private fun updateDescription() {
        when {
            description != null -> binding.description = description
            descriptionRes > 0 -> binding.description = context.getString(descriptionRes)
        }
    }

    private lateinit var binding: AwonarWidgetInfoBinding

    override fun setup() {
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetInfoBinding.inflate(
            LayoutInflater.from(context)
        )
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
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}