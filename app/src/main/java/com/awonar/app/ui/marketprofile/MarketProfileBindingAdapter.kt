package com.awonar.app.ui.marketprofile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.utils.loadImage


@BindingAdapter("marketProfileAvatar")
fun setAvatar(image: ImageView, url: String?) {
    if (url != null)
        image.loadImage(url)
}