package com.awonar.app.ui.socialtrade

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.awonar.app.ui.socialtrade.filter.SocialTradeFilterActivity
import com.google.android.material.chip.Chip
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

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
            onViewMore = { key ->
                val filter = getFilterWithViewMoreClick(key)
                filter?.let {
                    viewModel.filter(it)
                }
            }
            onLoadMore = {
                viewModel.filter()
            }
            onItemClick = { userId ->
                openActivity(
                    ProfileActivity::class.java,
                    bundleOf(ProfileActivity.EXTRA_USERID to userId)
                )
            }
        }
    }

    private fun getFilterWithViewMoreClick(key: String) = when (key) {
        "mostCopies" -> mapOf(
            "period" to "1MonthAgo",
            "verified" to "true",
            "maxRisk" to "7",
            "sort" to "-gain,username",
        )
        "lowRisk" -> mapOf(
            "period" to "1MonthAgo",
            "verified" to "true",
            "maxRisk" to "7",
            "sort" to "risk,-gain,username",
        )
        "longTerm" -> mapOf(
            "period" to "6MonthsAgo",
            "verified" to "true",
            "maxRisk" to "7",
            "sort" to "-gain,username",
        )
        "shortTerm" -> mapOf(
            "period" to "1MonthAgo",
            "verified" to "true",
            "maxRisk" to "8",
            "sort" to "-gain,username",
        )
        else -> null
    }

    private val horizontalWrapperAdapter: SocialTradeHorizontalWrapperAdapter by lazy {
        SocialTradeHorizontalWrapperAdapter(recommendedAdapter)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val mapper: HashMap<String, String> =
                    it.data?.getSerializableExtra("map") as (HashMap<String, String>)
                viewModel.filter(mapper)
            }
        }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
        }.build()
        ConcatAdapter(config, horizontalWrapperAdapter, socialTradeAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.filter.collect { filters ->
                binding.awonarSocialTradeChipGroupFilter.removeAllViews()
                for ((key, u) in filters ?: mutableMapOf()) {
                    u.split(",").forEach { value ->
                        binding.awonarSocialTradeChipGroupFilter.addView(Chip(requireContext())
                            .apply {
                                chipEndPadding = 8f
                                text = "$key $value"
                                isCloseIconVisible = true
                                setOnCloseIconClickListener {
                                    viewModel.removeFilter(key, value)
                                }
                            })
                    }

                }
            }
        }
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
        with(binding.awonarSocialTradeButtonFilter) {
            setOnClickListener {
                openActivityCompatForResult(getContent, SocialTradeFilterActivity::class.java)
            }
        }
    }
}