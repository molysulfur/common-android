package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.awonar.app.R
import com.molysulfur.library.widget.BaseViewGroup

class AwonarProgressBar : BaseViewGroup {


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

    override fun setup() {
    }

    override fun getLayoutResource(): Int = R.layout.awonar_widget_progress

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? = state

    override fun restoreInstanceState(state: Parcelable) {
    }
}