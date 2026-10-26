package com.bytheour.presentations.screens.signup

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.business
import bytheour.composeapp.generated.resources.caretright
import bytheour.composeapp.generated.resources.details
import bytheour.composeapp.generated.resources.email
import bytheour.composeapp.generated.resources.enter_email
import bytheour.composeapp.generated.resources.enter_first
import bytheour.composeapp.generated.resources.enter_last
import bytheour.composeapp.generated.resources.enter_mobile
import bytheour.composeapp.generated.resources.enter_user
import bytheour.composeapp.generated.resources.finish
import bytheour.composeapp.generated.resources.first
import bytheour.composeapp.generated.resources.individual
import bytheour.composeapp.generated.resources.last
import bytheour.composeapp.generated.resources.mobile
import bytheour.composeapp.generated.resources.select_profile
import bytheour.composeapp.generated.resources.user
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.customcomponents.JTBtoSocialHandleButtons
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.login.EntryViewModel
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.adress.LatLngObject
import com.bytheour.utils.COMPANY_LLC_TITLE
import com.bytheour.utils.COUNTRY_CODE
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.PlatformActivityContext
import com.bytheour.utils.SOLE_PROPRIETOR
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTSignUpScreen(
    viewModel: JTSignUpViewModel = getKoinViewModel(),
    entryViewModel: EntryViewModel = getKoinViewModel(),
    moreTabViewModel: JTMoreTabScreenViewModel = getKoinViewModel(),
    navigateToOtp:(
        String,
        Int,
        firstName:String,
        lastName:String,
        userName:String,
        email:String,
        profile:String,
            )->Unit,
    navigateBack:()-> Unit,
    activityInstance: PlatformActivityContext?,
    mobile:String,
    from:String = "",
) {
    val uiStates by viewModel.uiStates.collectAsState()

    val selectedOption by viewModel.selectedOption.collectAsState()


    val radioOptions = listOf(
        stringResource(Res.string.individual),
        stringResource(Res.string.business)
    )

    val typeMapping = mapOf(
        stringResource(Res.string.individual) to SOLE_PROPRIETOR,
        stringResource(Res.string.business) to COMPANY_LLC_TITLE
    )

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var focused by remember { mutableStateOf(false) }


    var showImagePicker by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        if (viewModel.selectedOption.value == null) {
            viewModel.updateSelectedOption(radioOptions[0])
        }
        entryViewModel.assignActivityContext(activityInstance)
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
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
                    modifier = Modifier.padding(start = 16.dp).clickable {
                        navigateBack()
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding()

            ) {
                JtBtoCommonText(
                    stringResource(Res.string.details),
                    fontSize = 26.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold()
                )

                Spacer(modifier = Modifier.height(25.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Column {
                            when {
                                uiStates.imageBitmap != null -> {
                                    Image(
                                        bitmap = uiStates.imageBitmap!!,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                    )
                                }

                                else -> {
                                    Image(
                                        painter = painterResource(Res.drawable.america),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }
                        Image(
                            painter = painterResource(Res.drawable.caretright),
                            contentDescription = "",
                            modifier = Modifier
                                .offset(x = 7.dp, y = (-4).dp)
                                .size(35.dp)
                                .clickable { showImagePicker = true }
                        )


                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    )
                    {
                        JTBtoCustomTextField(
                            name = stringResource(Res.string.first),
                            value = uiStates.firstName ?: "",
                            onValueChange = { viewModel.updateFirstName(it) },
                            modifier = Modifier.weight(1f),
                            placeholder = stringResource(Res.string.enter_first),
                            errorMessage = uiStates.firstErrorMessage,
                            keyboardType = KeyboardType.Text
                        )
                        JTBtoCustomTextField(
                            name = stringResource(Res.string.last),
                            value = uiStates.lastName ?: "",
                            onValueChange = { viewModel.updateLastName(it) },
                            modifier = Modifier.weight(1f),
                            placeholder = stringResource(Res.string.enter_last),
                            errorMessage = uiStates.lastErrorMessage,
                            keyboardType = KeyboardType.Text
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    JTBtoCustomTextField(
                        name = stringResource(Res.string.user),
                        value = uiStates.userName ?: "",
                        onValueChange = { viewModel.updateUserName(it) },
                        placeholder = stringResource(Res.string.enter_user),
                        errorMessage = uiStates.userErrorMessage,
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (focused && !focusState.isFocused) {
                                    coroutineScope.launch {
                                        val isValid = viewModel.validateUserNameFormat(
                                            uiStates.userName ?: ""
                                        )
                                        if (isValid) {
                                            viewModel.checkUserName(
                                                userName = uiStates.userName ?: ""
                                            )
                                        }
                                    }
                                }
                                focused = focusState.isFocused
                            },
                    )

                    Spacer(modifier = Modifier.height(24.dp))


                    JTBtoCustomTextField(
                        name = stringResource(Res.string.email),
                        value = uiStates.email ?: "",
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = stringResource(Res.string.enter_email),
                        errorMessage = uiStates.emailErrorMessage,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (focused && !focusState.isFocused) {
                                    coroutineScope.launch {
                                        viewModel.checkUserEmail(userMail = uiStates.email ?: "")
                                    }
                                }
                                focused = focusState.isFocused
                            },
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            value = mobile,
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(0.97f),
                            placeholder = stringResource(Res.string.enter_mobile),
                            enable = false
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    JtBtoCommonText(
                        text = stringResource(Res.string.select_profile),
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        color = JTColors.RadioText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        radioOptions.forEach { text ->
                            val isSelected = text == selectedOption
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = JTColors.radioBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        viewModel.updateSelectedOption(text)
                                    },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            viewModel.updateSelectedOption(text)
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = JTColors.enableButton,
                                            unselectedColor = JTColors.radioCircle
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    JtBtoCommonText(
                                        text = text,
                                        fontSize = 16.sp,
                                        fontFamily = if (isSelected) JtBtoFonts.getOpenSansBold()
                                        else JtBtoFonts.getOpenSansSemiBold(),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                    JTBtoPrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        name =
                            stringResource(Res.string.finish),
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            if (viewModel.validateAllFields()) {
                                val countryCode =
                                    if (COUNTRY_CODE.startsWith("+")) COUNTRY_CODE else "+$COUNTRY_CODE"
                                val cleanedMobile = mobile.trim().removePrefix("0")
                                val fullPhoneNumber = "$countryCode$cleanedMobile"
                                println("number-------------------------------------$fullPhoneNumber")
                                if (from.equals("more", ignoreCase = true)) {
                                    val selectedTaxType =
                                        typeMapping[viewModel.selectedOption.value]
                                            ?: SOLE_PROPRIETOR
                                    moreTabViewModel.saveProfile(
                                        firstname = uiStates.firstName ?: "",
                                        lastname = uiStates.lastName ?: "",
                                        username = uiStates.userName ?: "",
                                        email = uiStates.email ?: "",
                                        type = selectedTaxType,
                                        onSuccess = {
                                            LatLngObject.updateFromSign(from)
                                            navigateBack()
                                        }

                                    )
                                } else {
                                    entryViewModel.sendFirebaseNumber(
                                        phoneNumber = fullPhoneNumber,
                                        onCodeSent = {
                                            viewModel.updateMessage("OTP Sent Successfully ")
                                        },
                                        onVerificationCompleted = {
                                            viewModel.updateMessage("OTP Sent Successfully")
                                        },
                                        onVerificationFailed = {
                                            viewModel.updateMessage(it.message.toString())
                                            println("Failed:=========================== ${it.message}")
                                        }
                                    )
                                }


                            }
                        },
                        buttonColors = JTColors.disableButton,
                        selectedColor = JTColors.enableButton,
                        textColor = Color.White,
                        isSelected = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        if (uiStates.message?.isNotEmpty() == true) {
            CommonSnackbar(
                message = uiStates.message ?: "",
                type = if (uiStates.message?.contains("successfully", ignoreCase = true) == true) {
                    SnackbarType.SUCCESS
                } else {
                    SnackbarType.ERROR
                },
                autoDismissMillis = 1500,
                onDismiss = {
                    val message = uiStates.message ?: ""

                    // Navigate only if message is success and NOT about dynamic link
                    if (
                        message.contains("successfully", ignoreCase = true) &&
                        !message.contains("Dynamic link generated successfully", ignoreCase = true)
                    ) {
                        navigateToOtp(
                            mobile,
                            1,
                            uiStates.firstName ?: "",
                            uiStates.lastName ?: "",
                            uiStates.userName ?: "",
                            uiStates.email ?: "",
                            uiStates.imagePathUrl ?: ""
                        )
                    }

                    viewModel.clearError()
                }
            )
        }

    }
    if (showImagePicker) {
        PermissionImagePicker(
            imageBitmap = uiStates.imageBitmap,
            onImageSelected = {
                if (it != null) {
                    viewModel.updateImageBitmap(
                        imageBitmap = it,
                    )
                }
                showImagePicker = false
            },
            showBottomSheet = showImagePicker,
            onDismissRequest = {
                showImagePicker = false
            },
            filePathUri = {
                viewModel.updateImagePathUrl(it?:"")
            },
            byteArrayImage = {
                viewModel.updateByteArrayImage(it?:ByteArray(0))
                viewModel.uploadUserImage(it?:ByteArray(0))
            }

        )
    }

}

