package com.molysulfur.example.camerax

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.molysulfur.example.camerax.databinding.FragmentCameraBinding
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.io.File
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment() {

    companion object {
        private const val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null

    private val binding: FragmentCameraBinding by lazy {
        FragmentCameraBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionsGranted()) {
            setUpCamera()
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = CameraActivity.getOutputDirectory(requireContext())

        binding.viewFinder.post {
            setUpCamera()
        }

    }

    private fun bindTakePhoto() {
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        binding.cameraCaptureButton.setOnClickListener {
            imageCapture?.let { imageCapture ->
                imageCapture.takePicture(outputOptions, cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val result = Intent()
                            result.data = Uri.fromFile(photoFile)
                            Log.d(TAG, result.data.toString())
                            activity?.apply {
                                setResult(Activity.RESULT_OK, result)
                                finish()
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                        }
                    })
            }
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Enable or disable switching between cameras
            updateCameraSwitchButton()

            // Build and bind the camera use cases
            bindCameraUseCases()
            bindTakePhoto()
            bindingGallery()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindingGallery() {
        binding.photoViewButton.setOnClickListener {
            findNavController().navigate(
                CameraFragmentDirections.actionCameraFragmentToCameraGalleryFragment(
                    outputDirectory.absolutePath
                )
            )
        }
    }

    private fun bindCameraUseCases() {
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview = Preview.Builder()
            .build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
        imageCapture = ImageCapture.Builder()
            .build()

        val imageAnalyzer = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                    Log.d(TAG, "Average luminosity: $luma")
                })
            }
        try {
            // Unbind use cases before rebinding
            cameraProvider?.unbindAll()
            // Bind use cases to camera
            cameraProvider?.bindToLifecycle(
                viewLifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
            )
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun updateCameraSwitchButton() {
        try {
            binding.cameraSwitchButton.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            binding.cameraSwitchButton.isEnabled = false
        }
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private class LuminosityAnalyzer(private val listener: (Double) -> Unit) :
        ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }


    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Shut down our background executor
        cameraExecutor.shutdown()
    }
}