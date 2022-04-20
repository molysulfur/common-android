package com.awonar.app.widget.feed

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.databinding.AwonarWidgetDefaultFeedBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup

class DefaultFeed : BaseViewGroup {

    private lateinit var binding: AwonarWidgetDefaultFeedBinding
    private var preview: View? = null

    var likeCount = 0
        set(value) {
            field = value
            updateLikeCount()
        }

    private fun updateLikeCount() {
        binding.like = "$likeCount"
    }

    var commentCount = 0
        set(value) {
            field = value
            updateCommentCount()
        }

    private fun updateCommentCount() {
        binding.comment = "$commentCount Comments"
    }

    var sharedCount = 0
        set(value) {
            field = value
            updateSharedCount()
        }

    private fun updateSharedCount() {
        binding.shared = "$sharedCount Shared"
    }

    var description: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    private fun updateDescription() {
        with(binding.awonarDefaultFeedTextDescription) {
            text = description ?: ""
        }
    }

    var avatar: String? = null
        set(value) {
            field = value
            if (value != null) {
                avatarRes = 0
                updateAvatar()
            }
        }

    var avatarRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                avatar = null
                updateAvatar()
            }
        }

    private fun updateAvatar() {
        with(binding.awonarDefaultFeedImageAvatar) {
            when {
                avatar != null -> ImageUtil.loadImage(this, avatar)
                avatarRes > 0 -> setImageResource(avatarRes)
            }
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

    var subTitle: String? = null
        set(value) {
            field = value
            if (value != null) {
                subTitleRes = 0
                updateSubTitle()
            }
        }

    var subTitleRes: Int = 0
        set(value) {
            field = value
            if (value > 0) {
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

    fun addOptionView(view: View) {
        preview = view
        updatePreview()
    }

    fun clearOptionView() {
        preview = null
        updatePreview()
    }

    private fun updatePreview() {
        with(binding.awonarDefaultFeedViewPreview) {
            removeAllViews()
            if (preview != null) {
                addView(preview)
            }
            visibility = if (preview != null) View.VISIBLE else View.GONE
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

    override fun setup() {
        updateTitle()
        updateSubTitle()
        updateAvatar()
        updateDescription()
        updatePreview()
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetDefaultFeedBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        return state
    }

    override fun restoreInstanceState(state: Parcelable) {
    }

}