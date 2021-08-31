package com.awonar.app.ui.setting.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.awonar.app.databinding.AwonarFragmentPersonalStepOneBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class PersonalInfoStepOneFragment : Fragment() {

    private val viewModel: PrivacyViewModel by viewModels()

    private val binding: AwonarFragmentPersonalStepOneBinding by lazy {
        AwonarFragmentPersonalStepOneBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.personalState.collect {
                Timber.e("$it")
            }
        }
        return binding.root
    }
}