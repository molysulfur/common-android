package com.awonar.app.ui.auth.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.databinding.AwonarFragmentForgotPasswordBinding
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment() {

    companion object {
        private const val RESENT_TIMEOUT = 60
    }

    private val binding: AwonarFragmentForgotPasswordBinding by lazy {
        AwonarFragmentForgotPasswordBinding.inflate(layoutInflater)
    }

    val viewModel: ForgotPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        lifecycleScope.launch {
            viewModel.submitState.collect { text ->
                if (text?.isNotBlank() == true) {
                    binding.awonarForgotPasswordButtonSent.text = "Resend ($RESENT_TIMEOUT)"
                    binding.awonarForgotPasswordTextTitle.text = text
                    binding.awonarForgotPasswordTextSubtitle.visibility = View.VISIBLE
                    binding.awonarForgotPasswordInputEmail.visibility = View.GONE
                }
            }
        }
        return binding.root
    }
}