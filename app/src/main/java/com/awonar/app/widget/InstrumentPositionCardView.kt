package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetImageCheckboxBinding
import com.awonar.app.databinding.AwonarWidgetInstrumentPositionCardBinding
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup
import timber.log.Timber

class InstrumentPositionCardView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetInstrumentPositionCardBinding
    private var image: String? = null
    private var imageRes: Int = 0
    private var title: String? = null
    private var titleRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0
    private var status: String? = null
    private var price: Float = 0f
    private var change: Float = 0f
    private var changePercent: Float = 0f
    private var invested: Float = 0f
    private var valueInvest: Float = 0f
    private var units: Float = 0f
    private var profitLoss: Float = 0f
    private var avgOpen: Float = 0f


    var onSetting : (() ->Unit)? = null

    override fun setup() {
        binding.awonarInstrumentPositionCardButtonSetting.setOnClickListener {
            onSetting?.invoke()
        }
    }

    fun setAvgOpen(number: Float) {
        avgOpen = number
        updateAvgOpen()
    }

    private fun updateAvgOpen() {
        binding.avgOpen = avgOpen
    }

    fun setProfitLoss(number: Float) {
        profitLoss = number
        updateProfitLoss()
    }

    private fun updateProfitLoss() {
        binding.profitLoss = profitLoss
        binding.awonarInstrumentPositionCardTextProfitloss.setTextColor(
            ColorChangingUtil.getTextColorChange(
                context,
                profitLoss
            )
        )
    }

    fun setUnit(unit: Float) {
        this.units = unit
        updateUnit()
    }

    private fun updateUnit() {
        binding.units = units
    }

    fun setValueInvested(number: Float) {
        this.valueInvest = number
        updateValueInvested()
    }

    private fun updateValueInvested() {
        binding.valueInvest = valueInvest
    }


    fun setInvested(number: Float) {
        this.invested = number
        updateInvested()
    }

    private fun updateInvested() {
        binding.invested = invested
    }

    fun setChangePercent(percent: Float) {
        changePercent = percent
        updateChange()
    }


    fun setChange(change: Float) {
        this.change = change
        updateChange()
    }

    private fun updateChange() {
        binding.awonarInstrumentPositionCardTextChange.setTextColor(
            ColorChangingUtil.getTextColorChange(
                context,
                change
            )
        )
        binding.awonarInstrumentPositionCardTextChange.text =
            "%.2f (%.2f%s)".format(change, changePercent, "%")
    }

    private fun updatePrice() {
        binding.price = price
    }

    fun setPrice(price: Float) {
        this.price = price
        updatePrice()
    }

    fun setStatusText(text: String) {
        status = text
        updateStatusText()
    }

    private fun updateStatusText() {
        binding.status = status
    }

    fun setDescrption(description: String) {
        this.description = description
        descriptionRes = 0
        updateDescription()
    }

    private fun updateDescription() {
        when {
            description != null -> binding.description = description
            descriptionRes > 0 -> binding.description = context.getString(descriptionRes)
        }
    }

    fun setDescrption(descriptionRes: Int) {
        this.descriptionRes = descriptionRes
        description = null
        updateDescription()
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
            subTitle != null -> binding.subTitle = subTitle
            subTitleRes > 0 -> binding.subTitle = context.getString(subTitleRes)
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
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = context.getString(titleRes)
        }
    }

    fun setImage(image: String) {
        this.image = image
        imageRes = 0
        updateImage()
    }

    fun setImage(imageRes: Int) {
        this.imageRes = imageRes
        image = null
        updateImage()
    }

    private fun updateImage() {
        when {
            image != null -> ImageUtil.loadImage(
                binding.awonarInstrumentPositionCardImageLogo,
                image
            )
            imageRes > 0 -> binding.awonarInstrumentPositionCardImageLogo.setImageResource(imageRes)
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetInstrumentPositionCardBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.image = image
        ss?.imageRes = imageRes
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.subTitle = subTitle
        ss?.subTitleRes = subTitleRes
        ss?.description = description
        ss?.descriptionRes = descriptionRes
        ss?.status = status
        ss?.price = price
        ss?.change = change
        ss?.changePercent = changePercent
        ss?.invested = invested
        ss?.valueInvest = valueInvest
        ss?.units = units
        ss?.profitLoss = profitLoss
        ss?.avgOpen = avgOpen
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
        description = ss.description
        descriptionRes = ss.descriptionRes
        status = ss.status
        price = ss.price
        change = ss.change
        changePercent = ss.changePercent
        invested = ss.invested
        valueInvest = ss.valueInvest
        units = ss.units
        profitLoss = ss.profitLoss
        avgOpen = ss.avgOpen
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
        var image: String? = null
        var imageRes: Int = 0
        var title: String? = null
        var titleRes: Int = 0
        var subTitle: String? = null
        var subTitleRes: Int = 0
        var description: String? = null
        var descriptionRes: Int = 0
        var status: String? = null
        var price: Float = 0f
        var change: Float = 0f
        var changePercent: Float = 0f
        var invested: Float = 0f
        var valueInvest: Float = 0f
        var units: Float = 0f
        var profitLoss: Float = 0f
        var avgOpen: Float = 0f

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            image = parcel.readString()
            imageRes = parcel.readInt()
            title = parcel.readString()
            titleRes = parcel.readInt()
            subTitle = parcel.readString()
            subTitleRes = parcel.readInt()
            description = parcel.readString()
            descriptionRes = parcel.readInt()
            status = parcel.readString()
            price = parcel.readFloat()
            change = parcel.readFloat()
            changePercent = parcel.readFloat()
            invested = parcel.readFloat()
            valueInvest = parcel.readFloat()
            units = parcel.readFloat()
            profitLoss = parcel.readFloat()
            avgOpen = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(image)
            out.writeInt(imageRes)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(subTitle)
            out.writeInt(subTitleRes)
            out.writeString(description)
            out.writeInt(descriptionRes)
            out.writeString(status)
            out.writeFloat(price)
            out.writeFloat(change)
            out.writeFloat(changePercent)
            out.writeFloat(invested)
            out.writeFloat(valueInvest)
            out.writeFloat(units)
            out.writeFloat(profitLoss)
            out.writeFloat(avgOpen)
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