package com.awonar.app.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImage")
fun setImage(view: ImageView, url: String?) {
    ImageUtil.loadImage(view, url)
}