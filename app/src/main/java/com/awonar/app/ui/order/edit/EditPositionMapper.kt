package com.awonar.app.ui.order.edit

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent


class EditPositionMapper : EventMapper<EditPositionListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): EditPositionListener =
        object : EditPositionListener {
            override fun onEditSuccess(name: String?, key: String?, data: Bundle?) {
                event.value = DialogEvent.Builder(
                    event = EVENT_SUCCESS,
                    key = key,
                    data = data?.apply {
                    }
                ).build()
            }

            override fun onEditFailure(error: String?, key: String?, data: Bundle?) {
                event.value = DialogEvent.Builder(
                    event = EVENT_FAILURE,
                    key = key,
                    data = data?.apply {
                    }
                ).build()
            }
        }

    override fun toListener(event: DialogEvent, listener: EditPositionListener) {

    }

}