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
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentSigninBinding
import com.awonar.app.ui.auth.AuthViewModel
import com.awonar.app.utils.SpannableUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.awonar.app.ui.launcher.LauncherViewModel
import com.awonar.app.ui.main.MainActivity
import com.awonar.app.ui.market.MarketViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.extension.openActivityAndClearAllActivity
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest


class SignInFragment : Fragment(), FacebookCallback<LoginResult> {

    private lateinit var mCallbackManager: CallbackManager
    private lateinit var facebookManager: LoginManager

    companion object {
        private const val EMAIL = "email"
        private const val USER_POSTS = "public_profile"
    }

    private val binding: AwonarFragmentSigninBinding by lazy {
        AwonarFragmentSigninBinding.inflate(layoutInflater)
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val authViewModel: AuthViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupGoogleSignIn()
        setupFacebookSignIn()
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun setupFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create()
        facebookManager = LoginManager.getInstance()
        facebookManager.registerCallback(mCallbackManager, this)

    }

    private fun observe() {
        launchAndRepeatWithViewLifecycle {
            marketViewModel.navigateAction.collectLatest {
                openActivityAndClearAllActivity(MainActivity::class.java)
            }
        }
        launchAndRepeatWithViewLifecycle {
            authViewModel.navigation.collect { email ->
                if (!email.isNullOrBlank()) {
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToLinkAccountFragment(
                        email))
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            authViewModel.signInError.collect { message ->
                if (!message.isNullOrBlank()) {
                    toast(message)
                    binding.awonarSigninInputEmail.error =
                        getString(R.string.awonar_error_username)
                    binding.awonarSigninInputPassword.error =
                        getString(R.string.awonar_error_password)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            authViewModel.signInState.collect {
                if (it != null) {
                    marketViewModel.getTradingData()
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            authViewModel.goToSignUpState.collect { shouldGo ->
                if (shouldGo) {
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                }
            }
        }
    }


    private fun setupGoogleSignIn() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.awonar_google_server_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context ?: requireContext(), gso)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            authViewModel.signInWithGoogle(
                email = account.email,
                token = account.idToken,
                id = account.id
            )
        } catch (e: ApiException) {
            toast("Google sign in has something wrong!")
        }
    }

    private fun init() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }

        binding.awonarSigninButtonSignin.setOnClickListener {
            val password: String = binding.awonarSigninInputPassword.editText?.text.toString()
            val username: String = binding.awonarSigninInputEmail.editText?.text.toString()
            if (username.isNullOrBlank()) {
                binding.awonarSigninInputEmail.error = getString(R.string.awonar_error_username)
                return@setOnClickListener
            }
            if (password.isNullOrBlank()) {
                binding.awonarSigninInputPassword.error = getString(R.string.awonar_error_password)
                return@setOnClickListener
            }
            authViewModel.signIn(username = username, password = password)
        }
        binding.awonarSigninButtonFacebook.setOnClickListener {
            handleLogInFacebook()
        }
        binding.awonarSigninButtonForgot.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
        }

        binding.awonarSigninButtonGoogle.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val signInIntent: Intent = mGoogleSignInClient.signInIntent
                activityResultLauncher.launch(signInIntent)

            }
        }
        binding.awonarSigninTextNoAccount.apply {
            text = SpannableUtil.getDontHaveAccountSpannable(context ?: requireContext(),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                })
            movementMethod = LinkMovementMethod()
        }
    }

    private fun handleLogInFacebook() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            authViewModel.signInWithFacebook(accessToken?.token, accessToken?.userId)
        } else {
            facebookManager.logInWithReadPermissions(
                this, setOf(
                    EMAIL,
                    USER_POSTS
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityResultLauncher.unregister()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onSuccess(result: LoginResult?) {
        authViewModel.signInWithFacebook(result?.accessToken?.token, result?.accessToken?.userId)
    }

    override fun onCancel() {
        Timber.e("onCancel : cancel")
    }

    override fun onError(error: FacebookException?) {
        Timber.e("onError : ${error?.message}")
    }


}
