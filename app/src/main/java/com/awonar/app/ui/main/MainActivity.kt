package com.awonar.app.ui.main

import android.content.Intent
import com.awonar.app.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.awonar.android.model.user.AccountVerifyType
import com.awonar.android.model.user.User
import com.awonar.app.databinding.AwonarActivityMainBinding
import com.awonar.app.databinding.AwonarDrawerHeaderMainBinding
import com.awonar.app.ui.auth.AuthViewModel
import com.awonar.app.ui.auth.AuthenticationActivity
import com.awonar.app.ui.profile.ProfileActivity
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivityAndClearThisActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.openActivityCompatForResult


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var user: User? = null
    private lateinit var mNavController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val binding: AwonarActivityMainBinding by lazy {
        AwonarActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var headerBinding: AwonarDrawerHeaderMainBinding

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == ProfileActivity.RESULT_CODE) {
                mNavController.navigate(R.id.portFolioFragment)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeaderDrawer()
        setContentView(binding.root)
        obsever()
        setupNavigation()
        init()
    }

    private fun setupNavigation() {
        mNavController =
            (supportFragmentManager.findFragmentById(R.id.awonar_main_drawer_navigation_host_main) as NavHostFragment).findNavController()
        appBarConfiguration = AppBarConfiguration(
            mNavController.graph,
            binding.awonarMainDrawerSidebar
        )
        NavigationUI.setupWithNavController(binding.awonarMainDrawerNavigationMain, mNavController)
        binding.awonarMainDrawerNavigationMain.setNavigationItemSelectedListener { menuItem ->
            navigationDrawer(menuItem)
        }
    }

    private fun navigationDrawer(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        binding.awonarMainDrawerSidebar.close()
        return if (menuItem.itemId == R.id.awonar_menu_drawer_main_logout) {
            authViewModel.signOut()
            true
        } else {
            onNavDestinationSelected(menuItem, mNavController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            mNavController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun setHeaderDrawer() {
        headerBinding = AwonarDrawerHeaderMainBinding.bind(
            binding.awonarMainDrawerNavigationMain.getHeaderView(0)
        )
        headerBinding.awonarDrawerHeaderMainButtonClose.setOnClickListener {
            binding.awonarMainDrawerSidebar.close()
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
                    if (userInfo != null) {
                        user = userInfo
                        updateUser()
                    }
                }
            }
        }

    }

    private fun updateUser() {
        headerBinding.awonarDrawerHeaderMainTextName.text = user?.username ?: ""
        headerBinding.awonarDrawerHeaderMainConstraintProfile.setOnClickListener {
            openActivityCompatForResult(activityResult, ProfileActivity::class.java, null)
        }
        headerBinding.awonarDrawerHeaderMainButtonDeposit.setOnClickListener {
            findNavController(R.id.awonar_main_drawer_navigation_host_main).navigate(R.id.depositFragment)
        }
        ImageUtil.loadImage(headerBinding.awonarDrawerHeaderMainImageAvatar, user?.avatar)
        visibleAccountVerifyType()
    }


    private fun visibleAccountVerifyType() {
        headerBinding.awonarDrawerHeaderMainButtonVerifyAccount.visibility =
            if (user?.accountVerifyType == AccountVerifyType.PREPARE || user?.accountVerifyType == AccountVerifyType.REJECT)
                View.VISIBLE
            else View.GONE
    }


    private fun init() {
        binding.awonarMainToolbarHeader.setNavigationOnClickListener {
            binding.awonarMainDrawerSidebar.open()
        }

        userViewModel.getUser(needFresh = true)
    }

}