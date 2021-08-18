package com.awonar.app.ui.auth.signin

import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentSigninBinding
import com.awonar.app.ui.auth.AuthViewModel
import com.awonar.app.utils.SpannableUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern

class SignInFragment : Fragment(R.layout.awonar_fragment_signin) {

    private val binding: AwonarFragmentSigninBinding by lazy {
        AwonarFragmentSigninBinding.inflate(layoutInflater)
    }

    val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launch {
            authViewModel.signInWithPasswordState.collect {
                Timber.e("$it")
            }
        }
        init()
        return binding.root
    }

    private fun init() {
        binding.awonarSigninButtonSignin.setOnClickListener {
            val password: String = binding.awonarSigninInputPassword.editText?.text.toString()
            val regex: Pattern = Pattern.compile("[^A-Za-z0-9]")
            if (regex.matcher(password).find()) {
                binding.awonarSigninInputPassword.error = "Only A-Z,a-z,0-9"
                return@setOnClickListener;
            }
            binding.awonarSigninInputPassword.error = ""

        }
        binding.awonarSigninButtonFacebook.setOnClickListener {
            authViewModel.testViewModel()
        }

        binding.awonarSigninButtonGoogle.setOnClickListener {

        }
        binding.awonarSigninTextNoAccount.apply {
            text = SpannableUtil.getDontHaveAccountSpannable(context ?: requireContext(),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                })
            movementMethod = LinkMovementMethod()
        }

    }


}