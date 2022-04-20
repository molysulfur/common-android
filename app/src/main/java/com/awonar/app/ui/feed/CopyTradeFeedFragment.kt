package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentCopytradeFeedBinding

class CopyTradeFeedFragment : Fragment() {

    private val binding: AwonarFragmentCopytradeFeedBinding by lazy {
        AwonarFragmentCopytradeFeedBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}