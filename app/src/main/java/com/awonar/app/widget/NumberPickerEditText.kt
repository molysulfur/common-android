package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import com.awonar.app.databinding.AwonarWidgetEditNumberPickerBinding
import com.molysulfur.library.widget.BaseViewGroup

class NumberPickerEditText : BaseViewGroup {

    private var prefix: String = "$"
    private var number: Float = 0f
    private var helper: String = "Monimun position size is $5.00"
    private lateinit var binding: AwonarWidgetEditNumberPickerBinding

    override fun setup() {
        binding.awonarEditNumberPickerEditNumber.helperText = helper
        binding.awonarEditNumberPickerEditNumber.editText?.doAfterTextChanged {
            val numberText = it.toString().replace(prefix, "")
            number = if (!numberText.isNullOrBlank()) {
                numberText.toFloat()
            } else {
                0f
            }
        }
        binding.awonarEditNumberPickerImageMinus.setOnClickListener {
            number--
            updateNumber()
        }

        binding.awonarEditNumberPickerImagePlus.setOnClickListener {
            number++
            updateNumber()
        }
    }

    private fun updateNumber() {
        binding.awonarEditNumberPickerEditNumber.editText?.setText("$prefix $number")
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetEditNumberPickerBinding.inflate(LayoutInflater.from(context))
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