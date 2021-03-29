package com.molysulfur.library.utils

import com.akexorcist.localizationactivity.core.OnLocaleChangedListener

class OnLocaleChangedHelperAdapter(
    var beforeLocaleChanged: () -> Unit,
    var afterLocaleChanged: () -> Unit
) : OnLocaleChangedListener {
    override fun onAfterLocaleChanged() {
        afterLocaleChanged()
    }

    override fun onBeforeLocaleChanged() {
        beforeLocaleChanged()
    }

}