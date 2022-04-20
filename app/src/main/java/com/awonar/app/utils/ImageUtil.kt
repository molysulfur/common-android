package com.awonar.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R

fun ImageView.loadImage(url: String?) {
    ImageUtil.loadImage(this, url)
}

object ImageUtil {

    private val httpRegex = Regex("http[s]?://")

    fun loadImage(
        imageView: ImageView,
        url: String?,
        builder: ImageRequest.Builder.() -> Unit = {},
    ) {
        val link = if (url?.contains(httpRegex) == true) {
            url
        } else {
            BuildConfig.BASE_IMAGE_URL + url
        }
        imageView.load(link) {
            apply(builder)
            placeholder(R.drawable.awonar_placeholder_avatar)
        }
    }

    suspend fun getBitmap(url: String?, context: Context): Bitmap? {
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()
        val request = ImageRequest.Builder(context)
            .data(BuildConfig.BASE_IMAGE_URL + url)
            .crossfade(true)
            .build()
        return imageLoader.execute(request).drawable?.toBitmap()
    }
}