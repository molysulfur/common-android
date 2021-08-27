package com.awonar.app.ui.setting.about

import android.os.Bundle
import androidx.activity.viewModels
import com.awonar.app.databinding.AwonarActivityAboutMeBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutMeActivity : BaseActivity() {

    private val viewModel: UserViewModel by viewModels()

    val binding: AwonarActivityAboutMeBinding by lazy {
        AwonarActivityAboutMeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupListener()
        setContentView(binding.root)
        viewModel.getUser(needFresh = false)
    }

    private fun setupListener() {
        binding.awonarSettingAboutButtonSubmit.setOnClickListener {
            handleSubmitAboutMe()
        }
    }

    fun handleSubmitAboutMe() {
        val bio = binding.awonarSettingAboutInputBio.editText?.text.toString()
        val about = binding.awonarSettingAboutInputAbout.editText?.text.toString()
        val skill = binding.awonarSettingAboutInputSkill.editText?.text.toString()
        val facebook = binding.awonarSettingAboutInputFacebook.editText?.text.toString()
        val twitter = binding.awonarSettingAboutInputTwitter.editText?.text.toString()
        val linkedIn = binding.awonarSettingAboutInputLinkedin.editText?.text.toString()
        val youtube = binding.awonarSettingAboutInputYoutube.editText?.text.toString()
        val website = binding.awonarSettingAboutInputWebsite.editText?.text.toString()
        viewModel.updateUser(bio, about, skill, facebook, twitter, linkedIn, youtube, website)
        finish()
        toast("Updated.")
    }

}