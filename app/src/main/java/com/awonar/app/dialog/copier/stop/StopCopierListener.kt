package com.awonar.app.dialog.copier.stop

import com.akexorcist.library.dialoginteractor.DialogListener

interface StopCopierListener : DialogListener {

    fun onSuccess(message: String)
}