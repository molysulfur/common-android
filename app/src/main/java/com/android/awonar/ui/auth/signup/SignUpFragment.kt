package com.android.awonar.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.awonar.R
import com.android.awonar.databinding.AwonarFragmentSignupBinding

class SignUpFragment : Fragment(R.layout.awonar_fragment_signup) {
    private val binding: AwonarFragmentSignupBinding by lazy {
        AwonarFragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}