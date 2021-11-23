package com.awonar.app.dialog

import androidx.lifecycle.ViewModel
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.awonar.app.ui.order.OrderMapper
import com.awonar.app.ui.order.edit.OrderEditMapper

class DialogViewModel : ViewModel() {
    val order = DialogLauncher(OrderMapper::class.java)
    val orderEdit = DialogLauncher(OrderEditMapper::class.java)
}