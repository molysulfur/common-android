package com.awonar.app.ui.order

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.model.order.Price
import com.awonar.app.widget.NumberPickerCollapsibleView
import com.awonar.app.widget.NumberPickerEditText
import timber.log.Timber


@BindingAdapter("setOvernightDaily", "setOvernightWeek")
fun setOvernight(textView: AppCompatTextView, overnightDaliy: Float, overnightWeek: Float) {
    textView.text =
        "Overnight Fee : Daily: %.2f | Weekend: %.2f".format(overnightDaliy, overnightWeek)
}

@BindingAdapter("setRate")
fun setRate(view: NumberPickerEditText, rate: Float) {
    view.setNumber(rate)
}

@BindingAdapter("visibleHint")
fun setVisibleRateHint(view: NumberPickerEditText, type: MarketOrderType) {
    if (type == MarketOrderType.PENDING_ORDER) {
        view.setPlaceHolderEnable(false)
    } else {
        view.setPlaceHolderEnable(true)
    }
}

@BindingAdapter("setAmount")
fun setAmount(view: NumberPickerEditText, amount: Price) {
    when (amount.type) {
        "amount" -> {
            view.setPrefix("$")
            view.setNumber(amount.amount)
        }
        "unit" -> {
            view.setPrefix("")
            view.setNumber(amount.unit)
        }
    }
}

@BindingAdapter("setNumberPickerCollapsibleView", "setDigit")
fun setNumberPickerCollapsibleView(
    view: NumberPickerCollapsibleView,
    stopLoss: Price?,
    digit: Int
) {
    when (stopLoss?.type) {
        "amount" -> {
            view.setDigit(0)
            view.setPrefix("$")
            view.setDescription("%.2f".format(stopLoss.amount))
//            view.setNumber(stopLoss.amount)
        }
        "rate" -> {
            view.setDigit(digit)
            view.setPrefix("")
            view.setDescription("%.${digit}f".format(stopLoss.unit))
//            view.setNumber(stopLoss.unit)
        }
    }
}

@BindingAdapter("setHideNoSet")
fun hideNoSet(view: NumberPickerCollapsibleView, leverage: Int) {
    view.visibleNoSet(leverage == 1)
}