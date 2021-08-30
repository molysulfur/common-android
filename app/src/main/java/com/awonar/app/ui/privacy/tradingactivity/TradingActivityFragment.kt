package com.awonar.app.ui.privacy.tradingactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.android.model.user.User
import com.awonar.app.databinding.AwonarFragmentTradingActivityBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class TradingActivityFragment : Fragment() {

    private var user: User? = null

    private val binding: AwonarFragmentTradingActivityBinding by lazy {
        AwonarFragmentTradingActivityBinding.inflate(layoutInflater)
    }

    private val viewModel: TradingActivityViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.awonarTradingActivitySwitchPublic.setOnCheckedChangeListener { _, isChecked ->
            onPreferenceChange()
        }
        binding.awonarTradingActivitySwitchFullname.setOnCheckedChangeListener { _, isChecked ->
            onPreferenceChange()
        }
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getUser(false)
    }

    private fun observe() {
        launchAndRepeatWithViewLifecycle {
            userViewModel.userState.collect {
                user = it
                updateTradingActivityPreference()
            }
        }
    }

    private fun updateTradingActivityPreference() {
        binding.awonarTradingActivitySwitchPublic.isChecked = user?.isPrivate ?: false
        binding.awonarTradingActivitySwitchFullname.isChecked = user?.isDisplayFullName ?: false
    }

    private fun onPreferenceChange() {
        viewModel.toggleTradingActivity(
            isPrivate = binding.awonarTradingActivitySwitchPublic.isChecked,
            isDisplayFullName = binding.awonarTradingActivitySwitchFullname.isChecked
        )
    }

}