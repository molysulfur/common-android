package com.awonar.app.ui.setting.bank

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityBankAccountBinding
import com.awonar.app.ui.RemoteConfigViewModel
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.example.camerax.CameraActivity
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivityForResult
import com.molysulfur.library.extension.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.io.InputStream

@AndroidEntryPoint
class BankAccountActivity : BaseActivity() {

    private var image: String? = null

    private val viewModel: PrivacyViewModel by viewModels()
    private val remoteViewModel: RemoteConfigViewModel by viewModels()

    private val binding: AwonarActivityBankAccountBinding by lazy {
        AwonarActivityBankAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
        binding.lifecycleOwner = this
        binding.remote = remoteViewModel
        binding.viewModel = viewModel
        binding.awonarBankButtonUploadBank.setOnClickListener {
            openActivityForResult(CameraActivity::class.java, CameraActivity.CAMERA_REQUEST, null)
        }
        binding.awonarBankButtonSubmit.setOnClickListener {
            onSubmit()
        }

    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.toastMessageState.collect { message ->
                if (!message.isNullOrBlank()) {
                    toast("$message")
                    finish()
                }
            }
        }
    }

    private fun onSubmit() {
        val accountName: String? = binding.awonarBankInputAccountName.editText?.text?.toString()
        val accountNumber: String? = binding.awonarBankInputAccountNumber.editText?.text?.toString()
        val country: String? = binding.awonarBankInputCountry.editText?.text?.toString()
        val bank: String? = binding.awonarBankInputBank.editText?.text?.toString()
        val accountType: String? = binding.awonarBankInputAccountType.editText?.text?.toString()

        if (accountName.isNullOrBlank()) {
            binding.awonarBankInputAccountName.error = getString(R.string.awonar_text_required)
            return
        }
        binding.awonarBankInputAccountName.error = ""
        if (accountNumber.isNullOrBlank()) {
            binding.awonarBankInputAccountNumber.error = getString(R.string.awonar_text_required)
            return
        }
        binding.awonarBankInputAccountNumber.error = ""
        if (country.isNullOrBlank()) {
            binding.awonarBankInputCountry.error = getString(R.string.awonar_text_required)
            return
        }
        binding.awonarBankInputCountry.error = ""
        if (bank.isNullOrBlank()) {
            binding.awonarBankInputBank.error = getString(R.string.awonar_text_required)
            return
        }
        binding.awonarBankInputBank.error = ""
        if (accountType.isNullOrBlank()) {
            binding.awonarBankInputAccountType.error = getString(R.string.awonar_text_required)
            return
        }
        binding.awonarBankInputAccountType.error = ""
        if (image.isNullOrBlank()) {
            toast("Upload Your Book Bank.please.")
            return
        }
        val countryId = remoteViewModel.countryState.value?.findLast { it.name.equals(country) }?.id ?: ""
        if (countryId.isBlank()) {
            toast("Not found! ,your country is not support. please check again.")
            return
        }
        val bankId = remoteViewModel.bankState.value?.findLast { it.name.equals(bank) }?.id ?: ""
        if (bankId.isBlank()) {
            toast("Not found! ,your bank is not support. please check again.")
            return
        }
        viewModel.uploadBookBank(
            accountName,
            accountNumber,
            country,
            bankId,
            accountType,
            image!!
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (CameraActivity.CAMERA_REQUEST == requestCode) {
            val uri = data?.data
            uri?.let {
                binding.awonarBankButtonUploadBank.text = data.data?.path
                val imageStream: InputStream? = contentResolver?.openInputStream(it)
                val bm: Bitmap = BitmapFactory.decodeStream(imageStream)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                image = encImage
            }
        }

    }

}