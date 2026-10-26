package com.bytheour.presentations.screens.tabs.moreTab.chatSupport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.all_supports
import bytheour.composeapp.generated.resources.calendar_ico
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.issue_with_booking
import bytheour.composeapp.generated.resources.other_bookings
import bytheour.composeapp.generated.resources.paid_amount
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.time_slot
import bytheour.composeapp.generated.resources.view_Details
import bytheour.composeapp.generated.resources.view_all_tickets
import bytheour.composeapp.generated.resources.working_hours_including_extra
import coil3.compose.AsyncImage
import com.bytheour.data.request.AddSupportTicketRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings.TabItem
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTicketsScreen(
    navigateBack: () -> Unit,
    chatSupportScreenViewModel: ChatSupportScreenViewModel = getKoinViewModel(),
    navigateDetailsScreen: (String) -> Unit,
    navigateToChatScreen:(String) ->Unit
) {
    val uiState by chatSupportScreenViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var isBackArrowClicked by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(Res.string.all_supports),
        stringResource(Res.string.other_bookings)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.view_all_tickets),
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
            expandedHeight = 100.dp,
            colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor),
        )

        LaunchedEffect(Unit) {
            val payload = ServiceListRequest(limit = "10", offset = "0")
            chatSupportScreenViewModel.getSupportTicket(payload)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .padding(horizontal = 12.dp)
                .shadow(
                    elevation = 9.dp,
                    shape = RoundedCornerShape(25.dp),
                    clip = false
                )
                .background(
                    color = JTColors.WhiteColor,
                    shape = RoundedCornerShape(25.dp)
                )
                .height(44.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    TabItem(
                        text = title,
                        isSelected = selectedTab == index,
                    ) {
                        selectedTab = index
                        val payload = ServiceListRequest(limit = "10", offset = "0")
                        if (index == 0) {
                            chatSupportScreenViewModel.getSupportTicket(payload)
                        } else {
                            chatSupportScreenViewModel.getSupportBookings(payload)
                        }
                    }
                }
            }
        }
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(uiState.ticket.orEmpty()) { ticket ->
                            when {
                                ticket.status.equals("Requested", ignoreCase = true) -> {
                                    SupportTicketCard(
                                        supportData = ticket,
                                        onViewDetails = { navigateDetailsScreen(ticket.booking.id) },
                                        onIssueWithBooking = {
                                                navigateToChatScreen(ticket.id)
                                        },
                                        onCloseTicket = {
                                            chatSupportScreenViewModel.closeSupportTicket(
                                                payload = com.bytheour.data.request.CloseSupportTicketRequest(
                                                    supportId = ticket.id
                                                ),
                                                onResult = {
                                                    chatSupportScreenViewModel.getSupportTicket(
                                                        ServiceListRequest(limit = "10", offset = "0")
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                                ticket.status.equals("Closed", ignoreCase = true) -> {
                                    ClosedTicketCard(
                                        supportData = ticket,
                                        onViewDetails = { navigateDetailsScreen(ticket.booking.id) }
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        items(uiState.supportBookings.orEmpty()) { booking ->
                            SupportBookingCard1(
                                booking = booking,
                                navigateDetailsScreen = navigateDetailsScreen,
                                onIssueClick = {
                                    chatSupportScreenViewModel.addSupportTicket(
                                        AddSupportTicketRequest(booking.booking.id),
                                        onResult = {
                                            selectedTab = 0
                                            val payload = ServiceListRequest(limit = "10", offset = "0")
                                            chatSupportScreenViewModel.getSupportBookings(payload)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SupportBookingCard1(
    booking: ModelProviderBookingList.Data,
    navigateDetailsScreen: (String) -> Unit,
    onIssueClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 5.dp, top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = booking.service.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape).border(width = 1.dp, color = JTColors.border),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = booking.service.title.replaceFirstChar { it.uppercase() },
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            maxLines = 2,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            color = JTColors.BackGroundBlueColor
                        )
                        Text(
                            text = booking.booking.id,
                            fontSize = 12.sp,
                            color = Color(0xFF868686),
                            modifier = Modifier.padding(top = 2.dp),
                            fontFamily = JtBtoFonts.getInterRegular()
                        )
                    }
                }

                OutlinedButton(
                    onClick = { navigateDetailsScreen(booking.booking.id) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3BA4D8)),
                    border = BorderStroke(1.dp, Color(0xFF3BA4D8)),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.view_Details),
                        fontFamily = JtBtoFonts.getInterFont(),
                        fontSize = 10.sp
                    )
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
                        Icon(
                            painter = painterResource(Res.drawable.calendar_ico),
                            contentDescription = "calendar",
                            tint = JTColors.enableButton,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                stringResource(Res.string.date),
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                color = JTColors.transId,
                                fontSize = 10.sp,
                                lineHeight = 10.sp
                            )
                            Text(
                                text = formatDateString(booking.booking.date, showYear = true),
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                fontSize = 12.sp,
                                lineHeight = 12.sp
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
                        Icon(
                            painter = painterResource(Res.drawable.time_ico),
                            contentDescription = "time",
                            tint = JTColors.enableButton,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                stringResource(Res.string.time_slot),
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                color = JTColors.transId,
                                fontSize = 10.sp,
                                lineHeight = 10.sp
                            )
                            Text(
                                text = booking.booking.time,
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                fontSize = 12.sp,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = booking.customer.fullName,
                modifier = Modifier.padding(start = 10.dp),
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.working_hours_including_extra),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 12.sp
                )
                Text(
                    text = booking.additional.hours?.formatMinHrs ?: "0 Hr",
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.paid_amount),
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    fontSize = 12.sp,
                    color = JTColors.blackColor
                )
                Text(
                    text = "$${booking.bill.amount}",
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    fontSize = 12.sp,
                    color = JTColors.blackColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEFEF))
                    .clickable {onIssueClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.issue_with_booking),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 12.sp,
                    color = Color(0xFFF25454)
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFF25454))
            }
        }
    }
}


