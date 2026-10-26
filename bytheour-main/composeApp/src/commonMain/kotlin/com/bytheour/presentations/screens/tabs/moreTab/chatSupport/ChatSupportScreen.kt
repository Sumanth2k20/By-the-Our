package com.bytheour.presentations.screens.tabs.moreTab.chatSupport

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.calendar_ico
import bytheour.composeapp.generated.resources.chat_Support
import bytheour.composeapp.generated.resources.close_Ticket
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.issue_with_booking
import bytheour.composeapp.generated.resources.no_tickets
import bytheour.composeapp.generated.resources.ticket
import bytheour.composeapp.generated.resources.ticket_ico
import bytheour.composeapp.generated.resources.view_Details
import bytheour.composeapp.generated.resources.view_all_tickets
import coil3.compose.AsyncImage
import com.bytheour.data.request.CloseSupportTicketRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.SupportData
import com.bytheour.utils.JTColors
import com.bytheour.showProgress
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSupportScreen(
    viewModel: ChatSupportScreenViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    navigateToViewAllTickets: () -> Unit,
    navigateDetailsScreen: (String) -> Unit,
    navigateToChatScreen:(String) ->Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getSupportTicket(ServiceListRequest(limit = "10", offset = "0"))
    }
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState()),
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.chat_Support),
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

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val requestedTickets = uiState.ticket?.filter {
                it.status.equals("Requested", ignoreCase = true)
            }.orEmpty()

            if (!showProgress.value && requestedTickets.isEmpty()) {
                Text(
                    text = stringResource(Res.string.no_tickets),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                )
            } else {
                requestedTickets.forEach { supportData ->
                    SupportTicketCard(
                        supportData = supportData,
                        onViewDetails = { navigateDetailsScreen(supportData.booking.id) },
                        onIssueWithBooking = {
                            navigateToChatScreen(supportData.id)
                        },
                        onCloseTicket = {
                            viewModel.closeSupportTicket(
                                payload = CloseSupportTicketRequest(
                                    supportId = supportData.id
                                ),
                                onResult = {
                                    viewModel.getSupportTicket(
                                        ServiceListRequest(limit = "10", offset = "0")
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navigateToViewAllTickets() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor)
        ) {
            Text(stringResource(Res.string.view_all_tickets), color = Color.White)
        }
    }
}

@Composable
fun SupportTicketCard(
    supportData: SupportData,
    onViewDetails: (SupportData) -> Unit,
    onIssueWithBooking: (SupportData) -> Unit,
    onCloseTicket: (SupportData) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
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
                        model = supportData.booking.service.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = supportData.booking.service.title,
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        color = JTColors.BackGroundBlueColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = supportData.booking.service.id,
                        fontFamily = JtBtoFonts.getInterFont(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = JTColors.lightGrayColor
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        JTColors.timeSlotCardColor,
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.calendar_ico),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(Res.string.date),
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.grayColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatDateString(supportData.booking.date,true),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.blackColor,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE5E7EB))
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ticket_ico),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Ticket",
                            fontFamily = JtBtoFonts.getInterFont(),
                            color = JTColors.lightGrayColor,
                            fontSize = 10.sp,
                            lineHeight = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = supportData.status,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.ChatSupportStatusColor,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onViewDetails(supportData) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(1.dp, Color(0xFF3BA4D8)),
                ) {
                    Text(stringResource(Res.string.view_Details),
                        color = Color(0xFF3BA4D8),
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }

                Button(
                    onClick = { onCloseTicket(supportData) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF25454))
                ) {
                    Text(stringResource(Res.string.close_Ticket),
                        color = JTColors.WhiteColor,
                                fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEFEF))
                    .clickable { onIssueWithBooking(supportData) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.issue_with_booking),
                    color = Color(0xFFF25454),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFF25454))
            }
        }
    }
}



@Composable
fun ClosedTicketCard(
    supportData: SupportData,
    onViewDetails: (SupportData) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
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
                            model = supportData.booking.service.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }


                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = supportData.booking.service.title,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            color = JTColors.BackGroundBlueColor
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = supportData.booking.service.id,
                            fontFamily = JtBtoFonts.getInterFont(),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = JTColors.lightGrayColor
                        )
                    }
                }

                OutlinedButton(
                    onClick = { onViewDetails(supportData) },
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(1.dp, Color(0xFF3BA4D8)),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        stringResource(Res.string.view_Details),
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color(0xFF3BA4D8),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                       JTColors.timeSlotCardColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.calendar_ico),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(Res.string.date),
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.grayColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatDateString(supportData.booking.date,true),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.blackColor,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE5E7EB))
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ticket_ico),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = stringResource(Res.string.ticket),
                            fontFamily = JtBtoFonts.getInterFont(),
                            color = JTColors.lightGrayColor,
                            fontSize = 10.sp,
                            lineHeight = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = supportData.status,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.ChatSupportStatusColor,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }
    }
}