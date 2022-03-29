package com.awonar.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetListItemBinding
import com.awonar.app.databinding.AwonarWidgetSearchItemBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.readBooleanUsingCompat
import com.molysulfur.library.extension.writeBooleanUsingCompat
import com.molysulfur.library.widget.BaseViewGroup

class SearchItemView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetSearchItemBinding

    private var avatar: String? = null
    private var avatarRes: Int = 0
    private var buttonText: String? = null
    private var buttonTextRes: Int = 0
    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var buttonSelected: Boolean = false

    var onButtonClick: (() -> Unit)? = null


    override fun setup() {
        binding.awoanrSearchItemViewButton.setOnClickListener {
            onButtonClick?.invoke()
        }
        updateAvatar()
        updateTitle()
        updateSubTitle()
        updateButtonText()
        updateButtonSelected()
    }

    fun isButtonSelected(isSelected: Boolean) {
        buttonSelected = isSelected
        updateButtonSelected()
    }

    private fun updateButtonSelected() {
        binding.awoanrSearchItemViewButton.isSelected = buttonSelected
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
        avatarRes = 0
        updateAvatar()
    }

    fun setAvatar(avatarRes: Int) {
        this.avatarRes = avatarRes
        avatar = null
        updateAvatar()
    }


    fun setButtonText(text: String) {
        this.buttonText = text
        buttonTextRes = 0
        updateButtonText()
    }

    fun setButtonText(textRes: Int) {
        this.buttonTextRes = textRes
        buttonText = null
        updateButtonText()
    }

    private fun updateButtonText() {
        when {
            !buttonText.isNullOrBlank() -> {
                binding.buttonText = this.buttonText

            }
            buttonTextRes > 0 -> {
                binding.buttonText =
                    resources.getString(buttonTextRes)
            }
        }
    }

    fun setSubTitle(subTitle: String) {
        this.subTitle = subTitle
        subTitleRes = 0
        updateSubTitle()
    }

    fun setSubTitle(subTitleRes: Int) {
        this.subTitleRes = subTitleRes
        subTitle = null
        updateSubTitle()
    }

    private fun updateSubTitle() {
        when {
            !subTitle.isNullOrBlank() -> {
                binding.subTitle = this.subTitle
                binding.awoanrSearchItemViewTextSubTitle.visibility = View.VISIBLE

            }
            subTitleRes > 0 -> {
                binding.subTitle =
                    resources.getString(subTitleRes)
                binding.awoanrSearchItemViewTextSubTitle.visibility = View.VISIBLE
            }
            else -> binding.awoanrSearchItemViewTextSubTitle.visibility = View.GONE
        }
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
            !title.isNullOrBlank() -> {
                binding.title = this.title
                binding.awoanrSearchItemViewTextTitle.visibility = View.VISIBLE

            }
            titleRes > 0 -> {
                binding.title =
                    resources.getString(titleRes)
                binding.awoanrSearchItemViewTextTitle.visibility = View.VISIBLE
            }
            else -> binding.awoanrSearchItemViewTextTitle.visibility = View.GONE
        }
    }


    private fun updateAvatar() {
        when {
            !avatar.isNullOrBlank() -> {
                with(binding.awoanrSearchItemViewImageAvatar) {
                    ImageUtil.loadImage(this, avatar)
                }
            }
            avatarRes > 0 -> {
                with(binding.awoanrSearchItemViewImageAvatar) {
                    setImageResource(avatarRes)
                }
            }
        }
    }


    override fun getLayoutResource(): View {
        binding = AwonarWidgetSearchItemBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchItemView)
        title = typedArray.getString(R.styleable.SearchItemView_searchItemView_setTitle)
        subTitle =
            typedArray.getString(R.styleable.SearchItemView_searchItemView_setSubTitle)
        avatarRes =
            typedArray.getResourceId(R.styleable.ListItemView_listItemView_setStartIcon, 0)
        buttonText = typedArray.getString(R.styleable.SearchItemView_searchItemView_setTextButton)
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.avatar = avatar
        ss?.avatarRes = avatarRes
        ss?.buttonText = buttonText
        ss?.buttonTextRes = buttonTextRes
        ss?.buttonSelected = buttonSelected
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        title = ss.title
        titleRes = ss.titleRes
        subTitle = ss.subTitle
        subTitleRes = ss.subTitleRes
        avatar = ss.avatar
        avatarRes = ss.avatarRes
        buttonText = ss.buttonText
        buttonTextRes = ss.buttonTextRes
        buttonSelected = ss.buttonSelected
        updateTitle()
        updateAvatar()
        updateSubTitle()
        updateButtonText()
        updateButtonSelected()
    }

    private class SavedState : ChildSavedState {

        var avatar: String? = null
        var avatarRes: Int = 0
        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var buttonText: String? = null
        var buttonTextRes: Int = 0
        var buttonSelected: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            avatar = parcel.readString()
            avatarRes = parcel.readInt()
            buttonText = parcel.readString()
            buttonTextRes = parcel.readInt()
            buttonSelected = parcel.readBooleanUsingCompat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeString(avatar)
            out.writeInt(avatarRes)
            out.writeString(buttonText)
            out.writeInt(buttonTextRes)
            out.writeBooleanUsingCompat(buttonSelected)
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
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}