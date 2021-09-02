package com.awonar.app.ui.setting.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPersonalStepThreeBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.example.camerax.CameraActivity
import com.molysulfur.library.extension.openActivityForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appCompatButton3.setOnClickListener {
            openActivityForResult(CameraActivity::class.java, 100, Bundle())
        }
    }

    private fun observe() {
        launchAndRepeatWithViewLifecycle {
            personalViewModel.pageState.collect {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("${ data?.data }")
    }
}