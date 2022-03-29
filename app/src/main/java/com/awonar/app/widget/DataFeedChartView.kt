package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetDatafeedChartBinding
import com.github.mikephil.charting.data.LineData
import com.molysulfur.library.widget.BaseViewGroup

class DataFeedChartView : BaseViewGroup {

    var onTimeFrameChange: ((String, String) -> Unit)? = null

    private lateinit var binding: AwonarWidgetDatafeedChartBinding

    override fun setup() {
        with(binding.awonarDatafeedButtonGroupTimeframe) {
            addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    val (period, interval) = when (checkedId) {
                        R.id.awonar_datafeed_button_1d -> listOf("1", "D")
                        R.id.awonar_datafeed_button_1m -> listOf("1", "H")
                        R.id.awonar_datafeed_button_3m -> listOf("4", "H")
                        R.id.awonar_datafeed_button_6m -> listOf("1", "D")
                        R.id.awonar_datafeed_button_1y -> listOf("1", "D")
                        R.id.awonar_datafeed_button_3y -> listOf("3", "W")
                        R.id.awonar_datafeed_button_max -> listOf("1", "W")
                        else -> throw Error("Timeframe is not found!")
                    }
                    onTimeFrameChange?.invoke(period, interval)
                }
            }
        }
    }


    fun setData() {
        with(binding.awonarDatafeedChart) {
            data = LineData()
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetDatafeedChartBinding.inflate(LayoutInflater.from(context))
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