package com.awonar.app.ui.setting.personal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.app.databinding.AwonarActivityPersonalInfoBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PersonalInfoActivity : BaseActivity() {

    private var currentItem: Int = 0
    private var info: PersonalInfoResponse? = null

    private val personalViewModel: PersonalActivityViewModel by viewModels()
    private val viewModel: PrivacyViewModel by viewModels()

    private val binding: AwonarActivityPersonalInfoBinding by lazy {
        AwonarActivityPersonalInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
        initPagerView()
        setListenner()
    }

    private fun setListenner() {
        binding.awonarPersonalInfoTextBack.setOnClickListener {
            currentItem--
            binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
        }
        binding.awonarPersonalInfoTextContinus.setOnClickListener {
            when (currentItem) {
                0 -> {
                    if (info?.finish1 != true) {
                        MaterialAlertDialogBuilder(this).setTitle("Confirm")
                            .setMessage("Sending for information")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setPositiveButton("OK") { dialog, which ->
                                currentItem++
                                binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                                personalViewModel.onPageChange(currentItem)
                            }.show()
                    } else {
                        currentItem++
                        binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                    }
                }
                1 -> {
                    if (info?.finish2 != true) {
                        MaterialAlertDialogBuilder(this).setTitle("Confirm")
                            .setMessage("Sending for information")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setPositiveButton("OK") { dialog, which ->
                                currentItem++
                                binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                                personalViewModel.onPageChange(currentItem)
                            }.show()
                    } else {
                        currentItem++
                        binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                    }
                }
                2 -> {
                    if (info?.finish3 != true) {
                        MaterialAlertDialogBuilder(this).setTitle("Confirm")
                            .setMessage("Sending for information")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setPositiveButton("OK") { dialog, which ->
                                currentItem++
                                binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                            }.show()
                    } else {
                        currentItem++
                        binding.awonarPersonalInfoPagerInfo.currentItem = currentItem
                    }
                }
            }

            personalViewModel.onPageChange(currentItem + 1)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalState.collect {
                    info = it
                    val adapter =
                        (binding.awonarPersonalInfoPagerInfo.adapter as PersonalInfoAdapter?)
                    if (adapter != null) {
                        it?.run {
                            when {
                                finish3 -> {
                                    currentItem = 2
                                }
                                finish2 -> {
                                    currentItem = 1
                                }
                                else -> {
                                }
                            }
                            binding.awonarPersonalInfoPagerInfo.setCurrentItem(currentItem, false)
                        }
                    }
                }
            }
        }

    }

    private fun initPagerView() {
        if (binding.awonarPersonalInfoPagerInfo.adapter == null) {
            binding.awonarPersonalInfoPagerInfo.apply {
                adapter = PersonalInfoAdapter(supportFragmentManager, lifecycle)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                isUserInputEnabled = false
                binding.awonarPersonalInfoIndicatorPage.setViewPager2(this)
            }
        }
    }
}
