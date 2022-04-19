package com.awonar.app.widget.feed

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.molysulfur.library.widget.BaseViewGroup

class ImagePreviewFeed : BaseViewGroup {

    override fun setup() {
    }

    override fun getLayoutResource(): View {
        TODO("Not yet implemented")
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