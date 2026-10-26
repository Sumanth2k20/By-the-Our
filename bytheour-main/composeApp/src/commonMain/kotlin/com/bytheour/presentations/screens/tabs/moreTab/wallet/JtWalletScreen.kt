package com.bytheour.presentations.screens.tabs.moreTab.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.available_wallet_balance
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.confirm_withdraw
import bytheour.composeapp.generated.resources.group_14
import bytheour.composeapp.generated.resources.minimum_withdraw
import bytheour.composeapp.generated.resources.on_hold_balance
import bytheour.composeapp.generated.resources.onhold_ico
import bytheour.composeapp.generated.resources.please_withdraw_request
import bytheour.composeapp.generated.resources.transaction_History
import bytheour.composeapp.generated.resources.wallet
import bytheour.composeapp.generated.resources.walletbalance_ico
import bytheour.composeapp.generated.resources.withdraw
import bytheour.composeapp.generated.resources.withdrawal_amount_requested
import bytheour.composeapp.generated.resources.withdrawal_request
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.SummaryTransaction
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionHistoryScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionItem
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionItemView
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: TransactionHistoryScreenViewModel = getKoinViewModel(),
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialogBox by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    var selectedTransactionId by remember { mutableStateOf<String?>(null) }
    var showSummarySheet by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isBackArrowClicked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchTransactionList()
    }
//    Box(modifier = Modifier.fillMaxSize()){
        Column(
            Modifier.fillMaxSize()
                .background(color = JTColors.enableButton)
                .statusBarsPadding()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Top)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            isBackArrowClicked = true
                            onBackClick()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = JTColors.WhiteColor
                        )
                    }

                    JtBtoCommonText(
                        text = (stringResource(Res.string.wallet)),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp,
                        color = JTColors.WhiteColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
            WalletBalanceCard(
                available = uiState.walletAmount?.toDouble() ?: 0.0,
                onHold = uiState.holdAmount?.toDouble() ?: 0.0,
                onClick = {
                    showDialogBox = true
                },
                uiState
            )


            TransactionList(
                transactions = uiState.transactions,
                isLoading = uiState.isLoading,
                onLoadMore = { viewModel.loadMoreTransactions() },
                onTransactionClick = { id ->
                    selectedTransactionId = id
                    showSummarySheet = true
                    coroutine.launch { state.show() }
                }
            )
        }
        if (showDialogBox) {
            Dialog(onDismissRequest = { showDialogBox = false }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.confirm_withdraw),
                            fontFamily = JtBtoFonts.getInterBoldFont(),
                            fontSize = 18.sp,
                            color = JTColors.blackLight
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text =
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                    )
                                ) {
                                    append(stringResource(Res.string.please_withdraw_request))
                                }
                                withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterBoldFont())){
                                    append("${uiState.walletAmount ?: 0.0}")
                                }

                            },
                            fontSize = 14.sp,
                            color = JTColors.blackLight,
                        )


                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { showDialogBox = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    stringResource(Res.string.cancel),
                                    color = JTColors.BlackButtonColor
                                )
                            }

                            Button(
                                onClick = {
                                    coroutine.launch {
                                        viewModel.transactionsStripe()
                                    }
                                    showDialogBox = false
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(stringResource(Res.string.withdraw), color = JTColors.WhiteColor)
                            }
                        }
                    }
                }
            }
        }
        if (uiState.stripeMessage?.isNotEmpty() == true) {
            CommonSnackbar(
                message = uiState.stripeMessage ?: "",
                type = SnackbarType.SUCCESS,
                autoDismissMillis = 1500,
                onDismiss = {
                    viewModel.clearError()
                }
            )
        }
        if (selectedTransactionId != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutine.launch {
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

@Composable
fun WalletBalanceCard(
    available: Double, onHold: Double, onClick: () -> Unit,
    uiState: TransactionHistoryScreenViewModel.TransactionHistoryUiState
) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JTColors.WhiteColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(modifier = Modifier) {
                Image(
                    painter = painterResource(Res.drawable.group_14),
                    contentDescription = "Background Logo",
                    modifier = Modifier
                        .align(Alignment.TopEnd).padding(end = 20.dp)
                        .size(150.dp),
                )
                Column(modifier = Modifier.padding(16.dp)) {

                    Row {
                        Image(
                            painter = painterResource(Res.drawable.walletbalance_ico), contentDescription = "",
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                stringResource(Res.string.available_wallet_balance),
                                color = JTColors.blackLight,
                                fontSize = 14.sp,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                            Text(
                                "$ $available",
                                color = JTColors.enableButton,
                                fontSize = 20.sp,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )
                        }

                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                    Row {
                        Image(
                            painter = painterResource(Res.drawable.onhold_ico), contentDescription = "",
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                stringResource(Res.string.on_hold_balance),
                                color = JTColors.blackLight,
                                fontSize = 14.sp,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                            Text(
                                "$$onHold",
                                color = JTColors.enableButton,
                                fontSize = 20.sp,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )
                        }

                    }

                }

            }
            WithdrawButton(onClick = onClick, uiState = uiState)

        }

    }
}

@Composable
fun WithdrawButton(
    onClick: () -> Unit,
    uiState: TransactionHistoryScreenViewModel.TransactionHistoryUiState
) {

    if (uiState.stripeMessage == stringResource(Res.string.withdrawal_request)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = JTColors.timeSlotCardColor)
        ) {
            Text(

                text =buildAnnotatedString {
                    withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        color = JTColors.enableButton) ){
                        append("${uiState.walletAmount ?: 0.0}")
                    }
                    withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = JTColors.paidBooking)){
                        append(stringResource(Res.string.withdrawal_amount_requested))
                    }

                },
                fontSize = 13.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(stringResource(Res.string.withdraw), color = Color.White)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                stringResource(Res.string.minimum_withdraw),
                fontSize = 12.sp,
                color = JTColors.minWithdraw
            )
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun TransactionList(
    transactions: List<TransactionItem>,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    onTransactionClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(JTColors.CancelScreenBackGroundColor)
            .padding(top = 16.dp)
    ) {
        item {
            Text(
                stringResource(Res.string.transaction_History),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 16.sp
            )
        }

        items(transactions) { transaction ->
            TransactionItemView(
                transaction = transaction,
                onClick = { onTransactionClick(transaction.booking ?: "") })
        }
        item {
            SideEffect { onLoadMore() }
        }
    }
}

//@Composable
//fun TransactionItemView(transaction: TransactionItem, onClick: () -> Unit) {
//    val amountColor = when (transaction.status) {
//        "Success" -> Color(0xFF2ECC71)
//        "Pending" -> Color(0xFFF29339)
//        else -> Color.Gray
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//    ) {
//        Text(transaction.date, fontWeight = FontWeight.SemiBold)
//        Spacer(modifier = Modifier.height(4.dp))
//        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//            Column {
//                Text(
//                    text = "Transaction Id : ${transaction.id}",
//                    fontSize = 14.sp,
//                    color = Color(0xFF35A6DB)
//                )
//                Text(
//                    text = "Payment Status : ${transaction.status}",
//                    fontSize = 14.sp,
//                    color = amountColor
//                )
//                Text(
//                    text = transaction.message,
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//            }
//            Text(
//                text = if (transaction.status == "Success") "+$${transaction.amount}" else "$${transaction.amount}",
//                color = amountColor,
//                fontWeight = FontWeight.Bold
//            )
//        }
//        Divider(modifier = Modifier.padding(top = 12.dp))
//    }
//}
