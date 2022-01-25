package com.awonar.app.dialog.copier.add

import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class RemoveFundMapper : EventMapper<RemoveFundListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): RemoveFundListener =
        object : RemoveFundListener {

        }

    override fun toListener(event: DialogEvent, listener: RemoveFundListener) {

    }

}