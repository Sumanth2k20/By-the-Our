package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.for_all_services
import bytheour.composeapp.generated.resources.image_close_ico
import bytheour.composeapp.generated.resources.photos
import bytheour.composeapp.generated.resources.save
import bytheour.composeapp.generated.resources.select_Plan
import bytheour.composeapp.generated.resources.upload_Photos
import bytheour.composeapp.generated.resources.upload_img_ico
import bytheour.composeapp.generated.resources.your_monthly_subscription
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.core.JTURLS.BASE_IMAGE_URL
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.showToast
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadScreen(
    viewModel: ImageUploadViewModel = getKoinViewModel(),
    onBackClick: () -> Unit,
    serviceId:String,
    value:String,
    navigateSelectPlanScreen: () -> Unit
) {
    val uiStates by viewModel.uiStates.collectAsState()

    val previews by GlobalImageStore.imagePreviews.collectAsState()
    val pending by GlobalImageStore.pendingUploads.collectAsState()

    val onSaveClick:() -> Unit = {
        if (value == "tab"){
            viewModel.saveAllImages(
                serviceId = serviceId,
                onSuccess = {onBackClick()}
            )
        }else{
            onBackClick()
        }

    }

    val selectPlan:()->Unit = {
        navigateSelectPlanScreen()
    }

    // 🔹 Observe message changes and show toast when message is not null or blank
    LaunchedEffect(uiStates.message) {
        uiStates.message?.takeIf { it.isNotBlank() }?.let { message ->
            showToast(message)
            // Optional: clear the message from state after showing
            viewModel.clearMessage()
        }
    }


    LaunchedEffect(Unit){
        viewModel.loadServices(serviceId)
        viewModel.fetchGalleryBookings()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.upload_Photos),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold(),

                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JTColors.enableButton
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val maxPhotos = uiStates.maxPhotos
                val usedPhotos = uiStates.usedPhotos ?: 0
                val isUnlimited = uiStates.isUnlimited == true

                // 🔹 CONDITION 1: When to show "Upload Photos" or "Select Plan"
                if (isUnlimited || (maxPhotos != null && usedPhotos < maxPhotos)) {
                    // 🟢 CASE 1A: Unlimited plan
                    //     → Always show the "Upload Photos" button (no limits)
                    //
                    // 🟢 CASE 1B: Limited plan but not full
                    //     → maxPhotos is not null AND usedPhotos < maxPhotos
                    //     → User still has room to upload more photos
                    //
                    // ✅ Show "Upload Photos" button
                    OutlinedButton(
                        onClick = { viewModel.onUploadClick() },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFF3E2473)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF3E2473)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.upload_img_ico),
                            contentDescription = null,
                            tint = Color(0xFF3E2473)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(Res.string.upload_Photos),
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 12.sp,
                            color = Color(0xFF3E2473)
                        )
                    }
                } else {
                    // 🔴 CASE 1C: Limited plan AND upload limit reached
                    //     → maxPhotos != null AND usedPhotos == maxPhotos
                    //     → User cannot upload more photos under the current plan
                    //
                    // ✅ Show "Select Plan" button (to upgrade or renew)
                    OutlinedButton(
                        onClick = { selectPlan() },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFF3E2473)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF3E2473)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            stringResource(Res.string.select_Plan),
                            fontSize = 14.sp,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            color = Color(0xFF3E2473)
                        )
                    }
                }

                // 🔹 CONDITION 2: When to show the "Save" button
                if (isUnlimited || (maxPhotos != null && usedPhotos < maxPhotos)) {
                    // 🟢 CASE 2A: User can still upload (unlimited or within limit)
                    //     → Show "Save" button to commit pending uploads
                    //
                    // 🟣 Behavior:
                    //     - If pending list is not empty → perform save action
                    //     - If no pending uploads → navigate back
                    Button(
                        onClick = {
                            if (pending.isNotEmpty()) {
                                onSaveClick()
                            } else {
                                onBackClick()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2473)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            stringResource(Res.string.save),
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        )
                    }
                }

            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Subscription Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(Res.string.your_monthly_subscription),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color.Gray
                    )
                    Text(
                        stringResource(Res.string.for_all_services),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(50))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (uiStates.isUnlimited) {
                            "${uiStates.usedPhotos ?: 0} / ∞"+ stringResource(Res.string.photos)
                        } else {
                            "${uiStates.usedPhotos ?: 0} / ${uiStates.maxPhotos ?: 0}"+ stringResource(Res.string.photos)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF3E2473)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Image Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Server images
                items(uiStates.uploadedImages) { imageUrl ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(BASE_IMAGE_URL + imageUrl.path),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(Res.drawable.image_close_ico),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 12.dp, end = 12.dp)
                                .clickable { viewModel.deleteGalleryImage(imageUrl.customId ?: "")  }
                        )

                    }
                }

                // Local previews
                itemsIndexed(previews) { index, bitmap ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.removeImage(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color.White, shape = CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "", tint = Color.Black)
                        }
                    }
                }
            }
        }

    }
    val imageBitmapState by remember { mutableStateOf<ImageBitmap?>(null) }
    if (uiStates.showImagePicker) {
        PermissionImagePicker(
            imageBitmap = imageBitmapState,
            onImageSelectedM = {viewModel.onImageSelected(it)},
            showBottomSheet = uiStates.showImagePicker,
            onDismissRequest = { viewModel.onDismissImagePicker() },
            filePathUri = {  },
            allowMultipleImage = true,
            onMultipleImagesSelected = {
                viewModel.addPendingImages(it)
            }
        )

    }
}
