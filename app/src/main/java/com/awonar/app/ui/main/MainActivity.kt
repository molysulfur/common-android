package com.awonar.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import coil.load
import com.awonar.android.model.user.User
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityMainBinding
import com.awonar.app.databinding.AwonarDrawerHeaderMainBinding
import com.awonar.app.ui.auth.AuthViewModel
import com.awonar.app.ui.auth.AuthenticationActivity
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivityAndClearThisActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var user: User? = null

    private val binding by lazy {
        AwonarActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var headerBinding: AwonarDrawerHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeaderDrawer()
        setContentView(binding.root)
        obsever()
        init()
    }

    private fun setHeaderDrawer() {
        headerBinding = AwonarDrawerHeaderMainBinding.bind(
            binding.awonarMainDrawerNavigationMain.getHeaderView(0)
        )
        headerBinding.awonarDrawerHeaderMainButtonClose.setOnClickListener {
            binding.awonarMainDrawerSidebar.close()
        }
        headerBinding.awinarDrawerHeaderMainImageAvatar.setOnClickListener {

        }
    }

    private fun obsever() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.signOutState.collect {
                    if (it != null) {
                        openActivityAndClearThisActivity(AuthenticationActivity::class.java)
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userState.collect { userInfo ->
                    if (user != null) {
                        user = userInfo
                        updateUser()
                    }
                }
            }
        }

    }

    private fun updateUser() {
        headerBinding.awinarDrawerHeaderMainTextName.text = user?.username ?: ""
    }


    private fun init() {
        binding.awonarMainToolbarHeader.setNavigationOnClickListener {
            binding.awonarMainDrawerSidebar.open()
        }

        binding.awonarMainDrawerNavigationMain.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.awonar_menu_drawer_main_logout -> {
                    authViewModel.signOut()
                }
            }
            menuItem.isChecked = true
            binding.awonarMainDrawerSidebar.close()
            true
        }
    }

}