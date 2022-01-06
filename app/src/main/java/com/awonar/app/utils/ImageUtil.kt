package com.awonar.app.utils

import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R
import timber.log.Timber

fun ImageView.loadImage(url: String?) {
    ImageUtil.loadImage(this, url)
}

object ImageUtil {

    fun loadImage(
        imageView: ImageView,
        url: String?,
        builder: ImageRequest.Builder.() -> Unit = {}
    ) {
        imageView.load(BuildConfig.BASE_IMAGE_URL + url) {
            apply(builder)
            placeholder(R.drawable.awonar_placeholder_avatar)
        }
    }
}