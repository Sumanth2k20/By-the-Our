package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.accept
import bytheour.composeapp.generated.resources.accept_booking_request
import bytheour.composeapp.generated.resources.accepted_Refund
import bytheour.composeapp.generated.resources.address_ico1
import bytheour.composeapp.generated.resources.america
import bytheour.composeapp.generated.resources.amount
import bytheour.composeapp.generated.resources.are_you_sure_accept_the_booking_request
import bytheour.composeapp.generated.resources.are_you_sure_decline_the_booking_request
import bytheour.composeapp.generated.resources.arrowleft_ico
import bytheour.composeapp.generated.resources.avg
import bytheour.composeapp.generated.resources.awaiting_user_confirmation_to_end_job
import bytheour.composeapp.generated.resources.awaiting_user_confirmation_to_start_job
import bytheour.composeapp.generated.resources.bill_details
import bytheour.composeapp.generated.resources.billing_summary
import bytheour.composeapp.generated.resources.bookingId
import bytheour.composeapp.generated.resources.booking_Cancelled
import bytheour.composeapp.generated.resources.booking_ID
import bytheour.composeapp.generated.resources.booking_Request
import bytheour.composeapp.generated.resources.booking_by_you
import bytheour.composeapp.generated.resources.booking_info_ico
import bytheour.composeapp.generated.resources.calendar_ico
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.cancelByCustomer
import bytheour.composeapp.generated.resources.cancelByYou
import bytheour.composeapp.generated.resources.cancel_by
import bytheour.composeapp.generated.resources.cancel_reasons
import bytheour.composeapp.generated.resources.cancelled
import bytheour.composeapp.generated.resources.cancelled_ico
import bytheour.composeapp.generated.resources.chat
import bytheour.composeapp.generated.resources.citing_concerns_regarding
import bytheour.composeapp.generated.resources.complete
import bytheour.composeapp.generated.resources.completed_ico
import bytheour.composeapp.generated.resources.confirmation_Job
import bytheour.composeapp.generated.resources.convenience_fee
import bytheour.composeapp.generated.resources.customer
import bytheour.composeapp.generated.resources.customer_Details
import bytheour.composeapp.generated.resources.customer_Rating
import bytheour.composeapp.generated.resources.customer_Review_Rating
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.decline
import bytheour.composeapp.generated.resources.decline_booking_request
import bytheour.composeapp.generated.resources.directions_ico
import bytheour.composeapp.generated.resources.donloadinvoice_ico
import bytheour.composeapp.generated.resources.end_Inspection
import bytheour.composeapp.generated.resources.end_Job
import bytheour.composeapp.generated.resources.end_Job_Confirmation
import bytheour.composeapp.generated.resources.end_Service
import bytheour.composeapp.generated.resources.given_by_Customer
import bytheour.composeapp.generated.resources.great
import bytheour.composeapp.generated.resources.hours_details
import bytheour.composeapp.generated.resources.howCancelBooking
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.inspectionId
import bytheour.composeapp.generated.resources.inspectionRate
import bytheour.composeapp.generated.resources.mediation_amount_refunded
import bytheour.composeapp.generated.resources.mediation_declined
import bytheour.composeapp.generated.resources.mediation_declined_dec
import bytheour.composeapp.generated.resources.mediation_has_been
import bytheour.composeapp.generated.resources.miles
import bytheour.composeapp.generated.resources.no_address
import bytheour.composeapp.generated.resources.paymentMethod
import bytheour.composeapp.generated.resources.payment_Request_for_this_service
import bytheour.composeapp.generated.resources.phone_call
import bytheour.composeapp.generated.resources.poor
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.rate_Customer
import bytheour.composeapp.generated.resources.rate_service
import bytheour.composeapp.generated.resources.ratecustomer_ico
import bytheour.composeapp.generated.resources.ratings
import bytheour.composeapp.generated.resources.reason
import bytheour.composeapp.generated.resources.refund
import bytheour.composeapp.generated.resources.requesting_Mediation
import bytheour.composeapp.generated.resources.review_rating
import bytheour.composeapp.generated.resources.review_to_Customer
import bytheour.composeapp.generated.resources.send_estimation
import bytheour.composeapp.generated.resources.service_completed
import bytheour.composeapp.generated.resources.start_Inspection
import bytheour.composeapp.generated.resources.start_Job
import bytheour.composeapp.generated.resources.start_Job_Confirmation
import bytheour.composeapp.generated.resources.start_Service
import bytheour.composeapp.generated.resources.subTotal
import bytheour.composeapp.generated.resources.submit
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.time_slot
import bytheour.composeapp.generated.resources.total_amount
import bytheour.composeapp.generated.resources.total_amount_paid
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.view_Ratings
import bytheour.composeapp.generated.resources.waiting_user_accept_estimation
import bytheour.composeapp.generated.resources.write_a_review
import bytheour.composeapp.generated.resources.your_service_has_been_completed
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.presentations.customcomponents.formatDate
import com.bytheour.presentations.screens.tabs.bookings.HoursDetailsPopover
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.bookings.RatingRow
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.BillingSummaryContent
import com.bytheour.utils.COMPLETED
import com.bytheour.utils.Cancelled
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.MapNavigator
import com.bytheour.utils.ONGOING
import com.bytheour.utils.PENDING
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.StarRating
import com.bytheour.utils.Strings
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.UPCOMING
import com.bytheour.utils.createMapNavigator
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getFormattedDistanceInMiles
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderBookingInfoScreen(
    navigateToBookingTabScreen: () -> Unit,
    proBookingViewModel: JTProviderBookingTabScreenViewmodel = getKoinViewModel(),
    navigateToOneOnOneChat: (String, String, Boolean,String,String?) -> Unit,
    navigateToSendEstimationScreen:(String,String,String)->Unit,
    navigateProviderCancelReasonScreen:(String)->Unit,
    bookingViewModel:JTBookingTabViewmodel = getKoinViewModel(),
    navigateToMediationScreen:(String, String, String) ->Unit,
    navigateToInspectionReportScreen:(String,String,String,String)-> Unit,
    bookingId:String,
) {

    LaunchedEffect(Unit){
        proBookingViewModel.fetchBookingDetails(bookingId)
    }
    val bookingData by proBookingViewModel.bookingData.collectAsState()
    val flags = bookingData.flags ?: ModelProviderBookingList.Flags()
    val mediation = bookingData.mediation ?: ModelProviderBookingList.Mediation()
    var showStartJobButton by remember { mutableStateOf(false) }
    var showEndJobButton by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf<DialogType?>(null) }
    var jobTypeType by remember { mutableStateOf<JobType?>(null) }
    var showBillCard by remember { mutableStateOf(false) }
    var showMediationRequestBox by remember { mutableStateOf(false) }
    var amountProcessing by remember { mutableStateOf(false) }
    var showPaymentDone by remember { mutableStateOf(false) }
    var showDateCard by remember { mutableStateOf(false) }
    var showCompletedCard by remember { mutableStateOf(false) }
    var showCancelBox by remember { mutableStateOf(false) }
    var showCancelRatingCard by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showViewRatingBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showRateCustomerButton by remember { mutableStateOf(false) }
    var showBillingDetailsSheet by remember { mutableStateOf(false) }
    val amount = bookingData.bill?.amount?:0.0
    val feePercent = bookingData.bill?.commission?.percentage?:0.0
    val customerLat = bookingData.customerLat?.toDoubleOrNull()
    val customerLong = bookingData.customerLong?.toDoubleOrNull()
    val providerLat = bookingData.workerDetails?.workArea?.lat?.toDoubleOrNull()
    val providerLong = bookingData.workerDetails?.workArea?.lng?.toDoubleOrNull()
    val distanceInMiles = getFormattedDistanceInMiles(
        lat1 = customerLat,
        lon1 = customerLong,
        lat2 = providerLat,
        lon2 = providerLong
    ) ?: "N/A"

    val total = (amount * feePercent / 100).toInt()
    val subtotal = amount + total
    if(showBillingDetailsSheet){
        ModalBottomSheet(
            onDismissRequest = { showBillingDetailsSheet = false },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            dragHandle = null
        ) {
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
                ProviderReviewAndRatingBottomSheet(
                    serviceName = bookingData.title,
                    bookingId = bookingData.bookingId ?: "",
                    providerName = bookingData.customer?.firstName + bookingData.customer?.lastName,
                    onSubmitClick = { serviceRating, _, review ->
                        bookingViewModel.addRating(
                            bookingId = bookingData.bookingId ?: "",
                            serviceRating = serviceRating ?: "",
                            rating = serviceRating?:"",
                            review = review
                        )

                        showBottomSheet = false
                    },
                    profileImage = bookingData.customer?.profile?:"",
                    onCloseClick = { showBottomSheet = false },
                    address = bookingData.address
                )
            }
        }
    }
    if (showViewRatingBottomSheet) {
        ViewRatingsBottomSheet(
            givenByCustomerRating = bookingData.reviewList?.taken?.rating?.toFloat()?:0.0f,
            givenByCustomerReview = bookingData.reviewList?.taken?.review?:"",
            reviewToCustomerRating = bookingData.reviewList?.given?.rating?.toFloat()?:0.0f,
            reviewToCustomerReview = bookingData.reviewList?.given?.review?:"",
            onDismiss = { showViewRatingBottomSheet = false }
        )
    }
    val maxHeight = 400.dp
    val minHeight = 200.dp
    val density = LocalDensity.current
    var headerHeightPx by remember { mutableStateOf(with(density) { maxHeight.toPx() }) }
    val headerHeightDp = with(density) { headerHeightPx.toDp() }
    val scrollState = rememberScrollState()
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
                .padding(bottom = 60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeightDp)
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

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    if (flags.type == UPCOMING && flags.isQuotationSent && !flags.isQuotationAccepted) {
                        AwaitingConfirmationBox(
                            modifier = Modifier.wrapContentWidth(),
                            text = stringResource(Res.string.waiting_user_accept_estimation)
                        )
                    } else if (flags.type == UPCOMING && flags.isQuotationSent && flags.isQuotationAccepted && !flags.isPaymentConfirmed) {
                        AwaitingConfirmationBox(
                            modifier = Modifier.wrapContentWidth(),
                            text = stringResource(Res.string.payment_Request_for_this_service)
                        )
                    } else if (flags.type == UPCOMING &&
                        flags.isQuotationSent &&
                        flags.isQuotationAccepted &&
                        flags.paid && flags.isPaymentConfirmed) {

                        showStartJobButton = true
                        showBillCard = true

                    } else if (flags.type.equals(ONGOING, ignoreCase = true) &&
                        flags.isQuotationSent &&
                        flags.isQuotationAccepted &&
                        flags.paid &&
                        flags.isPaymentConfirmed &&
                        flags.jobStartedByWorker &&
                        flags.jobStartedByUser &&
                        flags.jobEndedByWorker
                    ) {
                        AwaitingConfirmationBox(
                            modifier = Modifier.wrapContentWidth(),
                            text = stringResource(Res.string.awaiting_user_confirmation_to_end_job)
                        )
                    } else if (flags.type.equals(ONGOING, ignoreCase = true) &&
                        flags.isQuotationSent &&
                        flags.isQuotationAccepted &&
                        flags.paid &&
                        flags.isPaymentConfirmed &&
                        flags.jobStartedByWorker &&
                        flags.jobStartedByUser
                    ) {
                        showEndJobButton = true
                        showBillCard = true

                    } else if (flags.type.equals(ONGOING, ignoreCase = true) &&
                        flags.isQuotationSent &&
                        flags.isQuotationAccepted &&
                        flags.paid &&
                        flags.isPaymentConfirmed &&
                        flags.jobStartedByWorker
                    ) {
                        AwaitingConfirmationBox(
                            modifier = Modifier.wrapContentWidth(),
                            text =  stringResource(Res.string.awaiting_user_confirmation_to_start_job)
                        )
                        showBillCard = true
                    }
                    else if (flags.type.equals(UPCOMING, ignoreCase = true) && flags.isInspection ) {
                        showStartJobButton = true
                        showBillCard = true
                    }
                    else if (flags.type.equals(ONGOING, ignoreCase = true) && flags.isInspection && !flags.jobEndedByWorker && flags.jobStartedByUser) {
                        showEndJobButton = true
                        showBillCard = true
                    }
                    else if (flags.type.equals(ONGOING, ignoreCase = true) && flags.isInspection && flags.jobEndedByWorker) {
                        showBillCard = true
                    }
                    else if(flags.type.equals(COMPLETED, ignoreCase = true)&&
                        flags.isQuotationSent &&
                        flags.isQuotationAccepted &&
                        flags.paid &&
                        flags.isPaymentConfirmed &&
                        flags.jobStartedByWorker
                    ) {
                        showBillCard = true
                        showDateCard = true
                        showCompletedCard = true
                        Row {
                            CompletedButton(
                                text = stringResource(Res.string.complete),
                                icon = painterResource(Res.drawable.completed_ico),
                                color = Color(0xFF7BD92F)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                        }
                        if (mediation.mediationRaised.equals("y", ignoreCase = true) && mediation.mediationAcceptAmount == null){
                            showMediationRequestBox = true
                            showBillCard = true
                            showDateCard = true
                        }
                        if (mediation.mediationAcceptAmount != null && mediation.amountProcessed.equals("n", ignoreCase = true)){
                            amountProcessing = true
                            showMediationRequestBox = false
                            showBillCard = true
                            showDateCard = true
                        }

                        if (mediation.mediationStatus.equals("y", ignoreCase = true) && mediation.amountProcessed.equals("y", ignoreCase = true)){
                            showBillCard = true
                            showDateCard = true
                            showPaymentDone = true
                        }


                    }
                    else if(flags.type.equals(COMPLETED, ignoreCase = true)&&
                        flags.isInspection &&
                        flags.paid &&
                        flags.isPaymentConfirmed &&
                        flags.jobStartedByWorker
                    ) {
                        showBillCard = true
                        showDateCard = true
                    }
                    else if (flags.type.equals(Cancelled, ignoreCase = true) && flags.cancelled){
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
                                text =  stringResource(Res.string.cancelled),
                                color = JTColors.cancelTextColor,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                        showDateCard = true
                        showCancelBox= true
                        showCancelRatingCard = true
                    }


                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bookingData.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getInterRegular(),
                                    color = JTColors.cancelId,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(
                                    if (flags.isInspection)
                                        stringResource(Res.string.inspectionId)
                                    else
                                        stringResource(Res.string.bookingId)
                                )
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                                    color = JTColors.WhiteColor,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(bookingId)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y= (-20).dp)
                    .verticalScroll(scrollState)
                    .background(JTColors.WhiteColor,
                        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .background(JTColors.grayDivider, shape = RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (showCancelBox){
                    Column (Modifier.padding(start = 20.dp, end = 20.dp)){
                        BookingCancelBox(
                            cancelledBy = bookingData.cancellation?.cancelledBy?:"" ,
                            otherPartyName = bookingData.customer?.lastName
                                ?: ("" + bookingData.customer?.firstName),
                            reason = bookingData.cancellation?.reason ?: ""
                        )
                    }
                }
                if (showMediationRequestBox){
                    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                        MediationRequestBox(
                            amount = mediation.mediationAmount ?: "",
                            onAcceptClick = { navigateToMediationScreen(bookingData.bookingId?:"",bookingData.mediation?.mediationAmount?:"",subtotal.toString())}
                        )
                    }
                }


                if (amountProcessing){
                    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                        MediationAcceptBox7days(
                            amount =mediation.mediationAcceptAmount ?:""
                        )
                    }
                }
                if (showPaymentDone){
                    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                        RefundSuccessOutlinedBox(amount = mediation.mediationAmount?:"")
                    }
                }

                if (showDateCard){
                    Spacer(modifier = Modifier.height(16.dp))
                    DateTimeCard(
                        date = formatDate(bookingData.date) ?:"",
                        time = bookingData.time
                    )
                }

                if (showCancelRatingCard){
                    ProviderBookingCancelledCard(
                        cancelledBy = bookingData.cancellation?.cancelledBy?:"" ,
                        otherPartyName = bookingData.customer?.lastName
                            ?: ("" + bookingData.customer?.firstName),
                        cancellationReason = bookingData.cancellation?.reason?:"",
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (
                    bookingData.flags?.type.equals(PENDING, ignoreCase = true) ||
                    bookingData.flags?.type.equals(UPCOMING, ignoreCase = true) ||
                    bookingData.flags?.type.equals(ONGOING, ignoreCase = true)
                ) {
                    Text(
                        text = stringResource(Res.string.customer_Details),
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomerDetailsCard(
                        date = formatDateString(bookingData.date),
                        time = bookingData.time,
                        imageUrl = bookingData.customer?.profile?:"",
                        customerName = bookingData.customer?.firstName ?: "N/A",
                        phoneNumber = bookingData.customer?.contact?.number ?: "N/A",
                        rating = bookingData.customer?.portfolio?.ratings?.toFloat() ?: 0.0f,
                        onChatClick = {
                            navigateToOneOnOneChat(
                                bookingData.customer?.fullName ?: "",
                                "",
                                false,
                                bookingData.customer?.profile ?: "",
                                bookingData.customer?.id
                            )
                        },
                        milesDistance = distanceInMiles
                    )
                }
                if ((flags.type.equals(PENDING, ignoreCase = true)) ||
                    (flags.type.equals(ONGOING, ignoreCase = true)) ||
                    (flags.type.equals(UPCOMING, ignoreCase = true))
                )
                {
                    val map:MapNavigator = createMapNavigator()
                    val  lat = bookingData.addressList?.lat
                    val lang = bookingData.addressList?.lng
                    val pinCode = bookingData.addressList?.address?.zip
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (bookingData.address.isEmpty()){
                            Text(
                                text = stringResource(Res.string.no_address),
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }else{
                            Row(Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.Center) {
                                Image(
                                    painter = painterResource(Res.drawable.address_ico1),
                                    contentDescription = "",
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = bookingData.address,
                                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                        Image(
                            painter = painterResource(Res.drawable.directions_ico),
                            contentDescription = "",
                            modifier = Modifier.clickable  { map.openGoogleMaps(latitude = lat?.toDouble()?:0.0,lang?.toDouble()?:0.0,pinCode?:"") }
                        )
                    }
                }
                if (showCompletedCard) {
                    val givenReview = bookingData.reviewList?.given
                    val takenReview = bookingData.reviewList?.taken

                    ProviderServiceCompletedCard(
                        servicePersonName = bookingData.customer?.firstName ?: "",
                        reviewGiven = givenReview != null,
                        reviewTaken = takenReview != null,
                        rating = takenReview?.rating ?: 0,
                        onViewRatingsClick = { showViewRatingBottomSheet = true },
                        rateCustomer = {
                            showRateCustomerButton = true
                        },
                        onRateServiceClick = {showBottomSheet = true}
                    )
                }
                if (showBillCard){
                    val bill = bookingData.bill
                    BillDetailsCard(
                        amount = amount,
                        feePercent = feePercent,
                        onDecline = {},
                        onAccept = {},
                        isVisible = false,
                        showPaymentMethod = bookingData.flags?.type.equals(COMPLETED, ignoreCase = true),
                        method = bill?.payment?.mode ?: "",
                        bookedHours = bookingData.hours ?: 0.0,
                        extraHours = bookingData.extraHours ?: "",
                        isInspection = bookingData.isInspection?:false,
                        inspectionRate = bookingData.inspectionCalculatedAmount?:0.0
                    )

                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .align(Alignment.TopCenter)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.arrowleft_ico),
                contentDescription = "",
                modifier = Modifier.clickable { navigateToBookingTabScreen() }
            )
            Text(
                stringResource(Res.string.booking_Request),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
            Icon(
                painter = painterResource(Res.drawable.booking_info_ico),
                contentDescription = "",
                tint = Color.Transparent
            )
        }

        if (
            bookingData.flags?.type.equals(COMPLETED, ignoreCase = true) ||
            showStartJobButton ||
            showEndJobButton ||
            (bookingData.flags?.type.equals(UPCOMING, ignoreCase = true) && flags.accepted && !flags.isQuotationSent)
            || bookingData.flags?.type.equals(PENDING, ignoreCase = true)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (showStartJobButton || showEndJobButton){
                            Color.Transparent
                        }else{
                            Color(0xFFEEEDF3)
                        }
                    )
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(87.dp),
                contentAlignment = Alignment.Center
            ) {
                if (bookingData.flags?.type.equals(COMPLETED, ignoreCase = true)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =  Arrangement.Center
                    ) {
                        if (showRateCustomerButton) {
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
                                        text = stringResource(Res.string.rate_Customer),
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
                val currentDate = LocalDate.now().toString()
                val serviceDate = bookingData.date
                when {
                    showStartJobButton -> {
                        StartServiceButton(
                            onClick = { jobTypeType = JobType.StartJob },
                            text =if (flags.isInspection)stringResource(Res.string.start_Inspection) else stringResource(Res.string.start_Service),
                            color = Color(0xFF7BD92F),
                            detail = true,
                            enable = currentDate == serviceDate
                        )
                    }

                    showEndJobButton -> {
                        StartServiceButton(
                            onClick = {
                                if (flags.isInspection) {
                                    navigateToInspectionReportScreen(bookingData.workerName,bookingData.title,bookingData.image,bookingId)
                                } else {
                                    jobTypeType = JobType.EndJob
                                }
                            },
                            text = if (flags.isInspection)stringResource(Res.string.end_Inspection) else stringResource(Res.string.end_Service),
                            color = Color.Red,
                            detail = true
                        )
                    }

                    (bookingData.flags?.type.equals(UPCOMING, ignoreCase = true)
                            && flags.accepted
                            && !flags.isQuotationSent) && !flags.isInspection -> {

                        AcceptDeclineButtonRow(
                            onDecline = {
                                navigateProviderCancelReasonScreen(
                                    bookingData.bookingId ?: ""
                                )
                            },
                            onAccept = {
                                navigateToSendEstimationScreen(
                                    bookingData.bookingId ?: "",
                                    bookingData.time,
                                    bookingData.costPerHour.toString()
                                )
                            },
                            acceptText = stringResource(Res.string.send_estimation),
                            declineText = stringResource(Res.string.cancel),
                        )
                    }

                    bookingData.flags?.type.equals(PENDING, ignoreCase = true) -> {
                        AcceptDeclineButtonRow(
                            onDecline = { dialogType = DialogType.Decline },
                            onAccept = { dialogType = DialogType.Accept },
                            acceptText = stringResource(Res.string.accept),
                            declineText = stringResource(Res.string.decline),
                        )
                    }


                }
            }


        }

        if (dialogType != null) {
            val isAccept = dialogType is DialogType.Accept
            CustomAlertDialog(
                showDialog = true,
                onDismiss = { dialogType = null },
                title = if (isAccept) stringResource(Res.string.accept_booking_request) else stringResource(Res.string.decline_booking_request),
                message = if (isAccept)
                    stringResource(Res.string.are_you_sure_accept_the_booking_request)
                else
                    stringResource(Res.string.are_you_sure_decline_the_booking_request),
                yes = true,
                no = true,
                yesText = if (isAccept) stringResource(Res.string.accept) else stringResource(Res.string.decline) ,
                noText = stringResource(Res.string.cancel),
                yesButtonColor = JTColors.enableButton,
                noButtonColor = Color.White,
                onConfirm = {
                    if (isAccept) {
                        proBookingViewModel.acceptBooking(
                            bookingId = bookingData.bookingId ?: "" ,
                            onSuccess = {
                            dialogType = null
                            navigateToBookingTabScreen()
                            proBookingViewModel.onTabSelected(1)
                        })
                    } else {
                        proBookingViewModel.declineBooking(bookingId = bookingData.bookingId ?:"", onSuccess = {
                            dialogType = null
                            navigateToBookingTabScreen()
                        })
                    }

                },
                onCancel = {
                    dialogType = null
                }
            )
        }
        LaunchedEffect(proBookingViewModel.uiStates.value.message) {
            if (!proBookingViewModel.uiStates.value.message.isNullOrBlank() &&
                !proBookingViewModel.uiStates.value.isSuccess &&
                jobTypeType != null) {
                jobTypeType = null
                dialogType = null
            }
        }
        if (jobTypeType != null) {
            val isStart = jobTypeType is JobType.StartJob
             CustomAlertDialog(
                showDialog = true,
                onDismiss = {
                    jobTypeType = null
                },
                title = stringResource(Res.string.confirmation_Job),
                message = if (isStart)
                    stringResource(Res.string.start_Job_Confirmation)
                else
                    stringResource(Res.string.end_Job_Confirmation),
                yes = true,
                no = true,
                yesText = if (isStart) stringResource(Res.string.start_Job) else stringResource(Res.string.end_Job),
                noText = stringResource(Res.string.cancel),
                yesButtonColor = JTColors.enableButton,
                noButtonColor = Color.White,
                onConfirm = {
                    if (isStart) {
                        jobTypeType = null
                        proBookingViewModel.startJob(
                            bookingId = bookingData.bookingId?:"",
                            onSuccess = {
                                navigateToBookingTabScreen()
                                proBookingViewModel.onTabSelected(1)
                            }
                        )
                    } else {
                        jobTypeType = null
                        proBookingViewModel.endJob(
                            bookingId = bookingData.bookingId?:"",
                            onSuccess = {
                                navigateToBookingTabScreen()
                                proBookingViewModel.onTabSelected(1)
                            }
                        )
                    }
                    jobTypeType = null
                },
                onCancel = {
                    jobTypeType = null
                    dialogType = null
                }
            )
        }
        if (!proBookingViewModel.uiStates.value.message.isNullOrBlank()) {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 70.dp)){
                CommonSnackbar(
                    message = proBookingViewModel.uiStates.value.message?:"",
                    type = if (proBookingViewModel.uiStates.value.isSuccess) SnackbarType.SUCCESS else SnackbarType.ERROR,
                    onDismiss = { proBookingViewModel.clearMessage() }
                )
            }
        }
    }
}


@Composable
fun CustomerDetailsCard(
    imageUrl: String,
    customerName: String,
    rating: Float,
    phoneNumber: String,
    date: String,
    time: String,
    onChatClick: () -> Unit,
    milesDistance :String,
) {
    Card(
        modifier = Modifier
            .height(155.dp)
            .fillMaxWidth(0.92f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(JTColors.timeSlotCardColor)
    ) {
        Column{
            Column (modifier = Modifier.background(Color(0xFFEEEDF6))){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (imageUrl.isNullOrBlank()){
                            Image(
                                painter = painterResource(Res.drawable.provider_logo),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = customerName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            StarRating(rating.toDouble())

                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(Res.drawable.phone_call),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                JtBtoCommonText(
                                    text = "+${phoneNumber}",
                                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                    fontSize = 10.sp,
                                    modifier = if (platform.equals("IOS", ignoreCase = true)) {
                                        Modifier.padding(top = 2.dp)
                                    } else {
                                        Modifier // no padding
                                    }
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Icon(
                                    painter = painterResource(Res.drawable.directions_ico),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                JtBtoCommonText(
                                    text = milesDistance + stringResource(Res.string.miles),
                                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                    fontSize = 10.sp,
                                    modifier = if (platform.equals("IOS", ignoreCase = true)) {
                                        Modifier.padding(top = 2.dp)
                                    } else {
                                        Modifier // no padding
                                    }
                                )
                            }
                        }
                    }


                    Image(
                        painter = painterResource(Res.drawable.chat),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp).clickable { onChatClick() }.clip(RoundedCornerShape(20.dp))
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            Column (modifier = Modifier.background(Color(0xFFF8F7FE))){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.calendar_ico),
                            contentDescription = "",
                            tint = JTColors.enableButton,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
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

                    VerticalDivider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp).align(Alignment.CenterVertically),
                        color = Color(0xFFE0E0E0),
                        thickness = 2.dp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.time_ico),
                            contentDescription = "",
                            tint = JTColors.enableButton,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
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
    }
}



@Composable
fun AcceptDeclineButtonRow(
    onDecline: () -> Unit,
    onAccept: () -> Unit,
    acceptText:String,
    declineText:String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedButton(
            onClick = onDecline,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .weight(1f)
                .height(45.dp)
        ) {
            Text(
                text = declineText,
                color = JTColors.enableButton,
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 14.sp,
                lineHeight = 15.sp
            )
        }

        Button(
            onClick = onAccept,
            colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .weight(1f)
                .height(45.dp)
        ) {
            Text(
                text = acceptText,
                color = Color.White,
                fontFamily = JtBtoFonts.getInterMediumFont(),
                fontSize = 14.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun DateTimeCard(
    date:String,
    time :String,
    isForBottomSheet:Boolean = false
) {
    Card(
        modifier =
            if (isForBottomSheet){
                Modifier.fillMaxWidth()
            }else{
                Modifier.fillMaxWidth(0.9f)
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(if(isForBottomSheet) Color.White else  JTColors.timeSlotCardColor)
    ) {
        Row(
            modifier = if (isForBottomSheet) Modifier.padding(top = 10.dp, bottom = 10.dp)else Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(Res.drawable.calendar_ico),
                    contentDescription = ""
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
                    contentDescription = ""
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
fun RefundSuccessOutlinedBox(
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFB2E8A3),
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = Color(0xFFE6FFCC), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.mediation_amount_refunded) + amount,
            color = Color(0xFF2E7D32),
            fontFamily = JtBtoFonts.getInterMediumFont(),
            fontSize = 11.sp
        )

        Image(
            painter = painterResource(Res.drawable.refund),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun MediationRequestBox(
    amount: String,
    onAcceptClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEEEDC), RoundedCornerShape(16.dp))
            .padding(12.dp)
            .height(80.dp)
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
                    text = stringResource(Res.string.requesting_Mediation),
                    color = JTColors.enableButton,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(Res.string.mediation_has_been)+ "$$amount," + stringResource(Res.string.citing_concerns_regarding),
                    color = Color(0xFF313131),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    lineHeight = 14.sp,
                )
            }

            Button(
                onClick = onAcceptClick,
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF2F1C6A)
                ),
                shape = CircleShape,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(38.dp)
            ) {
                Text(
                    text = stringResource(Res.string.accept),
                    color = Color.White,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun MediationAcceptBox7days(
    amount: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEEEDC), RoundedCornerShape(16.dp))
            .padding(12.dp)
            .wrapContentHeight()
    ) {
        Column {
        Text(
            text =
                if (amount == "0"){
                    stringResource(Res.string.mediation_declined)
                }else{
                    stringResource(Res.string.accepted_Refund) + " $$amount"
                },
            color = JTColors.enableButton,
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            lineHeight = 12.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text =if (amount == "0"){
                stringResource(Res.string.mediation_declined_dec)
            }else{
                Strings.MEDIATION_ACCEPTED_PROVIDER.replace("%s","$$amount")
            },
            color = Color(0xFF313131),
            fontSize = 11.sp,
            fontFamily = JtBtoFonts.getInterRegular(),
            lineHeight = 11.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        }
    }
}


@Composable
fun BookingCancelBox(
    cancelledBy: String,
    otherPartyName: String = "",
    reason: String
) {
    val isProvider = cancelledBy.lowercase() == TYPE_PROVIDER

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFD7D7), RoundedCornerShape(16.dp))
            .padding(start = 16.dp, end = 16.dp)
            .wrapContentHeight()
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(
                text = stringResource(Res.string.booking_Cancelled),
                color = Color.Red.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                lineHeight = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isProvider)
                    stringResource(Res.string.booking_by_you)
                else
                    stringResource(Res.string.cancel_by) + " $otherPartyName",
                color = Color(0xFF313131),
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                lineHeight = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(Res.string.reason) + " $reason",
                color = Color(0xFF313131),
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                lineHeight = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}


@Composable
fun ProviderBookingCancelledCard(
    cancelledBy: String,
    otherPartyName: String = "", // Use this for customer name when customer cancels
    cancellationReason: String = "",
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(JTColors.serviceBookedCardColor),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (cancelledBy.lowercase()) {
                TYPE_PROVIDER -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(JTColors.BackGroundBlueColor, JTColors.LightBlue)
                                )
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
                                text = stringResource(Res.string.howCancelBooking),
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontFamily = JtBtoFonts.getInterFont(),
                                color = JTColors.CancelReasonTextColor
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel_reasons),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = JTColors.textFieldColor,
                            fontFamily = JtBtoFonts.getInterFont()
                        )
                        Text(
                            text = cancellationReason,
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.BackGroundBlueColor
                        )
                    }

                }

                TYPE_CUSTOMER -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(JTColors.BackGroundBlueColor, JTColors.LightBlue)
                                )
                            )
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text =  stringResource(Res.string.cancel_by)+" $otherPartyName",
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                color = JTColors.WhiteColor,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "'$otherPartyName' ${stringResource(Res.string.cancelByCustomer)}",
                                fontSize = 11.sp,
                                lineHeight = 11.sp,
                                fontFamily = JtBtoFonts.getInterFont(),
                                color = JTColors.CancelReasonTextColor
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel_reasons),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = JTColors.textFieldColor,
                            fontFamily = JtBtoFonts.getInterFont()
                        )
                        Text(
                            text = cancellationReason,
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            color = JTColors.BackGroundBlueColor
                        )
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
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isVisible :Boolean? = true,
    showPaymentMethod:Boolean,
    method:String,
    bookedHours: Double,
    extraHours: String,
    isInspection: Boolean,
    inspectionRate: Double
) {
    var showHoursDetails by remember { mutableStateOf(false) }
    val fee = if (isInspection) 0.0 else (amount * feePercent / 100)
    val subtotal = if (isInspection) amount else amount + fee
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .wrapContentHeight()
            .border(1.dp,
                color = JTColors.lightGrayColor.copy(0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(JTColors.WhiteColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(JTColors.serviceBookedCardColor)
                .height(37.dp)
                .padding( start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.bill_details),
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                fontSize = 14.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.hours_details),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 10.sp,
                    color = JTColors.lightGrayColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp).clickable { showHoursDetails = true  },
                )
                HoursDetailsPopover(
                    expanded = showHoursDetails,
                    onDismissRequest = { showHoursDetails = false },
                    bookedHours = bookedHours,
                    extraHours = extraHours
                )
            }
        }
        if(showPaymentMethod) {
            HorizontalDivider(color = Color(0xFFEAEAEA))
            BillRow(label = stringResource(Res.string.paymentMethod), value = method)
        }
        HorizontalDivider(color = Color(0xFFEAEAEA))
        if (isInspection) {
            BillRow(
                label = stringResource(Res.string.inspectionRate),
                value = "$$inspectionRate"
            )
            HorizontalDivider(color = Color(0xFFEAEAEA))
            BillRow(
                label = stringResource(Res.string.total_amount),
                value = "$$inspectionRate",
                isSubtotal = true
            )
        } else {
            BillRow(label = stringResource(Res.string.amount), value = "$$amount")
            HorizontalDivider(color = Color(0xFFEAEAEA))
            BillRow(
                label = stringResource(Res.string.convenience_fee) + " (${feePercent}%)",
                value = "$$fee"
            )
            HorizontalDivider(color = Color(0xFFEAEAEA))

            if (!showPaymentMethod) {
                BillRow(
                    label = stringResource(Res.string.subTotal),
                    value = "$$subtotal",
                    isSubtotal = true
                )
            }

            if (showPaymentMethod) {
                HorizontalDivider(color = Color(0xFFEAEAEA))
                BillRow(
                    label = stringResource(Res.string.total_amount_paid),
                    value = "$$subtotal",
                    isSubtotal = true
                )
            }
        }

        HorizontalDivider(color = Color(0xFFEAEAEA))
        if (isVisible == true) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onDecline,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        stringResource(Res.string.decline),
                        color = Color(0xFF4B2DC9)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        stringResource(Res.string.accept),
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun BillRow(label: String,
            value: String,
            isSubtotal:Boolean = false
            ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(37.dp)
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = if(isSubtotal) JtBtoFonts.getOpenSansBold() else JtBtoFonts.getOpenSansRegular(),
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getOpenSansBold()
        )
    }
}


@Composable
fun ProviderServiceCompletedCard(
    servicePersonName: String,
    reviewGiven: Boolean,
    reviewTaken: Boolean,
    rating: Int,
    onViewRatingsClick: () -> Unit,
    onRateServiceClick:()->Unit,
    rateCustomer:()->Unit,
    isCustomer :Boolean = false
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
                        text = servicePersonName + stringResource(Res.string.your_service_has_been_completed),
                        color = Color(0xFFC9C9C9),
                        fontSize = 11.sp,
                        lineHeight = 11.sp
                    )
                }
                if (!reviewGiven && reviewTaken){
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        if (rating != 0) {
                            RatingBadge(rating = rating, outOf = 5)
                        }
                    }
                    rateCustomer()
                }

            }
        }

        when {
            !reviewGiven && !reviewTaken -> {
                // Show "Rate Customer"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 10.dp, end = 10.dp)
                        .clickable { onRateServiceClick() },
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
                            text = if (isCustomer) stringResource(Res.string.rate_service) else stringResource(Res.string.rate_Customer),
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
            else -> {
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
                            text =  stringResource(Res.string.view_Ratings),
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
//
//            !reviewGiven && reviewTaken -> {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//                        .padding(start = 10.dp, end = 10.dp)
//                        .clickable { rateCustomer() },
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(50.dp)
//                            .padding(horizontal = 10.dp)
//                    ) {
//                        Text(
//                            text = stringResource(Res.string.given_by_Customer),
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Color(0xFF3A2D94)
//                        )
//
//                        RatingRow(rating = rating.toFloat())
//
//                        Text(
//                            text = rating.toString(),
//                            fontSize = 14.sp,
//                            color = Color.Gray
//                        )
//                    }
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ){
//                        Text(
//                            text =  stringResource(Res.string.rate_Customer),
//                            fontSize = 14.sp,
//                            color = Color.Gray,
//                            fontFamily = JtBtoFonts.getInterFont()
//                        )
//                        Spacer(Modifier.width(3.dp))
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                            contentDescription = null,
//                            tint = Color.Black
//                        )
//                    }
//
//                }
//
//            }

//            else -> {
                // Show View Ratings
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                        .padding(start = 10.dp, end = 10.dp)
//                        .clickable { onViewRatingsClick() },
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = null,
//                            tint = Color(0xFFFFB800),
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text =  stringResource(Res.string.view_Ratings),
//                            color = JTColors.BackGroundBlueColor,
//                            fontSize = 12.sp,
//                            fontFamily = JtBtoFonts.getOpenSansSemiBold()
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                        contentDescription = null,
//                        tint = Color.Black
//                    )
//                    }
//                }
//            }

    }
}


@Composable
fun ProviderReviewAndRatingBottomSheet(
    serviceName: String,
    bookingId: String,
    providerName: String,
    profileImage: String,
    onSubmitClick: (serviceRating: String?, honestyRating: String?, review: String) -> Unit,
    onCloseClick: () -> Unit,
    address: String? = null
) {
    var serviceRating by remember { mutableStateOf<String?>(null) }
    val honestyRating by remember { mutableStateOf<String?>(null) }
    var reviewText by remember { mutableStateOf("") }
    val isSubmitEnabled = !serviceRating.isNullOrEmpty() &&
            (serviceRating == "Great" || serviceRating == "Excellent" || reviewText.isNotBlank())


    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
            .imePadding()
            .background(JTColors.WhiteColor, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.review_rating),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                lineHeight = 12.sp
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.clickable { onCloseClick() }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = serviceName,
            fontSize = 16.sp,
            color = JTColors.enableButton,
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            modifier = Modifier.align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .background(color = Color(0xFFF8F7FC), shape = RoundedCornerShape(50))
                .align(Alignment.Start)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(stringResource(Res.string.booking_ID))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("#$bookingId")
                }
            }

            Text(
                text = annotatedText,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =  stringResource(Res.string.customer),
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            fontSize = 11.sp,
            color = Color(0xFFABABAB),
            lineHeight = 11.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF8F7FE), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = "",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Res.drawable.america),
                error = painterResource(Res.drawable.america)
            )


            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = providerName,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = address ?: "",
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(Res.string.customer_Review_Rating),
            fontSize = 12.sp,
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            modifier = Modifier.align(Alignment.Start)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(15.dp))

        RatingRow(
            label =  stringResource(Res.string.customer_Rating),
            selectedRating = serviceRating,
            onRatingChange = { serviceRating = it }
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = reviewText ,
            onValueChange = { reviewText= it },
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
            onClick = {
                onSubmitClick(serviceRating,honestyRating, reviewText)
            },
            enabled = isSubmitEnabled,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRatingsBottomSheet(
    givenByCustomerRating: Float?,
    givenByCustomerReview: String,
    reviewToCustomerRating: Float?,
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
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text =  stringResource(Res.string.ratings),
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

            // Given by Customer Section
            if (givenByCustomerRating != 0.0f && givenByCustomerRating != null){
                Text(
                    text =  stringResource(Res.string.given_by_Customer),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3A2D94) // Purple
                )

                RatingRow(rating = givenByCustomerRating)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = givenByCustomerReview,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                HorizontalDivider(
                    color = Color(0xFFE5E5E5),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }


            // Review to Customer Section
            if (reviewToCustomerRating != 0.0f && reviewToCustomerRating != null){
                Text(
                    text = stringResource(Res.string.review_to_Customer),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3A2D94) // Purple
                )

                RatingRow(rating = reviewToCustomerRating)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = reviewToCustomerReview,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }


            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RatingRow(rating: Float) {
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
