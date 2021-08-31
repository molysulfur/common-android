package com.awonar.app.ui.setting.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPrivacyMenuBinding

class PrivacyMenuFragment : Fragment() {

    private val binding: AwonarFragmentPrivacyMenuBinding by lazy {
        AwonarFragmentPrivacyMenuBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.awonarPrivacyMenuButtonTradingActivity.setOnClickListener {
            findNavController().navigate(R.id.action_privacyMenuFragment_to_tradingActivityFragment)
        }
        return binding.root
    }
}