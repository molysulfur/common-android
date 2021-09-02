package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding
import com.awonar.app.databinding.AwonarFragmentPersonalStepTwoBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PersonalInfoStepTwoFragment : Fragment() {

    private val viewModel: PrivacyViewModel by activityViewModels()
    private val personalViewModel: PersonalActivityViewModel by activityViewModels()

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
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            personalViewModel.pageState.collect { next ->
                if (next == 2 && viewModel.personalState.value?.finish2 != true) {
                    onSubmit()
                }
            }
        }
    }

    private fun onSubmit() {
        val country: String? = binding.awonarPersonalStepTwoInputCountry.editText?.text?.toString()
        val city: String? = binding.awonarPersonalStepTwoInputCity.editText?.text?.toString()
        val postal: String? = binding.awonarPersonalStepTwoInputPostal.editText?.text?.toString()
        val address: String? = binding.awonarPersonalStepTwoInputAddress.editText?.text?.toString()
        viewModel.updateAddress(country, city, postal, address)
    }
}