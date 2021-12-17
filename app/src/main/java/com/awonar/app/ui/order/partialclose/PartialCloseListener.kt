package com.awonar.app.ui.order.partialclose

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener

interface PartialCloseListener : DialogListener {

    fun onCloseSuccess(name: String?, key: String?, data: Bundle?)

    fun onCloseFailure(error: String?, key: String?, data: Bundle?)
}