package com.bytheour.presentations.screens.tabs.moreTab.transactionHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.booked_hours
import bytheour.composeapp.generated.resources.bookingId
import bytheour.composeapp.generated.resources.convenience_fee
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.extra_hours
import bytheour.composeapp.generated.resources.hours_details
import bytheour.composeapp.generated.resources.payment_Status
import bytheour.composeapp.generated.resources.payment_mode
import bytheour.composeapp.generated.resources.service_provider
import bytheour.composeapp.generated.resources.summary
import bytheour.composeapp.generated.resources.time
import bytheour.composeapp.generated.resources.total_amount
import bytheour.composeapp.generated.resources.total_hours
import bytheour.composeapp.generated.resources.transaction_History
import bytheour.composeapp.generated.resources.transaction_id
import bytheour.composeapp.generated.resources.withdraw_Status
import bytheour.composeapp.generated.resources.withdraw_request
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHiStoryScreen(
    viewModel: TransactionHistoryScreenViewModel = getKoinViewModel(),
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }
    var selectedTransactionId by remember { mutableStateOf<String?>(null) }
    var showSummarySheet by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.fetchTransactionList()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier.fillMaxSize()
                .background(color = JTColors.enableButton)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            isBackArrowClicked = true
                            onBackClick()
                        },
                        enabled = !isBackArrowClicked,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = JTColors.WhiteColor
                        )
                    }

                    JtBtoCommonText(
                        stringResource(Res.string.transaction_History),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp,
                        color = JTColors.WhiteColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }

            // Main Transaction List with Pagination
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = JTColors.WhiteColor)
            ) {
                itemsIndexed(uiState.transactions) { index, transaction ->
                    TransactionItemView(
                        transaction,
                        onClick = {
                            selectedTransactionId = transaction.booking
                            showSummarySheet = true
                            coroutine.launch {
                                state.show()
                            }
                        }
                    )

                    // Handle pagination (load more when last item is reached)
                    if (
                        index == uiState.transactions.lastIndex &&
                        uiState.hasMoreData && !uiState.isLoadingMore
                    ) {
                        LaunchedEffect(transaction.id) {
                            viewModel.loadMoreTransactions()
                        }
                    }
                }


                // Show "Loading More" indicator
                if (uiState.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }

        if (selectedTransactionId != null) {
            ModalBottomSheet(
                onDismissRequest = {coroutine.launch {
                    state.hide()
                    showSummarySheet = false
                    selectedTransactionId= null
                } },
                sheetState = state,
                containerColor = Color.White,
                scrimColor = Color.Black.copy(alpha = 0.6f)
            ) {
                selectedTransactionId?.let { id ->
                    SummaryTransaction(
                        bookingId = id,
                        onDismiss = {
                            coroutine.launch {
                                state.hide()
                                showSummarySheet = false
                                selectedTransactionId = null
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryTransaction(
    bookingId: String,
    viewModel: TransactionDetailScreenViewModel = getKoinViewModel(),
    onDismiss: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookingId) {
        viewModel.fetchBookingDetails(bookingId)
    }

        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.summary),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.RadioText,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDismiss() }
                )
            }

            SectionTitle(uiState.serviceTitle)
            SummaryItem(stringResource(Res.string.bookingId), uiState.bookingId, valueColor = JTColors.blueId)
            SummaryItem(stringResource(Res.string.service_provider), uiState.serviceProviderName)
            SummaryItem(stringResource(Res.string.date), uiState.date)
            SummaryItem(stringResource(Res.string.time), uiState.time)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.fillMaxWidth(0.9f))
            Spacer(modifier = Modifier.height(16.dp))

            // Hours Details Section
            SectionTitle(stringResource(Res.string.hours_details))

            SummaryItem(stringResource(Res.string.booked_hours), uiState.bookedHours.toString())

            SummaryItem(stringResource(Res.string.extra_hours), uiState.extraHours.toString())

            Spacer(modifier = Modifier.height(8.dp))


            SummaryItemBold(stringResource(Res.string.total_hours), uiState.totalHours.toString())

            Spacer(modifier = Modifier.height(16.dp))

            // Bill Details Section
            SectionTitle(stringResource(Res.string.bill_details))

            SummaryItem(stringResource(Res.string.payment_mode), uiState.paymentMethod)

            SummaryItem(stringResource(Res.string.amount), "$${uiState.amount}", valueFontFamily = JtBtoFonts.getInterBoldFont())

            SummaryItem(stringResource(Res.string.convenience_fee), "$${uiState.convenienceFee}",valueFontFamily = JtBtoFonts.getInterBoldFont())

            // Bottom Total Amount Bar
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JTColors.enableButton)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = stringResource(Res.string.total_amount),
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    val amount = uiState.amount.takeIf { it != 0.0 } ?: 200.0
                    val feePercent = uiState.feePercent.takeIf { it != 0.0 } ?: 5.0
                    val fee = (amount * feePercent / 100).toInt()
                    val subtotal = amount + fee
                    Text(
                        text = " $$subtotal",
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

            }
        }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontFamily = JtBtoFonts.getInterBoldFont(),
        fontSize = 15.sp,
        color = JTColors.enableButton,
        modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 6.dp)
    )
}

@Composable
fun SummaryItem(label: String, value: String,
                labelFontFamily: FontFamily = JtBtoFonts.getInterMediumFont(),
                valueFontFamily: FontFamily = JtBtoFonts.getInterMediumFont(),
                labelColor: Color = JTColors.summary_text,
                valueColor: Color = JTColors.textFieldTextColor,) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = labelColor,
            fontFamily = labelFontFamily
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = valueColor,
            fontFamily = valueFontFamily
        )
    }
}

@Composable
fun SummaryItemBold(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = JTColors.summary_grey)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont()
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value, fontSize = 14.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont()
            )
        }
    }
}

@Composable
fun TransactionItemView(transaction: TransactionItem, onClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth()
        .background(color = JTColors.WhiteColor)
        .padding(horizontal = 16.dp, vertical = 10.dp)
        .border(
            1.dp, color = JTColors.notification_border.copy(5f),
            shape = RoundedCornerShape(12.dp)
        )
        .clickable { onClick() }
    ) {
        Column {
            Column(modifier = Modifier.padding(12.dp)) {
                Row {
                    Text(
                        text = transaction.date, // show formatted date here (e.g. "22 Jul, 2025")
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = JTColors.blackLight,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(JTColors.timeSlotCardColor, shape = RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val positive = transaction.amount > 0
                    val amountText =
                        if (positive) "+$${transaction.amount}" else "$${transaction.amount}"
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (positive) JTColors.greenSuccess else JTColors.redAmount,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = amountText,
                            color = Color.White,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 13.sp
                        )
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min), //  ensures Row height is based on tallest child
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (transaction.transactionsType != "withdrawal-request") {
                            Text(
                                text = stringResource(Res.string.transaction_id),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                color = JTColors.transId,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                            Text(
                                text = transaction.id,
                                color = JTColors.blueId,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.withdraw_request),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                color = JTColors.transId,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                            Text(
                                text = "$${transaction.amount}",
                                color = JTColors.blueId,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(0.9f), // now Row has real height
                        color = JTColors.notification_border,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        if (transaction.transactionsType != "withdrawal-request") {
                            Text(
                                text = stringResource(Res.string.payment_Status),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                color = JTColors.transId,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.withdraw_Status),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                color = JTColors.transId,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                        }

                        Text(
                            text = transaction.status,
                            color = JTColors.greenSuccess,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont()
                        )
                    }


                }

                Spacer(modifier = Modifier.height(8.dp))

            }
            if (transaction.transactionsType != "withdrawal-request") {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = JTColors.timeSlotCardColor)
                ) {
                    Text(
                        text = transaction.message,
                        fontSize = 12.sp,
                        color = JTColors.paidBooking,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

        }

    }
}