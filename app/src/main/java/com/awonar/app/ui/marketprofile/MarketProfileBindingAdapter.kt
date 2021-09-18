package com.awonar.app.ui.marketprofile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.awonar.android.shared.constrant.BuildConfig


@BindingAdapter("marketProfileAvatar")
fun setAvatar(image: ImageView, url: String?) {
    if (url != null)
        image.load(BuildConfig.BASE_IMAGE_URL + url)
}