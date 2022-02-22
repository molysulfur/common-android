package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioUserBinding
import com.awonar.app.ui.profile.StatisticProfileFragment

class UserPortfolioFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioUserBinding by lazy {
        AwonarFragmentPortfolioUserBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.column1 = "Buy/Sell"
        binding.column2 = "Invested"
        binding.column3 = "P/L(%)"
        binding.column4 = "Value"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getManual()
    }

    companion object {
        fun newInstance(): UserPortfolioFragment = UserPortfolioFragment()
    }


}