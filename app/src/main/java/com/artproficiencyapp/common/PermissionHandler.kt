package com.artproficiencyapp.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import com.artproficiencyapp.extension.SD_CARD_PATH
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object PermissionHandler {


    fun checkReadExternalStoragePermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    fun checkCameraPermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    fun checkLocationPermission(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }
    fun saveImage(myBitmap: Bitmap, context: Context): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + SD_CARD_PATH)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

}