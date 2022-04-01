package com.awonar.app.widget

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetTradeButtonBinding
import com.awonar.app.widget.TradeButton.ButtonState.*
import com.molysulfur.library.widget.BaseViewGroup

class TradeButton : BaseViewGroup {

    private var anim: AlphaAnimation? = null
    private lateinit var binding: AwonarWidgetTradeButtonBinding

    enum class ButtonState {
        UP,
        DOWN,
        NOTHING
    }

    var onClick: (() -> Unit)? = null

    private var state: ButtonState = NOTHING
    private var number: Float = 0f

    fun setNumber(newNumber: Float) {
        val newState: ButtonState = when (state) {
            NOTHING -> {
                when {
                    newNumber > this.number -> {
                        UP
                    }
                    newNumber < this.number -> {
                        DOWN
                    }
                    else -> {
                        NOTHING
                    }
                }
            }
            UP -> {
                if (newNumber < this.number) {
                    DOWN
                } else {
                    state
                }

            }
            DOWN -> {
                if (newNumber > this.number) {
                    UP
                } else {
                    state
                }
            }
        }
        this.number = newNumber
        if (newState != state) {
            state = newState
            updateState()
        }
        updateText()
    }

    private fun updateText() {
        binding.text = "$number"
    }

    private fun updateState() {
        if (state != NOTHING) {
            with(binding.awonarWidgetTradeButton) {
                setTextColor(ContextCompat.getColor(
                    context,
                    R.color.white))
                setBackgroundColor(when (state) {
                    UP -> ContextCompat.getColor(context, R.color.awonar_color_green)
                    DOWN -> ContextCompat.getColor(context, R.color.awonar_color_orange)
                    else -> ContextCompat.getColor(context, R.color.awonar_color_light_gray)
                })
            }
            startAnim()
        }
    }

    private fun startAnim() {
        if (anim == null) {
            anim = AlphaAnimation(0.2f, 1f)
            anim?.duration = 500
            anim?.isFillEnabled = true
            anim?.fillBefore = true
            anim?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.awonarWidgetTradeButton.setBackgroundColor(ContextCompat.getColor(
                        context,
                        R.color.awonar_color_light_gray))
                    binding.awonarWidgetTradeButton.setTextColor(ContextCompat.getColor(
                        context,
                        R.color.black))
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
        binding.awonarWidgetTradeButton.startAnimation(anim);
    }

    override fun setup() {
        binding.awonarWidgetTradeButton.setOnClickListener {
            onClick?.invoke()
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetTradeButtonBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        val ss = state?.let { SavedState(it) }
        ss?.number = number
        ss?.state = when (this.state) {
            UP -> 1
            NOTHING -> 0
            DOWN -> -1
        }
        return ss
    }

    override fun restoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        this.number = ss.number
        this.state = when (ss.state) {
            1 -> UP
            0 -> NOTHING
            -1 -> DOWN
            else -> NOTHING
        }
        updateState()
        updateText()
    }

    private class SavedState : ChildSavedState {

        var number: Float = 0f
        var state: Int = 0


        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            number = parcel.readFloat()
            state = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(number)
            out.writeInt(state)
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
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}