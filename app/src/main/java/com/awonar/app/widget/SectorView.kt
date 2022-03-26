package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetSectorBinding
import com.molysulfur.library.widget.BaseViewGroup

class SectorView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetSectorBinding

    var onClick: (() -> Unit)? = null

    var text: String? = null
        set(value) {
            if (value != null) {
                textRes = 0
                field = value
                updateText()
            }
        }

    var textRes: Int = 0
        set(value) {
            if (value > 0) {
                text = null
                field = value
                updateText()
            }
        }


    var textAction: String? = null
        set(value) {
            if (value != null) {
                textActionRes = 0
                field = value
                updateTextAction()
            }
        }

    var textActionRes: Int = 0
        set(value) {
            if (value > 0) {
                textAction = null
                field = value
                updateTextAction()
            }
        }

    private fun updateTextAction() {
        when {
            textAction != null -> binding.textAction = textAction
            textActionRes > 0 -> binding.textAction =
                context.getString(textActionRes)
        }
    }

    private fun updateText() {
        when {
            text != null -> binding.text = text
            textRes > 0 -> binding.text = context.getString(textRes)
        }
    }

    override fun setup() {
        binding.awonarWidgetSectorTextAction.setOnClickListener {
            onClick?.invoke()
        }
        updateText()
        updateTextAction()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetSectorBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.text = text
        ss?.textRes = textRes
        ss?.textAction = textAction
        ss?.textActionRes = textActionRes
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        text = ss.text
        textRes = ss.textRes
        textAction = ss.textAction
        textActionRes = ss.textActionRes
        updateTextAction()
        updateText()
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchItemView)
        text = typedArray.getString(R.styleable.SectorView_sectorView_setText)
        textAction =
            typedArray.getString(R.styleable.SectorView_sectorView_setActionText)
        typedArray.recycle()
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

    private class SavedState : ChildSavedState {

        var text: String? = null
        var textRes: Int = 0
        var textAction: String? = null
        var textActionRes: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            text = parcel.readString()
            textRes = parcel.readInt()
            textAction = parcel.readString()
            textActionRes = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
            out.writeInt(textRes)
            out.writeString(textAction)
            out.writeInt(textActionRes)
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
}