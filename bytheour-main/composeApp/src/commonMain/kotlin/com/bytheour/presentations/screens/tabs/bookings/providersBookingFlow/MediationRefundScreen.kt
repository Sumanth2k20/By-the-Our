package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.card_select_ico
import bytheour.composeapp.generated.resources.mediation_refund_decline
import bytheour.composeapp.generated.resources.mediation_refund_decline_description
import bytheour.composeapp.generated.resources.mediation_refund_half_amount
import bytheour.composeapp.generated.resources.mediation_refund_reason_label
import bytheour.composeapp.generated.resources.mediation_refund_reason_placeholder
import bytheour.composeapp.generated.resources.mediation_refund_select_refund
import bytheour.composeapp.generated.resources.mediation_refund_submit
import bytheour.composeapp.generated.resources.mediation_refund_topbar_title
import com.bytheour.utils.JTColors
import com.bytheour.utils.Decline
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.service_amount
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediationRefundingScreen(
    viewModel: MediationRefundViewModelProvider = getKoinViewModel(),
    provider: JTProviderBookingTabScreenViewmodel = getKoinViewModel(),
    navigateBack: () -> Unit,
    bookingId: String,
    amount: String,
    total: String,
) {
    val state by viewModel.uiState.collectAsState()

    val parsedAmount = amount.toDoubleOrNull() ?: 0.0
    val parsedTotal = total.toDoubleOrNull() ?: 0.0
    val isHalfOptionVisible = parsedAmount >= parsedTotal * 0.5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFFE9E9E9))
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.mediation_refund_topbar_title),
                    color = Color.White,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 16.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF3A1C78))
        )

        Text(
            text = stringResource(Res.string.mediation_refund_select_refund),
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF767676),
            fontFamily = JtBtoFonts.getInterMediumFont(),
            fontSize = 13.sp
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {

                RefundOptionItem(
                    option = "$$amount",
                    description = null,
                    isSelected = state.selectedOption == "$$amount",
                    isEnabled = true,
                    onClick = { viewModel.onOptionSelected("$$amount") }
                )

                HorizontalDivider(Modifier.fillMaxWidth(), color = Color(0XFFE9E9E9))

                if (isHalfOptionVisible) {
                    RefundOptionItem(
                        option = stringResource(Res.string.mediation_refund_half_amount),
                        description = null,
                        isSelected = state.selectedOption == stringResource(Res.string.mediation_refund_half_amount),
                        isEnabled = true,
                        onClick = { viewModel.onOptionSelected(service_amount) }
                    )

                    HorizontalDivider(Modifier.fillMaxWidth(),color = Color(0XFFE9E9E9))
                }

                RefundOptionItem(
                    option = stringResource(Res.string.mediation_refund_decline),
                    description = stringResource(Res.string.mediation_refund_decline_description),
                    isSelected = state.selectedOption == stringResource(Res.string.mediation_refund_decline),
                    isEnabled = true,
                    onClick = { viewModel.onOptionSelected(Decline) }
                )

                HorizontalDivider(Modifier.fillMaxWidth(), color = Color(0XFFE9E9E9))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.mediation_refund_reason_label),
                    color = Color(0xFF767676),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 13.sp
                )

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0XFFFCFCFC),
                        unfocusedContainerColor = Color(0XFFFCFCFC),
                        focusedBorderColor = Color(0XFFE9E9E9),
                        unfocusedBorderColor = Color(0XFFE9E9E9)
                    ),
                    value = state.reasonText,
                    onValueChange = viewModel::onReasonChanged,
                    placeholder = {
                        Text(
                            stringResource(Res.string.mediation_refund_reason_placeholder),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 13.sp
                        )
                                  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 5,
                    isError = state.reasonTextError != null,
                    supportingText = {
                        state.reasonTextError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (!viewModel.validate()) return@Button

                val refundAmount = when (state.selectedOption) {
                    "$$amount" -> parsedAmount.toInt()
                    service_amount -> parsedAmount - (parsedAmount * 0.5)
                    Decline -> 0.0
                    else -> 0.0
                }

                viewModel.onAcceptMediation(
                    bookingId = bookingId,
                    mediationAmount = refundAmount.toString(),
                    mediationReason = state.reasonText,
                    onSuccess = {
                        navigateBack()
                        provider.loadClosedBookings(limit = PAGINATION_LIMIT.toString(), offset = 0, isInitialLoad = false)
                    }
                )
            },

            enabled = state.selectedOption.isNotBlank() && state.reasonText.trim().isNotEmpty() && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A1C78)),
            shape = RoundedCornerShape(50),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = stringResource(Res.string.mediation_refund_submit),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun RefundOptionItem(
    option: String,
    description: String? = null,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option,
                color = if (isEnabled) Color(0xFF301E69) else Color.Gray,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 13.sp
            )
            description?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    fontSize = 10.sp
                )
            }
        }

        if (isSelected) {
            Image(
                painter = painterResource(Res.drawable.card_select_ico),
                contentDescription = null
            )
        }else{
            Box(modifier = Modifier.clip(CircleShape)
                .background(color = JTColors.notification_border)
                .size(18.dp))
        }
    }
}


