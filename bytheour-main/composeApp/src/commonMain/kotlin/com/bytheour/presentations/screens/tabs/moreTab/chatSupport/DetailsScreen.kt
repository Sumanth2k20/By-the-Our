package com.bytheour.presentations.screens.tabs.moreTab.chatSupport

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.address
import bytheour.composeapp.generated.resources.address_ico1
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.avg
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.billing_summary
import bytheour.composeapp.generated.resources.booked_Service
import bytheour.composeapp.generated.resources.booked_hours
import bytheour.composeapp.generated.resources.booking_id_out
import bytheour.composeapp.generated.resources.calendar_ico
import bytheour.composeapp.generated.resources.completed_Booking_Details
import bytheour.composeapp.generated.resources.convenience_fee
import bytheour.composeapp.generated.resources.customer_Support
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.directions_ico
import bytheour.composeapp.generated.resources.extra_hours
import bytheour.composeapp.generated.resources.honesty_rating
import bytheour.composeapp.generated.resources.hours_details
import bytheour.composeapp.generated.resources.paymentMethod
import bytheour.composeapp.generated.resources.phone_call
import bytheour.composeapp.generated.resources.poor
import bytheour.composeapp.generated.resources.providerverified
import bytheour.composeapp.generated.resources.service_Rating
import bytheour.composeapp.generated.resources.service_provider
import bytheour.composeapp.generated.resources.services
import bytheour.composeapp.generated.resources.status
import bytheour.composeapp.generated.resources.summary
import bytheour.composeapp.generated.resources.time
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.time_slot
import bytheour.composeapp.generated.resources.total_Working_Hours
import bytheour.composeapp.generated.resources.total_amount
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateBack: () -> Unit,
    bookingId: String,
    viewModel: ChatSupportScreenViewModel = getKoinViewModel(),
    navigateToChatScreen:(String) ->Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }
    var showBillingSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getBookingDetails(bookingId)
    }

    if (showBillingSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBillingSheet = false },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            dragHandle = null
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            BillingSummaryContent(
                serviceName = uiState.serviceName,
                bookingId = uiState.bookingId,
                providerName = uiState.providerName,
                date = uiState.date ?: "",
                time = uiState.time ?: "",
                bookedHours = uiState.bookedHours ?: "0",
                extraHours = uiState.extraHours.toString(),
                paymentMethod = uiState.paymentMethod ?: "",
                amount = uiState.amount ?: "0",
                convenienceFee = uiState.convenienceFee ?: "0",
                onDismiss = { showBillingSheet = false }
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(JTColors.WhiteColor)
    ){
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.completed_Booking_Details),
                    color = JTColors.WhiteColor,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 15.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    isBackArrowClicked = true
                    navigateBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
        )
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(JTColors.timeSlotCardColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(Res.string.booked_Service),
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 13.sp,
                        color = JTColors.BackGroundBlueColor,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(text = stringResource(Res.string.status),
                        fontFamily = JtBtoFonts.getInterFont(),
                        fontSize = 12.sp,
                        color = JTColors.grayColor,
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF50D091), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text( uiState.serviceStatus?:"", color = Color.White, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(Res.string.services),
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            color = JTColors.grayColor
                        )
                        Text(
                            text = uiState.serviceName,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            fontSize = 13.sp,
                            lineHeight = 13.sp
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(Color(0xFFE0E0E0)) // light gray divider
                    )

                    // Booking ID Section
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.booking_id_out),
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            color = JTColors.grayColor
                        )
                        Text(
                            uiState.bookingId,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            color = JTColors.blueId
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                HorizontalDivider(color = JTColors.timeSlotCardColor, thickness = 5.dp, modifier = Modifier.fillMaxWidth(0.9f))
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.service_provider),
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                color = JTColors.BackGroundBlueColor,
                modifier = Modifier.padding(start = 12.dp),
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = CircleShape
                            )
                            .padding(3.dp)
                    ) {
                        AsyncImage(
                            model = uiState.serviceImage,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(Modifier.width(8.dp))
                    Column{
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                uiState.providerName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.width(4.dp))
                            Image(
                                painter = painterResource(Res.drawable.providerverified),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.phone_call),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                uiState.providerPhone ?: "N/A",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row {
                                    Text(stringResource(Res.string.service_Rating), style = MaterialTheme.typography.bodySmall)
                                    Text(stringResource(Res.string.poor), color = Color(0xFF512DA8), style = MaterialTheme.typography.bodySmall)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RatingBar(rating = 2f)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0xFFE0E0E0))
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                                    .padding(start = 5.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row {
                                    Text(stringResource(Res.string.honesty_rating), style = MaterialTheme.typography.bodySmall)
                                    Text(stringResource(Res.string.avg), color = Color(0xFF512DA8), style = MaterialTheme.typography.bodySmall)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RatingBar(rating = 3f)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(JTColors.timeSlotCardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.calendar_ico),
                            contentDescription = "calendar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(stringResource(Res.string.date), style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                            Text(
                                text = formatDateString(uiState.date?:""),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Row(modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(Res.drawable.time_ico),
                            contentDescription = "time",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(stringResource(Res.string.time_slot), style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                            Text(
                                text = uiState.time?:"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Address
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.address),
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                color = JTColors.BackGroundBlueColor,
                modifier = Modifier.padding(start = 12.dp),
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(Res.drawable.address_ico1),
                        contentDescription = ""
                    )
                    Text(
                        text = uiState.address?:"",
                        fontFamily = JtBtoFonts.getInterFont(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF313131),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
                IconButton(onClick = {  }) {
                    Image(
                        painter = painterResource(Res.drawable.directions_ico),
                        contentDescription = ""
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        showBillingSheet = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D1D77)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        stringResource(Res.string.billing_summary),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }

                Button(
                    onClick = {
                        val firstTicketId = uiState.supportBookings
                            ?.firstOrNull()?.support?.ticket?.id ?: ""
                        navigateToChatScreen(firstTicketId)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        stringResource(Res.string.customer_Support),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }
            }
        }
    }

}



@Composable
fun BillingSummaryContent(
    serviceName: String,
    bookingId: String,
    providerName: String,
    date: String,
    time: String,
    bookedHours: String,
    extraHours: String,
    paymentMethod: String,
    amount: String,
    convenienceFee: String,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.summary),
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = JTColors.blackColor,
                modifier = Modifier
                    .size(25.dp)
                    .clickable { onDismiss() },
            )
        }
        Spacer(Modifier.height(8.dp))

        Text(
            text = serviceName,
            fontSize = 15.sp,
            fontFamily = JtBtoFonts.getInterBoldFont()
        )

        Spacer(Modifier.height(4.dp))
        SummaryRow(label = stringResource(Res.string.booking_id_out), value = bookingId, highlight = true)
        SummaryRow(label = stringResource(Res.string.service_provider), value = providerName)
        SummaryRow(label = stringResource(Res.string.date), value = date)
        SummaryRow(label = stringResource(Res.string.time), value = time)

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.hours_details),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF3D1D77)
        )
        Spacer(Modifier.height(4.dp))
        SummaryRow(label = stringResource(Res.string.booked_hours), value = bookedHours)
        SummaryRow(label = stringResource(Res.string.extra_hours), value = extraHours)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 8.dp)
        ) {
            val totalHours =
                (bookedHours.toDoubleOrNull() ?: 0.0) + (extraHours.toDoubleOrNull() ?: 0.0)
            SummaryRow(
                label = stringResource(Res.string.total_Working_Hours),
                value = totalHours.toString()
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.bill_details),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF3D1D77)
        )

        val amountValue = amount.toDoubleOrNull() ?: 0.0
        val feeValue = convenienceFee.toDoubleOrNull() ?: 0.0
        val fee = (amountValue * feeValue / 100)

        Spacer(Modifier.height(4.dp))
        SummaryRow(label = stringResource(Res.string.paymentMethod), value = paymentMethod)
        SummaryRow(label = stringResource(Res.string.amount), value = "$$amount")
        SummaryRow(label = stringResource(Res.string.convenience_fee), value = "$$fee")

        Spacer(Modifier.height(12.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3D1D77))
            .padding(12.dp)
    ) {
        val amountValue = amount.toDoubleOrNull() ?: 0.0
        val feeValue = convenienceFee.toDoubleOrNull() ?: 0.0
        val fee = (amountValue * feeValue / 100)
        val subtotal = amountValue + fee

        Text(
            text = stringResource(Res.string.total_amount) +"$${subtotal}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
private fun SummaryRow(label: String, value: String, highlight: Boolean = false, shaded: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (shaded) Modifier.background(Color(0xFFF2F2F2))
                else Modifier
            )
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = JTColors.summary_text,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = if (highlight) JTColors.blueId else JTColors.blackColor,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )
    }
}


@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier) {
        repeat(5) { index ->
            val tint = if (index < rating) Color(0xFFFFC107) else Color.Gray
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "${rating.toInt()}/5",
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}



