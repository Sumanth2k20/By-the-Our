package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.cancel_booking_confirm
import bytheour.composeapp.generated.resources.cancel_booking_reason_other
import bytheour.composeapp.generated.resources.cancel_booking_reason_placeholder
import bytheour.composeapp.generated.resources.cancel_booking_title
import bytheour.composeapp.generated.resources.other_reasons
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.CancelProviderBookingViewModel
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.JTProviderBookingTabScreenViewmodel
import com.bytheour.utils.CANCEL_AFTER_START_TIME
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelBookingReasonsScreen(
    viewModel: CancelProviderBookingViewModel = getKoinViewModel(),
    proBookingViewModel: JTProviderBookingTabScreenViewmodel = getKoinViewModel(),
    navigateBack: () -> Unit,
    id: String
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCancelReasons()
    }
Box {
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFE5E5E5))
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor),
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.cancel_booking_title),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 15.sp,
                    fontWeight = W700,
                    lineHeight = 12.sp,
                    color = JTColors.WhiteColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier.clickable { navigateBack() }
                        .padding(start = 16.dp, end = 8.dp).size(17.dp)
                )
            },
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CancelReasonDialog(
                reasons = uiState.reasons,
                selectedReasonId = uiState.reasonId,
                onReasonSelected = { viewModel.selectReason(it) }
            )

            if (uiState.selectedReason.equals(
                    stringResource(Res.string.cancel_booking_reason_other),
                    ignoreCase = true
                )
            ) {
                OutlinedTextField(
                    value = uiState.otherReasonInput,
                    onValueChange = { viewModel.onOtherReasonInputChanged(it) },
                    placeholder = {
                        Text(
                            stringResource(Res.string.cancel_booking_reason_placeholder),
                            color = JTColors.blackColor
                        )
                    },
                    supportingText = {
                        if (uiState.otherReasonError != null) {
                            Text(
                                text = uiState.otherReasonError ?: "",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(100.dp)
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = JTColors.WhiteColor,
                        unfocusedBorderColor = JTColors.WhiteColor,
                        cursorColor = JTColors.blackColor,
                        focusedContainerColor = JTColors.WhiteColor,
                        unfocusedContainerColor = JTColors.WhiteColor,
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            val reasonText = if (uiState.selectedReason.equals(
                    stringResource(Res.string.other_reasons),
                    ignoreCase = true
                )
            ) {
                uiState.otherReasonInput
            } else {
                uiState.selectedReason
            }

            Button(

                onClick = {
                    val isValid = viewModel.validateOtherReason()
                    if (!isValid) return@Button

                    viewModel.cancelRequest(
                        reasonId = uiState.reasonId ?: "",
                        bookingId = id,
                        cancelAfterStartTime = CANCEL_AFTER_START_TIME,
                        hourType = null,
                        text = reasonText,
                        onSucess = {
                            proBookingViewModel.onTabSelected(2)
                            navigateBack()
                        }
                    )
                },

                modifier = Modifier
                    .height(50.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(0.95f),
                colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor)
            ) {
                Text(
                    text = stringResource(Res.string.cancel_booking_confirm)
                )
            }
        }
    }
    if (uiState.message?.isNotEmpty() == true){
        Box(modifier = Modifier.padding(bottom = 65.dp)) {
            CommonSnackbar(
                message = uiState.message ?: "",
                type = SnackbarType.ERROR,
                autoDismissMillis = 1500,
                onDismiss = {


                }
            )
        }
    }
}
}






