package com.awonar.app.ui.setting.experience.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.android.model.experience.Topic
import com.awonar.app.databinding.AwonarFragmentExperienceBinding
import com.awonar.app.ui.setting.experience.ExperienceViewModel

class TradingExperienceFragment : Fragment() {
    private var topic: Topic? = null

    private val viewModel: ExperienceViewModel by activityViewModels()

    companion object {

        private const val EXTRA_TOPIC = "com.awonar.app.ui.setting.experience.page.one.extra.topic"

        fun newInstance(topic: Topic?) = TradingExperienceFragment().apply {
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
                title = "Your Trading Experience",
                subTitle = "In the past 3 months How many times have you invested in these instruments?",
                it
            )
        }
    }

}