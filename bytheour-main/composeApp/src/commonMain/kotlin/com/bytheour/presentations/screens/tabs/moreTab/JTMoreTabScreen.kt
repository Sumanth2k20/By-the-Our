package com.bytheour.presentations.screens.tabs.moreTab

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.About_Bto
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.aboutbto_ico
import bytheour.composeapp.generated.resources.account
import bytheour.composeapp.generated.resources.address
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.assistance
import bytheour.composeapp.generated.resources.availability
import bytheour.composeapp.generated.resources.bank_Details
import bytheour.composeapp.generated.resources.become_a_service_provider
import bytheour.composeapp.generated.resources.become_a_service_user
import bytheour.composeapp.generated.resources.booking_alerts_Offers_Discounts
import bytheour.composeapp.generated.resources.chat_Support
import bytheour.composeapp.generated.resources.check_Your_Bank_details
import bytheour.composeapp.generated.resources.delete_Account
import bytheour.composeapp.generated.resources.documents
import bytheour.composeapp.generated.resources.documents_Check
import bytheour.composeapp.generated.resources.edit_Profile
import bytheour.composeapp.generated.resources.edit_Share
import bytheour.composeapp.generated.resources.editprofile_ico
import bytheour.composeapp.generated.resources.faq
import bytheour.composeapp.generated.resources.faq_ico
import bytheour.composeapp.generated.resources.frame_1321315902
import bytheour.composeapp.generated.resources.frame_1321315903
import bytheour.composeapp.generated.resources.frame_1321315904
import bytheour.composeapp.generated.resources.frame_1321315906
import bytheour.composeapp.generated.resources.frame_1321315907
import bytheour.composeapp.generated.resources.frame_427319932
import bytheour.composeapp.generated.resources.frame_427319937
import bytheour.composeapp.generated.resources.invite_Friends
import bytheour.composeapp.generated.resources.invitefriends_ico
import bytheour.composeapp.generated.resources.logOut_2
import bytheour.composeapp.generated.resources.mediation_Guidelines
import bytheour.composeapp.generated.resources.mediationguide_ico
import bytheour.composeapp.generated.resources.more
import bytheour.composeapp.generated.resources.no_details_available
import bytheour.composeapp.generated.resources.notifications
import bytheour.composeapp.generated.resources.others
import bytheour.composeapp.generated.resources.payments_Refunds_Payment
import bytheour.composeapp.generated.resources.privacy_Policy
import bytheour.composeapp.generated.resources.privacy_ico
import bytheour.composeapp.generated.resources.profile
import bytheour.composeapp.generated.resources.rate_This_App
import bytheour.composeapp.generated.resources.ratethis_ico
import bytheour.composeapp.generated.resources.report_Issue
import bytheour.composeapp.generated.resources.reportissue_ico
import bytheour.composeapp.generated.resources.review_Ratings
import bytheour.composeapp.generated.resources.reviews_and_Ratings
import bytheour.composeapp.generated.resources.set_your_availability
import bytheour.composeapp.generated.resources.switch_acc_message
import bytheour.composeapp.generated.resources.switch_to_Customer
import bytheour.composeapp.generated.resources.switch_to_Provider
import bytheour.composeapp.generated.resources.tc_ico
import bytheour.composeapp.generated.resources.terms_and_conditions
import bytheour.composeapp.generated.resources.transaction_History
import bytheour.composeapp.generated.resources.view_Edit_profile_details
import bytheour.composeapp.generated.resources.wallet_His
import bytheour.composeapp.generated.resources.wallet_mor
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS.ROLETYPE
import com.bytheour.device
import com.bytheour.getDeviceToken
import com.bytheour.osName
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.adress.LatLngObject
import com.bytheour.presentations.screens.tabs.reports.CommonDialogBox
import com.bytheour.sdkVersionName
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.openPlayStore
import com.bytheour.utils.platform
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTMoreTabScreen(
    viewModel: JTMoreTabScreenViewModel = getKoinViewModel(),
    navigateToProfileEditScreen:() -> Unit,
    navigateToMediationGuidelines:()-> Unit,
    navigateToPrivacyAndPolicyScreen:() -> Unit,
    navigateAddress:(Boolean) ->Unit,
    navigateToTermsAndConditionsScreen:() -> Unit,
    navigateToAboutBtoScreen:()-> Unit,
    navigateToReportAnIssueScreen:() -> Unit,
    navigateToFaqScreen:() -> Unit,
    navigateToInviteFriendsScreen:() -> Unit,
    navigateToUserTypeScreen:()->Unit,
    navigateToDeleteScreen:()->Unit,
    navigateToWalletScreen:()->Unit,
    navigateToTransactionHistoryScreen:()->Unit,
    navigateToReviewAndRatingsScreen:()->Unit,
    navigateToChatSupportScreen:()->Unit,
    navigateToMainScreen:()-> Unit,
    navigateToDocumentsScreen:()->Unit,
    navigateToBankDetailsScreen:()->Unit,
    navigateToSignUpScreen:(String,String)->Unit,
    navigateToProfileEditScreenFromDialog:(String) -> Unit,
){
    val uiState by viewModel.uiState.collectAsState()
    var navigateLogout by remember { mutableStateOf(false) }
    val usertype = viewModel.userType
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    var showDialog by remember { mutableStateOf(false) }


    val switchClick:() ->Unit = {
        sharedPreferenceUseCase.getUserInfo()?.let {
            println("sharedPreferenceUseCase.getUserInfo() ==========================${sharedPreferenceUseCase.getUserInfo()}")
            if (it.flags.linked){
                viewModel.isSwitchToUserClicked = true
                viewModel.isFromSwitchAccount = true
                viewModel.switchAccount()
            }else{
                if (it.flags.profileIncomplete && !it.flags.linked){
                    viewModel.checkSwitchAccountStatus(
                        navigateToSignUpScreen = {
                            navigateToSignUpScreen(it.contact.number,"more")
                        }
                    )
                }else{
                    viewModel.checkSwitchAccountStatus(
                        navigateToSignUpScreen = {
                            navigateToSignUpScreen(it.contact.number,"more")
                        }
                    )
                }
            }
        }
    }
    val fromDialog by LatLngObject.fromDialog.collectAsState()
    val fromSignUp by LatLngObject.fromSign.collectAsState()

    LaunchedEffect(fromDialog, fromSignUp) {
        launch {
            if (fromDialog.isNotEmpty()) {
                viewModel.switchAccountClick(fromDialog)
                viewModel.getProfile()
                LatLngObject.clearFromDialog()
            }
        }

        launch {
            if (fromSignUp.isNotEmpty()) {
                viewModel.getProfile(onSuccess = {
                    viewModel.switchFromSignUp(fromSignUp)
                })
                LatLngObject.clearFromSign()
            }
        }
    }



    if (uiState.switchAccountClicked.equals("fromDialog", ignoreCase = true) || uiState.switchFromSignUp.equals("more", ignoreCase = true)){
        LaunchedEffect(Unit){
            switchClick()
        }
    }

    LaunchedEffect(uiState.destination) {
        when (uiState.destination) {
            JTMoreTabScreenViewModel.SwitchDestination.MAIN_SCREEN -> {
                navigateToMainScreen()
            }
            else -> {}
        }
    }

    LaunchedEffect(Unit){
        viewModel.getProfile()
    }

    if (uiState.showCreateProfile){
        CommonDialogBox(
            title = "Create A Profile",
            message = "Please create a profile to continue",
            onCancelClick = {
                viewModel.updateShowProfile()
            },
            onContinueClick = {
                navigateToProfileEditScreenFromDialog("fromDialog")
                viewModel.updateShowProfile()
            },
            cancelText = "Cancel",
            deleteText = "Continue"
        )
    }

    LaunchedEffect(uiState.data) {
        val response = sharedPreferenceUseCase.getUserInfo()

        if (response != null) {
            sharedPreferenceUseCase.putUserInfo(response)
            if (viewModel.isFromSwitchAccount) {
                val newUserType = when (sharedPreferenceUseCase.getUser()) {
                    TYPE_CUSTOMER -> TYPE_PROVIDER
                    TYPE_PROVIDER -> TYPE_CUSTOMER
                    else -> TYPE_CUSTOMER
                }
                sharedPreferenceUseCase.putUser(newUserType)
                ROLETYPE = newUserType
            }

            if (viewModel.isSwitchToUserClicked && viewModel.isFromSwitchAccount) {
                getDeviceToken { tokenFromDevice ->
                    viewModel.deviceToken(
                        device = device,
                        token = tokenFromDevice ?: "",
                        type = if (com.bytheour.platform.equals("Android", ignoreCase = true)) {
                            "android"
                        } else {
                            "ios"
                        },
                        os = osName,
                        appVersion = sdkVersionName,
                        version = 1
                    )
                }
            }
        }
    }

    if (showDialog){
        CustomAlertDialog(
            showDialog = showDialog,
            onDismiss = {showDialog = false},
            title =  if (usertype == "user") {
                stringResource(Res.string.switch_to_Provider)
            } else {
                stringResource(Res.string.switch_to_Customer)
            },
            message = stringResource(Res.string.switch_acc_message),
            onCancel = { showDialog = false },
            onConfirm = {
                switchClick()
                showDialog = false
            },
            yes = true,
            no = true
        )
    }

    Box(Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color.White),
                title = {
                    JtBtoCommonText(
                        stringResource(Res.string.more),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 19.sp,
                        lineHeight = 12.sp,
                        fontWeight = W700,

                        )
                },
//                actions = {
//                    Box(
//                        modifier = Modifier
//                            .height(40.dp)
//                            .width(140.dp)
//                            .border(
//                                width = 1.dp,
//                                color = JTColors.border,
//                                shape = RoundedCornerShape(50.dp)
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        JtBtoCommonText(
//                            text = stringResource(Res.string.help_and_support),
//                            color = JTColors.BlackButtonColor,
//                            fontFamily = JtBtoFonts.getInterMediumFont(),
//                            fontSize = 13.sp,
//                            lineHeight = 15.sp
//                        )
//                    }
//                }
            )

            HorizontalDivider(Modifier.fillMaxWidth(), color =JTColors.border)

            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {


                // User Info Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val hasName = !uiState.userName.isNullOrBlank()
                        val hasMail = !uiState.mail.isNullOrBlank()

                        if (!hasName && !hasMail) {
                            JtBtoCommonText(
                                stringResource(Res.string.no_details_available),
                                fontWeight = W700,
                                fontSize = 15.sp,
                                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                lineHeight = 15.sp,
                            )
                        } else {
                            if (hasName) {
                                JtBtoCommonText(
                                    uiState.userName!!,
                                    fontWeight = W700,
                                    fontSize = 15.sp,
                                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                    lineHeight = 15.sp,
                                )
                            }
                            if (hasMail) {
                                Spacer(Modifier.height(3.dp))
                                JtBtoCommonText(
                                    uiState.mail!!,
                                    color = JTColors.mailColor,
                                    fontWeight = W400,
                                    fontSize = 12.sp,
                                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                                    lineHeight = 14.sp,
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))
                        Row(modifier = Modifier.clickable {
                            navigateToProfileEditScreen()
                        }) {
                            JtBtoCommonText(
                                stringResource(Res.string.edit_Profile),
                                color = JTColors.enableButton,
                                fontWeight = W400,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                lineHeight = 14.sp,
                            )
                            Icon(
                                painter = painterResource(Res.drawable.editprofile_ico),
                                contentDescription = "",
                            )
                        }
                    }

                    if (uiState.image.isNullOrEmpty()) {
                        Image(
                            painter = painterResource(Res.drawable.america),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = uiState.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                HorizontalDivider(thickness = 8.dp, color = JTColors.border)

                // Account Section
                SectionTitle(title = stringResource(Res.string.account))

                MoreOption(
                    icon = MoreOptionIcon.VectorIcon(Icons.Default.Person),
                    title = stringResource(Res.string.profile),
                    subtitle = stringResource(Res.string.view_Edit_profile_details),
                    onclick = { navigateToProfileEditScreen() }
                )
                if (sharedPreferenceUseCase.getUser() == TYPE_CUSTOMER) {
                    MoreOption(
                        icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_1321315903)),
                        title = stringResource(Res.string.transaction_History),
                        subtitle = stringResource(Res.string.payments_Refunds_Payment),
                        onclick = { navigateToTransactionHistoryScreen() }
                    )
                } else {
                    MoreOption(
                        icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_1321315903)),
                        title = stringResource(Res.string.wallet_mor),
                        subtitle = stringResource(Res.string.wallet_His),
                        onclick = { navigateToWalletScreen() }
                    )
                }
                if (sharedPreferenceUseCase.getUser() == TYPE_PROVIDER) {
                    AvailabilityToggle(
                        title = stringResource(Res.string.availability),
                        subtitle = stringResource(Res.string.set_your_availability),
                        onClick = { },
                    )
                    MoreOption(
                        icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.tc_ico)),
                        title = stringResource(Res.string.documents),
                        subtitle = stringResource(Res.string.documents_Check),
                        onclick = { navigateToDocumentsScreen() }
                    )
                    MoreOption(
                        icon = MoreOptionIcon.VectorIcon(Icons.Default.Info),
                        title = stringResource(Res.string.bank_Details),
                        subtitle = stringResource(Res.string.check_Your_Bank_details),
                        onclick = { navigateToBankDetailsScreen() }
                    )
                }


                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_427319937)),
                    title = stringResource(Res.string.address),
                    subtitle = stringResource(Res.string.edit_Share),
                    onclick = {
                        navigateAddress(false)
                    }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_1321315902)),
                    title = stringResource(Res.string.chat_Support),
                    subtitle = stringResource(Res.string.assistance),
                    onclick = { navigateToChatSupportScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_1321315904)),
                    title = stringResource(Res.string.review_Ratings),
                    subtitle = stringResource(Res.string.reviews_and_Ratings),
                    onclick = { navigateToReviewAndRatingsScreen() }
                )

                // Notification
                NotificationToggleOption(
                    title = stringResource(Res.string.notifications),
                    subtitle = stringResource(Res.string.booking_alerts_Offers_Discounts),
                    onClick = { },
                )


                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(
                        painterResource(Res.drawable.frame_1321315906)
                    ),
                    title = if (usertype == "user") {
                        stringResource(Res.string.switch_to_Provider)
                    } else {
                        stringResource(Res.string.switch_to_Customer)
                    },
                    subtitle = if (usertype == "user") {
                        stringResource(Res.string.become_a_service_provider)
                    } else {
                        stringResource(Res.string.become_a_service_user)
                    },
                    onclick = { showDialog = true }
                )

                HorizontalDivider(thickness = 8.dp, color = JTColors.border)

                // Others
                SectionTitle(title = stringResource(Res.string.others))
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.invitefriends_ico)),
                    title = stringResource(Res.string.invite_Friends),
                    onclick = {
                        navigateToInviteFriendsScreen()
                    }
                )

                Image(
                    painter = painterResource(Res.drawable.frame_427319932),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        .clickable { navigateToInviteFriendsScreen() }
                )

                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.reportissue_ico)),
                    title = stringResource(Res.string.report_Issue),
                    onclick = { navigateToReportAnIssueScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.aboutbto_ico)),
                    title = stringResource(Res.string.About_Bto),
                    onclick = { navigateToAboutBtoScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.mediationguide_ico)),
                    title = stringResource(Res.string.mediation_Guidelines),
                    onclick = { navigateToMediationGuidelines() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.faq_ico)),
                    title = stringResource(Res.string.faq),
                    onclick = { navigateToFaqScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.tc_ico)),
                    title = stringResource(Res.string.terms_and_conditions),
                    onclick = { navigateToTermsAndConditionsScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.privacy_ico)),
                    title = stringResource(Res.string.privacy_Policy),
                    onclick = { navigateToPrivacyAndPolicyScreen() }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.ratethis_ico)),
                    title = stringResource(Res.string.rate_This_App),
                    onclick = {
                        openPlayStore(
                            appPackageName = if (platform.equals("Android", ignoreCase = true))
                                "https://play.google.com/store/apps/details?id=com.bytheour"
                            else
                                "https://apps.apple.com/in/app/bytheour-service-pros-rental/id6471470381"
                        )
                    }
                )
                MoreOption(
                    icon = MoreOptionIcon.PainterIcon(painterResource(Res.drawable.frame_1321315907)),
                    title = stringResource(Res.string.delete_Account),
                    onclick = {
                        navigateToDeleteScreen()
                    }
                )

                HorizontalDivider(thickness = 8.dp, color = JTColors.border)

                Spacer(Modifier.height(18.dp))
                // Log Out Button
                Button(
                    onClick = {
                        viewModel
                            .logOut(
                                navigateToUserTypeScreen = {
                                    navigateLogout = true
                                }
                            )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JTColors.logoutButtonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .padding(start = 28.dp, end = 28.dp)
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.logOut_2),
                        color = JTColors.logoutColor,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontWeight = W700,
                        fontSize = 12.sp

                    )
                }
                Spacer(Modifier.height(80.dp))
            }
        }
        if (uiState.message?.isNotEmpty() == true){
            Box(modifier = Modifier.padding(bottom = 65.dp)) {
                CommonSnackbar(
                    message = uiState.message ?: "",
                    type =
                        if (uiState.message!!.contains("Successfully",ignoreCase = true)){
                            SnackbarType.SUCCESS
                        }else{
                            SnackbarType.ERROR
                        },
                    autoDismissMillis = 1500,
                    onDismiss = {
                        viewModel.clearError()
                        if (navigateLogout){
                            navigateToUserTypeScreen()
                        }

                    }
                )
            }
        }
    }

}



@Composable
fun SectionTitle(title: String) {
    JtBtoCommonText(
        text = title,
        color = JTColors.textFieldTextColor,
        fontWeight = W600,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        fontFamily = JtBtoFonts.getOpenSansRegular(),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp)
    )
}


sealed class MoreOptionIcon {
    data class PainterIcon(val painter: Painter) : MoreOptionIcon()
    data class VectorIcon(val vector: ImageVector) : MoreOptionIcon()
}

@Composable
fun MoreOption(
    icon: MoreOptionIcon,
    title: String,
    subtitle: String = "",
    onclick:() -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onclick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(JTColors.otpBox, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is MoreOptionIcon.PainterIcon -> {
                    Image(
                        painter = icon.painter,
                        contentDescription = title,
                    )
                }

                is MoreOptionIcon.VectorIcon -> {
                    Icon(
                        imageVector = icon.vector,
                        contentDescription = title,
                        tint = JTColors.enableButton,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            JtBtoCommonText(
                text = title,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = W600
            )
            if (subtitle.isNotEmpty()) {
                JtBtoCommonText(
                    text = subtitle,
                    color = JTColors.code,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = W400
                )
            }
        }
    }
}

@Composable
fun AvailabilityToggle(
    title: String,
    subtitle: String = "",
    onClick:() -> Unit = {},
    viewModel: JTMoreTabScreenViewModel = getKoinViewModel()
) {

    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val availabilityService = sharedPreferenceUseCase.getUserInfo()
    val initialAllow = availabilityService?.flags?.status == true
    // Local state for immediate UI update
    var allow by remember { mutableStateOf(initialAllow) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onClick()}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(JTColors.otpBox, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = title,
                    tint = JTColors.iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                JtBtoCommonText(
                    text = title,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = W600
                )
                if (subtitle.isNotEmpty()) {
                    JtBtoCommonText(
                        text = subtitle,
                        color = JTColors.iconColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = W400
                    )
                }
            }
        }
        Switch(
            checked = allow,
            onCheckedChange =   {
                allow = it
                viewModel.updateVisibility(it)},
            modifier = Modifier.width(40.dp).height(24.dp),
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
        )
    }
}

@Composable
fun NotificationToggleOption(
    title: String,
    subtitle: String = "",
    onClick:() -> Unit = {},
    viewModel: JTMoreTabScreenViewModel = getKoinViewModel()
) {

    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val notificationService = sharedPreferenceUseCase.getUserInfo()
    val initialAllow = notificationService?.flags?.notification == true
    var allow by remember { mutableStateOf(initialAllow) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onClick()}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(JTColors.otpBox, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = title,
                    tint = JTColors.iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                JtBtoCommonText(
                    text = title,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = W600
                )
                if (subtitle.isNotEmpty()) {
                    JtBtoCommonText(
                        text = subtitle,
                        color = JTColors.iconColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = W400
                    )
                }
            }
        }
        Switch(
            checked =  allow,
            onCheckedChange =   {
                //immediate UI change
                allow = it
                //fire API call
                viewModel.notificationStatusUpdate(it) },
            modifier = Modifier.width(40.dp).height(24.dp),
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
        )
    }
}