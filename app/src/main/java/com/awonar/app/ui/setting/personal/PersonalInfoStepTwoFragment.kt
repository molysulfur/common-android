package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding
import com.awonar.app.databinding.AwonarFragmentPersonalStepTwoBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel

class PersonalInfoStepTwoFragment : Fragment() {

    private val viewModel: PrivacyViewModel by activityViewModels()

    private val binding: AwonarFragmentPersonalStepTwoBinding by lazy {
        AwonarFragmentPersonalStepTwoBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(): PersonalInfoStepTwoFragment = PersonalInfoStepTwoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}