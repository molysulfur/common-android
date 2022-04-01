package com.awonar.app.ui.profile.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentPortfolioUserBinding

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