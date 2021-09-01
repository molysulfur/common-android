package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PersonalInfoStepOneFragment : Fragment() {

    private val viewModel: PrivacyViewModel by activityViewModels()
    private val personalViewModel: PersonalActivityViewModel by activityViewModels()

    private val binding: AwonarFragmentPersonalStepOneBinding by lazy {
        AwonarFragmentPersonalStepOneBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(): PersonalInfoStepOneFragment = PersonalInfoStepOneFragment()
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
            personalViewModel.pageState.collect { next ->
                if (next == 2 && viewModel.personalState.value?.finish1 != true) {
                    onSubmit()
                }
            }
        }
    }

    private fun onSubmit() {
        val name: String? = binding.awonarPersonalStepOneInputName.editText?.text?.toString()
        val middleName: String? =
            binding.awonarPersonalStepOneInputMiddle.editText?.text?.toString()
        val lastName: String? =
            binding.awonarPersonalStepOneInputLastname.editText?.text?.toString()
        val gender: Boolean =
            binding.awonarPersonalStepOneTextRadioGender.checkedRadioButtonId == R.id.awonar_personal_step_one_text_radio_male
        val birth : String? = binding.awonarPersonalStepOneTextInputBirthday.editText?.text?.toString()
        viewModel.updatePersonalProfile(name,middleName,lastName,gender,birth)
    }
}