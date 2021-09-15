package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import coil.load
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.databinding.AwonarWidgetCardViewInstrumentBinding
import com.awonar.app.databinding.AwonarWidgetInstrumentItemViewBinding
import com.molysulfur.library.widget.BaseViewGroup

class InstrumentItemView : BaseViewGroup {

    private var image: String? = null
    private var imageRes: Int = 0
    private var title: String? = null
    private var titleRes: Int = 0
    private var ask: Float = 0f
    private var bid: Float = 0f
    private var change: Float = 0f
    private var percentChange: Float = 0f

    private lateinit var binding: AwonarWidgetInstrumentItemViewBinding

    override fun setup() {
        updateImage()
        updateAsk()
        updateBid()
        updateChange()
        updateTitle()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetInstrumentItemViewBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    fun setAsk(ask: Float) {
        this.ask = ask
        updateAsk()
    }

    private fun updateAsk() {
        binding.awonarInstrumentItemTextAsk.text = "$ask"
    }

    fun setBid(bid: Float) {
        this.bid = bid
        updateBid()
    }

    private fun updateBid() {
        binding.awonarInstrumentItemTextBid.text = "$bid"
    }

    private fun updateImage() {
        when {
            image != null -> binding.awonarInstrumentItemImageLogo.load(BuildConfig.BASE_IMAGE_URL + image)
            imageRes > 0 -> binding.awonarInstrumentItemImageLogo.setImageResource(imageRes)
        }
    }

    private fun updateTitle() {
        when {
            title != null -> binding.awonarInstrumentItemTextTitle.text = title
            titleRes > 0 -> binding.awonarInstrumentItemTextTitle.setText(titleRes)
        }
    }

    private fun updateChange() {
        binding.awonarInstrumentItemTextChange.text = "$change ($percentChange%)"
    }

    fun setPercentChange(percentChange: Float) {
        this.percentChange = percentChange
        updateChange()
    }

    fun setChange(change: Float) {
        this.change = change
        updateChange()
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

    fun setImage(imageUrl: String) {
        image = imageUrl
        imageRes = 0
        updateImage()
    }

    fun setImage(imageRes: Int) {
        this.imageRes = imageRes
        image = null
        updateImage()
    }


    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.image = image
        ss?.imageRes = imageRes
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.ask = ask
        ss?.bid = bid
        ss?.change = change
        ss?.percentChange = percentChange
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        image = ss.image
        imageRes = ss.imageRes
        title = ss.title
        titleRes = ss.titleRes
        ask = ss.ask
        bid = ss.bid
        change = ss.change
        percentChange = ss.percentChange
        updateImage()
        updateAsk()
        updateBid()
        updateChange()
        updateTitle()
    }

    private class SavedState : ChildSavedState {

        var image: String? = null
        var imageRes: Int = 0
        var title: String? = null
        var titleRes: Int = 0
        var ask: Float = 0f
        var bid: Float = 0f
        var change: Float = 0f
        var percentChange: Float = 0f

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            image = parcel.readString()
            imageRes = parcel.readInt()
            title = parcel.readString()
            titleRes = parcel.readInt()
            ask = parcel.readFloat()
            bid = parcel.readFloat()
            change = parcel.readFloat()
            percentChange = parcel.readFloat()

        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(image)
            out.writeInt(imageRes)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeFloat(ask)
            out.writeFloat(bid)
            out.writeFloat(change)
            out.writeFloat(percentChange)
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