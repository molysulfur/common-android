package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding
import com.awonar.app.databinding.AwonarFragmentPersonalStepThreeBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PersonalInfoStepThreeFragment : Fragment() {

    private val personalViewModel: PersonalActivityViewModel by activityViewModels()
    private val viewModel: PrivacyViewModel by activityViewModels()

    private val binding: AwonarFragmentPersonalStepThreeBinding by lazy {
        AwonarFragmentPersonalStepThreeBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(): PersonalInfoStepThreeFragment = PersonalInfoStepThreeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observe()
        return binding.root
    }

    private fun observe() {
        launchAndRepeatWithViewLifecycle {
            personalViewModel.pageState.collect {
            }
        }
    }
}