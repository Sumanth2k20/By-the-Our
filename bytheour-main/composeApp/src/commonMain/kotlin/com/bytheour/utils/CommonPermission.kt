package com.bytheour.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.camera_ico
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.gallery_ico
import bytheour.composeapp.generated.resources.permission_message
import bytheour.composeapp.generated.resources.permission_required
import bytheour.composeapp.generated.resources.settings
import com.bytheour.presentations.customcomponents.CameraBottomSheetContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertMessageDialog(
    resource: Painter? = null,
    onSettingsClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {

    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(size = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                resource?.let {
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = it,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.permission_required),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(Res.string.permission_message),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp, start = 16.dp)
                ) {

                        Button(
                            modifier = Modifier.weight(1f), onClick = {
                                onCancelClick()
                            },
                            colors = ButtonDefaults.buttonColors(JTColors.enableButton)
                        ) {
                            Text(text = stringResource(Res.string.cancel), textAlign = TextAlign.Center, maxLines = 1)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Button(
                            modifier = Modifier.weight(1f), onClick = {
                                onSettingsClick()
                            },
                            colors = ButtonDefaults.buttonColors(JTColors.enableButton)
                        ) {
                            Text(text = stringResource(Res.string.settings), textAlign = TextAlign.Center, maxLines = 1)
                        }
                }
            }

        }

    }
}

@Composable
fun usePermission(
    permissionType: PermissionType,
    onPermissionGranted: (() -> Unit)? = null
): () -> Unit {
    var launchPermission by remember { mutableStateOf(false) }
    var launchSetting by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            if (permissionType == permissionType) {
                when (status) {
                    PermissionStatus.GRANTED -> onPermissionGranted?.invoke()
                    PermissionStatus.DENIED -> showPermissionRationale = true
                    PermissionStatus.SHOW_RATIONAL -> showPermissionRationale = true
                    PermissionStatus.NOT_DETERMINED -> showPermissionRationale = true
                }
            }
        }
    })

    if (launchPermission) {
        if (permissionsManager.isPermissionGranted(permissionType)) {
            onPermissionGranted?.invoke()
        } else {
            permissionsManager.AskPermission(permissionType)
        }
        launchPermission = false
    }
    if (launchSetting) {
        permissionsManager.LaunchSettings()
        launchSetting = false
    }

    if (showPermissionRationale) {
        ShowPermissionRationaleDialog(
            visible = showPermissionRationale,
            onSettingsClick = {
                showPermissionRationale = false
                launchSetting = true
            },
            onCancelClick = {
                showPermissionRationale = false
            }
        )
    }

    return {
        launchPermission = true
    }
}

@Composable
fun ShowPermissionRationaleDialog(
    visible: Boolean,
    onSettingsClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    if (visible) {
        AlertMessageDialog(
            onSettingsClick = onSettingsClick,
            onCancelClick = onCancelClick
        )
    }
}



@Composable
fun PermissionImagePicker(
    imageBitmap: ImageBitmap? =null,
    onImageSelected: ((ImageBitmap?) -> Unit)? = null,
    onImageSelectedM: (List<ImageBitmap>?) -> Unit = { _ -> },
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    byteArrayImage:((ByteArray?) -> Unit)? = null,
    filePathUri: ((String?) -> Unit)? = null,
    onMultipleImagesSelected: (List<ByteArray?>) -> Unit = { _ -> },
    allowMultipleImage:Boolean = false


) {
    val coroutineScope = rememberCoroutineScope()
    var launchCamera by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(false) }
    var launchSetting by remember { mutableStateOf(false) }
    var permissionRationalDialog by remember { mutableStateOf(false) }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
//                        PermissionType.CONTACTS -> TODO()
                        PermissionType.RECORD_AUDIO -> TODO()
                        PermissionType.NOTIFICATION -> TODO()
                    }
                }
                else -> {
                    permissionRationalDialog = true //shows a dialog asking the user to go to settings.
                }
            }
        }
    })


    val cameraManager = rememberCameraManager { sharedImage ->
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                sharedImage?.toImageBitmap()
            }

            onImageSelectedM(
                bitmap?.let { listOf(it) } ?: emptyList()
            )

            val byteArray = sharedImage?.toByteArray()
            onMultipleImagesSelected(byteArray?.let { listOf(it) } ?: emptyList())


            if (onImageSelected != null) {
                onImageSelected(bitmap)
            }
            val filePath = sharedImage?.getFilePath()
            println("Captured camera  image path:-------------------------- $filePath")

            if (filePathUri != null) {
                filePathUri(filePath)
            }

            val bytearrayImage = sharedImage?.toByteArray()

            if (byteArrayImage != null) {
                byteArrayImage(bytearrayImage)
            }

        }
    }

    val galleryManager = rememberGalleryManager(
        allowMultiple = allowMultipleImage, // or false, depending on the flow
        onResult = { singleImage ->
            coroutineScope.launch {
                val bitmap = withContext(Dispatchers.Default) {
                    singleImage?.toImageBitmap()
                }
                onImageSelected?.invoke(bitmap)

                filePathUri?.invoke(singleImage?.getFilePath())
                byteArrayImage?.invoke(singleImage?.toByteArray())
            }
        },
        onResultM = { imageList ->
            coroutineScope.launch {
                val byteArrays = imageList.map { it.toByteArray() }
                onMultipleImagesSelected(byteArrays)
                val bitmaps = withContext(Dispatchers.Default) {
                    imageList.mapNotNull { it.toImageBitmap() }
                }
                onImageSelectedM(bitmaps)
            }
        }
    )


    /* check permission and launch */
    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.AskPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }
    /* check permission and launch */
    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.AskPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }


    if (launchSetting) {
        permissionsManager.LaunchSettings()
        launchSetting = false
    }
    ShowPermissionRationaleDialog(
        visible = permissionRationalDialog,
        onSettingsClick = {
            permissionRationalDialog = false
            launchSetting = true
        },
        onCancelClick = {
            permissionRationalDialog = false
        }
    )

    if (showBottomSheet) {
        CameraBottomSheetContent(
            onDismiss = onDismissRequest,
            onGalleryClick = {
                launchGallery = true
            },
            onCameraClick = {
              launchCamera = true
            },
            cameraIconPainter = Res.drawable.camera_ico,
            galleryIconPainter = Res.drawable.gallery_ico
        )
    }

//    imageBitmap?.let {
//        Image(
//            bitmap = it,
//            contentDescription = null,
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//                .clickable {},
//            contentScale = ContentScale.Crop
//        )
//    }
}
