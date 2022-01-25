package com.awonar.app.dialog.copier.stop

import androidx.core.os.bundleOf
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class StopCopierMapper : EventMapper<StopCopierListener>() {

    companion object {
        private val EVENT_SUCCESS = "event_success"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): StopCopierListener =
        object : StopCopierListener {
            override fun onSuccess(message: String) {
                event.value = DialogEvent.Builder(
                    event = EVENT_SUCCESS,
                    key = "success",
                    data = bundleOf("message" to message)
                ).build()
            }

        }

    override fun toListener(event: DialogEvent, listener: StopCopierListener) {
        when (event.getEvent()) {
            EVENT_SUCCESS -> listener.onSuccess(event.getData()?.getString("message") ?: "")
        }
    }
}