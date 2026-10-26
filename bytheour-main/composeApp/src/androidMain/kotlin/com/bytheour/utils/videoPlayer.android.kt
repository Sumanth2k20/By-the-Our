package com.bytheour.utils

import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.bytheour.R

@Composable
actual fun JTVideoPlayer(modifier: Modifier) {
    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/${R.raw.splashlogo}"

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            VideoView(context).apply {
                setVideoURI(videoUri.toUri())
                val mediaController = MediaController(context)
                mediaController.setAnchorView(this)
                setOnPreparedListener { mp ->
                    mp.isLooping = false
                    start()
                }
            }
        }
    )
}
