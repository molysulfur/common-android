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
import com.awonar.app.databinding.AwonarWidgetListItemBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup

class ListItemView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetListItemBinding

    private var startIcon: String? = null
    private var startIconRes: Int = 0
    private var endIcon: String? = null
    private var endIconRes: Int = 0
    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var meta: String? = null
    private var metaRes: Int = 0


    override fun setup() {
        updateStartIcon()
        updateEndIcon()
        updateTitle()
        updateSubTitle()
        updateMeta()
    }

    fun setMeta(meta: String) {
        this.meta = meta
        metaRes = 0
        updateMeta()
    }

    fun setMeta(metaRes: Int) {
        this.metaRes = metaRes
        meta = null
        updateMeta()
    }

    private fun updateMeta() {
        when {
            !meta.isNullOrBlank() -> {
                binding.meta = this.meta
                binding.awoanrListItemViewTextMeta.visibility = View.VISIBLE

            }
            metaRes > 0 -> {
                binding.meta =
                    resources.getString(metaRes)
                binding.awoanrListItemViewTextMeta.visibility = View.VISIBLE
            }
            else -> binding.awoanrListItemViewTextMeta.visibility = View.GONE
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
                binding.awoanrListItemViewTextSubTitle.visibility = View.VISIBLE

            }
            subTitleRes > 0 -> {
                binding.subTitle =
                    resources.getString(subTitleRes)
                binding.awoanrListItemViewTextSubTitle.visibility = View.VISIBLE
            }
            else -> binding.awoanrListItemViewTextSubTitle.visibility = View.GONE
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
                binding.awoanrListItemViewTextTitle.visibility = View.VISIBLE

            }
            titleRes > 0 -> {
                binding.title =
                    resources.getString(titleRes)
                binding.awoanrListItemViewTextTitle.visibility = View.VISIBLE
            }
            else -> binding.awoanrListItemViewTextTitle.visibility = View.GONE
        }
    }

    fun setEndIcon(url: String) {
        endIcon = url
        endIconRes = 0
        updateEndIcon()
    }

    fun setEndIcon(res: Int) {
        endIconRes = res
        endIcon = null
        updateEndIcon()
    }

    private fun updateEndIcon() {
        when {
            !endIcon.isNullOrBlank() -> {
                with(binding.awoanrListItemViewImageIconEnd) {
                    ImageUtil.loadImage(this, endIcon)
                    visibility = View.VISIBLE
                }
            }
            endIconRes > 0 -> {
                with(binding.awoanrListItemViewImageIconEnd) {
                    setImageResource(endIconRes)
                    visibility = View.VISIBLE
                }
            }
            else -> binding.awoanrListItemViewImageIconEnd.visibility = View.GONE
        }
    }

    fun setStartIcon(url: String) {
        startIcon = url
        startIconRes = 0
        updateStartIcon()
    }

    fun setStartIcon(res: Int) {
        startIconRes = res
        startIcon = null
        updateStartIcon()
    }

    private fun updateStartIcon() {
        when {
            !startIcon.isNullOrBlank() -> {
                with(binding.awoanrListItemViewImageIconStart) {
                    ImageUtil.loadImage(this, startIcon)
                    visibility = View.VISIBLE
                }
            }
            startIconRes > 0 -> {
                with(binding.awoanrListItemViewImageIconStart) {
                    setImageResource(startIconRes)
                    visibility = View.VISIBLE
                }
            }
            else -> binding.awoanrListItemViewImageIconStart.visibility = View.GONE
        }
    }


    override fun getLayoutResource(): View {
        binding = AwonarWidgetListItemBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemView)
        title = typedArray.getString(R.styleable.ListItemView_listItemView_setTitle)
        subTitle =
            typedArray.getString(R.styleable.ListItemView_listItemView_setSubTitle)
        meta =
            typedArray.getString(R.styleable.ListItemView_listItemView_setMeta)
        startIconRes =
            typedArray.getResourceId(R.styleable.ListItemView_listItemView_setStartIcon, 0)
        endIconRes = typedArray.getResourceId(R.styleable.ListItemView_listItemView_setEndIcon, 0)
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.meta = meta
        ss?.metaRes = metaRes
        ss?.startIcon = startIcon
        ss?.startIconRes = startIconRes
        ss?.endIcon = endIcon
        ss?.endIconRes = endIconRes
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        title = ss.title
        titleRes = ss.titleRes
        subTitle = ss.subTitle
        subTitleRes = ss.subTitleRes
        meta = ss.meta
        metaRes = ss.metaRes
        startIcon = ss.startIcon
        startIconRes = ss.startIconRes
        endIcon = ss.endIcon
        endIconRes = ss.endIconRes
        updateTitle()
        updateStartIcon()
        updateEndIcon()
        updateSubTitle()
        updateMeta()
    }

    private class SavedState : ChildSavedState {

        var startIcon: String? = null
        var startIconRes: Int = 0
        var endIcon: String? = null
        var endIconRes: Int = 0
        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var meta: String? = null
        var metaRes: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            startIcon = parcel.readString()
            startIconRes = parcel.readInt()
            endIcon = parcel.readString()
            endIconRes = parcel.readInt()
            meta = parcel.readString()
            metaRes = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeString(startIcon)
            out.writeInt(startIconRes)
            out.writeString(endIcon)
            out.writeInt(endIconRes)
            out.writeString(meta)
            out.writeInt(metaRes)
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