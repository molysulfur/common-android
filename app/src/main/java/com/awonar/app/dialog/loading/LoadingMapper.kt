package com.awonar.app.dialog.loading

import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent
import com.awonar.app.dialog.copier.CopierListener

class LoadingMapper : EventMapper<LoadingListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): LoadingListener =
        object : LoadingListener {

        }

    override fun toListener(event: DialogEvent, listener: LoadingListener) {

    }

}