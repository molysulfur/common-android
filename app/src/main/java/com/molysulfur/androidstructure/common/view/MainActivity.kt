package com.molysulfur.androidstructure.common.view

import androidx.appcompat.app.AppCompatDelegate
import com.molysulfur.library.ui.activity.ScreenOrientationHelperActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class MainActivity : ScreenOrientationHelperActivity() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}