package com.awonar.app.widget.feed

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.databinding.AwonarWidgetImagesFeedBinding
import com.molysulfur.library.widget.BaseViewGroup

class ImagePreviewFeed : BaseViewGroup {

    private lateinit var binding: AwonarWidgetImagesFeedBinding


    override fun setup() {
    }


    fun setImages(images: MutableList<Bitmap?>) {
        with(binding.awonarImagesFeedsContainer) {
            setImageUrlList(images)
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetImagesFeedBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        return state
    }

    override fun restoreInstanceState(state: Parcelable) {
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