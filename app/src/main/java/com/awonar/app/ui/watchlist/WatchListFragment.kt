package com.awonar.app.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentWatchlistBinding
import com.awonar.app.ui.setting.SettingViewModel

class WatchListFragment : Fragment() {


    private val binding: AwonarFragmentWatchlistBinding by lazy {
        AwonarFragmentWatchlistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = activity
        return binding.root
    }
}