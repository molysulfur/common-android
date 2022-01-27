package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetCopiesItemBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup

class CopiesItemView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCopiesItemBinding

    var hasWatchlistIcon: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                updateHasWatchlistIcon()
            }
        }

    private fun updateHasWatchlistIcon() {
        binding.awonarCopiesItemImageAdd.visibility =
            if (hasWatchlistIcon) View.VISIBLE else View.GONE
    }

    var gainColor: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                updateGainColor()
            }
        }

    private fun updateGainColor() {
        when {
            gainColor > 0 -> binding.awonarCopiesItemTextGain.setTextColor(ContextCompat.getColor(
                context,
                gainColor))
        }
    }

    var image: String? = null
        set(value) {
            if (value != null) {
                field = value
                imageRes = 0
                updateImage()
            }
        }

    var imageRes: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                image = null
                updateImage()
            }
        }

    private fun updateImage() {
        when {
            image != null -> ImageUtil.loadImage(binding.awonarCopiesItemImageAvatar, image)
            imageRes > 0 -> binding.awonarCopiesItemImageAvatar.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    imageRes
                )
            )
        }
    }

    var title: String? = null
        set(value) {
            if (value != null) {
                field = value
                titleRes = 0
                updateTitle()
            }
        }

    var titleRes: Int = 0
        set(value) {
            if (value > 0) {
                field = value
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

    var subTitle: String? = null
        set(value) {
            if (value != null) {
                subTitleRes = 0
                field = value
                updateSubTitle()
            }
        }

    var subTitleRes: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                subTitle = null
                updateSubTitle()
            }
        }

    private fun updateSubTitle() {
        when {
            subTitle != null -> binding.subTitle = subTitle
            subTitleRes > 0 -> binding.subTitle = context.getString(subTitleRes)
        }
    }

    var gain: Float = 0f
        set(value) {
            field = value
            updateChange()
        }

    private fun updateChange() {
        binding.gain = "%.2f%s".format(gain, "%")
    }

    var risk: Int = 0
        set(value) {
            field = value
            updateRisk()
        }

    private fun updateRisk() {
        binding.awonarCopiesItemImageRisk.setImageResource(
            when (risk) {
                1 -> R.drawable.awonar_ic_risk_1
                2 -> R.drawable.awonar_ic_risk_2
                3 -> R.drawable.awonar_ic_risk_3
                4 -> R.drawable.awonar_ic_risk_4
                5 -> R.drawable.awonar_ic_risk_5
                6 -> R.drawable.awonar_ic_risk_6
                7 -> R.drawable.awonar_ic_risk_7
                8 -> R.drawable.awonar_ic_risk_8
                9 -> R.drawable.awonar_ic_risk_9
                10 -> R.drawable.awonar_ic_risk_10
                else -> 0
            }
        )
    }

    var watchlistClick: (() -> Unit)? = null

    override fun setup() {
        updateChange()
        updateGainColor()
        updateHasWatchlistIcon()
        updateImage()
        updateRisk()
        updateSubTitle()
        updateTitle()
        binding.awonarCopiesItemImageAdd.setOnClickListener {
            watchlistClick?.invoke()
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCopiesItemBinding.inflate(LayoutInflater.from(context))
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