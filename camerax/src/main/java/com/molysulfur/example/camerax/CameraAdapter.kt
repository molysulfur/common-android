package com.molysulfur.example.camerax

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.molysulfur.example.camerax.databinding.ItemPhotoBinding
import java.io.File

class CameraAdapter constructor(
    private val mediaList: MutableList<File>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val media: File = mediaList[position]
        (holder as PhotoViewHolder).setImage(media)
    }

    override fun getItemCount(): Int = mediaList.size

    private class PhotoViewHolder constructor(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(image: File) {
            binding.itemPhoto.load(image)
        }
    }
}