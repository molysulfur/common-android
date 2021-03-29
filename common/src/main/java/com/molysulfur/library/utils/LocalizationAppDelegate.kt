package com.molysulfur.library.utils

import android.app.Application
import android.content.Context
import com.akexorcist.localizationactivity.core.LocalizationUtility

class LocalizationAppDelegate constructor(private val application: Application) {

    fun onConfigurationChanged(context: Context) {
        LocalizationUtility.applyLocalizationContext(context)
    }

    fun attachBaseContext(context: Context): Context {
        return LocalizationUtility.applyLocalizationContext(context)
    }

    fun getApplicationContext(applicationContext: Context): Context {
        return LocalizationUtility.applyLocalizationContext(applicationContext)
    }
}