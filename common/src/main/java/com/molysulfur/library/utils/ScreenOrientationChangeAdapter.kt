package com.molysulfur.library.utils

import com.akexorcist.screenorientationhelper.ScreenOrientationHelper

class ScreenOrientationChangeAdapter(var screenOrientationChanged: (orientation: Int) -> Unit) : ScreenOrientationHelper.ScreenOrientationChangeListener {
    override fun onScreenOrientationChanged(orientation: Int) {
        screenOrientationChanged(orientation)
    }
}