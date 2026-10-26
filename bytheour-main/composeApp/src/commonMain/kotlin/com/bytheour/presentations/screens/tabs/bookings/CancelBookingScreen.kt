package com.bytheour.presentations.screens.tabs.bookings

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.cancel_booking
import bytheour.composeapp.generated.resources.cancel_reservation
import bytheour.composeapp.generated.resources.confirm
import bytheour.composeapp.generated.resources.enter_reasons
import bytheour.composeapp.generated.resources.other_reasons
import com.bytheour.data.response.CancelReasonsResponse
import com.bytheour.utils.JTColors
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.calculateHourType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelBookingReasonsScreen(
    viewModel: CancelBookingViewModel = getKoinViewModel(),
    navigateBack:()-> Unit,
    id :String,
    cancelAfterStartTime :String,
    providerStartTime :String,
    date :String
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getCancelReasons()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .imePadding()
                .background(color = JTColors.CancelScreenBackGroundColor)
        ) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor),
                title = {
                    JtBtoCommonText(
                        text = stringResource(Res.string.cancel_booking),
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
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CancelReasonDialog(
                    reasons = uiState.reasons,
                    selectedReasonId = uiState.reasonId,
                    onReasonSelected = { viewModel.selectReason(it) }
                )

                if (uiState.selectedReason.equals(
                        stringResource(Res.string.other_reasons),
                        ignoreCase = true
                    )
                ) {
                    OutlinedTextField(
                        value = uiState.otherReasonInput,
                        onValueChange = { viewModel.onOtherReasonInputChanged(it) },
                        placeholder = {
                            Text(
                                stringResource(Res.string.enter_reasons),
                                color = JTColors.blackColor
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(100.dp)
                            .padding(top = 16.dp),
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
                val reasonText = if (uiState.selectedReason.equals(
                        stringResource(Res.string.other_reasons),
                        ignoreCase = true
                    )
                ) {
                    uiState.otherReasonInput
                } else {
                    uiState.selectedReason
                }

                val hourType = calculateHourType(timeString = providerStartTime, dateString = date)
                println("StartTime and Date ======================$providerStartTime  $date")
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {

                        viewModel.cancelRequest(
                            reasonId = uiState.reasonId ?: "",
                            bookingId = id,
                            cancelAfterStartTime = cancelAfterStartTime,
                            hourType = hourType.toString(),
                            text = reasonText
                        )
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(0.95f),
                    colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor)
                ) {
                    Text(
                        text = stringResource(Res.string.confirm)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        if (uiState.message?.isNotEmpty() == true){
            CommonSnackbar(
                message = uiState.message?:"",
                type = SnackbarType.SUCCESS,
                autoDismissMillis = 2000,
                onDismiss = {
                    navigateBack()
                    viewModel.clearSuccess()
                }
            )
        }
        if (uiState.errorMessage?.isNotEmpty() == true){
            CommonSnackbar(
                message = uiState.errorMessage?:"",
                type = SnackbarType.ERROR,
                autoDismissMillis = 2000,
                onDismiss = {
                    navigateBack()
                    viewModel.clearError()
                }
            )
        }
    }
}

@Composable
fun CancelReasonDialog(
    reasons: List<CancelReasonsResponse.Data>,
    selectedReasonId: String?,
    onReasonSelected: (CancelReasonsResponse.Data) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(JTColors.WhiteColor, shape = RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.background(color = JTColors.CountBackGroundColor)){
            Text(
                text = stringResource(Res.string.cancel_reservation),
                fontSize = 14.sp,
                color = JTColors.lightGrayColor,
                modifier = Modifier.padding(16.dp)
            )
        }

        reasons.forEachIndexed { index, reason ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onReasonSelected(reason) }
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reason.reason,
                    color = JTColors.BackGroundBlueColor,
                    fontSize = 16.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold()
                )

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedReasonId == reason.id) Color(0xFF4CAF50) else Color(0xFFF0F0F0),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedReasonId == reason.id) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            if (index < reasons.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFEAEAEA)
                )
            }
        }
    }
}




