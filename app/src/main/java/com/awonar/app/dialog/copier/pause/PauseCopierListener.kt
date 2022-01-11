package com.awonar.app.dialog.copier.pause

import com.akexorcist.library.dialoginteractor.DialogListener

interface PauseCopierListener : DialogListener {

    fun onSuccess(message: String)
}