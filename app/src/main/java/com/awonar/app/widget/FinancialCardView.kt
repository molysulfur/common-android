package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.databinding.AwonarWidgetFacebookBinding
import com.awonar.app.databinding.AwonarWidgetFinancialCardBinding
import com.molysulfur.library.widget.BaseViewGroup

class FinancialCardView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetFinancialCardBinding

    private var title: String? = null
    private var titleRes: Int = 0
    private var dateString: String? = null
    private var value: String? = null

    fun setValue(value: String) {
        this.value = value
        updateValue()
    }

    private fun updateValue() {
        binding.value = value
    }

    fun setDate(dateString: String) {
        this.dateString = dateString
        updateDateString()
    }

    private fun updateDateString() {
        binding.dateString = dateString
    }


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


    override fun setup() {
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetFinancialCardBinding.inflate(
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
