package com.bytheour.presentations.screens.tabs.moreTab.delete

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.delete_Account
import bytheour.composeapp.generated.resources.delete_acc_message
import bytheour.composeapp.generated.resources.please_let_us_know_why
import bytheour.composeapp.generated.resources.type_here
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.JTColors
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.showToast
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteScreen(
    onBackClick: () -> Unit,
    viewModel: DeleteScreenViewModel = getKoinViewModel(),
    navigateToUserTypeScreen:() ->Unit
){
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDialog by remember { mutableStateOf(false) }
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    LaunchedEffect(uiState.message) {
        uiState.message?.takeIf { it.isNotBlank() }?.let { message ->
            showToast(message)
            if (message == "Account deleted successfully.") {
                navigateToUserTypeScreen()
                sharedPreferenceUseCase.clearSharedPrefs()
            }
            viewModel.clearMessage()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .imePadding()
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
    ) {
        // Top Bar
        JtBtoCommonTopAppBarForMoreScreens(
            title =stringResource(Res.string.delete_Account),
            isBackArrowClicked = isBackArrowClicked,
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            }
        )
        JtBtoCommonText(
            text = stringResource(Res.string.please_let_us_know_why) ,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            fontSize = 13.sp,
            color = JTColors.BlackButtonColor,
            lineHeight = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        // Description TextField
        OutlinedTextField(
            value = uiState.reason?:"",
            onValueChange = { viewModel.onIssueDescriptionChange(it) },
            supportingText = {
                if (uiState.reasonError?.isNotEmpty() == true) {
                    JtBtoCommonText(
                        text = uiState.reasonError ?: "",
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                }
            },
            placeholder = {
                JtBtoCommonText(
                    stringResource(Res.string.type_here),
                    fontFamily = JtBtoFonts.getOpenSansLightItalic(),
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedPlaceholderColor = JTColors.addressText,
                unfocusedPlaceholderColor = Color(0xFF31313166),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = JTColors.border,
                unfocusedIndicatorColor = JTColors.border,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (viewModel.validateInputs()){ showDialog = true }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = JTColors.enableButton,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                JtBtoCommonText(
                    stringResource(Res.string.delete),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontWeight = W700, color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 14.sp

                )
            }
        }
    }
    if (showDialog){
        CustomAlertDialog(
            showDialog = showDialog,
            onDismiss = {showDialog = false},
            title = stringResource(Res.string.delete_Account),
            message = stringResource(Res.string.delete_acc_message),
            onCancel = { showDialog = false },
            onConfirm = {
                viewModel.deleteAccount(
                    reason = uiState.reason?:"",
                    onSuccess = {
                        showDialog = false
                    }
                )
            },
            yes = true,
            no = true
        )
    }
}