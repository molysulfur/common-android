package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import com.akexorcist.library.dialoginteractor.DialogLauncher

class DialogViewModel : ViewModel() {
    val order = DialogLauncher(OrderMapper::class.java)
}