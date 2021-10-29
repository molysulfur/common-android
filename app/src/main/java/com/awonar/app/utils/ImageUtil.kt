package com.awonar.app.utils

import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R

fun ImageView.loadImage(url: String?) {
    ImageUtil.loadImage(this, url)
}

object ImageUtil {

    fun loadImage(imageView: ImageView, url: String?) {
        imageView.load(BuildConfig.BASE_IMAGE_URL + url){
            crossfade(true)
            placeholder(R.drawable.awonar_placeholder_avatar)
            error(R.drawable.awonar_placeholder_avatar)
        }
    }
}