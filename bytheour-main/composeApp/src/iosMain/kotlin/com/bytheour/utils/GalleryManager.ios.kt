package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@Composable
actual fun rememberGalleryManager(
    allowMultiple: Boolean,
    onResult: (SharedImage?) -> Unit,
    onResultM: (List<SharedImage>) -> Unit
): GalleryManager {
    val imagePicker = UIImagePickerController()

    val galleryDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image = didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage] as? UIImage
                    ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

                image?.let {
                    // If multiple selection was enabled, wrap it into a list
                    if (allowMultiple) {
                        onResultM(listOf(SharedImage(it)))
                    } else {
                        onResult(SharedImage(it))
                    }
                }

                picker.dismissViewControllerAnimated(true, null)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        GalleryManager {
            imagePicker.setSourceType(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            )
            imagePicker.setAllowsEditing(true)

            // iOS UIKit UIImagePickerController does not support true multiple selection
            // If you need real multiple selection, you must use PHPickerViewController (iOS 14+)
            imagePicker.setDelegate(galleryDelegate)

            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                imagePicker, true, null
            )
        }
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GalleryManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}