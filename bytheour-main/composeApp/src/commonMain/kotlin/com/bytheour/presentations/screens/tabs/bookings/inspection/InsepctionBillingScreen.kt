package com.bytheour.presentations.screens.tabs.bookings.inspection

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.billing_summary
import bytheour.composeapp.generated.resources.pay
import bytheour.composeapp.generated.resources.total_amount
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.bookings.PayCard
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionBillingSummaryScreen(
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    navigateBack: () -> Unit,
    address:String,
    title:String,
    image:String,
    bookingId :String,
    navigateNotInspectionRequestSent:(String,String,String,String)-> Unit
) {
    LaunchedEffect(Unit) {
        bookingViewModel.fetchBookingDetails(bookingId, true)
    }

    val uiState by bookingViewModel.uiStates.collectAsState()
    val bookingData by bookingViewModel.bookingData.collectAsState()
    val amount = bookingData.isInspectionRate ?: 0.0


    if (uiState.navigateBack){
        navigateNotInspectionRequestSent(address,title,image,bookingId)
    }
    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.billing_summary),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 16.sp,
                        color = JTColors.WhiteColor
                    )
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(Res.drawable.arrowleft),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                navigateBack()
                            }
                            .padding(start = 10.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Inspection Details",
                        fontFamily = JtBtoFonts.getInterBoldFont(),
                        fontSize = 16.sp,
                        color = JTColors.BackGroundBlueColor,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.amount),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF6F6F6F)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "$${amount}",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                            .padding(start = 10.dp, end = 10.dp)
//                    ) {
//                        Text(
//                            text = "percentageAmount($feePercent %)",
//                            fontFamily = JtBtoFonts.getInterMediumFont(),
//                            fontSize = 14.sp,
//                            color = Color(0xFF6F6F6F)
//                        )
//
//                        Spacer(modifier = Modifier.weight(1f))
//
//                        Text(
//                            text = "$$fee",
//                            fontFamily = JtBtoFonts.getInterMediumFont(),
//                            fontSize = 14.sp,
//                            color = Color(0xFF1C1C1C)
//                        )
//                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(Res.string.bill_details),
                        fontFamily = JtBtoFonts.getInterBoldFont(),
                        fontSize = 15.sp,
                        color = JTColors.BackGroundBlueColor,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(JTColors.summary_grey)
                    ) {
                        Text(
                            text = stringResource(Res.string.total_amount),
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            color = JTColors.BackGroundBlueColor,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "$${amount}",
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            color = JTColors.BackGroundBlueColor,
                            modifier = Modifier.padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PayCard(selectedCard =  uiState.cardNumber, onSelect = {
                        bookingViewModel.onSelectCard()
                    })
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        bookingViewModel.onPayNow()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JTColors.BackGroundBlueColor
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.pay),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 13.sp,
                        color = JTColors.WhiteColor,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}