package com.bytheour.presentations.screens.tabs.reports.workers

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.account_Type
import bytheour.composeapp.generated.resources.address
import bytheour.composeapp.generated.resources.average_reviews
import bytheour.composeapp.generated.resources.camera_ico1
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.category
import bytheour.composeapp.generated.resources.circleminus
import bytheour.composeapp.generated.resources.circleplus
import bytheour.composeapp.generated.resources.collapse
import bytheour.composeapp.generated.resources.contactNumber
import bytheour.composeapp.generated.resources.edit_worker_details
import bytheour.composeapp.generated.resources.email_id
import bytheour.composeapp.generated.resources.enter_Title
import bytheour.composeapp.generated.resources.enter_address
import bytheour.composeapp.generated.resources.enter_contact_number
import bytheour.composeapp.generated.resources.enter_email_id
import bytheour.composeapp.generated.resources.enter_last_name
import bytheour.composeapp.generated.resources.expand
import bytheour.composeapp.generated.resources.firstName
import bytheour.composeapp.generated.resources.incorrect_details
import bytheour.composeapp.generated.resources.invalid_documents
import bytheour.composeapp.generated.resources.lastName
import bytheour.composeapp.generated.resources.minimum_hours
import bytheour.composeapp.generated.resources.na
import bytheour.composeapp.generated.resources.no_of_services
import bytheour.composeapp.generated.resources.no_services_available
import bytheour.composeapp.generated.resources.numberofservices_ico
import bytheour.composeapp.generated.resources.others
import bytheour.composeapp.generated.resources.previous_comment
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.rate
import bytheour.composeapp.generated.resources.review_details
import bytheour.composeapp.generated.resources.save
import bytheour.composeapp.generated.resources.select_rejection_status
import bytheour.composeapp.generated.resources.select_status
import bytheour.composeapp.generated.resources.service_title
import bytheour.composeapp.generated.resources.total_reviews
import bytheour.composeapp.generated.resources.update_status
import bytheour.composeapp.generated.resources.verification_Status
import bytheour.composeapp.generated.resources.verified
import bytheour.composeapp.generated.resources.wallet_amount
import bytheour.composeapp.generated.resources.wallet_ico
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue.CustomDropdowns
import com.bytheour.presentations.screens.tabs.reports.BookingInfoCard
import com.bytheour.presentations.screens.tabs.reports.BookingStatItem
import com.bytheour.presentations.screens.tabs.reports.VerticalDivider
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkerDetailsScreen(
    userId: String,
    firstname:String,
    lastname:String,
    email:String,
    phoneNumber:String,
    profileImage:String,
    accountType:String,
    documentStatus: String,
    navigateBack:()-> Unit,
    viewModel: EditWorkerDetailsScreenViewModel = getKoinViewModel(),
    workerScreenViewModel: WorkersScreenViewModel = getKoinViewModel(),
) {
    val uiState by viewModel.uiStates.collectAsState()

    var showImagePicker by remember { mutableStateOf(false) }

    LaunchedEffect (Unit){
        viewModel.fetchAdminWorkerData(userId)
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor), // Purple background
                title = {
                    JtBtoCommonText(
                        stringResource(Res.string.edit_worker_details),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
            )

            Column(
                modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(JTColors.BackGroundBlueColor)
                        .padding(vertical = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { showImagePicker = true }
                            .background(Color.Gray),
                        contentAlignment = Alignment.BottomCenter
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
                            profileImage.isBlank() -> {
                                Image(
                                    painter = painterResource(Res.drawable.provider_logo),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                            else -> {
                                AsyncImage(
                                    model = "${JTURLS.BASE_IMAGE_URL}$profileImage",
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(27.dp)
                                .background(Color.Black.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { showImagePicker = true }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.camera_ico1),
                                    contentDescription = "Camera Icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(bottom = 10.dp)
                                        .clickable { showImagePicker = true }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    JtBtoCommonText(
                        ("$firstname $lastname"),
                        fontSize = 18.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        color = Color.White
                    )

                    Spacer(Modifier.height(4.dp))

                    JtBtoCommonText(
                        stringResource(Res.string.account_Type) + "${uiState.accountType}",
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF5A4FCF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.wallet_ico),
                                        contentDescription = ""
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.wallet_amount),
                                        fontSize = 12.sp,
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        color = Color.Gray
                                    )
                                    JtBtoCommonText(
                                        text = uiState.walletAmount.toString(),
                                        fontSize = 16.sp,
                                        fontFamily = JtBtoFonts.getOpenSansBold(),
                                        color = Color.Black
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp).padding(horizontal = 10.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF5A4FCF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.numberofservices_ico),
                                        contentDescription = ""
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    JtBtoCommonText(
                                        stringResource(Res.string.no_of_services),
                                        fontSize = 12.sp,
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        color = Color.Gray
                                    )
                                    JtBtoCommonText(
                                        uiState.noOfServices ?: "100",
                                        fontSize = 16.sp,
                                        fontFamily = JtBtoFonts.getOpenSansBold(),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp)
                        .padding(horizontal = 16.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(25.dp),
                            clip = false
                        )
                        .background(
                            color = JTColors.WhiteColor,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.tabs.forEachIndexed { index, title ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (uiState.selectedTab == index) JTColors.BackGroundBlueColor
                                    else JTColors.WhiteColor
                                )
                                .clickable {
                                    viewModel.onTabSelected(index)
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            JtBtoCommonText(
                                title,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                                color = if (uiState.selectedTab == index) JTColors.WhiteColor
                                else JTColors.BackGroundBlueColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                when (uiState.selectedTab) {
                    0 -> PersonalInfoSection(
                        uiState = uiState,
                        viewModel = viewModel,
                        id = userId,
                        firstName = firstname,
                        lastName = lastname,
                        email = email,
                        documentStatus = documentStatus,
                        contactNumber = phoneNumber,
                        navigateBack = navigateBack,
                        imagePicker = {
                            showImagePicker = true
                        },
                        profileImage = profileImage
                    )

                    1 -> ServiceDetailsSection(
                        viewModel = viewModel,
                        uiState = uiState,
                        documentStatus = documentStatus,
                        userId = userId
                    )

                    2 -> KYCDetailsSection(
                        viewModel = viewModel,
                        uiState = uiState,
                        documentStatus = documentStatus,
                        userId = userId
                    )
                }
            }
        }
    }
    if (uiState.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiState.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 2000,
            onDismiss = {}
        )
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
}

@Composable
fun ServiceDetailsSection(
    uiState: EditWorkerDetailsScreenViewModel.AdminWorkerUiState,
    viewModel: EditWorkerDetailsScreenViewModel = getKoinViewModel(),
    documentStatus: String,
    userId: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
//            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(10.dp))

        if (uiState.services.isNotEmpty()) {
            uiState.services.forEachIndexed { index, service ->
                ContactCardWorkersEdit(
                    id = service.id ?: "",
                    service = service.title,
                    category = service.category,
                    rate = service.rate?.toString(),
                    minHour = service.minHour?.toString()
                )
                if (index < uiState.services.size - 1) {
                    Spacer(Modifier.height(12.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_services_available),
                        fontSize = 16.sp,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = JTColors.grayColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Text(
            text = stringResource(Res.string.verification_Status),
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansBold(),
            color = JTColors.blackColor,
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.Start)
        )

        VerificationCard(
            documentStatus = documentStatus,
            verificationStatus = uiState.serviceVerificationStatus ?: "",
            onVerificationStatusChange = { viewModel.onServiceVerificationStatusChange(it) },
            comment = uiState.serviceComment ?: "",
            onCommentChange = { viewModel.onServiceCommentChange(it) },
            rejectionStatus = uiState.serviceRejectionStatus ?: "",
            onRejectionStatusChange = { viewModel.onServiceRejectionStatusChange(it) },
            onUpdateClick = {
                viewModel.updateVerificationStatus(
                    id = userId,
                    onSuccess = {},
                    onFailure = {}
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun KYCDetailsSection(
    uiState: EditWorkerDetailsScreenViewModel.AdminWorkerUiState,
    viewModel: EditWorkerDetailsScreenViewModel = getKoinViewModel(),
    documentStatus:String,
    userId:String
){
    Column(
        modifier = Modifier.fillMaxWidth().imePadding(),
//            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(Res.string.verification_Status),
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansBold(),
            color = JTColors.blackColor,
            modifier = Modifier.padding(start = 15.dp)
                .align(Alignment.Start)
        )

        VerificationCard(
            documentStatus = documentStatus,
            verificationStatus = uiState.kycVerificationStatus ?: "",
            onVerificationStatusChange = { viewModel.onKycServiceVerificationStatusChange(it) },
            comment = uiState.kycComment ?: "",
            onCommentChange = { viewModel.onKycServiceCommentChange(it) },
            rejectionStatus = uiState.kycRejectionStatus ?: "",
            onRejectionStatusChange = { viewModel.onKycServiceRejectionStatusChange(it) },
            onUpdateClick = { viewModel.updateVerificationStatus (id = userId, onSuccess = {}, onFailure = {})}
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

}


@Suppress("UNUSED_PARAMETER")
@Composable
fun PersonalInfoSection(
    uiState: EditWorkerDetailsScreenViewModel.AdminWorkerUiState,
    viewModel: EditWorkerDetailsScreenViewModel = getKoinViewModel(),
    workerScreenViewModel: WorkersScreenViewModel = getKoinViewModel(),
    id: String,
    firstName:String,
    lastName:String,
    email:String,
    documentStatus:String,
    contactNumber:String,
    navigateBack:()-> Unit,
    imagePicker:()->Unit,
    profileImage: String
) {
    Column(modifier = Modifier.padding(10.dp).imePadding()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                JTBtoCustomTextField(
                    name = stringResource(Res.string.firstName),
                    value = uiState.firstName?:firstName,
                    onValueChange = {  viewModel.onFirstNameChange(it)},
                    modifier = Modifier.fillMaxWidth(0.9f),
                    placeholder = stringResource(Res.string.enter_Title),
                    errorMessage = uiState.firstNameError,
                    isShowBorderError = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                JTBtoCustomTextField(
                    name = stringResource(Res.string.lastName),
                    value = uiState.lastName?:lastName,
                    onValueChange = { viewModel.onLastNameChange(it) },
                    placeholder = stringResource(Res.string.enter_last_name),
                    errorMessage = uiState.lastNameError,
                    isShowBorderError = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )

                Spacer(modifier = Modifier.height(12.dp))

                JTBtoCustomTextField(
                    name = stringResource(Res.string.email_id),
                    value = uiState.email?:contactNumber,
                    onValueChange = {  viewModel.onEmailChange(it)},
                    placeholder = stringResource(Res.string.enter_email_id),
                    errorMessage = uiState.emailError,
                    isShowBorderError = true,
                    enable = false,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )

                Spacer(modifier = Modifier.height(12.dp))

                JTBtoCustomTextField(
                    name =stringResource(Res.string.contactNumber),
                    value = uiState.contactNumber?:email,
                    onValueChange = {  viewModel.onContactNumberChange(it)},
                    placeholder = stringResource(Res.string.enter_contact_number),
                    errorMessage = uiState.contactNumberError,
                    isShowBorderError = true,
                    enable = false,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )

                Spacer(modifier = Modifier.height(12.dp))

                JTBtoCustomTextField(
                    name = stringResource(Res.string.address),
                    value = uiState.address?:"",
                    onValueChange = {  viewModel.onAddressChange(it)},
                    placeholder = stringResource(Res.string.enter_address),
                    textFieldModifier = Modifier.height(100.dp),
                    errorMessage = uiState.addressError,
                    isShowBorderError = true,
                    enable = false,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
        Spacer(modifier = Modifier.height(18.dp))



        JtBtoCommonText(
            text = stringResource(Res.string.verification_Status),
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        VerificationCard(
            documentStatus = documentStatus,
            verificationStatus = uiState.verificationStatus ?: "",
            onVerificationStatusChange = { viewModel.onVerificationStatusChange(it) },
            comment = uiState.comment ?: "",
            onCommentChange = { viewModel.onCommentChange(it) },
            rejectionStatus = uiState.rejectionStatus ?: "",
            onRejectionStatusChange = { viewModel.onRejectionStatusChange(it) },
            onUpdateClick = { viewModel.updateVerificationStatus (id = id, onSuccess = {}, onFailure = {})}

        )


        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier) {
            BookingInfoCard(
                completed = uiState.completed.toString(),
                ongoing = uiState.onGoing.toString(),
                upcoming = uiState.upComing.toString()
            )
        }

        ReviewDetailsCard(
            review = uiState.totalReviews.toString(),
            averageReviews = uiState.avgReviews?.let {
                it.takeWhile { ch -> ch != '.' } +
                        (it.substringAfter('.', "").take(2).let { d -> if (d.isNotEmpty()) ".$d" else "" })
            } ?: stringResource(Res.string.na)
        )


        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth().background(color = JTColors.WhiteColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xF0EFEAFF),
                        contentColor = Color(0xFF2D1A6E)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.cancel),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W700,
                        fontSize = 12.sp,
                        lineHeight = 14.sp

                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (viewModel.validate()) {
                            viewModel.adminEditWorker(
                                id = id,
                                firstname = uiState.firstName?:firstName,
                                lastName = uiState.lastName?:lastName,
                                profile = uiState.avatarFileName?:"",
                                onSuccess = {
                                    navigateBack()
                                    workerScreenViewModel.loadMoreWorkers()
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D1A6E),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.save),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W700,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun VerificationCard(
    documentStatus: String,
    verificationStatus: String,
    onVerificationStatusChange: (String) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    rejectionStatus: String,
    onRejectionStatusChange: (String) -> Unit,
    onUpdateClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.95f),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (documentStatus.equals("Verified", ignoreCase = true)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9FB), shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                JtBtoCommonText(
                    text = stringResource(Res.string.verified),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontWeight = W700,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9FB), shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                JtBtoCommonText(
                    text =stringResource(Res.string.previous_comment),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontWeight = W700,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                CustomDropdowns(
                    placeHolder = {
                        JtBtoCommonText(
                            stringResource(Res.string.select_status),
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    },
                    options = listOf(
                        "process",
                        "verified",
                        "rejected"
                    ),
                    selectedOption = verificationStatus,
                    onOptionSelected = onVerificationStatusChange,
                    modifier = Modifier.fillMaxWidth()
                )

                JTBtoCustomTextField(
                    name = "",
                    value = comment,
                    onValueChange = onCommentChange,
                    placeholder = "Comment",
                    textFieldModifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    isShowBorderError = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomDropdowns(
                    placeHolder = {
                        JtBtoCommonText(
                            stringResource(Res.string.select_rejection_status),
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    },
                    options = listOf(
                        stringResource(Res.string.invalid_documents),
                        stringResource(Res.string.incorrect_details),
                        stringResource(Res.string.others)
                    ),
                    selectedOption = rejectionStatus,
                    onOptionSelected = onRejectionStatusChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {onUpdateClick()},
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JTColors.enableButton,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.update_status))
                }
            }
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun ContactCardWorkersEdit(
    id:String?,
    service: String?,
    category: String?,
    rate: String?,
    minHour:String?,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = JTColors.WhiteColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.service_title),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = service ?: "",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Image(
                    painter = if (expanded) painterResource(Res.drawable.circleminus) else painterResource(Res.drawable.circleplus),
                    contentDescription = if (expanded) stringResource(Res.string.collapse) else stringResource(Res.string.expand),
                    modifier = Modifier.size(14.dp) .clickable { expanded = !expanded },
                )

            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.category),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = category ?: "",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.rate),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = rate ?: "",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.minimum_hours),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = minHour ?: "",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun ReviewDetailsCard(
    review: String,
    averageReviews: String,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE4E2EE))
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.review_details),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth().background(color = JTColors.WhiteColor)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BookingStatItem(label = stringResource(Res.string.total_reviews), count = review)
                VerticalDivider()
                BookingStatItem(label = stringResource(Res.string.average_reviews), count = averageReviews)
            }
        }
    }
}