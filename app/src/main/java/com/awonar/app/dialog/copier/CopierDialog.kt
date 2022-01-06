package com.awonar.app.dialog.copier

import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.awonar.app.dialog.DialogViewModel

class CopierDialog :  InteractorDialog<CopierMapper, CopierListener, DialogViewModel>()  {

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<CopierMapper, CopierListener>  = viewModel.copierDialog

    override fun bindViewModel(): Class<DialogViewModel>  =  DialogViewModel::class.java
}