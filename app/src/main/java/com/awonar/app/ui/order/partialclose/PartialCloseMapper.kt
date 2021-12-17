package com.awonar.app.ui.order.partialclose

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class PartialCloseMapper : EventMapper<PartialCloseListener>() {

    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): PartialCloseListener =
        object : PartialCloseListener {
            override fun onCloseSuccess(name: String?, key: String?, data: Bundle?) {
                event.value = DialogEvent.Builder(
                    event = EVENT_SUCCESS,
                    key = key,
                    data = data?.apply {
                    }
                ).build()
            }

            override fun onCloseFailure(error: String?, key: String?, data: Bundle?) {
                event.value = DialogEvent.Builder(
                    event = EVENT_FAILURE,
                    key = key,
                    data = data?.apply {
                    }
                ).build()
            }
        }

    override fun toListener(event: DialogEvent, listener: PartialCloseListener) {
    }
}