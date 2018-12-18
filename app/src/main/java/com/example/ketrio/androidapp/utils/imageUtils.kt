package com.example.ketrio.androidapp.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.media.ExifInterface
import java.io.InputStream
import android.provider.OpenableColumns
import android.provider.MediaStore
import android.app.Activity
import android.content.ContentResolver


fun bitmapToBytes(bmp: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.WEBP, 100, stream)
    return stream.toByteArray()
}

fun bytesToBitmap(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size);
}

fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    if (maxHeight > 0 && maxWidth > 0) {
        val width = image.width
        val height = image.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
    } else {
        return image
    }
}