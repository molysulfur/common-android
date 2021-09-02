package com.awonar.app.ui.setting.personal

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPersonalStepThreeBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.example.camerax.CameraActivity
import com.molysulfur.library.extension.openActivityForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.InputStream
import com.molysulfur.library.extension.toast
import java.io.ByteArrayOutputStream


class PersonalInfoStepThreeFragment : Fragment() {

    private var imageFront: String? = null
    private var imageBack: String? = null

    private val personalViewModel: PersonalActivityViewModel by activityViewModels()
    private val viewModel: PrivacyViewModel by activityViewModels()

    private val binding: AwonarFragmentPersonalStepThreeBinding by lazy {
        AwonarFragmentPersonalStepThreeBinding.inflate(layoutInflater)
    }

    companion object {
        private const val REQUEST_CODE_FRONT = 200
        private const val REQUEST_CODE_BACK = 201

        fun newInstance(): PersonalInfoStepThreeFragment = PersonalInfoStepThreeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarPersonalStepThreeButtonTakeFront.setOnClickListener {
            openActivityForResult(CameraActivity::class.java, REQUEST_CODE_FRONT, Bundle())
        }
        binding.awonarPersonalStepThreeButtonTakeBack.setOnClickListener {
            openActivityForResult(CameraActivity::class.java, REQUEST_CODE_BACK, Bundle())
        }
        binding.awonarPersonalStepThreeRadioIdType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.awonar_personal_step_three_radio_card_id -> binding.awonarPersonalStepThreeConstraintGroup.visibility =
                    View.VISIBLE
                else -> binding.awonarPersonalStepThreeConstraintGroup.visibility = View.GONE
            }
        }
    }

    private fun observe() {
        launchAndRepeatWithViewLifecycle {
            personalViewModel.pageState.collect { next ->
                if (next == 3 && viewModel.personalState.value?.finish3 != true) {
                    onSubmit()
                }
            }
        }
    }

    private fun onSubmit() {
        val idCard = binding.awonarPersonalStepThreeInputNumber.editText?.text?.toString()
        if (idCard.isNullOrBlank()) {
            binding.awonarPersonalStepThreeInputNumber.error = "Required."
            return
        }
        binding.awonarPersonalStepThreeInputNumber.error = ""
        val idType = when (binding.awonarPersonalStepThreeRadioIdType.checkedRadioButtonId) {
            R.id.awonar_personal_step_three_radio_passport -> 0
            R.id.awonar_personal_step_three_radio_card_id -> 1
            R.id.awonar_personal_step_three_radio_license -> 2
            else -> -1
        }
        viewModel.updateVerifyCardId(idCard,idType, arrayListOf(imageFront, imageBack))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FRONT) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                binding.awonarPersonalStepThreeButtonTakeFront.text = data.data?.path
                val imageStream: InputStream? = activity?.contentResolver?.openInputStream(it)
                val bm: Bitmap = BitmapFactory.decodeStream(imageStream)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                imageFront = encImage
            }

        }
        if (requestCode == REQUEST_CODE_BACK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                binding.awonarPersonalStepThreeButtonTakeBack.text = data.data?.path
                val imageStream: InputStream? = activity?.contentResolver?.openInputStream(it)
                val bm: Bitmap = BitmapFactory.decodeStream(imageStream)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                imageBack = encImage
            }
        }
    }
}