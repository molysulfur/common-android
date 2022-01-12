package com.awonar.app.ui.socialtrade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentSocialTradeBinding
import com.awonar.app.ui.profile.ProfileActivity
import com.awonar.app.ui.socialtrade.adapter.SocialTradeAdapter
import com.awonar.app.ui.socialtrade.adapter.SocialTradeHorizontalWrapperAdapter
import com.awonar.app.ui.socialtrade.adapter.SocialTradeRecommendedAdapter
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SocialTradeFragment : Fragment() {

    private val binding: AwonarFragmentSocialTradeBinding by lazy {
        AwonarFragmentSocialTradeBinding.inflate(layoutInflater)
    }

    private val viewModel: SocialTradeViewModel by activityViewModels()

    private val recommendedAdapter: SocialTradeRecommendedAdapter by lazy {
        SocialTradeRecommendedAdapter().apply {
            onItemClick = { userId ->
                openActivity(
                    ProfileActivity::class.java,
                    bundleOf(ProfileActivity.EXTRA_USERID to userId)
                )
            }
        }
    }

    private val socialTradeAdapter: SocialTradeAdapter by lazy {
        SocialTradeAdapter().apply {
            onItemClick = { userId ->
                openActivity(
                    ProfileActivity::class.java,
                    bundleOf(ProfileActivity.EXTRA_USERID to userId)
                )
            }
        }
    }

    private val horizontalWrapperAdapter: SocialTradeHorizontalWrapperAdapter by lazy {
        SocialTradeHorizontalWrapperAdapter(recommendedAdapter)
    }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
        }.build()
        ConcatAdapter(config, horizontalWrapperAdapter, socialTradeAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.copiesList.collect {
                socialTradeAdapter.itemList = it
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.recommendedState.collect {
                recommendedAdapter.itemList = it?.toMutableList() ?: mutableListOf()
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.awonarSocialTradeRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = concatAdapter
        }

    }
}