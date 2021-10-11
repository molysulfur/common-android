package com.awonar.app.ui.order

import androidx.databinding.BindingAdapter
import com.awonar.android.model.order.Price
import com.awonar.app.widget.NumberPickerCollapsibleView


@BindingAdapter("setNumberPickerCollapsible")
fun setNumberPickerCollapsible(numberPicker: NumberPickerCollapsibleView, price: Price?) {
    price?.let {
        when (price.type) {
            "amount" -> {
                numberPicker.setDescription("${price.amount}")
                numberPicker.setNumber(price.amount)
            }
            "rate" -> {
                numberPicker.setDescription("${price.unit}")
                numberPicker.setNumber(price.unit)
            }
        }
    }
}