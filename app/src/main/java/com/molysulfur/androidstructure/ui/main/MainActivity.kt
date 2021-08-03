package com.molysulfur.androidstructure.ui.main

import android.os.Bundle
import com.molysulfur.androidstructure.R
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}