package com.awonar.app.ui.order

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener

interface OrderDialogListener : DialogListener {
    fun onEditSuccess(name: String?, key: String?, data: Bundle?)

    fun onEditFailure(error: String?, key: String?, data: Bundle?)
}