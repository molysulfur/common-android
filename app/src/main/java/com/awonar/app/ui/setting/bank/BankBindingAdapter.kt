package com.awonar.app.ui.setting.bank

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.awonar.android.model.settting.Bank
import com.awonar.android.model.settting.Country
import com.awonar.app.R
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber


@BindingAdapter("setCountries")
fun setCountries(textField: TextInputLayout, items: List<Country> = listOf()) {
    if ((textField.editText as? AutoCompleteTextView)?.adapter == null && items.isNotEmpty()) {
        val countriesNameLists: List<String?> = items.map { it.name }
        val adapter = ArrayAdapter(textField.context, R.layout.awonar_item_list, countriesNameLists)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}

@BindingAdapter("setBanks")
fun setBanks(textField: TextInputLayout, items: List<Bank> = listOf()) {
    if ((textField.editText as? AutoCompleteTextView)?.adapter == null && items.isNotEmpty()) {
        val banks: List<String?> = items.map { it.name }
        val adapter = ArrayAdapter(textField.context, R.layout.awonar_item_list, banks)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}

@BindingAdapter("setAccountType")
fun setAccountType(textField: TextInputLayout, items: List<String> = listOf()) {
    if ((textField.editText as? AutoCompleteTextView)?.adapter == null && items.isNotEmpty()) {
        val adapter = ArrayAdapter(textField.context, R.layout.awonar_item_list, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}