package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.databinding.AwonarWidgetStepBinding
import com.molysulfur.library.widget.BaseViewGroup
import timber.log.Timber

class StepView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetStepBinding

    var label: String? = null
        set(value) {
            if (value != field)
                field = value
            setLabel()
        }

    var viewState: STATE = STATE.CENTER
        set(value) {
            if (value != field) {
                field = value
                lineVisible()
            }
        }

    var onChecked: (() -> Unit)? = null

    private fun setLabel() {
        binding.awonarWidgetStepRadioItem.text = label
    }

    private fun lineVisible() {
        when (viewState) {
            STATE.START -> {
                binding.awonarWidgetStepLineStart.visibility = View.GONE
                binding.awonarWidgetStepLineEnd.visibility = View.VISIBLE
            }
            STATE.CENTER -> {
                binding.awonarWidgetStepLineStart.visibility = View.VISIBLE
                binding.awonarWidgetStepLineEnd.visibility = View.VISIBLE
            }
            STATE.END -> {
                binding.awonarWidgetStepLineStart.visibility = View.VISIBLE
                binding.awonarWidgetStepLineEnd.visibility = View.GONE
            }
        }
    }

    private var isChecked = false

    fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
        updateChecked()
    }

    private fun updateChecked() {
        binding.awonarWidgetStepRadioItem.isChecked = isChecked
    }


    override fun setup() {
        binding.awonarWidgetStepRadioItem.setOnClickListener {
            onChecked?.invoke()
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetStepBinding.inflate(LayoutInflater.from(context))
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


    enum class STATE {
        START,
        CENTER,
        END
    }
}