package com.awonar.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


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

    fun convertBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file =
                File("${Environment.getExternalStorageDirectory()}${File.separator}${fileNameToSave}")
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }
    }
}