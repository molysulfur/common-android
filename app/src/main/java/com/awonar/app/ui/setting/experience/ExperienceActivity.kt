package com.awonar.app.ui.setting.experience

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.awonar.android.model.experience.ExperienceResponse
import com.awonar.app.databinding.AwonarActivityExperienceBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExperienceActivity : BaseActivity() {

    private val viewModel: ExperienceViewModel by viewModels()

    private val binding: AwonarActivityExperienceBinding by lazy {
        AwonarActivityExperienceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
        binding.awonarExperiencePagerContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.experienceQuestion.value.let { questionnaire ->
                    if (questionnaire?.id != null) {
                        viewModel.createRequest(
                            questionnaireId = questionnaire.id!!,
                            data = questionnaire.topics?.get(position),
                            position = position
                        )
                    }
                }
            }
        })
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.experienceQuestion.collect {
                    setUpPagerView(it)
                }
            }
        }

    }

    private fun setUpPagerView(experience: ExperienceResponse?) {
        if (experience != null && binding.awonarExperiencePagerContainer.adapter == null) {
            val adapter = ExperienceAdapter(experience, supportFragmentManager, lifecycle)
            binding.awonarExperiencePagerContainer.apply {
                this.adapter = adapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }

        }
    }
}