package com.awonar.app.ui.auth

import android.os.Bundle
import com.awonar.app.R
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivityForResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.awonar_activity_auth)

    }

}