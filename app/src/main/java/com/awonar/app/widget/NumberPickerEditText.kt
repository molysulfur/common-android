package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetEditNumberPickerBinding
import com.molysulfur.library.extension.readBooleanUsingCompat
import com.molysulfur.library.extension.writeBooleanUsingCompat
import com.molysulfur.library.widget.BaseViewGroup
import timber.log.Timber

class NumberPickerEditText : BaseViewGroup {

    private var prefix: String = "$"
    private var number: Float = 0f
    private var helper: String = "Minimun position size is $5.00"
    private var placeholder: String? = null
    private var placeholderRes: Int = 0
    private var isEnablePlaceholder: Boolean = false
    private lateinit var binding: AwonarWidgetEditNumberPickerBinding

    var doAfterFocusChange: ((Float, Boolean) -> Unit)? = null

    override fun setup() {
        updatePlaceHolder()
        updatePlaceHolderEnable()
        binding.awonarEditNumberPickerEditNumber.editText?.setOnFocusChangeListener { v, hasFocus ->
            doAfterFocusChange?.invoke(number, hasFocus)
        }
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

    fun setPlaceHolderEnable(placeholderEnable: Boolean) {
        this.isEnablePlaceholder = placeholderEnable
        updatePlaceHolderEnable()
    }

    private fun updatePlaceHolderEnable() {
        binding.awonarEditNumberPickerLayoutInputContainer.visibility =
            if (!isEnablePlaceholder) View.VISIBLE else View.GONE
        binding.awonarEditNumberPickerTextPlaceholder.visibility =
            if (isEnablePlaceholder) View.VISIBLE else View.GONE
    }

    fun setPlaceholder(placeholderRes: Int) {
        this.placeholderRes = placeholderRes
        placeholder = null
        updatePlaceHolder()
    }

    fun setPlaceholder(placeholder: String) {
        this.placeholder = placeholder
        placeholderRes = 0
        updatePlaceHolder()
    }

    private fun updatePlaceHolder() {
        when {
            placeholder != null -> binding.awonarEditNumberPickerTextPlaceholder.text = placeholder
            placeholderRes > 0 -> binding.awonarEditNumberPickerTextPlaceholder.setText(
                placeholderRes
            )
        }
    }

    fun setNumber(number: Float) {
        this.number = number
        updateNumber()
    }

    private fun updateNumber() {
        binding.awonarEditNumberPickerEditNumber.editText?.setText("$prefix $number")
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetEditNumberPickerBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerEditText)
        prefix = typedArray.getString(R.styleable.NumberPickerEditText_numberPickerEditText_setPrefix)?:""
        number = typedArray.getFloat(R.styleable.NumberPickerEditText_numberPickerEditText_setNumber,0f)
        helper = typedArray.getString(R.styleable.NumberPickerEditText_numberPickerEditText_setHelper)?:""
        placeholder = typedArray.getString(R.styleable.NumberPickerEditText_numberPickerEditText_setPlaceHolder)?:""
        isEnablePlaceholder = typedArray.getBoolean(R.styleable.NumberPickerEditText_numberPickerEditText_setEnablePlaceHolder,false)
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.prefix = prefix
        ss?.number = number
        ss?.helper = helper
        ss?.placeholder = placeholder
        ss?.placeholderRes = placeholderRes
        ss?.isEnablePlaceholder = isEnablePlaceholder
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        prefix = ss.prefix ?: "$"
        number = ss.number
        helper = ss.helper ?: ""
        placeholder = ss.placeholder
        placeholderRes = ss.placeholderRes
        isEnablePlaceholder = ss.isEnablePlaceholder
        updatePlaceHolder()
        updatePlaceHolderEnable()
        updateNumber()
    }


    private class SavedState : ChildSavedState {

        var prefix: String? = "$"
        var number: Float = 0f
        var helper: String? = "Monimun position size is $5.00"
        var placeholder: String? = null
        var placeholderRes: Int = 0
        var isEnablePlaceholder = false

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            prefix = parcel.readString()
            number = parcel.readFloat()
            helper = parcel.readString()
            placeholder = parcel.readString()
            placeholderRes = parcel.readInt()
            isEnablePlaceholder = parcel.readBooleanUsingCompat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(prefix)
            out.writeFloat(number)
            out.writeString(helper)
            out.writeString(placeholder)
            out.writeInt(placeholderRes)
            out.writeBooleanUsingCompat(isEnablePlaceholder)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> =
                object : Parcelable.ClassLoaderCreator<SavedState> {
                    override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                        return SavedState(source, loader)
                    }

                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return null
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
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