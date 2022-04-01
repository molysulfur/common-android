package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemLogoBinding
import com.awonar.app.databinding.AwonarWidgetFolderWatchlistBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.readBooleanUsingCompat
import com.molysulfur.library.extension.writeBooleanUsingCompat
import com.molysulfur.library.widget.BaseViewGroup

class WatchlistFolderView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetFolderWatchlistBinding


    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var isCloseVisible: Boolean = false

    var onClose: (() -> Unit)? = null

    fun setSubTitle(subTitle: String) {
        this.subTitle = subTitle
        subTitleRes = 0
        updateSubTitle()
    }

    fun setSubTitle(res: Int) {
        this.subTitleRes = res
        subTitle = null
        updateSubTitle()
    }

    private fun updateSubTitle() {
        when {
            subTitle != null -> binding.subTitle = subTitle
            subTitleRes > 0 -> binding.subTitle = resources.getString(subTitleRes)
        }
    }

    fun setTitle(title: String) {
        this.title = title
        titleRes = 0
        updateTitle()
    }

    fun setTitle(res: Int) {
        this.titleRes = res
        title = null
        updateTitle()
    }

    private fun updateTitle() {
        when {
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = resources.getString(titleRes)
        }
    }

    fun setRecentImage(images: List<String>) {
        with(binding.awonarFolderWatchlistRecyclerRecent) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = WatchListRecentImageFolderAdapter(images)
        }
    }

    fun isCloseIconVisible(isVisible: Boolean) {
        this.isCloseVisible = isVisible
        updateCloseIconVisible()
    }

    private fun updateCloseIconVisible() {
        binding.awonarFolderWatchlistButtonClose.visibility =
            if (isCloseVisible) View.VISIBLE else View.GONE
    }


    override fun setup() {
        updateTitle()
        updateSubTitle()
        updateCloseIconVisible()
        binding.awonarFolderWatchlistButtonClose.setOnClickListener {
            onClose?.invoke()
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetFolderWatchlistBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.isCloseVisible = isCloseVisible
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        title = ss.title
        titleRes = ss.titleRes
        subTitle = ss.subTitle
        subTitleRes = ss.subTitleRes
        isCloseVisible = ss.isCloseVisible
    }

    private class SavedState : ChildSavedState {

        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var isCloseVisible: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            isCloseVisible = parcel.readBooleanUsingCompat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeBooleanUsingCompat(isCloseVisible)
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

    private class LogoRecentViewHolder constructor(private val binding: AwonarItemLogoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            ImageUtil.loadImage(binding.awonarLogoImageLogo, imageUrl)
        }
    }

    private class WatchListRecentImageFolderAdapter constructor(private val images: List<String>) :
        RecyclerView.Adapter<LogoRecentViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogoRecentViewHolder =
            LogoRecentViewHolder(AwonarItemLogoBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))

        override fun onBindViewHolder(holder: LogoRecentViewHolder, position: Int) {
            holder.bind(images[position])
        }

        override fun getItemCount(): Int = images.size

    }

}
