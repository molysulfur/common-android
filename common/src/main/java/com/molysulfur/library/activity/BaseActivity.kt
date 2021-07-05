package com.forexcity.common.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.akexorcist.screenorientationhelper.ScreenOrientationHelper


open class BaseActivity constructor(resLayout: Int) : LocalizationActivity(resLayout), ScreenOrientationHelper.ScreenOrientationChangeListener {

    @Suppress("LeakingThis")
    private val helper = ScreenOrientationHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper.onCreate(savedInstanceState);
        helper.setScreenOrientationChangeListener(this)
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

    override fun onScreenOrientationChanged(orientation: Int) {

    }

}