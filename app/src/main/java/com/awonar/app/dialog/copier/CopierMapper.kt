package com.awonar.app.dialog.copier

import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class CopierMapper : EventMapper<CopierListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): CopierListener =
        object : CopierListener {

        }

    override fun toListener(event: DialogEvent, listener: CopierListener) {

    }

}