package com.bytheour.presentations.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.agree
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.and
import bytheour.composeapp.generated.resources.apple
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.continue_apple
import bytheour.composeapp.generated.resources.continue_email
import bytheour.composeapp.generated.resources.continue_google
import bytheour.composeapp.generated.resources.continues
import bytheour.composeapp.generated.resources.enter_mobile
import bytheour.composeapp.generated.resources.google
import bytheour.composeapp.generated.resources.info_black_ico
import bytheour.composeapp.generated.resources.login
import bytheour.composeapp.generated.resources.mail
import bytheour.composeapp.generated.resources.mobile
import bytheour.composeapp.generated.resources.or
import bytheour.composeapp.generated.resources.otp_Info
import bytheour.composeapp.generated.resources.policy
import bytheour.composeapp.generated.resources.privacy_Policy
import bytheour.composeapp.generated.resources.terms
import bytheour.composeapp.generated.resources.terms_and_conditions
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.customcomponents.JTBtoSocialHandleButtons
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.COUNTRY_CODE
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JTWebViewScreen
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PRIVACY_POLICY_WEB_VIEW_URL
import com.bytheour.utils.PlatformActivityContext
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.TERMS_AND_CONDITIONS_WEB_VIEW_URL
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getKoinViewModel
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTLoginScreen(
    viewModel: JTLoginViewModel = getKoinViewModel(),
    entryViewModel: EntryViewModel = getKoinViewModel(),
    navigateToOtpScreen:(String)->Unit,
    navigateToBack :()->Unit,
//    isProvider:Boolean,
    activityInstance: PlatformActivityContext?,
    navigateToSignupScreen:(String)->Unit,
) {
    var showWebView by remember { mutableStateOf<String?>(null) }

    val uiStates by viewModel.uiStates.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val selected = uiStates.number?.length == 10 && uiStates.number!!.all { it.isDigit() }



    LaunchedEffect(Unit) {
        entryViewModel.assignActivityContext(activityInstance)
    }
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val role = sharedPreferenceUseCase.getUser()



Box{
    Column(modifier = Modifier.fillMaxSize()
        .background(Color.White)
        .statusBarsPadding()
        .imePadding()
        .pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
                keyboardController?.hide()
            })
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(Res.drawable.arrowleft),
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp)
                    .clickable {
                        navigateToBack()
                    }
            )

        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))


        Spacer(modifier =Modifier.height(8.dp))
        Column(modifier = Modifier
            .padding(16.dp)
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
            JtBtoCommonText(
                stringResource(Res.string.login),
                fontSize = 26.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
            Spacer(modifier =Modifier.height(34.dp))

            Row (modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top)
            {
                Column {
                    JtBtoCommonText(
                        text = stringResource(Res.string.mobile),
                        fontSize = 14.sp,
                        color = JTColors.textFieldTextColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular()

                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    JTBtoSocialHandleButtons(
                        onClick = {},
                        image = painterResource(Res.drawable.america),
                        text = "+$COUNTRY_CODE",
                        modifier = Modifier.wrapContentWidth()
                    )
                }

                Spacer(modifier = Modifier.width(1.dp))
                JTBtoCustomTextField(
                    value =uiStates.number ?:"" ,
                    onValueChange = {
                        viewModel.mobileNumberChange(it)
                        if (it.length == 10) {
                            keyboardController?.hide()
                            viewModel.clearMobileNumberError()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.97f)
                        .fillMaxHeight(),
                    errorMessage = uiStates.mobileNumberError,
                    placeholder = stringResource(Res.string.enter_mobile),
                    keyboardType = KeyboardType.Number,
                )
            }
            Spacer(modifier =Modifier.height(24.dp))

            OtpInformation()

            Spacer(modifier =Modifier.height(24.dp))

            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(Res.string.continues),
                onClick = {
                    if (viewModel.validation()) {
                        val fullNumber = "+${COUNTRY_CODE}${uiStates.number}"

                        viewModel.makeLogin(
                            number = viewModel.uiStates.value.number ?: "",
                            onOtpSend = {
                                val isProviderRole = role == TYPE_PROVIDER
                                val isCustomerRole = role == TYPE_CUSTOMER
                                val isExisting = viewModel.uiStates.value.isExistCheck == true
                                val mobile = viewModel.uiStates.value.number ?: ""

                                // ✅ CASE 1: Customer OR existing provider → send OTP
                                if (isCustomerRole || (isProviderRole && isExisting)) {
                              //      if (platform.equals("Android", ignoreCase = true)) {
                                        entryViewModel.sendFirebaseNumber(
                                            phoneNumber = fullNumber,
                                            onCodeSent = {
                                                viewModel.updateMessage("OTP Sent Successfully")
//                                                navigateToOtpScreen(mobile)
                                            },
                                            onVerificationCompleted = {
                                            },
                                            onVerificationFailed = {
                                                viewModel.updateErrorMessage(it.message.toString())
                                            }
                                        )
//                                    }else   {
//                                        navigateToOtpScreen(mobile)
//                                    }
                                }

                                else if (isProviderRole && !isExisting) {
                                    navigateToSignupScreen(mobile)
                                }
                            },
                            onOtpFailed = {
                            }
                        )
                    }
                },
                buttonColors = JTColors.disableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White,
                isSelected = selected
            )

            Spacer(modifier =Modifier.height(12.dp))

//            SocialSignUps()

            Spacer(modifier = Modifier.weight(1f))

            TermsAndPrivacy(
                onTermsClick = { showWebView = "terms" },
                onPrivacyClick = { showWebView = "privacy" }
            )
        }
    }
    if (uiStates.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiStates.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 1500,
            onDismiss = {
                navigateToOtpScreen(viewModel.uiStates.value.number ?: "")
                viewModel.clearError()
            }
        )
    }
    if (uiStates.errorMessage?.isNotBlank() == true){
        CommonSnackbar(
            message = uiStates.errorMessage?:"",
            type = SnackbarType.ERROR,
            onDismiss = {
                viewModel.clearError()
            }
        )
    }
}



    when (showWebView) {
        "terms" -> JTWebViewScreen(
            title = stringResource(Res.string.terms_and_conditions),
            url = TERMS_AND_CONDITIONS_WEB_VIEW_URL,
            onBackPress = { showWebView = null },
            onAcceptClick = { showWebView = null },
            onDeclineClick = { showWebView = null },
            showDownBox = false
        )
        "privacy" -> JTWebViewScreen(
            title = stringResource(Res.string.privacy_Policy),
            url = PRIVACY_POLICY_WEB_VIEW_URL,
            onBackPress = { showWebView = null },
            onAcceptClick = { showWebView = null },
            onDeclineClick = { showWebView = null },
            showDownBox = false
        )
    }
}

@Composable
fun OtpInformation() {
    Card (modifier = Modifier.fillMaxWidth()
        .wrapContentHeight(),
            colors = CardDefaults.cardColors(
            containerColor = JTColors.otpBox
            ),
        shape = RoundedCornerShape(8.dp)
    )
    {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {
            Image(painter = painterResource(Res.drawable.info_black_ico),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.width(12.dp))
            JtBtoCommonText(stringResource(Res.string.otp_Info),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                textAlign = TextAlign.Start,
                fontFamily = JtBtoFonts.getOpenSansRegular())
        }

    }

}

@Composable
fun SocialSignUps() {
    Column (modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Row(
            modifier = Modifier.fillMaxWidth(0.95f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = JTColors.socialSignUpBorder
            )
            JtBtoCommonText(
                text = stringResource(Res.string.or),
                modifier = Modifier.padding(horizontal = 8.dp),
                color = JTColors.textFieldColor,
                fontSize = 14.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = JTColors.socialSignUpBorder
            )
        }

        Spacer(modifier =Modifier.height(30.dp))

        JTBtoSocialHandleButtons(
            onClick = {},
            image = painterResource(Res.drawable.google),
            text = stringResource(Res.string.continue_google),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier =Modifier.height(24.dp))

        JTBtoSocialHandleButtons(
            onClick = {},
            image = painterResource(Res.drawable.apple),
            text = stringResource(Res.string.continue_apple),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier =Modifier.height(24.dp))

        JTBtoSocialHandleButtons(
            onClick = {},
            image = painterResource(Res.drawable.mail),
            text = stringResource(Res.string.continue_email),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

    }


}

@Composable
fun TermsAndPrivacy(onTermsClick: () -> Unit,
                    onPrivacyClick: () -> Unit
) {
    Column (modifier = Modifier.fillMaxWidth()
        .background(color = JTColors.otpBox.copy(alpha = 0.5f))
        .padding(12.dp)
        .wrapContentHeight()
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        JtBtoCommonText(stringResource(Res.string.agree),
            color = JTColors.textFieldColor,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansRegular()
        )
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            JtBtoCommonText(
                text = stringResource(Res.string.terms),
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                modifier = Modifier.clickable { onTermsClick() }
            )

            JtBtoCommonText(
                text = " ${stringResource(Res.string.and)} ",
                color = JTColors.textFieldColor,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansRegular()
            )

            JtBtoCommonText(
                text = stringResource(Res.string.policy),
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                modifier = Modifier.clickable { onPrivacyClick() }
            )
        }

    }
    
}
