package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnActivedBinding
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnListBinding

class PortFolioColumnFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioColumnListBinding by lazy {
        AwonarFragmentPortfolioColumnListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}