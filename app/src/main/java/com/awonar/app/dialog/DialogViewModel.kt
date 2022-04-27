package com.awonar.app.dialog

import androidx.lifecycle.ViewModel
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.awonar.app.dialog.copier.CopierMapper
import com.awonar.app.dialog.copier.add.AddFundMapper
import com.awonar.app.dialog.copier.add.RemoveFundMapper
import com.awonar.app.dialog.copier.edit.EditCopierMapper
import com.awonar.app.dialog.copier.pause.PauseCopierMapper
import com.awonar.app.dialog.copier.stop.StopCopierMapper
import com.awonar.app.dialog.loading.LoadingMapper
import com.awonar.app.ui.order.OrderMapper
import com.awonar.app.ui.order.edit.OrderEditMapper
import com.awonar.app.ui.order.partialclose.PartialCloseMapper

class DialogViewModel : ViewModel() {
    val order = DialogLauncher(OrderMapper::class.java)
    val orderEdit = DialogLauncher(OrderEditMapper::class.java)
    val partialClose = DialogLauncher(PartialCloseMapper::class.java)
    val copierDialog = DialogLauncher(CopierMapper::class.java)
    val addCopierDialog = DialogLauncher(AddFundMapper::class.java)
    val removeCopierDialog = DialogLauncher(RemoveFundMapper::class.java)
    val stopCopierDialog = DialogLauncher(StopCopierMapper::class.java)
    val pauseCopierDialog = DialogLauncher(PauseCopierMapper::class.java)
    val editStopLossDialog = DialogLauncher(EditCopierMapper::class.java)
    val loadingDialog = DialogLauncher(LoadingMapper::class.java)
}