package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.description_Star
import bytheour.composeapp.generated.resources.enter_a_description
import bytheour.composeapp.generated.resources.enter_new_service_title
import bytheour.composeapp.generated.resources.request_new_service
import bytheour.composeapp.generated.resources.reset
import bytheour.composeapp.generated.resources.submit
import bytheour.composeapp.generated.resources.title_required
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue.CustomOutlinedTextField
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ServiceRequestScreen(
    viewModel: ServiceRequestScreenViewModel = getKoinViewModel(),
    onBackClick: () -> Unit,
    catId:String
) {

    val uiState by viewModel.uiState.collectAsState()
    var navigateBack by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
                keyboardController?.hide()
            })
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                JtBtoCommonText(
                    text =     stringResource(Res.string.request_new_service),
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.clickable { onBackClick() }
                )

            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.title_required),
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                color = Color(0xFF7F7F7F)

            )
            CustomOutlinedTextField(
                value =  uiState.service?:"",
                onValueChange = { viewModel.onServiceChange(it) },
                placeHolder = {
                    JtBtoCommonText(
                        stringResource(Res.string.enter_new_service_title),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = Color(0xFF7F7F7F),
                        lineHeight = 14.sp
                    )
                },
                errorMessage =  uiState.serviceError,
                hasBorder = true,
                borderColor = JTColors.border, // Or your custom color
                textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                enabled = true
            )

            Text(
                text =stringResource(Res.string.description_Star),
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                color =Color(0xFF7F7F7F)

            )


            OutlinedTextField(
                value = uiState.serviceDescription?:"",
                onValueChange = { viewModel.onServiceDescriptionChange(it) },
                supportingText = {
                    if (uiState.serviceDescriptionError?.isNotEmpty() == true) {
                        JtBtoCommonText(
                            text = uiState.serviceDescriptionError ?: "",
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            color = Color.Red,
                            fontSize = 10.sp
                        )
                    }
                },
                placeholder = {
                    JtBtoCommonText(
                        stringResource(Res.string.enter_a_description),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF7F7F7F)
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
                    imeAction = ImeAction.Done
                ),
            )

            Spacer(modifier = Modifier.height(30.dp))
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
                        if (viewModel.validateInputs()) {
                            viewModel.addNewService(
                                catId = catId,
                                onSuccess = {
                                    onBackClick()
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
                        stringResource(Res.string.submit),
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
                type =    SnackbarType.SUCCESS,
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