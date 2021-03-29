package com.molysulfur.library.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.akexorcist.screenorientationhelper.ScreenOrientationHelper
import com.molysulfur.library.utils.ScreenOrientationChangeAdapter

abstract class ScreenOrientationHelperActivity : LocalizationHelperActivity() {
    @Suppress("LeakingThis")
    private val helper = ScreenOrientationHelper(this)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        helper.onCreate(savedInstanceState)
        helper.setScreenOrientationChangeListener(ScreenOrientationChangeAdapter { orientation ->
            onScreenOrientationChanged(orientation)
        })
    }

    override fun onStart() {
        super.onStart()
        helper.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        helper.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        helper.onRestoreInstanceState(savedInstanceState)
    }

    open fun onScreenOrientationChanged(orientation: Int) {
    }
}