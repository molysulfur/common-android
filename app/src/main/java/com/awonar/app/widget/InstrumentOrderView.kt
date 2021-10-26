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
import coil.load
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetInstrumentOrderBinding
import com.molysulfur.library.widget.BaseViewGroup

class InstrumentOrderView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetInstrumentOrderBinding

    private var title: String? = null
    private var titleRes: Int = 0
    private var image: String? = null
    private var imageRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0
    private var columnOne: String? = null
    private var columnTwo: String? = null
    private var columnThree: String? = null
    private var columnFour: String? = null
    private var columnOneColor: Int = 0
    private var columnTwoColor: Int = 0
    private var columnThreeColor: Int = 0
    private var columnFourColor: Int = 0

    override fun setup() {
        updateTitle()
        updateDescription()
        updateImage()
        updateTextColorColumnFour()
        updateTextColorColumnThree()
        updateTextColorColumnTwo()
        updateTextColorColumnOne()
    }

    fun setTextColorColumnFour(color: Int) {
        this.columnFourColor = color
        updateTextColorColumnFour()
    }

    private fun updateTextColorColumnFour() {
        when {
            columnFourColor > 0 -> binding.awonarInstrumentOrderTextValueColumnFour.setTextColor(
                ContextCompat.getColor(
                    context,
                    columnFourColor
                )
            )
            else -> binding.awonarInstrumentOrderTextValueColumnFour.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.awonar_color_text_primary
                )
            )
        }
    }

    fun setTextColorColumnThree(color: Int) {
        this.columnThreeColor = color
        updateTextColorColumnThree()
    }

    private fun updateTextColorColumnThree() {
        when {
            columnThreeColor > 0 -> binding.awonarInstrumentOrderTextValueColumnThree.setTextColor(
                ContextCompat.getColor(
                    context,
                    columnThreeColor
                )
            )
            else -> binding.awonarInstrumentOrderTextValueColumnThree.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.awonar_color_text_primary
                )
            )
        }
    }

    fun setTextColorColumnTwo(color: Int) {
        this.columnTwoColor = color
        updateTextColorColumnTwo()
    }

    private fun updateTextColorColumnTwo() {
        when {
            columnTwoColor > 0 -> binding.awonarInstrumentOrderTextValueColumnTwo.setTextColor(
                ContextCompat.getColor(
                    context,
                    columnTwoColor
                )
            )
            else -> binding.awonarInstrumentOrderTextValueColumnTwo.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.awonar_color_text_primary
                )
            )
        }
    }

    fun setTextColorColumnOne(color: Int) {
        this.columnOneColor = color
        updateTextColorColumnOne()
    }

    private fun updateTextColorColumnOne() {
        when {
            columnOneColor > 0 -> binding.awonarInstrumentOrderTextValueColumnOne.setTextColor(
                ContextCompat.getColor(
                    context,
                    columnTwoColor
                )
            )
            else -> binding.awonarInstrumentOrderTextValueColumnOne.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.awonar_color_text_primary
                )
            )
        }
    }

    fun setTextColumnOne(text: String) {
        columnOne = text
        updateColumnOne()
    }

    private fun updateColumnOne() {
        binding.column1 = columnOne
    }

    fun setTextColumnTwo(text: String) {
        columnTwo = text
        updateColumnTwo()
    }

    private fun updateColumnTwo() {
        binding.column2 = columnTwo
    }

    fun setTextColumnThree(text: String) {
        columnThree = text
        updateColumnThree()
    }

    private fun updateColumnThree() {
        binding.column3 = columnThree
    }

    fun setTextColumnFour(text: String) {
        columnFour = text
        updateColumnFour()
    }

    private fun updateColumnFour() {
        binding.column4 = columnFour
    }


    fun setImage(imageRes: Int) {
        this.imageRes = imageRes
        image = null
        updateImage()
    }

    fun setImage(image: String) {
        this.image = image
        imageRes = 0
        updateImage()
    }

    private fun updateImage() {
        when {
            image != null -> binding.awonarInstrumentOrderImageAvatar.load(BuildConfig.BASE_IMAGE_URL + image)
            imageRes > 0 -> binding.awonarInstrumentOrderImageAvatar.setImageResource(imageRes)
        }
    }

    fun setDescription(descriptionRes: Int) {
        this.descriptionRes = descriptionRes
        description = null
        updateDescription()
    }


    fun setDescription(description: String) {
        this.description = description
        this.descriptionRes = 0
        updateDescription()
    }

    private fun updateDescription() {
        when {
            description != null -> binding.description = description
            descriptionRes > 0 -> binding.description = context.getString(descriptionRes)
        }
    }

    fun setTitle(title: String) {
        this.title = title
        this.titleRes = 0
        updateTitle()
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
        this.title = null
        updateTitle()
    }

    private fun updateTitle() {
        when {
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = context.getString(titleRes)
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetInstrumentOrderBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InstrumentOrderView)
        imageRes =
            typedArray.getResourceId(
                R.styleable.InstrumentOrderView_instrumentOrderView_setImage,
                0
            )
        title = typedArray.getString(R.styleable.InstrumentOrderView_instrumentOrderView_setTitle)
        description =
            typedArray.getString(R.styleable.InstrumentOrderView_instrumentOrderView_setDescription)
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.image = image
        ss?.imageRes = imageRes
        ss?.title = title
        ss?.titleRes = titleRes
        ss?.description = description
        ss?.descriptionRes = descriptionRes
        ss?.columnOne = columnOne
        ss?.columnTwo = columnTwo
        ss?.columnThree = columnThree
        ss?.columnFour = columnFour
        ss?.columnOneColor = columnOneColor
        ss?.columnTwoColor = columnTwoColor
        ss?.columnThreeColor = columnThreeColor
        ss?.columnFourColor = columnFourColor
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        image = ss.image
        imageRes = ss.imageRes
        title = ss.title
        titleRes = ss.titleRes
        description = ss.description
        descriptionRes = ss.descriptionRes
        columnOne = ss.columnOne
        columnTwo = ss.columnTwo
        columnThree = ss.columnThree
        columnFour = ss.columnFour
        columnOneColor = ss.columnOneColor
        columnTwoColor = ss.columnTwoColor
        columnThreeColor = ss.columnThreeColor
        columnFourColor = ss.columnFourColor
        updateTitle()
        updateDescription()
        updateImage()
        updateColumnOne()
        updateColumnTwo()
        updateColumnThree()
        updateColumnFour()
        updateTextColorColumnOne()
        updateTextColorColumnTwo()
        updateTextColorColumnThree()
        updateTextColorColumnFour()
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

    private class SavedState : ChildSavedState {

        var title: String? = null
        var titleRes: Int = 0
        var image: String? = null
        var imageRes: Int = 0
        var description: String? = null
        var descriptionRes: Int = 0
        var columnOne: String? = null
        var columnTwo: String? = null
        var columnThree: String? = null
        var columnFour: String? = null
        var columnOneColor: Int = 0
        var columnTwoColor: Int = 0
        var columnThreeColor: Int = 0
        var columnFourColor: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            image = parcel.readString()
            imageRes = parcel.readInt()
            title = parcel.readString()
            titleRes = parcel.readInt()
            description = parcel.readString()
            descriptionRes = parcel.readInt()
            columnOne = parcel.readString()
            columnTwo = parcel.readString()
            columnThree = parcel.readString()
            columnFour = parcel.readString()
            columnOneColor = parcel.readInt()
            columnTwoColor = parcel.readInt()
            columnThreeColor = parcel.readInt()
            columnFourColor = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(image)
            out.writeInt(imageRes)
            out.writeString(title)
            out.writeInt(titleRes)
            out.writeString(description)
            out.writeInt(descriptionRes)
            out.writeString(columnOne)
            out.writeString(columnTwo)
            out.writeString(columnThree)
            out.writeString(columnFour)
            out.writeInt(columnOneColor)
            out.writeInt(columnTwoColor)
            out.writeInt(columnThreeColor)
            out.writeInt(columnFourColor)
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