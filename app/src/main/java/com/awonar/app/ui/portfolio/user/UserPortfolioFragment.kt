package com.awonar.app.ui.portfolio.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioUserBinding
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class UserPortfolioFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioUserBinding by lazy {
        AwonarFragmentPortfolioUserBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        fun newInstance(): UserPortfolioFragment = UserPortfolioFragment()
    }


}