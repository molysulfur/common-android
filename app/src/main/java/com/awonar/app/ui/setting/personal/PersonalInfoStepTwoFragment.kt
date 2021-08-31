package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding

class PersonalInfoStepTwoFragment : Fragment() {

    private val binding: AwonarFragmentPersonalStepOneBinding by lazy {
        AwonarFragmentPersonalStepOneBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}