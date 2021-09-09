package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import coil.load
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R
import com.molysulfur.library.extension.readBooleanUsingCompat
import com.molysulfur.library.extension.writeBooleanUsingCompat
import com.molysulfur.library.widget.BaseViewGroup

class ImageCheckBoxView : BaseViewGroup {

    lateinit var checkBoxView: CheckBox
    lateinit var textView: TextView
    lateinit var imageView: ImageView

    private var isCheck: Boolean = false
    private var enable: Boolean = true
    private var icon: String? = null
    private var iconRes: Int = 0
    private var text: String? = null
    private var textRes: Int = 0

    var setOnCheckChangeListener: ((Boolean) -> Unit)? = null

    override fun setup() {
        checkBoxView = findViewById(R.id.awonar_image_checkbox_check_select)
        imageView = findViewById(R.id.awonar_image_checkbox_image_icon)
        textView = findViewById(R.id.awonar_image_checkbox_text_description)
        checkBoxView.setOnCheckedChangeListener { _, isChecked ->
            setOnCheckChangeListener?.invoke(isChecked)
        }
        updateText()
        updateIcon()
        updateChecked()
        updateEnable()
    }

    fun setEnable(enable: Boolean) {
        this.enable = enable
        updateEnable()
    }

    private fun updateEnable() {
        checkBoxView.isEnabled = enable
    }

    fun isChecked(isChecked: Boolean) {
        isCheck = isChecked
        updateChecked()
    }

    private fun updateChecked() {
        checkBoxView.isChecked = isCheck
    }

    fun setIcon(iconRes: Int) {
        this.iconRes = iconRes
        icon = null
        updateIcon()
    }

    private fun updateIcon() {
        when {
            icon != null -> {
                imageView.load(BuildConfig.BASE_IMAGE_URL + icon)
                imageView.visibility = VISIBLE

            }
            iconRes > 0 -> {
                imageView.setImageResource(iconRes)
                imageView.visibility = VISIBLE
            }
            else -> {
                imageView.visibility = GONE
            }
        }
    }

    fun setIcon(icon: String) {
        this.icon = icon
        iconRes = 0
        updateIcon()
    }

    fun setText(textRes: Int) {
        this.textRes = textRes
        text = null
        updateText()
    }

    private fun updateText() {
        when {
            text != null -> textView.text = text
            textRes > 0 -> textView.setText(textRes)
        }
    }

    fun setText(text: String) {
        this.text = text
        textRes = 0
        updateText()
    }

    override fun getLayoutResource(): Int = R.layout.awonar_widget_image_checkbox

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageCheckBoxView)
        text = typedArray.getString(R.styleable.ImageCheckBoxView_imageCheckBoxView_setText)
        iconRes =
            typedArray.getResourceId(R.styleable.ImageCheckBoxView_imageCheckBoxView_setIcon, 0)
        isCheck =
            typedArray.getBoolean(R.styleable.ImageCheckBoxView_imageCheckBoxView_isCheck, false)
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.enable = enable
        ss?.isCheck = isCheck
        ss?.text = text
        ss?.textRes = textRes
        ss?.icon = icon
        ss?.iconRes = iconRes
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        isCheck = ss.isCheck
        enable = ss.enable
        text = ss.text
        textRes = ss.textRes
        icon = ss.icon
        iconRes = ss.iconRes
        updateText()
        updateIcon()
        updateChecked()
        updateEnable()
    }

    private class SavedState : ChildSavedState {

        var isCheck: Boolean = false
        var enable: Boolean = true
        var icon: String? = null
        var iconRes: Int = 0
        var text: String? = null
        var textRes: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            isCheck = parcel.readBooleanUsingCompat()
            enable = parcel.readBooleanUsingCompat()
            icon = parcel.readString()
            iconRes = parcel.readInt()
            text = parcel.readString()
            textRes = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBooleanUsingCompat(isCheck)
            out.writeBooleanUsingCompat(enable)
            out.writeString(icon)
            out.writeInt(iconRes)
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