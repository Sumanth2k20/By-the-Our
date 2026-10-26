package com.bytheour.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberGalleryManager(
    allowMultiple: Boolean,
    onResult: (SharedImage?) -> Unit,
    onResultM: (List<SharedImage>) -> Unit
): GalleryManager {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    // Single image picker
    val singlePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onResult(
                SharedImage(
                        BitmapUtils.getBitmapFromUri(uri, contentResolver),
                        uri = uri
                )
            )
        }
    }

    // Multiple image picker
    val multiPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        onResultM(
            uris.map { uri ->
                SharedImage(
                    BitmapUtils.getBitmapFromUri(uri, contentResolver),
                    uri = uri
                )
            }
        )
    }

    return remember {
        GalleryManager(onLaunch = {
            if (allowMultiple) {
                multiPicker.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            } else {
                singlePicker.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        })
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GalleryManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}

