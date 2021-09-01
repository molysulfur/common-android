package com.molysulfur.example.camerax

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.molysulfur.example.camerax.databinding.FragmentCameraGalleryBinding
import java.io.File
import java.util.*

val EXTENSION_WHITELIST = arrayOf("JPG")

class CameraGalleryFragment : Fragment() {

    companion object {
        private const val TAG = "CameraGalleryFragment"
    }

    private val binding: FragmentCameraGalleryBinding by lazy {
        FragmentCameraGalleryBinding.inflate(layoutInflater)
    }

    private val args: CameraGalleryFragmentArgs by navArgs()

    private lateinit var mediaList: MutableList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootDirectory = File(args.rootDirectory)

        // Walk through all files in the root directory
        // We reverse the order of the list to present the last photos first
        mediaList = rootDirectory.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.uppercase(Locale.ROOT))
        }?.sortedDescending()?.toMutableList() ?: mutableListOf()
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
        if (mediaList.isEmpty()) {
            Log.d(TAG, "Images is Empty")
        }

        // setup recycler view
        binding.cameraRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = CameraAdapter(mediaList)
        }

        binding.cameraToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}