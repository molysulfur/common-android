package com.molysulfur.androidstructure.common.view

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {


    fun test() {
        Log.e("TAG", "TEST")
    }
}