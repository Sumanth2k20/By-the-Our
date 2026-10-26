package com.bytheour

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects

//Android requires content URIs (content://) instead of file URIs (file://).
class ComposeFileProvider : FileProvider( //It allows secure file sharing between apps.
    R.xml.path_provider
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            // 1
            val tempFile = File.createTempFile(
                "picture_${System.currentTimeMillis()}", ".png", context.cacheDir
            ).apply {
                createNewFile()
            }
            // 2
            val authority = context.applicationContext.packageName + ".provider"
            // 3
            return getUriForFile(
                Objects.requireNonNull(context),
                authority,
                tempFile,
            )
        }
    }
}