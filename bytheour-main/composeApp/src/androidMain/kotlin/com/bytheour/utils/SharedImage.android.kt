package com.bytheour.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SharedImage(
    private val bitmap: Bitmap?,
    private val uri: Uri? = null,
) {

    private val context = AppContext.get()

    actual fun toByteArray(): ByteArray? {//converts the Bitmap into a ByteArray
        return if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()//to hold the image data in memory before converting it to a byte array
            @Suppress("MagicNumber") bitmap.compress(
                android.graphics.Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream
            )
            byteArrayOutputStream.toByteArray()//Convert the stream into a ByteArray
        } else {
            println("toByteArray null")
            null
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
        } else {
            println("toImageBitmap null")
            null
        }
    }

    actual fun getFilePath(): String? {
        if (uri != null) {
            return getRealPathFromUri(context, uri)
        }
        return null
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
        // For Android Q+ (API 29+), the _data column is deprecated and often unavailable
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Instead of trying to get a file path, copy the URI content to a temp file and return its path
            return copyUriToTempFile(context, uri)?.absolutePath
        } else {
            // For older devices, fallback to querying the _data column
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    cursor.moveToFirst()
                    return cursor.getString(columnIndex)
                }
            }
            return null
        }
    }

    private fun copyUriToTempFile(context: Context, uri: Uri): java.io.File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = java.io.File.createTempFile("temp_image", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}