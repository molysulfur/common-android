package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetFacebookBinding
import com.awonar.app.databinding.AwonarWidgetGoogleBinding
import com.molysulfur.library.widget.BaseViewGroup

class GoogleButton : BaseViewGroup {

    private lateinit var binding: AwonarWidgetGoogleBinding

    override fun setup() {
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetGoogleBinding.inflate(
            LayoutInflater.from(context)
        )
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
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}