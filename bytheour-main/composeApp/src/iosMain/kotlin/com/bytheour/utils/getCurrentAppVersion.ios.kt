package com.bytheour.utils


import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSBundle
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationOptions

@Composable
actual fun getAppVersion(): String {
    val build = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
    return build.toString()
}

actual val platform: String = "iOS"


@OptIn(ExperimentalForeignApi::class)
actual fun showToast(message: String) {
    val window = UIApplication.sharedApplication.keyWindow ?: return


    val windowWidth = window.frame.useContents { size.width }
    val windowHeight = window.frame.useContents { size.height }

    // Width and height of toast
    val labelWidth = windowWidth - 40.0
    val labelHeight = 35.0

    // Calculate position (bottom center)
    val xPos = (windowWidth - labelWidth) / 2
    val yPos = windowHeight - labelHeight - 80.0 // 80px above bottom edge

    val toastLabel = UILabel(CGRectMake(xPos, yPos, labelWidth, labelHeight)).apply {
        backgroundColor = UIColor.whiteColor.colorWithAlphaComponent(0.6)
        textColor = UIColor.blackColor
        textAlignment = NSTextAlignmentCenter
        font = UIFont.systemFontOfSize(14.0)
        text = message
        numberOfLines = 0
        layer.cornerRadius = 10.0
        layer.masksToBounds = true
    }

    window.addSubview(toastLabel)

    // Fade in, wait, fade out
    toastLabel.alpha = 0.0
    UIView.animateWithDuration(
        duration = 0.5,
        animations = { toastLabel.alpha = 1.0 },
        completion = {
            UIView.animateWithDuration(
                duration = 0.5,
                delay = 2.0,
                options = UIViewAnimationOptions.MIN_VALUE,
                animations = { toastLabel.alpha = 0.0 },
                completion = { toastLabel.removeFromSuperview() }
            )
        }
    )
}