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
import androidx.core.content.ContextCompat
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetVerticalTitleBinding
import com.molysulfur.library.widget.BaseViewGroup

class VerticalTitleView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetVerticalTitleBinding

    private lateinit var titleTextView: AppCompatTextView
    private lateinit var subTitleTextView: AppCompatTextView

    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var titleTextColor: Int = 0
    private var subTitleTextColor: Int = 0

    fun setSubTitleTextColor(color: Int) {
        subTitleTextColor = color
        updateSubTitleTextColor()
    }

    private fun updateSubTitleTextColor() {
        when {
            subTitleTextColor > 0 -> subTitleTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    subTitleTextColor
                )
            )
        }
    }

    fun setTitleTextColor(color: Int) {
        titleTextColor = color
        updateTitleTextColor()
    }

    private fun updateTitleTextColor() {
        when {
            titleTextColor > 0 -> titleTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    titleTextColor
                )
            )
        }
    }

    fun setSubTitle(subTitleRes: Int) {
        this.subTitleRes = subTitleRes
        subTitle = null
        updateSubTitle()
    }

    fun setSubTitle(subTitle: String) {
        this.subTitle = subTitle
        subTitleRes = 0
        updateSubTitle()
    }

    private fun updateSubTitle() {
        when {
            subTitle != null -> subTitleTextView.text = subTitle
            subTitleRes > 0 -> subTitleTextView.setText(subTitleRes)
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
            title != null -> titleTextView.text = title
            titleRes > 0 -> titleTextView.setText(titleRes)
        }
    }

    override fun setup() {
        titleTextView = findViewById(R.id.awonar_veritical_title_text_title)
        subTitleTextView = findViewById(R.id.awonar_veritical_title_text_subtitle)
        updateTitle()
        updateSubTitle()
        updateTitleTextColor()
        updateSubTitleTextColor()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetVerticalTitleBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTitleView)
        title = typedArray.getString(R.styleable.VerticalTitleView_verticalTitleView_setTitle)
        subTitle =
            typedArray.getString(R.styleable.VerticalTitleView_verticalTitleView_setSubTitle)
        titleTextColor = typedArray.getColor(
            R.styleable.VerticalTitleView_verticalTitleView_titleTextColor,
            0
        )
        subTitleTextColor = typedArray.getColor(
            R.styleable.VerticalTitleView_verticalTitleView_subTitleTextColor,
            0
        )
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.titleTextColor = titleTextColor
        ss?.subTitleTextColor = subTitleTextColor
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        title = ss.title
        titleRes = ss.titleRes
        subTitle = ss.subTitle
        subTitleRes = ss.subTitleRes
        titleTextColor = ss.titleTextColor
        subTitleTextColor = ss.subTitleTextColor
        updateTitle()
        updateSubTitle()
        updateTitleTextColor()
        updateSubTitleTextColor()
    }

    private class SavedState : ChildSavedState {

        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var titleTextColor: Int = 0
        var subTitleTextColor: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            titleTextColor = parcel.readInt()
            subTitleTextColor = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeInt(titleTextColor)
            out.writeInt(subTitleTextColor)

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