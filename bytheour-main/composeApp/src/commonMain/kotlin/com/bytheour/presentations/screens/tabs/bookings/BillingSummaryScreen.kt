package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.apply
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.billing_summary
import bytheour.composeapp.generated.resources.booked_hours
import bytheour.composeapp.generated.resources.change_Card
import bytheour.composeapp.generated.resources.convenience_fee
import bytheour.composeapp.generated.resources.extra_hours
import bytheour.composeapp.generated.resources.hours_details
import bytheour.composeapp.generated.resources.no_promo_codes_found
import bytheour.composeapp.generated.resources.offer_and_Promo_code
import bytheour.composeapp.generated.resources.pay
import bytheour.composeapp.generated.resources.pay_from
import bytheour.composeapp.generated.resources.payfrom_ico
import bytheour.composeapp.generated.resources.payment_offer
import bytheour.composeapp.generated.resources.promocode_ico
import bytheour.composeapp.generated.resources.search_coupon_code
import bytheour.composeapp.generated.resources.select_Card
import bytheour.composeapp.generated.resources.total_amount
import bytheour.composeapp.generated.resources.total_hours
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingSummaryScreen(
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    navigateBack: () -> Unit,
    navigateToMainScreen:(Boolean)-> Unit,
    bookingId: String,
    fromInspection:Boolean
) {

    LaunchedEffect(Unit) {
        bookingViewModel.fetchBookingDetails(bookingId, true)
    }

    println("Get Booking Id ====================$bookingId")


    var showPromoCodeBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bookingData by bookingViewModel.bookingData.collectAsState()
    val uiState by bookingViewModel.uiStates.collectAsState()
    val amount = bookingData.amount ?: 0.0
    val feePercent = bookingData.percentage ?: 0.0
    val fee = (amount * feePercent / 100).toInt()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(showPromoCodeBottomSheet) {
        if (showPromoCodeBottomSheet) {
            bookingViewModel.getPromoCodesList(bookingId)
        }
    }

    if (uiState.navigateBack){
        if (fromInspection){
            navigateToMainScreen(true)
        }else{
            navigateBack()
        }

    }

    val filteredPromos = remember(uiState.promoList, searchQuery) {
        if (searchQuery.isEmpty()) {
            uiState.promoList
        } else {
            uiState.promoList.filter { promo ->
                promo.title.contains(searchQuery, ignoreCase = true) ||
                        promo.code.contains(searchQuery, ignoreCase = true) ||
                        promo.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    if (showPromoCodeBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPromoCodeBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.offer_and_Promo_code),
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 16.sp,
                        color = JTColors.blackColor,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Expand",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                            .clickable { showPromoCodeBottomSheet = false }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                JTSearchTextField(
                    value = searchQuery,
                    placeholder = stringResource(Res.string.search_coupon_code),
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFF5F5F5))
                        .padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(Res.string.payment_offer),
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 14.sp,
                        color = JTColors.blackColor,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (filteredPromos.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = stringResource(Res.string.no_promo_codes_found),
                            fontFamily = JtBtoFonts.getInterBoldFont(),
                            fontSize = 16.sp,
                            color = JTColors.BackGroundBlueColor,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        )

                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredPromos,
                    ) { promoItem ->
                        PromoCard(
                            image = promoItem.image.ifEmpty { "https://picsum.photos/200/300/?blur" },
                            title = promoItem.title,
                            description = promoItem.description,
                            code = promoItem.code,
                            onApply = {
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
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
                                bookingViewModel.onScreenExit()
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
                        text = stringResource(Res.string.hours_details),
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
                            text = stringResource(Res.string.booked_hours),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF6F6F6F)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${bookingData.hours ?: 0.0}Hr",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.extra_hours),
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF6F6F6F)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = bookingData.extraHours ?: "",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(JTColors.summary_grey)
                    ) {
                        Text(
                            text = stringResource(Res.string.total_hours),
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            color = JTColors.blackColor,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${(bookingData.hours ?: 0.0) + (bookingData.extraHours?.toDoubleOrNull() ?: 0.0)}",
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            color = JTColors.blackColor,
                            modifier = Modifier.padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OfferPromoCodeCard(
                        onClick = { showPromoCodeBottomSheet = true }
                    )

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
                            text = "$${bookingData.amount}",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.convenience_fee) + " ($feePercent %)",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF6F6F6F)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "$$fee",
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    }

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
                            text = "$${amount + fee}",
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            color = JTColors.BackGroundBlueColor,
                            modifier = Modifier.padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PayCard(selectedCard = uiState.cardNumber, onSelect = {
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

        if (uiState.message != null) {
            CommonSnackbar(
                message = uiState.message ?: "",
                type = if (uiState.isErrorMsg) SnackbarType.ERROR else SnackbarType.SUCCESS,
                autoDismissMillis = 1500,
                onDismiss = {
//                navigateToOtpScreen(viewModel.uiStates.value.number ?: "")
                    bookingViewModel.clearError()
                }
            )
        }
    }
}

@Composable
fun OfferPromoCodeCard(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val dashLength = 6.dp.toPx()
                val gapLength = 4.dp.toPx()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
                drawRoundRect(
                    color = JTColors.BackGroundBlueColor,
                    style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                    cornerRadius = CornerRadius(7.dp.toPx())
                )
            }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = JTColors.BackGroundBlueColor),
            shape = RoundedCornerShape(7.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.promocode_ico),
                        contentDescription = "",
                        modifier = Modifier.size(19.dp)
                            .clip(CircleShape)

                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(Res.string.offer_and_Promo_code),
                        color = JTColors.WhiteColor,
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                        .clickable { onClick() }
                )
            }
        }
    }
}


@Composable
fun PromoCard(
    image: String,
    title: String,
    description: String,
    code: String,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = JTColors.WhiteColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            AsyncImage(
                model = image,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp) // Slightly larger image
                    .clip(CircleShape),
                placeholder = painterResource(Res.drawable.promocode_ico)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Content Column
            Column(
                modifier = Modifier.weight(1f), // Take remaining space
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    color = JTColors.blackColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontFamily = JtBtoFonts.getInterFont(),
                    color = Color(0xFF858585)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .wrapContentWidth()
                            .drawBehind {
                                val strokeWidth = 2.dp.toPx()
                                val dashLength = 6.dp.toPx()
                                val gapLength = 4.dp.toPx()
                                val pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(dashLength, gapLength),
                                    0f
                                )

                                drawRoundRect(
                                    color = JTColors.promoCodeBackGround,
                                    cornerRadius = CornerRadius(6.dp.toPx())
                                )

                                drawRoundRect(
                                    color = Color.White,
                                    style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                                    cornerRadius = CornerRadius(6.dp.toPx())
                                )
                            }
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = code,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(containerColor = JTColors.BackGroundBlueColor),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .wrapContentWidth()
                            .defaultMinSize(minWidth = 70.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.apply),
                            color = JTColors.WhiteColor,
                            fontSize = 14.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PayCard(
    selectedCard: String?,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = JTColors.timeSlotCardColor),
        shape = RoundedCornerShape(7.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.payfrom_ico),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = stringResource(Res.string.pay_from),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 14.sp,
                    color = JTColors.summary_text
                )

                if (selectedCard != null) {
                    Text(
                        text = selectedCard,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        fontSize = 14.sp,
                        color = JTColors.BackGroundBlueColor
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (selectedCard == null) stringResource(Res.string.select_Card) else stringResource(Res.string.change_Card),
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 14.sp,
                color = JTColors.BackGroundBlueColor,
                modifier = Modifier.clickable { onSelect() }
            )
        }
    }
}

//BTHx1154#3893