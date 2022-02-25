package com.awonar.app.ui.portfolio.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentPortfolioPublicBinding
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PublicPortfolioFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioPublicBinding by lazy {
        AwonarFragmentPortfolioPublicBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateActions.collect {
                findNavController().navigate(it)
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
        viewModel.getManual(userViewModel.userState.value?.username)

    }

    companion object {
        fun newInstance(): UserPortfolioFragment = UserPortfolioFragment()
    }


}