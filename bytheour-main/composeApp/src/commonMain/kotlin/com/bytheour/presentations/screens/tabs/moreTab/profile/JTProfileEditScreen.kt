package com.bytheour.presentations.screens.tabs.moreTab.profile

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.caretright
import bytheour.composeapp.generated.resources.delete_Account
import bytheour.composeapp.generated.resources.edit_Profile_Details
import bytheour.composeapp.generated.resources.email
import bytheour.composeapp.generated.resources.enter_email
import bytheour.composeapp.generated.resources.enter_first
import bytheour.composeapp.generated.resources.enter_last
import bytheour.composeapp.generated.resources.enter_mobile
import bytheour.composeapp.generated.resources.enter_user
import bytheour.composeapp.generated.resources.first
import bytheour.composeapp.generated.resources.last
import bytheour.composeapp.generated.resources.log_Out
import bytheour.composeapp.generated.resources.mobile
import bytheour.composeapp.generated.resources.one
import bytheour.composeapp.generated.resources.save
import bytheour.composeapp.generated.resources.user
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.adress.LatLngObject
import com.bytheour.showProgress
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun JTProfileEditScreen(
    onBackClick: () -> Unit = {},
    moreTabViewModel: JTMoreTabScreenViewModel = getKoinViewModel(),
    viewModel: JTProfileEditScreenViewModel = getKoinViewModel(),
    navigateToUserTypeScreen:() ->Unit,
    navigateToDeleteScreen:() ->Unit,
    fromDialog:String,
) {

    val uiStates by viewModel.uiStates.collectAsState()
    var showImagePicker by remember { mutableStateOf(false) }

    var navigateBack by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current


    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(Color.White)
        ) {
            CommonTopBar(
                title = stringResource(Res.string.edit_Profile_Details),
                isBackArrowClicked = navigateBack,
                onBackClick = {
                    navigateBack = true
                    onBackClick()
                },
                actionText = stringResource(Res.string.save),
                onActionClick ={
                    keyboardController?.hide()
                    if (viewModel.validateAllFields()){
                        viewModel.updateProfile(
                            firstName = uiStates.firstName,
                            lastName = uiStates.lastName,
                            email = uiStates.email,
                            country = "",
                            number = uiStates.number,
                            profile = uiStates.filename,
                            about = "",
                            username = uiStates.userName,
                            onSuccess = {
                                navigateBack = true
                            }
                        )
                    }
                }
            )

            HorizontalDivider(Modifier.fillMaxWidth(), color = JTColors.outerColor)
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
                        if (!showProgress.value) {
                            when {
                                uiStates.imageBitmap != null -> {
                                    Image(
                                        bitmap = uiStates.imageBitmap!!,
                                        contentDescription = "User Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                    )
                                }

                                !uiStates.imageFromServer.isNullOrBlank() -> {
                                    AsyncImage(
                                        model = uiStates.imageFromServer,
                                        contentDescription = "Server Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }

                                else -> {
                                    Image(
                                        painter = painterResource(Res.drawable.america),
                                        contentDescription = "Default Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }

                        if (uiStates.imageErrorMessage != null) {
                            JtBtoCommonText(
                                text = uiStates.imageErrorMessage!!,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
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

            Spacer(modifier = Modifier.height(14.dp))
            // Input Fields
            Column(modifier = Modifier
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState())
            ) {
                Row {
                    JTBtoCustomTextFieldForEdit(
                        name = stringResource(Res.string.first),
                        value = uiStates.firstName ?:"",
                        onValueChange = { viewModel.updateFirstName(it)},
                        modifier = Modifier.weight(1f),
                        placeholder = stringResource(Res.string.enter_first),
                        errorMessage = uiStates.firstErrorMessage,
                    )

                    Spacer(Modifier.width(24.dp))

                    JTBtoCustomTextFieldForEdit(
                        name = stringResource(Res.string.last),
                        value = uiStates.lastName ?:"",
                        onValueChange = { viewModel.updateLastName(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = stringResource(Res.string.enter_last),
                        errorMessage = uiStates.lastErrorMessage
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                JTBtoCustomTextFieldForEdit(
                    name = stringResource(Res.string.user),
                    value = uiStates.userName?:"",
                    onValueChange = {viewModel.updateUserName(it)},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(Res.string.enter_user),
                    errorMessage = uiStates.userErrorMessage
                )

                Spacer(modifier = Modifier.height(8.dp))

                JTBtoCustomTextFieldForEdit(
                    name = stringResource(Res.string.email),
                    value = uiStates.email?:"",
                    onValueChange = { viewModel.updateEmail(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(Res.string.enter_email),
                    errorMessage = uiStates.emailErrorMessage
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.27f)
                    ) {
                        JtBtoCommonText(
                            text = stringResource(Res.string.mobile),
                            fontSize = 10.sp,
                            color = JTColors.textFieldTextColor,
                            fontFamily = JtBtoFonts.getOpenSansRegular()
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                        JTBtoSocialHandleBox(
                            onClick = {},
                            image = painterResource(Res.drawable.america),
                            text = stringResource(Res.string.one),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        JTBtoCustomTextFieldForEdit(
                            modifier = Modifier,
                            value = uiStates.number ?: "",
                            onValueChange = { viewModel.updateNumber(it) },
                            placeholder = stringResource(Res.string.enter_mobile),
                            errorMessage = uiStates.numberErrorMessage
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(32.dp))

                // Log Out Button
                Button(
                    onClick = {  moreTabViewModel.logOut { navigateToUserTypeScreen() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEFEF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.log_Out),
                        color = Color(0xFF7F1F1F),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontWeight = W700,
                        fontSize = 12.sp

                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Delete Account Text
                JtBtoCommonText(
                    text = stringResource(Res.string.delete_Account),
                    fontWeight = W600,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),

                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navigateToDeleteScreen()
                        },
                    color = Color.Black,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (uiStates.imageErrorMessage?.isNotEmpty() == true || uiStates.message?.isNotEmpty() == true){
            CommonSnackbar(
                message = if(uiStates.message?.isNotEmpty() == true){
                    uiStates.message
                }else{
                    uiStates.imageErrorMessage
                }?:"",
                type =
                    if (uiStates.message!!.contains("Successfully",ignoreCase = true)){
                        SnackbarType.SUCCESS
                    }else{
                        SnackbarType.ERROR
                    },
                autoDismissMillis = 1500,
                onDismiss = {
                    viewModel.clearError()
                    if (navigateBack){
                        if (fromDialog.isNotEmpty()){
                            LatLngObject.updateFromDialog(fromDialog)
//                            navigateBackDialog(fromDialog)
                            onBackClick()
                        }else{
                            onBackClick()
                        }

                    }
                }
            )
        }

    }

    if (showImagePicker) {
        PermissionImagePicker(
            imageBitmap = uiStates.imageBitmap,
            onImageSelected = {
                viewModel.updateImageBitmap(
                    imageBitmap = it,
                )
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







@Composable
fun CommonTopBar(
    title: String,
    onBackClick: () -> Unit,
    onActionClick: (() -> Unit)? = null,
    actionText: String? = null,
    isBackArrowClicked: Boolean = false,
    ) {
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(
            enabled = !isBackArrowClicked,
            onClick = {onBackClick()}) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.padding(end = 20.dp).size(22.dp)
            )
        }

        JtBtoCommonText(
            text = title,
            fontSize = 16.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            modifier = Modifier.weight(1f),
            fontWeight = W700,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        if (actionText != null && onActionClick != null) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF313131), RoundedCornerShape(10.dp))
                    .width(60.dp)
                    .height(30.dp)
                    .clickable(onClick = onActionClick),
                contentAlignment = Alignment.Center
            ) {
                JtBtoCommonText(
                    text = actionText,
                    color = Color.White,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontWeight = W600,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

        } else {
            // placeholder to balance the back button space
            Spacer(modifier = Modifier.size(48.dp)) // Same as IconButton size
        }
    }
}

@Composable
fun JTBtoSocialHandleBox(
    onClick: () -> Unit,
    image: Painter,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .border(
                BorderStroke(width = 1.dp, JTColors.border),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = image,
                contentDescription = "",
                modifier = Modifier.size(25.dp),
            )
            Spacer(Modifier.width(10.dp))

            JtBtoCommonText(
                text,
                color = JTColors.socialSignUpText,
                fontSize = 14.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
        }
    }
}


@Composable
fun JTBtoCustomTextFieldForEdit(
    name: String? = null,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    enabled: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.padding(start = 4.dp)) {
        if (name != null) {
            JtBtoCommonText(
                text = name,
                fontSize = 10.sp,
                color = JTColors.textFieldTextColor
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                JtBtoCommonText(
                    text = placeholder,
                    color = JTColors.textFieldColor,
                    fontSize = 14.sp,
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(68.dp),
            singleLine = true,
            maxLines = 1,
            enabled = enabled,
            textStyle = TextStyle(
                fontSize = 14.sp,
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedPlaceholderColor = JTColors.outerColor,
                unfocusedPlaceholderColor = JTColors.outerColor,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = JTColors.border,
                unfocusedIndicatorColor = JTColors.border,
                disabledContainerColor = JTColors.WhiteColor,
                disabledTextColor = Color.Black

            ),
            isError = !errorMessage.isNullOrEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions ?: KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            trailingIcon = trailingIcon,
            supportingText = {
                if (!errorMessage.isNullOrEmpty()) {
                    JtBtoCommonText(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                }
            }
        )
    }
}
