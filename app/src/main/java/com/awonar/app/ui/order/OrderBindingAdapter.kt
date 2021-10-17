package com.awonar.app.ui.order

import androidx.databinding.BindingAdapter
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.model.order.Price
import com.awonar.app.widget.NumberPickerEditText
import timber.log.Timber


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