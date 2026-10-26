package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSBundle
import platform.UIKit.UIColor
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun JTVideoPlayer(modifier: Modifier) {
    UIKitView(
        modifier = modifier,
        factory = {
            // Get URL for the bundled splash_logo.mp4
            val url = NSBundle.mainBundle.URLForResource("splashlogo", "mp4")
            val player = url?.let { AVPlayer(it) }

            // Create the AVPlayerViewController
            val controller = AVPlayerViewController().apply {
                this.player = player
                showsPlaybackControls = false
//                view.frame = UIScreen.mainScreen.bounds // Set proper size

                // Set background color of the main view
                view.backgroundColor = UIColor(
                    red = 0x30 / 255.0,
                    green = 0x25 / 255.0,
                    blue = 0x6C / 255.0,
                    alpha = 1.0
                )

            }

            // Start playback on the main thread
            dispatch_async(dispatch_get_main_queue()) {
                player?.play()
            }

            controller.view
        }
    )
}