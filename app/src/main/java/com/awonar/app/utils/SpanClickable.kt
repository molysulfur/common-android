package com.awonar.app.utils

import android.text.style.ClickableSpan
import android.view.View
import timber.log.Timber

class SpanClickable(private val callback: (() -> Unit)) : ClickableSpan() {

    override fun onClick(widget: View) {
        Timber.e("Span Click")
        callback.invoke()
    }
}
