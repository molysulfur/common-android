package com.awonar.app.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.transform.CircleCropTransformation
import com.awonar.android.model.user.User
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityProfileBinding
import com.awonar.app.dialog.copier.CopierDialog
import com.awonar.app.ui.user.UserViewModel
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {

    private var userId: String? = null
    private var user: User? = null

    private val userViewModel: UserViewModel by viewModels()

    companion object {
        const val EXTRA_USERID = "com.awonar.app.ui.profile.extra.userid"
    }

    private val binding: AwonarActivityProfileBinding by lazy {
        AwonarActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intent.extras?.apply {
            userId = getString(EXTRA_USERID)
        }
        observer()
        init()
    }

    private fun observer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userState.collect { userInfo ->
                    if (userInfo != null) {
                        user = userInfo
                        updateUser()
                        updateEditVisible()
                    }
                }
            }
        }
    }

    private fun updateEditVisible() {
        if (user?.isMe == true) {
            binding.awonarProfileButtonEditProfile.visibility = View.VISIBLE
            binding.awonarProfileGroupOptions.visibility = View.GONE
        } else {
            binding.awonarProfileButtonEditProfile.visibility = View.GONE
            binding.awonarProfileGroupOptions.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUser() {
        ImageUtil.loadImage(binding.awonarProfileImageAvatar, user?.avatar) {
            crossfade(true)
            error(R.drawable.awonar_placeholder_avatar)
            transformations(CircleCropTransformation())
        }
        binding.awonarProfileTitleFollowers.setTitle("${user?.followerCount ?: 0}")
        binding.awonarProfileTitleFollowing.setTitle("${user?.followingCount ?: 0}")
        binding.awonarProfileTitleCopies.setTitle("${user?.copiesCount ?: 0}")
        binding.awonarProfileTextUsername.text = user?.username
        binding.awonarProfileTextName.text =
            "${user?.firstName} ${user?.middleName} ${user?.lastName}"
        binding.awonarProfileTextAbout.text = user?.about
        binding.awonarProfileButtonEditProfile.visibility =
            if (user?.isMe != true) View.GONE else View.VISIBLE
        binding.awonarProfileGroupOptions.visibility =
            if (user?.isMe == true) View.GONE else View.VISIBLE
    }

    private fun init() {
        binding.awonarProfileButtonCopy.setOnClickListener {
            CopierDialog.Builder()
                .setCopiesId(user?.id)
                .build()
                .show(supportFragmentManager)
        }
        binding.awonarProfileToolbarProfile.apply {
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        if (userId.isNullOrBlank()) {
            userViewModel.getUser(false)
        } else {
            userViewModel.getUser(userId!!)
        }
    }
}