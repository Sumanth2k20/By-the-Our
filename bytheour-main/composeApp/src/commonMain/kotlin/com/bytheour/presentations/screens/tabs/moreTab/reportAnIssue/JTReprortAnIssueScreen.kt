package com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.reportSmall
import bytheour.composeapp.generated.resources.report_Issue
import bytheour.composeapp.generated.resources.reset
import bytheour.composeapp.generated.resources.subject_of_Issue
import bytheour.composeapp.generated.resources.tell_us_more_about
import com.bytheour.utils.JTColors
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTReportAnIssueScreen(
    viewModel: JTReportAnIssueScreenViewModel = getKoinViewModel(),
    onBackClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    var navigateBack by remember { mutableStateOf(false)}
    var isBackArrowClicked by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()){
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
                title = stringResource(Res.string.report_Issue),
                isBackArrowClicked = isBackArrowClicked,
                onBackClick = {
                    isBackArrowClicked = true
                    onBackClick()
                }
            )

            // Dropdown
//        CustomDropdowns(
//            placeHolder = {
//                JtBtoCommonText(
//                    stringResource(Res.string.subject_of_Issue),
//                    fontFamily = JtBtoFonts.getOpenSansRegular(),
//                    fontWeight = W600,
//                    fontSize = 13.sp,
//                    lineHeight = 14.sp
//                )
//                          },
//            options = subjectOptions,
//            selectedOption = uiState.issueType?:"",
//            onOptionSelected = { viewModel.onIssueTypeChange(it) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            errorMessage = uiState.issueTypeError
//
//        )
            CustomOutlinedTextField(
                value =  uiState.issueType?:"",
                onValueChange = { viewModel.onIssueTypeChange(it) },
                placeHolder = {
                    JtBtoCommonText(
                        stringResource(Res.string.subject_of_Issue),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.Gray
                    )
                },
                errorMessage =  uiState.issueTypeError,
                hasBorder = true,
                borderColor = JTColors.border, // Or your custom color
                textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                enabled = true
            )


            // Description TextField
            OutlinedTextField(
                value = uiState.issueDescription?:"",
                onValueChange = { viewModel.onIssueDescriptionChange(it) },
                supportingText = {
                    if (uiState.issueDescriptionError?.isNotEmpty() == true) {
                        JtBtoCommonText(
                            text = uiState.issueDescriptionError ?: "",
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            color = Color.Red,
                            fontSize = 10.sp
                        )
                    }
                },
                placeholder = {
                    JtBtoCommonText(
                        stringResource(Res.string.tell_us_more_about),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = JTColors.addressText,
                    unfocusedPlaceholderColor = JTColors.reportTellUs,
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
                        viewModel.clearFields()
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
                        stringResource(Res.string.reset),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W700,
                        fontSize = 12.sp,
                        lineHeight = 14.sp

                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (viewModel.validateInputs()) {
                            viewModel.sendReportAnIssue(
                                subject = uiState.issueType?:"",
                                description = uiState.issueDescription?:"",
                                onReportSuccess = {
                                    navigateBack = true
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
                        stringResource(Res.string.reportSmall),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W700,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
        if (uiState.message?.isNotEmpty() == true){
            CommonSnackbar(
                message = uiState.message?:"",
                type =
                    if (uiState.message!!.contains("Thanks for your valuable feedback",ignoreCase = true)){
                        SnackbarType.SUCCESS
                    }else{
                        SnackbarType.ERROR
                    },
                autoDismissMillis = 1500,
                onDismiss = {
                    viewModel.clearFields()
                    if (navigateBack){
                        onBackClick()
                    }


                }
            )
        }

    }

}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: @Composable (() -> Unit)? = null,
    hasBorder: Boolean = true,
    errorMessage: String? = null,
    borderColor: Color = JTColors.border,
    textStyle: TextStyle = TextStyle.Default,
    enabled: Boolean = true,
    maxLines: Int = 1
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeHolder,
            supportingText = {
                if (!errorMessage.isNullOrEmpty()) {
                    JtBtoCommonText(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Start
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedPlaceholderColor = Color.Black,
                unfocusedPlaceholderColor = Color(0xFF31313166),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (hasBorder) borderColor else Color.Transparent,
                unfocusedIndicatorColor = if (hasBorder) borderColor else Color.Transparent
            ),
            textStyle = textStyle,
            enabled = enabled,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
    }
}

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdowns(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    modifierMenu: Modifier = Modifier,
    placeHolder: @Composable (() -> Unit)? = null,
    options: List<String>,
    hasBorder: Boolean = true,
    errorMessage: String? = null,
    borderColor: Color = JTColors.border,
    textStyle: TextStyle = TextStyle.Default,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                placeholder = placeHolder,
                supportingText = {
                    if (!errorMessage.isNullOrEmpty()) {
                        JtBtoCommonText(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                        )
                    }
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = enabled)
                    .focusRequester(focusRequester),
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = JTColors.enableButton
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor =  Color(0xFF31313166),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = JTColors.border,
                    unfocusedIndicatorColor = JTColors.border,
                ),
                textStyle = textStyle,
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifierMenu.background(color = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .padding(2.dp)
                            .background(color = Color.White, RoundedCornerShape(12.dp)),
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item, color = Color.Black, fontSize = 12.sp)
                            }
                        },
                        onClick = {
                            onOptionSelected(item) // Updated to match function call
                            expanded = false
                        }
                    )
                }
            }
        }

    }
}