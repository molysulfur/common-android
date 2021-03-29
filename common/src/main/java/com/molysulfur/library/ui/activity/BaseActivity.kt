package com.molysulfur.library.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

open abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("setContentView")
        val layoutResId = setupLayoutView()
        if (layoutResId != 0) {
            setContentView(layoutResId)
            Timber.v("bindView")
            bindView()
        }
        Timber.v("prepare")
        prepare()
        savedInstanceState?.let { bundle ->
            Timber.v("restoreInstanceState: bundle=$bundle")
            restoreInstanceState(bundle)
            Timber.v("prepare")
            restore()
        } ?: run {
            Timber.v("restoreArgument: bundle=${intent.extras}")
            intent.extras?.let { bundle ->
                restoreArgument(bundle)
            }
            Timber.v("initialize")
            initialize()
        }
        Timber.v("setup")
        setup()
    }

    override fun onStart() {
        Timber.v("onStart")
        super.onStart()
    }

    override fun onResume() {
        Timber.v("onResume")
        super.onResume()
    }

    override fun onPause() {
        Timber.v("onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.v("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.v("onDestroy")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.v("onSaveInstanceState")
        super.onSaveInstanceState(outState)
        Timber.v("saveInstanceState")
        saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Timber.v("onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    @LayoutRes
    abstract fun setupLayoutView(): Int

    abstract fun bindView()

    abstract fun prepare()

    abstract fun restoreArgument(bundle: Bundle)

    abstract fun initialize()

    abstract fun restoreInstanceState(savedInstanceState: Bundle)

    abstract fun restore()

    abstract fun setup()

    abstract fun saveInstanceState(outState: Bundle)
}