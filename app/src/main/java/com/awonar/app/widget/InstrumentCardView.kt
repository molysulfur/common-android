package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import coil.load
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetCardViewInstrumentBinding
import com.molysulfur.library.widget.BaseViewGroup

class InstrumentCardView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCardViewInstrumentBinding

    private var image: String? = null
    private var imageRes: Int = 0
    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var price: Float = 0f
    private var change: Float = 0f
    private var percentChange: Float = 0f

    override fun setup() {
        updateChange()
        updatePrice()
        updateSubTitle()
        updateTitle()
        updateImage()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCardViewInstrumentBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    private fun updateImage() {
        when {
            image != null -> binding.awonarInstrumentCardViewImageLogo.load(BuildConfig.BASE_IMAGE_URL + image)
            imageRes > 0 -> binding.awonarInstrumentCardViewImageLogo.setImageResource(imageRes)
        }
    }

    private fun updateTitle() {
        when {
            title != null -> binding.awonarInstrumentCardViewTextTitle.text = title
            titleRes > 0 -> binding.awonarInstrumentCardViewTextTitle.setText(titleRes)
        }
    }

    private fun updateSubTitle() {
        when {
            subTitle != null -> binding.awonarInstrumentCardViewTextSubtitle.text = subTitle
            subTitleRes > 0 -> binding.awonarInstrumentCardViewTextSubtitle.setText(subTitleRes)
        }
    }

    private fun updatePrice() {
        binding.awonarInstrumentCardViewTextPrice.text = "$price"
    }

    private fun updateChange() {
        binding.awonarInstrumentCardViewTextChange.text = "$price ($percentChange%)"
    }

    fun setPercentChange(percentChange: Float) {
        this.percentChange = percentChange
        updateChange()
    }

    fun setChange(change: Float) {
        this.change = change
        updateChange()
    }

    fun setPrice(price: Float) {
        this.price = price
        updatePrice()
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InstrumentCardView)
        imageRes =
            typedArray.getResourceId(R.styleable.InstrumentCardView_instrumentCardView_setImage, 0)
        title = typedArray.getString(R.styleable.InstrumentCardView_instrumentCardView_setTitle)
        subTitle =
            typedArray.getString(R.styleable.InstrumentCardView_instrumentCardView_setSubTitle)
        price =
            typedArray.getFloat(R.styleable.InstrumentCardView_instrumentCardView_setPrice, 0f)
        change =
            typedArray.getFloat(R.styleable.InstrumentCardView_instrumentCardView_setChange, 0f)
        percentChange =
            typedArray.getFloat(
                R.styleable.InstrumentCardView_instrumentCardView_setPercentChange,
                0f
            )

        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.image = image
        ss?.imageRes = imageRes
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.price = price
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
        subTitle = ss.subTitle
        subTitleRes = ss.subTitleRes
        price = ss.price
        change = ss.change
        percentChange = ss.percentChange
    }

    private class SavedState : ChildSavedState {

        var image: String? = null
        var imageRes: Int = 0
        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var price: Float = 0f
        var change: Float = 0f
        var percentChange: Float = 0f

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            image = parcel.readString()
            imageRes = parcel.readInt()
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            price = parcel.readFloat()
            change = parcel.readFloat()
            percentChange = parcel.readFloat()

        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(image)
            out.writeInt(imageRes)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeFloat(price)
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