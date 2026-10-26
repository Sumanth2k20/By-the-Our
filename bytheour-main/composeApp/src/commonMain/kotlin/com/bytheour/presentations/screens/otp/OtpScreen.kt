package com.bytheour.presentations.screens.otp


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.business
import bytheour.composeapp.generated.resources.enter_code
import bytheour.composeapp.generated.resources.individual
import bytheour.composeapp.generated.resources.receive_code
import bytheour.composeapp.generated.resources.resendCode
import bytheour.composeapp.generated.resources.verify_number
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.screens.login.CheckSumUtil
import com.bytheour.presentations.screens.login.EntryViewModel
import com.bytheour.presentations.screens.signup.JTSignUpViewModel
import com.bytheour.utils.COMPANY_LLC_TITLE
import com.bytheour.utils.COUNTRY_CODE
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PlatformActivityContext
import com.bytheour.utils.SOLE_PROPRIETOR
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTOtpScreen(
    mobile: String,
    viewModel: OtpViewModel = getKoinViewModel(),
    entryViewModel: EntryViewModel = getKoinViewModel(),
    viewModelSignUp: JTSignUpViewModel = getKoinViewModel(),
    navigateToBack: () -> Unit,
    navigateToLocation: (Boolean) -> Unit,
    value: Int,
    firstName: String,
    lastName: String,
    userName: String,
    email: String,
    profile: String,
    activityInstance: PlatformActivityContext?,
) {
    val uiState by viewModel.uiStates.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var timerKey by remember { mutableStateOf(0) }
    val timerCompleted = remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }

    println("mobileNumber=============================== $mobile")
    val typeMapping = mapOf(
        stringResource(Res.string.individual) to SOLE_PROPRIETOR,
        stringResource(Res.string.business) to COMPANY_LLC_TITLE
    )

    LaunchedEffect(Unit) {
        entryViewModel.assignActivityContext(activityInstance)
    }

    LaunchedEffect(uiState.otp) {
        if (uiState.otp.length == 6 && uiState.otp.all { it.isDigit() } && !isVerifying) {
            if (viewModel.validateOtp()) {
                isVerifying = true
                val security = CheckSumUtil()

                if (value == 1) {
                    entryViewModel.verifyOtpCode(
                        code = uiState.otp,
                        onSuccess = {
                            viewModelSignUp.register(
                                number = mobile,
                                firstname = firstName,
                                lastname = lastName,
                                username = userName,
                                email = email,
                                profile = profile,
                                onSuccess = {
                                    val selectedTaxType =
                                        typeMapping[viewModelSignUp.selectedOption.value]
                                            ?: SOLE_PROPRIETOR
                                    viewModelSignUp.getTaxClassification(
                                        selectedTaxType,
                                        navigateToLocation = {
                                            viewModel.markOtpVerified()
                                            viewModel.updateMessage(
                                                viewModelSignUp.uiStates.value.message ?: ""
                                            )
                                        }
                                    )
                                }
                            )
                            isVerifying = false
                        },
                        onFailure = { errorMsg ->
                            viewModel.updateErrorMessage(errorMsg.message ?: "Invalid OTP. Please try again.")
                            viewModel.clearOtp()
                            isVerifying = false
                        }
                    )
                } else {
                    entryViewModel.verifyOtpCode(
                        code = uiState.otp,
                        onSuccess = {
                            viewModel.login(
                                securityToken = security.generateChecksum("$COUNTRY_CODE$mobile"),
                                contact = "$COUNTRY_CODE$mobile",
                                onSuccess = {
                                    viewModel.updateMessage("OTP Verified Successfully")
                                    viewModel.markOtpVerified()
                                },
                            )
                            isVerifying = false
                        },
                        onFailure = { errorMsg ->
                            viewModel.updateErrorMessage(errorMsg.message ?: "Invalid OTP. Please try again.")
                            viewModel.clearOtp()
                            isVerifying = false
                        }
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .imePadding()
                .pointerInput(Unit) {
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
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 16.dp)
                        .clickable {
                            navigateToBack()
                        }
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                JtBtoCommonText(
                    stringResource(Res.string.verify_number),
                    fontSize = 26.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    minLines = 2,
                    maxLines = 2,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                JtBtoCommonText(
                    text = stringResource(Res.string.enter_code) + " +${COUNTRY_CODE} $mobile",
                    color = JTColors.code,
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold()
                )

                Spacer(modifier = Modifier.height(34.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OtpTextField(
                        otpText = uiState.otp,
                        onOtpTextChange = { value, isComplete ->
                            viewModel.onOtpChange(value)
                            if (isComplete) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                JtBtoCommonText(
                    text = stringResource(Res.string.receive_code),
                    color = JTColors.code,
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold()
                )

                if (!timerCompleted.value) {
                    BasicCountdownTimer(
                        timerKey = timerKey,
                        onTimerComplete = { timerCompleted.value = true }
                    )
                }

                if (timerCompleted.value) {
                    JTBtoPrimaryButton(
                        modifier = Modifier.fillMaxWidth(0.45f)
                            .height(40.dp),
                        name = stringResource(Res.string.resendCode),
                        onClick = {
                            if (timerCompleted.value) {
                                viewModel.clearOtp()
                                timerKey++
                                timerCompleted.value = false

                                val fullNumber = "+${COUNTRY_CODE}${mobile}"

                                if (entryViewModel.hasResendToken()) {
                                    entryViewModel.resendOtp(
                                        phoneNumber = fullNumber,
                                        onCodeSent = { viewModel.updateMessage("OTP Sent Successfully") },
                                        onVerificationCompleted = {},
                                        onVerificationFailed = { viewModel.updateMessage("Resend failed: ${it.message}") }
                                    )
                                } else {
                                    entryViewModel.sendFirebaseNumber(
                                        phoneNumber = fullNumber,
                                        onCodeSent = { viewModel.updateMessage("OTP Sent Successfully") },
                                        onVerificationCompleted = {},
                                        onVerificationFailed = { viewModel.updateMessage("Send failed: ${it.message}") }
                                    )
                                }
                            }
                        },
                        buttonColors = Color.White,
                        selectedColor = Color.White,
                        textColor = JTColors.code,
                        borderColor = JTColors.code,
                        enable = true
                    )
                }
            }
        }

        if (viewModelSignUp.uiStates.value.message?.isNotEmpty() == true || uiState.message?.isNotEmpty() == true) {
            CommonSnackbar(
                message = if (value == 1) viewModelSignUp.uiStates.value.message
                    ?: "" else uiState.message ?: "",
                type = SnackbarType.SUCCESS,
                autoDismissMillis = 3000,
                onDismiss = {
                    viewModel.clearError()
                    if (uiState.isOtpVerified) {
                        navigateToLocation(uiState.isAdmin)
                        viewModel.clearOtpVerifiedFlag()
                    }
                }
            )
        }
        if (uiState.errorMessage?.isNotEmpty() == true || viewModelSignUp.uiStates.value.error?.isNotEmpty() == true) {
            CommonSnackbar(
                message = uiState.errorMessage ?: viewModelSignUp.uiStates.value.error ?: "",
                type = SnackbarType.ERROR,
                autoDismissMillis = 2000,
                onDismiss = {
                    viewModel.clearError()
                    viewModelSignUp.clearError()
                }
            )
        }
    }
}

@Composable
fun BasicCountdownTimer(
    timerKey: Int,
    onTimerComplete: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(60) }

    LaunchedEffect(key1 = timerKey) {
        timeLeft = 60
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        onTimerComplete()
    }

    Text(
        text = if (timeLeft < 10) "00:0$timeLeft" else "00:$timeLeft",
        color = JTColors.enableButton
    )
}


@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester), // Attach FocusRequester
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    if (index < otpCount - 1) {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "|"
        index > text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .size(45.dp)
            .border(
                1.dp,
                when {
                    isFocused -> JTColors.lightGrayColor
                    else -> JTColors.lightGrayColor
                },
                RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            color = JTColors.grayColor,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getOpenSansBold()
        )
    }
}
