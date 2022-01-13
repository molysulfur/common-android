package com.awonar.app.dialog.copier.edit

import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class EditCopierMapper : EventMapper<EditCopierListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): EditCopierListener =
        object : EditCopierListener {

        }

    override fun toListener(event: DialogEvent, listener: EditCopierListener) {

    }

}