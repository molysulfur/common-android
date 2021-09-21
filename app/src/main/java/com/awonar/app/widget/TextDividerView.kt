package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.awonar.app.R
import com.molysulfur.library.widget.BaseViewGroup
import com.awonar.app.databinding.AwonarWidgetTextDividerBinding

class TextDividerView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetTextDividerBinding

    private lateinit var textView: AppCompatTextView
    private var text: String? = null
    private var textRes: Int = 0

    fun setText(text: String) {
        this.text = text
        textRes = 0
        updateText()
    }

    fun setText(textRes: Int) {
        this.textRes = textRes
        text = null
        updateText()
    }

    override fun setup() {
        textView = findViewById(R.id.awonar_text_divider_text_title)
        updateText()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetTextDividerBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextDividerView)
        text = typedArray.getString(R.styleable.TextDividerView_textDividerView_setText)
        typedArray.recycle()

    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.text = text
        ss?.textRes = textRes
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        text = ss.text
        textRes = ss.textRes
        updateText()
    }

    private fun updateText() {
        when {
            !text.isNullOrBlank() -> {
                textView.text = text
            }
            textRes > 0 -> {
                textView.setText(textRes)
            }
        }
    }

    private class SavedState : ChildSavedState {

        var text: String? = null
        var textRes: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            text = parcel.readString()
            textRes = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
            out.writeInt(textRes)
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