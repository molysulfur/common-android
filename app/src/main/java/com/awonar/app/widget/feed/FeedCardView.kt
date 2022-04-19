package com.awonar.app.widget.feed

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.databinding.AwonarWidgetCardFeedBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup

class FeedCardView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCardFeedBinding

    var meta: String? = null
        set(value) {
            field = value
            if (value != null) {
                metaRes = 0
                updateMeta()
            }
        }

    var metaRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                meta = null
                updateMeta()
            }
        }

    private fun updateMeta() {
        when {
            meta != null -> binding.meta = meta
            metaRes > 0 -> binding.meta = context.getString(metaRes)
        }
    }

    var description: String? = null
        set(value) {
            field = value
            if (value != null) {
                descriptionRes = 0
                updateDescription()
            }
        }

    var descriptionRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                description = null
                updateDescription()
            }
        }

    private fun updateDescription() {
        when {
            description != null -> binding.description = description
            descriptionRes > 0 -> binding.description = context.getString(descriptionRes)
        }
    }

    var title: String? = null
        set(value) {
            field = value
            if (value != null) {
                titleRes = 0
                updateTitle()
            }
        }

    var titleRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                title = null
                updateTitle()
            }
        }

    private fun updateTitle() {
        when {
            title != null -> binding.title = title
            titleRes > 0 -> binding.title = context.getString(titleRes)
        }
    }

    var image: String? = null
        set(value) {
            field = value
            if (value != null) {
                imageRes = 0
                updateImage()
            }
        }

    var imageRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                image = null
                updateImage()
            }
        }

    private fun updateImage() {
        when {
            image != null -> with(binding.awonarCardFeedImageCover) {
                ImageUtil.loadImage(this, image)
            }
            imageRes > 0 -> binding.awonarCardFeedImageCover.setImageResource(imageRes)
        }
    }

    override fun setup() {
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCardFeedBinding.inflate(LayoutInflater.from(context))
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