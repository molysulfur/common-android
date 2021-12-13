package com.awonar.app.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeUtil {

    fun generateQRCode(content: String, width: Int = 200, height: Int = 200): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(
                content,
                BarcodeFormat.QR_CODE,
                width,
                height
            )
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (bitMatrix.get(j, i)) {
                        pixels[i * width + j] = 0x000000
                    } else {
                        pixels[i * width + j] = 0xffffff
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}