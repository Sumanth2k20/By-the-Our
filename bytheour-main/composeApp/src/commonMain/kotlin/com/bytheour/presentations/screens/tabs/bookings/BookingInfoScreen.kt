package com.bytheour.presentations.screens.tabs.bookings

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.accept
import bytheour.composeapp.generated.resources.address_ico1
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.amount_refunded
import bytheour.composeapp.generated.resources.are_you_sure_to_end_work
import bytheour.composeapp.generated.resources.are_you_sure_to_start_work
import bytheour.composeapp.generated.resources.arrowleft_ico
import bytheour.composeapp.generated.resources.average_active
import bytheour.composeapp.generated.resources.average_inactive
import bytheour.composeapp.generated.resources.avg
import bytheour.composeapp.generated.resources.bad_active
import bytheour.composeapp.generated.resources.bad_inactive
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.bill_summary
import bytheour.composeapp.generated.resources.billing_summary
import bytheour.composeapp.generated.resources.booked_hours
import bytheour.composeapp.generated.resources.bookingId
import bytheour.composeapp.generated.resources.booking_info
import bytheour.composeapp.generated.resources.booking_info_ico
import bytheour.composeapp.generated.resources.calendar_ico
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.cancelByProvider
import bytheour.composeapp.generated.resources.cancelByYou
import bytheour.composeapp.generated.resources.cancel_booking
import bytheour.composeapp.generated.resources.cancel_policy
import bytheour.composeapp.generated.resources.cancel_reasons
import bytheour.composeapp.generated.resources.cancelled
import bytheour.composeapp.generated.resources.cancelled_ico
import bytheour.composeapp.generated.resources.chat
import bytheour.composeapp.generated.resources.chat_ico
import bytheour.composeapp.generated.resources.complete
import bytheour.composeapp.generated.resources.completed_service
import bytheour.composeapp.generated.resources.confirmed
import bytheour.composeapp.generated.resources.confirmed_ico
import bytheour.composeapp.generated.resources.continues
import bytheour.composeapp.generated.resources.convenience_fee
import bytheour.composeapp.generated.resources.customer_Rating
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.decline
import bytheour.composeapp.generated.resources.donloadinvoice_ico
import bytheour.composeapp.generated.resources.end
import bytheour.composeapp.generated.resources.estimated_booking_cost
import bytheour.composeapp.generated.resources.estimatedbooking_ico
import bytheour.composeapp.generated.resources.excellent_active
import bytheour.composeapp.generated.resources.excellent_inactive
import bytheour.composeapp.generated.resources.extra_hours
import bytheour.composeapp.generated.resources.find_bg_img
import bytheour.composeapp.generated.resources.find_good_provider
import bytheour.composeapp.generated.resources.find_good_technician
import bytheour.composeapp.generated.resources.find_provider
import bytheour.composeapp.generated.resources.find_technician
import bytheour.composeapp.generated.resources.findprovider_ico
import bytheour.composeapp.generated.resources.great
import bytheour.composeapp.generated.resources.great_active
import bytheour.composeapp.generated.resources.great_inactive
import bytheour.composeapp.generated.resources.honesty_rating
import bytheour.composeapp.generated.resources.hours_details
import bytheour.composeapp.generated.resources.howCancelBooking
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.in_progres
import bytheour.composeapp.generated.resources.inprogress_ico
import bytheour.composeapp.generated.resources.inspectionId
import bytheour.composeapp.generated.resources.inspection_end_conformation
import bytheour.composeapp.generated.resources.inspection_start_conformation
import bytheour.composeapp.generated.resources.mediation
import bytheour.composeapp.generated.resources.mediation_Raised
import bytheour.composeapp.generated.resources.mediation_accepted
import bytheour.composeapp.generated.resources.mediation_available
import bytheour.composeapp.generated.resources.mediation_guide
import bytheour.composeapp.generated.resources.mediation_req_declined
import bytheour.composeapp.generated.resources.mediation_req_declined_dsc
import bytheour.composeapp.generated.resources.need_help_with_booking
import bytheour.composeapp.generated.resources.no_charge_until_completion
import bytheour.composeapp.generated.resources.payment_mode
import bytheour.composeapp.generated.resources.policy_customer_cancel_2hr
import bytheour.composeapp.generated.resources.policy_customer_cancel_2hr_provider
import bytheour.composeapp.generated.resources.policy_customer_cancel_30min
import bytheour.composeapp.generated.resources.policy_customer_cancel_30min_provider
import bytheour.composeapp.generated.resources.policy_customer_cancel_anytime
import bytheour.composeapp.generated.resources.policy_customer_cancel_before_2hr
import bytheour.composeapp.generated.resources.poor
import bytheour.composeapp.generated.resources.poor_active
import bytheour.composeapp.generated.resources.poor_inactive
import bytheour.composeapp.generated.resources.provider_accept
import bytheour.composeapp.generated.resources.provider_has_to_start
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.provider_review
import bytheour.composeapp.generated.resources.providerverified
import bytheour.composeapp.generated.resources.rate_service
import bytheour.composeapp.generated.resources.ratecustomer_ico
import bytheour.composeapp.generated.resources.ratings
import bytheour.composeapp.generated.resources.request_start_service
import bytheour.composeapp.generated.resources.review_rating
import bytheour.composeapp.generated.resources.searching_for_provider
import bytheour.composeapp.generated.resources.service_booked
import bytheour.composeapp.generated.resources.service_completed
import bytheour.composeapp.generated.resources.service_rating
import bytheour.composeapp.generated.resources.start
import bytheour.composeapp.generated.resources.subTotal
import bytheour.composeapp.generated.resources.submit
import bytheour.composeapp.generated.resources.tenPercent
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.time_slot
import bytheour.composeapp.generated.resources.total_amount
import bytheour.composeapp.generated.resources.total_amount_paid
import bytheour.composeapp.generated.resources.total_hours
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.view_Ratings
import bytheour.composeapp.generated.resources.view_profile
import bytheour.composeapp.generated.resources.work_end_conformation
import bytheour.composeapp.generated.resources.work_start_conformation
import bytheour.composeapp.generated.resources.write_a_review
import bytheour.composeapp.generated.resources.you_cancel_booking
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.RatingBadge
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.RefundSuccessOutlinedBox
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.BillingSummaryContent
import com.bytheour.utils.Approvals
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.ServiceRatingText
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.Status
import com.bytheour.utils.Strings
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getCancelMessageByTimeAndDate
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoScreen(
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    navigateToBookingTabScreen:()-> Unit,
    navigateToOneOnOneChat: (String, String, Boolean,String,String?) -> Unit,
    navigateToCancelBookingScreen :(String,String,String,String)-> Unit,
    navigateToMediationScreen:(String)-> Unit,
    navigateBack:()-> Unit,
    navigateToViewProfileScreen:(BookingData)-> Unit,
    navigateToChatSupportScreen:(String)-> Unit,
    navigateToBillingSummaryScreen:(String,Boolean)-> Unit,
    bookingId: String,
    navigateToViewInspectionDetails:(String,String,String,String)-> Unit
){

    LaunchedEffect(Unit){
        bookingViewModel.fetchBookingDetails(bookingId)
    }

    val bookingData by bookingViewModel.bookingData.collectAsState()
    val uiState by bookingViewModel.uiStates.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showMediationDialogBox by remember{ mutableStateOf(false) }
    val isQuotationAccepted = bookingData.isQuotationAccepted
    var showViewRatingBottomSheet by remember { mutableStateOf(false) }
    var showWorkStartDialog by remember { mutableStateOf(false) }
    var showWorkEndDialog by remember { mutableStateOf(false) }
    var showRateProviderButton by remember { mutableStateOf(false) }
    var showBillingDetailsSheet by remember { mutableStateOf(false) }
    var showBottomSheetForCancellationPolicy by remember { mutableStateOf(false) }
    var isAccepted by remember { mutableStateOf(false) }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ReviewAndRatingBottomSheet(
                    serviceName = bookingData.title,
                    bookingId = bookingData.bookingId ?: "",
                    providerName = bookingData.workerName,
                    providerRating = bookingData.ratings ?: 0.0f,
                    providerReviews = bookingData.serviceRating?.toIntOrNull() ?: 0,
                    onSubmitClick = { serviceRating, _ ->
                        bookingViewModel.addRating(
                            bookingId = bookingData.bookingId ?: "",
                            serviceRating = serviceRating ?: "",
                            rating = serviceRating,
                            review = uiState.reviewMessage
                        )

                        showBottomSheet = false
                    },
                    profileImage = bookingData.image,
                    onCloseClick = { showBottomSheet = false },
                    onMediationClick = {
                        showMediationDialogBox = true
                    },
                    mediationRaised = bookingData.mediationRaise?:"",
                    mediationStatus = bookingData.mediationStatus?:"",
                    bookingViewModel = bookingViewModel
                )
            }
        }
    }
    if(showBillingDetailsSheet){
        ModalBottomSheet(
            onDismissRequest = { showBillingDetailsSheet = false },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            dragHandle = null
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            BillingSummaryContent(
                serviceName = bookingData.title,
                bookingId = bookingData.bookingId?:"",
                providerName = bookingData.workerName,
                date = bookingData.date,
                time = bookingData.time,
                bookedHours = bookingData.hours.toString(),
                extraHours = bookingData.extraHours?:"",
                paymentMethod = bookingData.paymentType?:"",
                amount = bookingData.amount.toString(),
                convenienceFee = bookingData.percentage.toString(),
                onDismiss = { showBillingDetailsSheet = false }
            )
        }
    }
    if (showBottomSheetForCancellationPolicy){
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetForCancellationPolicy = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            CancellationPolicies(
                onCloseClick = {
                    showBottomSheetForCancellationPolicy = false
                }
            )
        }
    }

    val maxHeight = 400.dp
    val minHeight = 200.dp
    val density = LocalDensity.current

    var headerHeightPx by remember { mutableStateOf(with(density) { maxHeight.toPx() }) }
    val headerHeightDp = with(density) { headerHeightPx.toDp() }

    val scrollState = rememberScrollState()

    // Nested scroll connection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val oldHeight = headerHeightPx

                // 🔼 Scrolling down → Expand header (only if content is at top)
                if (delta > 0 && scrollState.value == 0) {
                    headerHeightPx = (headerHeightPx + delta).coerceIn(
                        with(density) { minHeight.toPx() },
                        with(density) { maxHeight.toPx() }
                    )
                    val consumed = headerHeightPx - oldHeight
                    return Offset(0f, consumed)
                }

                // 🔽 Scrolling up → Collapse header first
                if (delta < 0 && headerHeightPx > with(density) { minHeight.toPx() }) {
                    headerHeightPx = (headerHeightPx + delta).coerceIn(
                        with(density) { minHeight.toPx() },
                        with(density) { maxHeight.toPx() }
                    )
                    val consumed = headerHeightPx - oldHeight
                    return Offset(0f, consumed)
                }

                return Offset.Zero
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JTColors.WhiteColor)
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JTColors.WhiteColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                    )
                    .height(headerHeightDp),
            ) {
                val imageToLoad = if (bookingData.image.isEmpty()) {
                    painterResource(Res.drawable.image_1)
                } else {
                    rememberAsyncImagePainter(model = bookingData.image)
                }
                Image(
                    painter = imageToLoad,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Image(
                    painter = painterResource(Res.drawable.transparent_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 45.dp, start = 16.dp, end = 16.dp)
                ) {
                    when (bookingData.status) {
                        Status.OrderStatus.COMPLETED.status -> {
                            Image(
                                painter = painterResource(Res.drawable.booking_info_ico),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .clickable {
                                        navigateToChatSupportScreen(
                                            bookingData.bookingId ?: ""
                                        )
                                    }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    when (bookingData.status) {
                        Status.OrderStatus.SEARCHING.status -> {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = JTColors.searchStatusColor,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                JtBtoCommonText(
                                    text = stringResource(Res.string.searching_for_provider),
                                    color = JTColors.searchTextColor,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Status.OrderStatus.CONFIRMED.status -> {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = JTColors.OrderStatusConfirmed,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.confirmed_ico),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                JtBtoCommonText(
                                    text = stringResource(Res.string.confirmed),
                                    color = JTColors.confirmedGreenColor,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Status.OrderStatus.IN_PROGRESS.status -> {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = JTColors.inProgressStatusColor,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.inprogress_ico),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                JtBtoCommonText(
                                    text = stringResource(Res.string.in_progres),
                                    color = JTColors.inProgressTextColor,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Status.OrderStatus.COMPLETED.status -> {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = JTColors.completeStatusColor,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.confirmed_ico),
                                        contentDescription = "",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.complete),
                                        color = JTColors.completeTextColor,
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))
                                when {
                                    bookingData.amountProceed == "y" -> {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = JTColors.WhiteColor,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            JtBtoCommonText(
                                                text = stringResource(Res.string.amount_refunded),
                                                color = JTColors.BackGroundBlueColor,
                                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                    bookingData.mediationRaise == "y" && bookingData.mediationStatus == "n" -> {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = JTColors.WhiteColor,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            JtBtoCommonText(
                                                text = stringResource(Res.string.mediation_Raised) + bookingData.mediationAmount,
                                                color = JTColors.BackGroundBlueColor,
                                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                    bookingData.mediationRaise == "y" && bookingData.mediationStatus == "y" -> {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = JTColors.WhiteColor,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            JtBtoCommonText(
                                                text =
                                                if (bookingData.mediationAcceptAmount.equals("0")){
                                                    "Mediation declined"
                                                }else{
                                                    stringResource(Res.string.mediation_accepted) + "$" + bookingData.mediationAmount
                                                },

                                                color = JTColors.BackGroundBlueColor,
                                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Status.OrderStatus.CANCELLED.status -> {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = JTColors.cancelStatusColor,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.cancelled_ico),
                                    contentDescription = "",
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                JtBtoCommonText(
                                    text = stringResource(Res.string.cancelled),
                                    color = JTColors.cancelTextColor,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bookingData.title,
                        color = JTColors.WhiteColor,
                        fontSize = 24.sp,
                        fontFamily = JtBtoFonts.getInterBoldFont()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterFont())) {
                                append(
                                    if (bookingData.isInspection == true)
                                        stringResource(Res.string.inspectionId)
                                    else
                                        stringResource(Res.string.bookingId)
                                )
                            }

                            withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterBoldFont())) {
                                append(bookingData.bookingId)
                            }
                        },
                        color = JTColors.WhiteColor,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .verticalScroll(scrollState)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .background(
                            JTColors.grayDivider,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                when (bookingData.status) {
                    Status.OrderStatus.SEARCHING.status -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        DateTimeCard(
                            date = formatDateString(bookingData.date),
                            time = bookingData.time
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        FindingProviderCard(
                            isInspection = bookingData.isInspection == true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.address_ico1),
                                contentDescription = ""
                            )
                            Text(
                                text = bookingData.address,
                                fontFamily = JtBtoFonts.getInterFont(),
                                fontSize = 12.sp,
                                color = Color(0xFF313131),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(15.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.estimatedbooking_ico),
                                contentDescription = ""
                            )
                            Text(
                                text = stringResource(Res.string.estimated_booking_cost),
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                fontSize = 12.sp,
                                color = Color(0xFF313131),
                                modifier = Modifier.padding(start = 16.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (bookingData.isInspection== true) "$${bookingData.isInspectionRate}" else "$${bookingData.priceRange}",
                                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                fontSize = 12.sp,
                                color = Color(0xFF313131),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(Res.string.no_charge_until_completion),
                            fontFamily = JtBtoFonts.getInterFont(),
                            fontSize = 12.sp,
                            color = Color(0xFF777777),
                            modifier = Modifier.padding(16.dp)
                        )
                        HorizontalDivider()
                        Spacer(modifier = Modifier.weight(1f))
                        BookingHelpSection(
                            onCancelBookingClick = {
                                navigateToCancelBookingScreen(
                                    bookingData.bookingId ?: "",
                                    bookingData.cancelAfterTime?:"",
                                    bookingData.time,
                                    bookingData.date
                                )
                            },
                            onPolicyClick = {
                                showBottomSheetForCancellationPolicy = true
                            }
                        )
                    }

                    Status.OrderStatus.IN_PROGRESS.status -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        DateTimeCard(
                            date = formatDateString(bookingData.date),
                            time = bookingData.time
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ServiceBookedCard(
                            name = bookingData.workerName,
                            role = bookingData.workerId ?: "",
                            rating = bookingData.ratings ?: 0.0f,
                            amount = 100.0f,
                            profileImage = bookingData.providerImage ?: "https://stagingbythehour.s3.us-east-2.amazonaws.com/default/worker.png",
                            onChatClick = {
                                navigateToOneOnOneChat(
                                    bookingData.workerName,
                                    "",
                                    false,
                                    bookingData.providerImage ?: "",
                                    bookingData.workerId
                                )
                            },
                            onProfileClick = {
                                navigateToViewProfileScreen(bookingData)
                            },
                            isVerified = bookingData.providerVerified ?: "",
                            isInspection = bookingData.isInspection?:true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        BillDetailsCard(
                            orderStatus = Status.OrderStatus.IN_PROGRESS.status,
                            amount = bookingData.amount ?: 0.0,
                            feePercent = bookingData.percentage ?: 0.0,
                            bookedHours = bookingData.hours ?: 0.0,
                            extraHours = bookingData.extraHours ?: "",
                            onWorkStartConfirmation = {
                                showWorkStartDialog = true
                            },
                            onWorkEndConfirmation = {
                                showWorkEndDialog = true
                            },
                            bookingStartedByUser = bookingData.startedByUser == true,
                            workEndByProvider = bookingData.workEndByProvider ?: true,
                            isInspection = bookingData.isInspection?:false,
                            navigateToViewInspectionDetails = {
                                navigateToViewInspectionDetails(
                                    bookingData.title,
                                    bookingData.image,
                                    bookingData.workerName,
                                    bookingId
                                )
                            },
                            isInspectionRate = bookingData.isInspectionRate?:0.0
                        )



                        Spacer(modifier = Modifier.height(10.dp))

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Status.OrderStatus.CONFIRMED.status -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        DateTimeCard(
                            date = formatDateString(bookingData.date),
                            time = bookingData.time
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ServiceBookedCard(
                            name = bookingData.workerName,
                            role =  bookingData.workerId ?: "",
                            rating = bookingData.ratings ?: 0.0f,
                            amount = 100.0f,
                            profileImage = bookingData.providerImage ?: "https://stagingbythehour.s3.us-east-2.amazonaws.com/default/worker.png",
                            onChatClick = {
                                navigateToOneOnOneChat(
                                    bookingData.workerName,
                                    "",
                                    false,
                                    bookingData.providerImage ?: "",
                                    bookingData.workerId
                                )
                            },
                            onProfileClick = { navigateToViewProfileScreen(bookingData) },
                            isVerified = bookingData.providerVerified ?: "",
                            isInspection = bookingData.isInspection?:false
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val bookingTimeRange = bookingData.time
                        val bookingTime = bookingTimeRange.to24HourFormat()

                        if (bookingData.isInspection == true) {
                            BillDetailsCard(
                                orderStatus = Status.OrderStatus.CONFIRMED.status,
                                amount = bookingData.amount ?: 0.0,
                                feePercent = bookingData.percentage ?: 0.0,
                                bookedHours = bookingData.hours ?: 0.0,
                                extraHours = bookingData.extraHours ?: "",
                                paymentMethod = bookingData.paymentType ?: "",
                                isInspection = bookingData.isInspection?:false,
                                isInspectionRate = bookingData.isInspectionRate?:0.0
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        } else if (bookingData.isQuotationSent == true) {
                            BillDetailsCard(
                                orderStatus = Status.OrderStatus.CONFIRMED.status,
                                amount = bookingData.amount ?: 0.0,
                                feePercent = bookingData.percentage ?: 0.0,
                                bookedHours = bookingData.hours ?: 0.0,
                                extraHours = bookingData.extraHours ?: "",
                                onAccept = if (isQuotationAccepted == false) {
                                    {
                                        bookingViewModel.manageQuotation(
                                            bookingId = bookingData.bookingId ?: "",
                                            bookingTime = bookingTime,
                                            status = Approvals.ACCEPT.type,
                                            userTime = ""
                                        )
                                    }
                                } else {
                                    {}
                                },
                                onDecline = if (isQuotationAccepted == false) {
                                    {
                                        bookingViewModel.manageQuotation(
                                            bookingId = bookingData.bookingId ?: "",
                                            bookingTime = bookingData.time,
                                            status = Approvals.REJECT.type,
                                            userTime = ""
                                        )
                                    }
                                } else {
                                    {}
                                },
                                isQuotationAccepted = isQuotationAccepted ?: false,
                                isQuotationSent = true,
                                isPaymentConformed = bookingData.isPaymentConfirmed ?: false,
                                isInspection = bookingData.isInspection?:false,
                                isInspectionRate = bookingData.isInspectionRate?:0.0
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Spacer(modifier = Modifier.weight(1f))

                        if (isQuotationAccepted == true && bookingData.isPaymentConfirmed == false) {
                            Button(
                                onClick = {
                                    navigateToBillingSummaryScreen(bookingId,false)
                                },
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth(0.9f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = JTColors.BackGroundBlueColor,
                                    disabledContainerColor = Color(0xFF4F4A5F),
                                ),
                                enabled = true
                            ) {
                                Text(
                                    text = stringResource(Res.string.billing_summary),
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 13.sp,
                                    color = JTColors.WhiteColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (bookingData.isPaymentConfirmed == true || bookingData.isQuotationSent == true || isQuotationAccepted == true) {
                            BookingHelpSection(
                                onCancelBookingClick = {
                                    if (bookingData.isPaymentConfirmed == true) {
                                        showDialog = true
                                    } else {
                                        navigateToCancelBookingScreen(bookingData.bookingId ?: "", bookingData.cancelAfterTime?:"",  bookingData.time,bookingData.date)
                                    }
                                },
                                onPolicyClick = {
                                    showBottomSheetForCancellationPolicy = true
                                }
                            )
                        }
                    }

                    Status.OrderStatus.COMPLETED.status -> {
                        Spacer(modifier = Modifier.height(16.dp))

                        if (bookingData.amountProceed == "y") {
                            RefundSuccessOutlinedBox(amount = bookingData.mediationAmount ?: "")
                        } else if (bookingData.mediationRaise == "y" || bookingData.mediationStatus == "y") {
                            MediationRequestCard(
                                amount = if (bookingData.mediationStatus == "y")
                                    bookingData.mediationAcceptAmount ?: ""
                                else
                                    bookingData.mediationAmount ?: "",
                                isApproved = bookingData.mediationStatus == "y"
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }




                        Spacer(modifier = Modifier.height(16.dp))

                        DateTimeCard(
                            date = formatDateString(bookingData.date),
                            time = bookingData.time
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val givenReview = bookingData.reviewList?.given
                        val takenReview = bookingData.reviewList?.taken
                        if (bookingData.isInspection != true) {
                            ServiceCompletedCard(
                                servicePersonName = bookingData.workerName,
                                onRateServiceClick = {
                                    showBottomSheet = true
                                },
                                rating = bookingData.givenRating ?: 0,
                                reviewGiven = givenReview != null,
                                serviceRatingText = bookingData.serviceRatingText ?: "",
                                providerRating = bookingData.reviewTaken ?: 0,
                                onViewRatingsClick = {
                                    showViewRatingBottomSheet = true
                                },
                                reviewTaken = takenReview != null,
                                rateProvider = {
                                    showRateProviderButton = true
                                }
                            )
                        }
                        BillDetailsCard(
                            orderStatus = Status.OrderStatus.COMPLETED.status,
                            amount = bookingData.amount ?: 0.0,
                            feePercent = bookingData.percentage ?: 0.0,
                            bookedHours = bookingData.hours ?: 0.0,
                            extraHours = bookingData.extraHours ?: "",
                            paymentMethod = bookingData.paymentType ?: "",
                            isInspection = bookingData.isInspection?:false,
                            isInspectionRate = bookingData.isInspectionRate?:0.0
                        )


                        Spacer(modifier = Modifier.height(30.dp))
                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (showRateProviderButton) {
                                OutlinedButton(
                                    onClick = {
                                        showBottomSheet = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.height(45.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(Res.drawable.ratecustomer_ico),
                                            contentDescription = ""
                                        )
                                        Spacer(Modifier.width(7.dp))
                                        Text(
                                            text = stringResource(Res.string.rate_service),
                                            color = JTColors.iosWhite,
                                            fontFamily = JtBtoFonts.getInterMediumFont(),
                                            fontSize = 14.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                                Spacer(Modifier.width(8.dp))
                            }

                            OutlinedButton(
                                onClick = {
                                    showBillingDetailsSheet = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.height(45.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(Res.drawable.donloadinvoice_ico),
                                        contentDescription = ""
                                    )
                                    Spacer(Modifier.width(7.dp))
                                    Text(
                                        text = stringResource(Res.string.billing_summary),
                                        color = JTColors.enableButton,
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                        fontSize = 14.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    Status.OrderStatus.CANCELLED.status -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        DateTimeCard(
                            date = formatDateString(bookingData.date),
                            time = bookingData.time
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        BookingCancelledCard(
                            cancelledBy = bookingData.cancelBy ?: "",
                            providerName = bookingData.workerName,
                            cancellationReason = bookingData.reason ?: "",
                            onRateServiceClick = {
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .align(Alignment.TopCenter)
                .padding(start = 16.dp, end = 16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.arrowleft_ico),
                contentDescription = "",
                modifier = Modifier.clickable { navigateToBookingTabScreen() }
            )
            Text(
                stringResource(Res.string.booking_info),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
            Text("")
        }
    }


    if (showDialog) {
        CommonDialogBoxForMediationAndCancel(
            title = stringResource(Res.string.cancel_booking),
            message = buildAnnotatedString {
                append(getCancelMessageByTimeAndDate(bookingData.time,bookingData.date))
            },
            onCancelClick = { showDialog = false },
            onContinueClick = {
                navigateToCancelBookingScreen(bookingData.bookingId ?: "", bookingData.cancelAfterTime?:"", bookingData.time,bookingData.date)
                showDialog = false
            },
            continueText = stringResource(Res.string.continues),
            cancelText = stringResource(Res.string.cancel)
        )
    }
    if (showMediationDialogBox) {
        CommonDialogBoxForMediationAndCancel(
            title = stringResource(Res.string.mediation),
            message = getMediationMessage(),
            onCancelClick = { showMediationDialogBox = false },
            onContinueClick = {
                navigateToMediationScreen(bookingData.bookingId?:"")
                showMediationDialogBox = false
            },
            continueText = stringResource(Res.string.continues),
            cancelText = stringResource(Res.string.cancel)
        )
    }
    if (showViewRatingBottomSheet) {
        ViewCustomersRatingsBottomSheet(
            givenByCustomerRating = bookingData.reviewList?.given?.rating?.toFloat()?:0.0f,
            givenByCustomerReview = bookingData.reviewList?.given?.review?:"",
            reviewToCustomerRating = bookingData.reviewList?.taken?.rating?.toFloat()?:0.0f,
            reviewToCustomerReview = bookingData.reviewList?.taken?.review?:"",
            onDismiss = {
                showViewRatingBottomSheet = false
            }
        )
    }
    if (showWorkEndDialog) {
        CommonDialogBoxForMediationAndCancel(
            title = stringResource(Res.string.work_end_conformation),
            message = buildAnnotatedString {
                append(stringResource(Res.string.are_you_sure_to_end_work))
            },
            onCancelClick = { showWorkEndDialog = false },
            onContinueClick = {
                bookingViewModel.completeRequestBooking(booking = bookingData.bookingId ?: "")
                showWorkStartDialog = false
            },
            continueText = stringResource(Res.string.end),
            cancelText = stringResource(Res.string.cancel)
        )
    }
    if (showWorkStartDialog) {
        CommonDialogBoxForMediationAndCancel(
            title = stringResource(Res.string.work_start_conformation),
            message = buildAnnotatedString {
                append(stringResource(Res.string.are_you_sure_to_start_work))
            },
            onCancelClick = { showWorkStartDialog = false },
            onContinueClick = {
                bookingViewModel.startBooking(booking = bookingData.bookingId ?: "")
                showWorkStartDialog = false
            },
            continueText = stringResource(Res.string.start),
            cancelText = stringResource(Res.string.cancel)
        )
    }
    if (uiState.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiState.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 2000L,
            onDismiss = {
                navigateBack()
                bookingViewModel.clearMessage()
            }
        )
    }
}


@Composable
fun DateTimeCard(
    date:String,
    time :String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(JTColors.timeSlotCardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(Res.drawable.calendar_ico),
                    contentDescription = "calender"
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = stringResource(Res.string.date),
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        color = JTColors.dateTextColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )
                    Text(
                        text = date,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            VerticalDivider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = Color(0xFFE0E0E0),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(Res.drawable.time_ico),
                    contentDescription = "time"
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = stringResource(Res.string.time_slot),
                        color = JTColors.dateTextColor,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )
                    Text(
                        text = time,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BookingHelpSection(
    onPolicyClick: () -> Unit,
    onCancelBookingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF1EFF6))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier =  Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.need_help_with_booking),
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.cancel_policy),
                color = Color(0xFF2E1A47),
                fontSize = 11.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onPolicyClick() }
            )
        }
        Button(
            onClick = onCancelBookingClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White),
            shape = RoundedCornerShape(50),
            modifier = Modifier.width(89.dp).height(45.dp).weight(1f),
            border = BorderStroke(1.dp, Color(0xFFE5E5E5))
        ) {
            Text(
                text = stringResource(Res.string.cancel_booking),
                color = JTColors.BackGroundBlueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                minLines = 1
            )
        }
    }
}


@Composable
fun FindingProviderCard(
    isInspection: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(120.dp)
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(Res.drawable.find_bg_img),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(Res.drawable.findprovider_ico),
                contentDescription = "provider",
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (isInspection)
                        stringResource(Res.string.find_technician)
                    else
                        stringResource(Res.string.find_provider),
                    color = JTColors.WhiteColor,
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = if (isInspection)
                        stringResource(Res.string.find_good_technician)
                    else
                        stringResource(Res.string.find_good_provider),
                    color = JTColors.WhiteColor,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontFamily = JtBtoFonts.getInterFont()
                )
            }
        }
    }
}


@Composable
fun ServiceBookedCard(
    name: String,
    role: String,
    rating: Float,
    amount: Float,
    profileImage: String,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    isVerified:String,
    isInspection:Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(JTColors.serviceBookedCardColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(JTColors.BackGroundBlueColor,JTColors.LightBlue)
                    ),
                )
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Column {
                Text(
                    text = if (isInspection)"Inspection Raised" else stringResource(Res.string.service_booked),
                    fontSize = 15.sp,
                    lineHeight = 15.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = JTColors.WhiteColor
                )
                Text(
                    text = "'${name}'"+stringResource(Res.string.provider_accept),
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontFamily = JtBtoFonts.getInterFont(),
                    color = JTColors.WhiteColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profileImage.isNullOrBlank()){
                Image(
                    painter = painterResource(Res.drawable.provider_logo),
                    contentDescription = "null",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                )
            } else {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    if (isVerified == "verified"){
                        Image(
                            painter = painterResource(Res.drawable.providerverified),
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    text = role,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
                if (!isInspection){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = JTColors.ratingStarColor,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterSemiBoldFont())) {
                                    append(rating.toString())
                                }

                                withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterFont())) {
                                    append("(${5.0})")
                                }
                            },
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "$$amount/h",
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onChatClick,
                colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {

                Image(
                    painter = painterResource(Res.drawable.chat_ico),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = stringResource(Res.string.chat), color = Color.White, fontFamily = JtBtoFonts.getOpenSansBold())
            }
            if (!isInspection){
                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = onProfileClick,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = stringResource(Res.string.view_profile), color = JTColors.BackGroundBlueColor)
                }
            }
        }
    }
}

@Composable
fun HoursDetailsPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    bookedHours: Double,
    extraHours: String
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = 0.dp, y = 8.dp),
        modifier = Modifier
            .width(290.dp)
            .background(JTColors.WhiteColor, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(10.dp)
        ) {
            Text(
                text = stringResource(Res.string.hours_details),
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            PopoverRow(stringResource(Res.string.booked_hours), "${bookedHours}hr")
            HorizontalDivider()
            PopoverRow(stringResource(Res.string.extra_hours) , extraHours)
            HorizontalDivider()
            PopoverRow(stringResource(Res.string.total_hours), "${bookedHours}hr", isBold = true)
        }
    }
}

@Composable
fun PopoverRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun ServiceCompletedCard(
    servicePersonName: String,
    reviewGiven: Boolean,
    rating: Int,
    providerRating: Int,
    reviewTaken: Boolean,
    onRateServiceClick: () -> Unit,
    serviceRatingText: String,
    onViewRatingsClick: () -> Unit,
    rateProvider:()->Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(JTColors.serviceBookedCardColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(JTColors.BackGroundBlueColor, JTColors.LightBlue)
                    ),
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text(
                        text = stringResource(Res.string.service_completed),
                        color = Color.White,
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 15.sp,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = servicePersonName + stringResource(Res.string.completed_service),
                        color = Color(0xFFC9C9C9),
                        fontSize = 11.sp,
                        lineHeight = 11.sp
                    )
                }
                if (reviewTaken && providerRating != 0) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        RatingBadge(rating = providerRating, outOf = 5)
                    }
                }
            }
        }
        when {
            !reviewGiven && !reviewTaken -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 10.dp, end = 10.dp)
                        .clickable { onRateServiceClick() }, // Opens bottom sheet for rating
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Res.string.rate_service),
                            color = JTColors.BackGroundBlueColor,
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold()
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
            reviewGiven || reviewTaken -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 10.dp, end = 10.dp)
                        .clickable { onViewRatingsClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Res.string.view_Ratings),
                            color = JTColors.BackGroundBlueColor,
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold()
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

        }
        if (!reviewGiven && reviewTaken){
            Column(modifier = Modifier.padding(top = 10.dp)) {
                if (rating != 0) {
                    RatingBadge(rating = rating, outOf = 5)
                }
            }
            rateProvider()
        }
    }
}



@Composable
fun ReviewAndRatingBottomSheet(
    serviceName: String,
    bookingId: String,
    providerName: String,
    profileImage: String,
    providerRating: Float = 4.7f,
    providerReviews: Int = 115,
    onSubmitClick: (serviceRating: String?, honestyRating: String?) -> Unit,
    onCloseClick: () -> Unit,
    onMediationClick: () -> Unit,
    showMediation:Boolean = true,
    mediationRaised :String,
    mediationStatus:String,
    bookingViewModel: JTBookingTabViewmodel= getKoinViewModel()
) {
    val uiState by bookingViewModel.uiStates.collectAsState()
    var serviceRating by remember { mutableStateOf<String?>(null) }
    var honestyRating by remember { mutableStateOf<String?>(null) }

    val poorRatings = listOf(
        ServiceRatingText.BAD.type,
        ServiceRatingText.POOR.type,
        ServiceRatingText.AVERAGE.type
    )
    val isSubmitEnabled = !serviceRating.isNullOrEmpty() &&
            (serviceRating == "Great" || serviceRating == "Excellent" || !uiState.reviewMessage.isNullOrBlank())

    val shouldShowMediation = listOfNotNull(serviceRating)
        .any { it in poorRatings }

    Column(
        modifier = Modifier
            .then(
                if (mediationRaised ==  "n" && mediationStatus == "n") {
                    if (shouldShowMediation && showMediation) {
                        Modifier.fillMaxHeight(0.9f)
                    } else {
                        Modifier.wrapContentHeight()
                    }
                } else {
                    Modifier.wrapContentHeight()
                }
            )
            .wrapContentHeight()
            .background(JTColors.WhiteColor, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(Res.string.review_rating))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                modifier = Modifier.clickable { onCloseClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = serviceName,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansBold(),
            modifier = Modifier.align(Alignment.Start)
        )
        Box(modifier = Modifier.background(color = JTColors.timeSlotCardColor,
            shape = RoundedCornerShape(22.dp))
            .padding(4.dp)
            .align(Alignment.Start)
            .clip(RoundedCornerShape(22.dp)))
        {
            Text( buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = JTColors.blackLight,
                    fontSize = 12.sp
                )){
                    append(text = stringResource(Res.string.bookingId))
                }
                withStyle(style = SpanStyle(
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = JTColors.blackLight,
                    fontSize = 12.sp
                )){
                    append(text = bookingId)
                } },
            )
        }


        Spacer(modifier = Modifier.height(12.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    JTColors.serviceBookedCardColor,
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (profileImage.isEmpty()) {
                Image(
                    painter = painterResource(Res.drawable.image_1),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            } else {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = providerName,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                    Image(
                        painter = painterResource(Res.drawable.providerverified),
                        contentDescription = "null"
                    )
                }
                Text(
                    text = serviceName,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 11.sp,
                    lineHeight = 11.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 11.sp
                                )
                            ) {
                                append("$providerRating")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                                    fontSize = 11.sp
                                )
                            ) {
                                append(" ($providerReviews)")
                            }
                        },
                        lineHeight = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(Res.string.provider_review),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        RatingRow(
            label = stringResource(Res.string.service_rating),
            selectedRating = serviceRating,
            onRatingChange = { serviceRating = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        RatingRow(
            label = stringResource(Res.string.honesty_rating),
            selectedRating = honestyRating,
            onRatingChange = { honestyRating = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (mediationRaised ==  "n" && mediationStatus == "n"){
            if (shouldShowMediation) {
                if(showMediation){
                    MediationCard(
                        onClick = {
                            onMediationClick()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                }


            }
        }
        OutlinedTextField(
            value = uiState.reviewMessage?:"" ,
            onValueChange = { bookingViewModel.onReviewMessage(it) },
            placeholder = { Text(stringResource(Res.string.write_a_review),
                color = JTColors.mediationText,
                fontFamily = JtBtoFonts.getInterRegular())},
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE9E9E9),
                unfocusedBorderColor = Color(0xFFE9E9E9),
                cursorColor = JTColors.blackColor,
                focusedContainerColor = Color(0xFFFCFCFC),
                unfocusedContainerColor =  Color(0xFFFCFCFC),

                )
        )

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = {  onSubmitClick(serviceRating, honestyRating)  },
            enabled =isSubmitEnabled ,
            colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(stringResource(Res.string.submit), color = Color.White)
        }
    }
}

@Composable
fun RatingRow(
    label: String,
    selectedRating: String?,
    onRatingChange: (String) -> Unit
) {
    val ratings = listOf(
        ServiceRatingText.BAD.type to Pair(Res.drawable.bad_active, Res.drawable.bad_inactive),
        ServiceRatingText.POOR.type to Pair(Res.drawable.poor_active, Res.drawable.poor_inactive),
        ServiceRatingText.AVERAGE.type to Pair(Res.drawable.average_active, Res.drawable.average_inactive),
        ServiceRatingText.GREAT.type to Pair(Res.drawable.great_active, Res.drawable.great_inactive),
        ServiceRatingText.EXCELLENT.type to Pair(Res.drawable.excellent_active, Res.drawable.excellent_inactive),
    )

    Column {
        Text(
            label,
            fontWeight = FontWeight.Medium,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            fontSize = 14.sp,
            color = JTColors.ratingColor,
            lineHeight = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ratings.forEach { (title, icons) ->
                val isSelected = selectedRating == title
                val image = if (isSelected) painterResource(icons.first) else painterResource(icons.second)
                RatingIcon(
                    label = title,
                    image = image,
                    isSelected = isSelected,
                    onClick = { onRatingChange(title) }
                )
            }
        }
    }
}


@Composable
fun RatingIcon(
    label: String,
    image: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) JTColors.BackGroundBlueColor else JTColors.serviceBookedCardColor
    val textColor = if (isSelected) Color.White else Color.Black

    Card(
        modifier = Modifier
            .height(71.dp)
            .width(60.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = image,
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = textColor)
        }
    }
}

@Composable
fun MediationCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFEFEF)
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFA494D6),
                    shape = RoundedCornerShape(12.dp)
                ).padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(Res.string.mediation_available),
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color(0xFF313131)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.mediation_guide),
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF6D6D6D),
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = JTColors.blackColor,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun BookingCancelledCard(
    cancelledBy: String,
    providerName: String = "",
    cancellationReason: String = "",
    onRateServiceClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(JTColors.serviceBookedCardColor),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (cancelledBy.lowercase()) {
                TYPE_PROVIDER -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(JTColors.BackGroundBlueColor,JTColors.LightBlue)
                                ),
                            )
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(Res.string.cancelByProvider),
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                color = JTColors.WhiteColor,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = providerName + stringResource(Res.string.howCancelBooking),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontFamily = JtBtoFonts.getInterFont(),
                                color = JTColors.CancelReasonTextColor
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel_reasons),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = JTColors.textFieldColor,
                            fontFamily = JtBtoFonts.getInterFont(),
                        )
                        Text(
                            text = cancellationReason,
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.BackGroundBlueColor,

                            )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                onRateServiceClick()
                            },
                            modifier = Modifier
                                .height(38.dp),
                            colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.rate_service),
                                fontSize = 16.sp,
                                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                color = Color.White
                            )
                        }
                    }
                }

                TYPE_CUSTOMER -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(JTColors.BackGroundBlueColor,JTColors.LightBlue)
                                ),
                            )
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(Res.string.cancelByYou),
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                color = JTColors.WhiteColor,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()

                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(Res.string.you_cancel_booking),
                                fontSize = 11.sp,
                                lineHeight = 11.sp,
                                fontFamily = JtBtoFonts.getInterFont(),
                                color = JTColors.CancelReasonTextColor
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel_reasons),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = JTColors.textFieldColor,
                            fontFamily = JtBtoFonts.getInterFont(),
                        )
                        Text(
                            text = cancellationReason,
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.BackGroundBlueColor,

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommonDialogBoxForMediationAndCancel(
    title: String,
    message: AnnotatedString?,
    onCancelClick: () -> Unit,
    onContinueClick: () -> Unit,
    cancelText: String,
    continueText: String
) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (message != null) {
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        color = JTColors.blackLight,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(cancelText, color = JTColors.enableButton)
                    }

                    Button(
                        onClick = onContinueClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(continueText, color = JTColors.WhiteColor)
                    }
                }
            }
        }
    }
}


@Composable
fun BillDetailsCard(
    amount: Double,
    feePercent: Double,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    onWorkStartConfirmation: () -> Unit = {},
    onWorkEndConfirmation: () -> Unit = {},
    orderStatus: String,
    bookedHours: Double,
    extraHours: String,
    paymentMethod: String? = null,
    isQuotationAccepted: Boolean = false,
    bookingStartedByUser: Boolean? = null,
    workEndByProvider: Boolean = false,
    isQuotationSent: Boolean = false,
    isPaymentConformed: Boolean = false,
    isInspection: Boolean,
    navigateToViewInspectionDetails: () -> Unit = {},
    isInspectionRate: Double
) {
    var showHoursDetails by remember { mutableStateOf(false) }
    val fee = (amount * feePercent / 100)
    val subtotal = amount + fee
    val inspectionRate = isInspectionRate.toDouble()

    val cardTitle = when (orderStatus) {
        Status.OrderStatus.COMPLETED.status -> stringResource(Res.string.bill_summary)
        else -> stringResource(Res.string.bill_details)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(
                1.dp,
                color = JTColors.lightGrayColor.copy(0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(JTColors.WhiteColor),
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(JTColors.serviceBookedCardColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cardTitle,
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 14.sp
            )
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.hours_details),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 10.sp,
                        color = Color(0xFF828282)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { showHoursDetails = true },
                    )
                }
                HoursDetailsPopover(
                    expanded = showHoursDetails,
                    onDismissRequest = { showHoursDetails = false },
                    bookedHours = bookedHours,
                    extraHours = extraHours
                )
            }
        }

        HorizontalDivider()

        if (orderStatus == Status.OrderStatus.COMPLETED.status) {
            BillRow(label = stringResource(Res.string.payment_mode), value = paymentMethod ?: "")
            HorizontalDivider()
        }
        if (isInspection) {
            BillRow(
                label = "Inspection rate",
                value = "$$inspectionRate"
            )
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.total_amount),
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.blackColor,
                )
                Text(
                    text = "$$inspectionRate",
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.blackColor,
                )
            }
        } else {
            // ---- Normal Billing ----
            BillRow(label = stringResource(Res.string.amount), value = "$$amount")
            HorizontalDivider()
            BillRow(
                label = stringResource(Res.string.convenience_fee) + " ($feePercent%)",
                value = "$$fee"
            )
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (orderStatus == Status.OrderStatus.COMPLETED.status)
                        stringResource(Res.string.total_amount_paid)
                    else
                        stringResource(Res.string.subTotal),
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.blackColor,
                )
                Text(
                    text = "$$subtotal",
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.blackColor,
                )
            }
        }

        // 🔹 Action buttons remain unchanged
        when (orderStatus) {
            Status.OrderStatus.CONFIRMED.status -> {
                Spacer(modifier = Modifier.height(5.dp))
                when {
                    isQuotationSent && !isQuotationAccepted -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            OutlinedButton(
                                onClick = onDecline,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.width(140.dp)
                            ) {
                                Text(stringResource(Res.string.decline), color = JTColors.BackGroundBlueColor)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = onAccept,
                                colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.width(140.dp)
                            ) {
                                Text(stringResource(Res.string.accept), color = Color.White)
                            }
                        }
                    }
                    isPaymentConformed -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.provider_has_to_start),
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    isQuotationAccepted -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.request_start_service),
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Status.OrderStatus.IN_PROGRESS.status -> {
                Spacer(modifier = Modifier.height(12.dp))

                val isStartedByUser = bookingStartedByUser == true
                val buttonEnabled = !isStartedByUser || workEndByProvider

                if (buttonEnabled) {
                    Button(
                        onClick = {
                            if (isInspection && isStartedByUser) {
                                navigateToViewInspectionDetails()
                            } else if (isInspection && !isStartedByUser) {
                                onWorkStartConfirmation()
                            } else if (!isInspection && isStartedByUser) {
                                onWorkEndConfirmation()
                            } else {
                                onWorkStartConfirmation()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JTColors.BackGroundBlueColor
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isInspection && isStartedByUser) {
                                stringResource(Res.string.inspection_end_conformation)
                            } else if (isInspection && !isStartedByUser) {
                                stringResource(Res.string.inspection_start_conformation)
                            } else if (!isInspection && isStartedByUser) {
                                stringResource(Res.string.work_end_conformation)
                            } else {
                                stringResource(Res.string.work_start_conformation)
                            },
                            color = Color.White,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 14.sp
                        )
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun BillRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            color = Color(0xFF313131)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            color = Color(0xFF313131)
        )
    }
}

@Composable
fun MediationRequestCard(
    amount: String,
    isApproved: Boolean
) {
    val isDeclined = amount == "0" || amount == "0.0"

    val titleText: String
    val descriptionText: String

    if (isDeclined) {
        titleText = stringResource(Res.string.mediation_req_declined)
        descriptionText =  stringResource(Res.string.mediation_req_declined_dsc)
    } else if (isApproved) {
        titleText = Strings.MEDIATION_TITLE_APPROVED.replace("%s", "$$amount")
        descriptionText = Strings.MEDIATION_DESCRIPTION_APPROVED.replace("%s", "$$amount")
    } else {
        titleText = Strings.MEDIATION_TITLE_REQUESTED.replace("%s", "$$amount")
        descriptionText = Strings.MEDIATION_DESCRIPTION_REQUESTED.replace("%s", "$$amount")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = Color(0xFFFFF0E5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = titleText,
            color = JTColors.BackGroundBlueColor,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterSemiBoldFont()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = descriptionText,
            fontSize = 11.sp,
            color = JTColors.blackColor,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterFont()
        )
    }
}

@Composable
fun getMediationMessage(): AnnotatedString {
    return styledMessage(Strings.MEDIATION_MSG, stringResource(Res.string.tenPercent))
}

@Composable
fun styledMessage(format: String, boldPart: String): AnnotatedString {
    val message = format.replace("%1\$s", boldPart)

    return buildAnnotatedString {
        val startIndex = message.indexOf(boldPart)
        val endIndex = startIndex + boldPart.length

        append(message)

        if (startIndex in 0 until endIndex) {
            addStyle(
                style = SpanStyle(
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 16.sp
                ),
                start = startIndex,
                end = endIndex
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCustomersRatingsBottomSheet(
    givenByCustomerRating: Float,
    givenByCustomerReview: String,
    reviewToCustomerRating: Float,
    reviewToCustomerReview: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.ratings),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (givenByCustomerRating != 0.0f) {
                Text(
                    text = stringResource(Res.string.customer_Rating),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3A2D94)
                )

                BottomSheetRatingRow(rating = givenByCustomerRating)

                Spacer(modifier = Modifier.height(8.dp))

                if (givenByCustomerReview.isNotEmpty()) {
                    Text(
                        text = givenByCustomerReview,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

            }
            if (givenByCustomerRating != 0.0f && reviewToCustomerRating != 0.0f) {
                HorizontalDivider(
                    color = Color(0xFFE5E5E5),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            if (reviewToCustomerRating != 0.0f) {
                Text(
                    text = stringResource(Res.string.provider_review), // "Provider's Rating" or "Rating from Provider"
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3A2D94) // Purple
                )

                BottomSheetRatingRow(rating = reviewToCustomerRating)

                Spacer(modifier = Modifier.height(8.dp))

                if (reviewToCustomerReview.isNotEmpty()) {
                    Text(
                        text = reviewToCustomerReview,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
 fun BottomSheetRatingRow(rating: Float) {
    val emoji = when {
        rating < 2 -> "😞"
        rating < 4 -> "😐"
        else -> "😊"
    }
    val label = when {
        rating < 2 ->  stringResource(Res.string.poor)
        rating < 4 ->  stringResource(Res.string.avg)
        else ->  stringResource(Res.string.great)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 2.dp)
    ) {
        repeat(5) { index ->
            val color = if (index < rating.toInt()) Color(0xFFFFC107) else Color(0xFFE0E0E0)
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "",
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${rating.toInt()}/5 ($label) $emoji",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

fun String.to24HourFormat(): String {
    val startTime = this.split(" - ")[0]

    val parts = startTime.split(" ")
    val timePart = parts[0]
    val period = parts[1]

    val timeParts = timePart.split(":")
    val hours = timeParts[0].toInt()
    val minutes = timeParts[1]

    val hour24 = when {
        period == "PM" && hours != 12 -> hours + 12
        period == "AM" && hours == 12 -> 0
        else -> hours
    }

    return "${hour24.toString().padStart(2, '0')}:${minutes}:00"
}


@Composable
fun CancellationPolicies(
    onCloseClick: () -> Unit
) {
    val policies = listOf(
        Res.string.policy_customer_cancel_30min,
        Res.string.policy_customer_cancel_2hr,
        Res.string.policy_customer_cancel_before_2hr,
        Res.string.policy_customer_cancel_anytime,
        Res.string.policy_customer_cancel_30min_provider,
        Res.string.policy_customer_cancel_2hr_provider,
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.cancel_policy),
                fontSize = 16.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.size(30.dp)
                    .padding(end = 10.dp)
                    .clickable { onCloseClick() }
            )
        }

        policies.forEach { resId ->
            PolicyItem(policyText = stringResource(resId))
        }
    }
}

@Composable
fun PolicyItem(policyText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "•",
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = policyText,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )
    }
}