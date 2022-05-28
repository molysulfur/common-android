package com.awonar.app.ui.order.edit

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener

interface EditPositionListener : DialogListener {

    fun onEditSuccess(name: String?, key: String?, data: Bundle?)

    fun onEditFailure(error: String?, key: String?, data: Bundle?)
}