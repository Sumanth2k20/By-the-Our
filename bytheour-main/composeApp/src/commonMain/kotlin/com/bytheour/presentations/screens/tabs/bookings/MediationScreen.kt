package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.dollar_ico
import bytheour.composeapp.generated.resources.enter_amount
import bytheour.composeapp.generated.resources.enter_reasons
import bytheour.composeapp.generated.resources.mediation
import bytheour.composeapp.generated.resources.not_happy_with_work
import bytheour.composeapp.generated.resources.note
import bytheour.composeapp.generated.resources.refund_amount_note
import bytheour.composeapp.generated.resources.request_refund_amount
import bytheour.composeapp.generated.resources.submit
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestMediationScreen(
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    bookingId : String,
    navigateBack:()-> Unit
){
    val uiState by bookingViewModel.uiStates.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

//    LaunchedEffect(uiState.isNavigateBack) {
//        if (uiState.isNavigateBack == true) {
//            navigateBack()
//        }
//    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = JTColors.WhiteColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.mediation),
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.WhiteColor,
                    modifier = Modifier.padding(start = 10.dp),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 10.dp).clickable { navigateBack() }
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = JTColors.enableButton
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.not_happy_with_work),
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            modifier = Modifier.align(Alignment.Start)
                .padding(start = 10.dp),
            color = JTColors.enableButton
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = uiState.mediationReason?:"" ,
            onValueChange = { bookingViewModel.onMediationReasonChange(it) },
            placeholder = { Text(stringResource(Res.string.enter_reasons),
                color = JTColors.mediationText,
                fontFamily = JtBtoFonts.getInterRegular())},
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE9E9E9),
                unfocusedBorderColor = Color(0xFFE9E9E9),
                cursorColor = JTColors.blackColor,
                focusedContainerColor = Color(0xFFFCFCFC),
                unfocusedContainerColor =  Color(0xFFFCFCFC),

            )
        )


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.request_refund_amount),
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            modifier = Modifier.align(Alignment.Start).padding(10.dp),
            color = JTColors.ratingColor
        )


        OutlinedTextField(
            value = uiState.mediationAmount?:"",
            onValueChange = { bookingViewModel.onMediationAmountChange(it)
                            keyboardController?.hide()},
            placeholder = {
                Text(
                    text = stringResource(Res.string.enter_amount),
                    color = JTColors.mediationText,
                    fontFamily = JtBtoFonts.getInterRegular()
                )
            },
            leadingIcon = {
                Image(painter = painterResource(Res.drawable.dollar_ico),
                    contentDescription = "")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(0.95f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE9E9E9),
                unfocusedBorderColor = Color(0xFFE9E9E9),
                focusedContainerColor = Color(0xFFFCFCFC),
                unfocusedContainerColor =  Color(0xFFFCFCFC),
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )


        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(Res.string.note),
                fontSize = 14.sp,
                color = Color(0xFFEF833A),
                fontFamily = JtBtoFonts.getInterSemiBoldFont()
            )
            Text(
                text = stringResource(Res.string.refund_amount_note),
                fontSize = 14.sp,
                color = Color(0xFF707070),
                fontFamily = JtBtoFonts.getInterRegular()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                bookingViewModel.raiseMediation(
                    bookingId = bookingId ,
                    mediationReason = uiState.mediationReason?:"",
                    mediationAmount = uiState.mediationAmount?:""
                )
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.95f),
            colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor)
        ){
            Text(
                text = stringResource(Res.string.submit),
                color = JTColors.WhiteColor,
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
        }
    }
    if (uiState.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiState.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 2000L,
            onDismiss = {
                navigateBack()
                bookingViewModel.clearMessage()
            }
        )
    }
}