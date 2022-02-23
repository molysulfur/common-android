package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioUserBinding
import com.awonar.app.ui.profile.StatisticProfileFragment
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class UserPortfolioFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioUserBinding by lazy {
        AwonarFragmentPortfolioUserBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            userViewModel.userState.collect {
                it?.let {
                    viewModel.getManual(it.username)
                }
            }
        }
        binding.column1 = "Buy/Sell"
        binding.column2 = "Invested(%)"
        binding.column3 = "P/L(%)"
        binding.column4 = "Value(%)"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): UserPortfolioFragment = UserPortfolioFragment()
    }


}