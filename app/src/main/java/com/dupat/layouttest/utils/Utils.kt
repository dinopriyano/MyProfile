package com.dupat.layouttest.utils

import android.graphics.Bitmap
import android.util.Log

class Utils{
    fun resizedBitmap(bm: Bitmap, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scale: Float = width.toFloat()/height.toFloat()
        val newWidth = newHeight * scale
        Log.d("Ukuran","$width $height $scale $newWidth $newHeight")

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createScaledBitmap(bm,newWidth.toInt(), newHeight,false)
    }
}