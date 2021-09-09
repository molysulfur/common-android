package com.awonar.app.ui.setting.experience.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.android.model.experience.Topic
import com.awonar.app.databinding.AwonarFragmentExperienceBinding
import com.awonar.app.ui.setting.experience.ExperienceViewModel
import com.awonar.app.ui.setting.experience.adapter.QuestionAdapter
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle

class TradingKnowledgeExperienceFragment : Fragment() {
    private var topic: Topic? = null
    private val viewModel: ExperienceViewModel by activityViewModels()

    companion object {

        private const val EXTRA_TOPIC = "com.awonar.app.ui.setting.experience.page.two.extra.topic"

        fun newInstance(topic: Topic?) = TradingKnowledgeExperienceFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_TOPIC, topic)
            }
        }
    }

    private val binding: AwonarFragmentExperienceBinding by lazy {
        AwonarFragmentExperienceBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            topic = it.getParcelable(EXTRA_TOPIC)
        }
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topic?.let {
            viewModel.convertExperienceItem(
                title = "Trading Knowledge (Choose more than one)",
                subTitle = "What’s your investment education experience regarding leveraged products?(CFDs, Futures, Options, Forex, Margin trade and ETF)",
                topic = it,
                isNoExpShow = true
            )
        }
    }

}