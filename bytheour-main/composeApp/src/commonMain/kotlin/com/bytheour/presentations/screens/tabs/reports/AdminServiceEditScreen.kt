package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.approved
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.description_Star
import bytheour.composeapp.generated.resources.edit_Service_Request
import bytheour.composeapp.generated.resources.enter_Description
import bytheour.composeapp.generated.resources.enter_Sequence
import bytheour.composeapp.generated.resources.enter_Title
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.rejected
import bytheour.composeapp.generated.resources.save
import bytheour.composeapp.generated.resources.select_A_Category
import bytheour.composeapp.generated.resources.select_Service_Status
import bytheour.composeapp.generated.resources.sequence_Star
import bytheour.composeapp.generated.resources.serviceStatus
import bytheour.composeapp.generated.resources.title_Star
import bytheour.composeapp.generated.resources.upload_img_ico
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue.CustomDropdowns
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditServiceRequestScreen(
    navigateBack:()->Unit,
    viewModel: AdminServiceEditedScreenViewmodel = getKoinViewModel(),
    id:Int,
    title:String,
    description:String,
    category: String,
    avatar:String,
    sequence:Int,
    serviceStatus:String
) {

    val uiState  by viewModel.uiState.collectAsState()

    var showImagePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCategoriesList(
            limit = PAGINATION_LIMIT.toString(),
        )
    }
    Column (Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.edit_Service_Request),
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navigateBack()}
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2D1A6E))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)) // round corners
                    .clickable { showImagePicker = true }
            ) {
                when {
                    uiState.imageBitmap != null -> {
                        Image(
                            bitmap = uiState.imageBitmap!!,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                    avatar.isBlank() -> {
                        Image(
                            painter = painterResource(Res.drawable.image_1),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                    else -> {
                        AsyncImage(
                            model = "${JTURLS.BASE_IMAGE_URL}$avatar",
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                Image(
                    painter = painterResource(Res.drawable.upload_img_ico),
                    contentDescription = "Upload Icon",
                    modifier = Modifier.align(Alignment.Center)
                )
            }



            Spacer(modifier = Modifier.height(24.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.title_Star),
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 12.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            JTBtoCustomTextField(
                name = "",
                value = uiState.title?:title,
                onValueChange = {  viewModel.onTitleChange(it)},
                modifier = Modifier,
                placeholder = stringResource(Res.string.enter_Title),
                errorMessage = uiState.titleError,
                isShowBorderError = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.description_Star),
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 12.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            JTBtoCustomTextField(
                name = "",
                value = uiState.description?:description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                textFieldModifier = Modifier.height(120.dp),
                placeholder = stringResource(Res.string.enter_Description),
                errorMessage = uiState.descriptionError,
                maxLines = 7,
                isShowBorderError = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.select_A_Category),
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 12.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            CustomDropdowns(
                placeHolder = {
                    JtBtoCommonText(
                        stringResource(Res.string.select_A_Category),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W400,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                },
                options = uiState.categoryNames,
                selectedOption = uiState.category?:category,
                onOptionSelected = {viewModel.onCategoryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                errorMessage = uiState.categoryError
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sequence Field
            JtBtoCommonText(
                text = stringResource(Res.string.sequence_Star),
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 12.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            JTBtoCustomTextField(
                name = "",
                value = uiState.sequence?:sequence.toString(),
                onValueChange = {  viewModel.onSequenceChange(it)},
                placeholder = stringResource(Res.string.enter_Sequence),
                errorMessage = uiState.sequenceError,
                isShowBorderError = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Service Status Dropdown
            JtBtoCommonText(
                text = stringResource(Res.string.serviceStatus),
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 12.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            CustomDropdowns(
                placeHolder = {
                    JtBtoCommonText(
                        stringResource(Res.string.select_Service_Status),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W400,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                },
                options = listOf(
                    stringResource(Res.string.approved),
                    stringResource(Res.string.rejected)
                ),
                selectedOption = uiState.serviceStatus?:serviceStatus,
                onOptionSelected = { viewModel.onServiceStatusChange(it) },
                modifier = Modifier.fillMaxWidth(),
                errorMessage = ""
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navigateBack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF0EFEAFF),
                        contentColor = Color(0xFF2D1A6E)
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    border = BorderStroke(1.dp, Color(0xFF2D1A6E))
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.cancel),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 14.sp,
                        color = Color(0xFF2D1A6E)
                    )
                }

                Button(
                    onClick = {
                        val correctedUiStatus = fixServiceStatus(uiState.serviceStatus)
                        val correctedFallbackStatus = fixServiceStatus(serviceStatus)
                        val finalStatus = correctedUiStatus ?: correctedFallbackStatus
                        if (viewModel.validate()){
                            val selectedCategoryName = uiState.category ?: category
                            val selectedCategoryId = uiState.categories.find {
                                it.title == selectedCategoryName
                            }?.id.toString()

                            viewModel.submitEditRequest(
                                serviceId = id,
                                title = uiState.title?:title,
                                description = uiState.description?:description,
                                categoryId = selectedCategoryId,
                                picture = uiState.avatarFileName?:avatar,
                                sequence = uiState.sequence?:sequence.toString(),
                                serviceStatus = finalStatus,

                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D1A6E),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.save),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
    if (showImagePicker) {
        PermissionImagePicker(
            imageBitmap = uiState.imageBitmap,
            onImageSelected = {
                viewModel.updateImageBitmap(it)
                showImagePicker = false
            },
            showBottomSheet = showImagePicker,
            onDismissRequest = {
                showImagePicker = false
            },
            filePathUri = {
                showImagePicker = false
                viewModel.onAvatarFileNameChange(it?:"")
            },
            byteArrayImage = {
                viewModel.imageByteArrayChange(it?:ByteArray(0))
                viewModel.uploadUserImage(it?:ByteArray(0))
            }

        )
    }
    if (uiState.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiState.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 1500,
            onDismiss = {
                if (uiState.message?.contains("successfully", ignoreCase = true) == true){
                    navigateBack()
                }

            }
        )
    }
}

fun fixServiceStatus(status: String?): String? {
    return when  {
        status.equals("Approved", ignoreCase = true) -> "approved"
        status.equals("Rejected", ignoreCase = true) -> "rejected"
        else -> status
    }
}
