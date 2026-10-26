package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.accept
import bytheour.composeapp.generated.resources.accept_booking_request
import bytheour.composeapp.generated.resources.are_you_sure_accept_the_booking_request
import bytheour.composeapp.generated.resources.are_you_sure_decline_the_booking_request
import bytheour.composeapp.generated.resources.awaiting_user_confirmation_to_end_job
import bytheour.composeapp.generated.resources.awaiting_user_confirmation_to_start_job
import bytheour.composeapp.generated.resources.booking
import bytheour.composeapp.generated.resources.calendar_2_1
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.cancelled
import bytheour.composeapp.generated.resources.cancelled_ico
import bytheour.composeapp.generated.resources.closed
import bytheour.composeapp.generated.resources.complete
import bytheour.composeapp.generated.resources.completed_ico
import bytheour.composeapp.generated.resources.confirmation_Job
import bytheour.composeapp.generated.resources.decline
import bytheour.composeapp.generated.resources.decline_booking_request
import bytheour.composeapp.generated.resources.end_Inspection
import bytheour.composeapp.generated.resources.end_Job
import bytheour.composeapp.generated.resources.end_Job_Confirmation
import bytheour.composeapp.generated.resources.end_Service
import bytheour.composeapp.generated.resources.end_inspection_Confirmation
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.listchat_ico
import bytheour.composeapp.generated.resources.location2_ico
import bytheour.composeapp.generated.resources.mediation_Raised
import bytheour.composeapp.generated.resources.miles
import bytheour.composeapp.generated.resources.miles_ico
import bytheour.composeapp.generated.resources.no_address
import bytheour.composeapp.generated.resources.no_bookings
import bytheour.composeapp.generated.resources.payment_Request_for_this_service
import bytheour.composeapp.generated.resources.processing
import bytheour.composeapp.generated.resources.scheduled_for
import bytheour.composeapp.generated.resources.send_estimation
import bytheour.composeapp.generated.resources.start_Inspection
import bytheour.composeapp.generated.resources.start_Job
import bytheour.composeapp.generated.resources.start_Job_Confirmation
import bytheour.composeapp.generated.resources.start_Service
import bytheour.composeapp.generated.resources.start_inspection_Confirmation
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.upcoming
import bytheour.composeapp.generated.resources.waiting_user_accept_estimation
import bytheour.composeapp.generated.resources.your_bookings
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.presentations.customcomponents.CustomTabRow
import com.bytheour.presentations.screens.JTBtoMainScreenViewModel
import com.bytheour.utils.JTColors
import com.bytheour.utils.COMPLETED
import com.bytheour.utils.Cancelled
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.MapNavigator
import com.bytheour.utils.ONGOING
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PENDING
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.Status
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.UPCOMING
import com.bytheour.utils.createMapNavigator
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getFormattedDistanceInMiles
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


sealed class DialogType {
    object Accept : DialogType()
    object Decline : DialogType()
}
sealed class JobType {
    object StartJob : JobType()
    object EndJob : JobType()
}


@Serializable
data class BookingDataProvider(
    val status: String = "",
    val title: String = "",
    val address: String = "",
    val priceRange: String = "",
    val image: String = "",
    val date: String = "",
    val time: String = "",
    val customerId: String = "",
    val workerName: String = "",
    val serverId: String = "",
    val percentage : Double? = 0.0,
    val amount :Double? = 0.0,
    val workerId :String? = "",
    val serviceRating :String? = null,
    val ratings :Float? = null,
    val costPerHour :Double? = null,
    val bookingId:String? = null,
    val flags: ModelProviderBookingList.Flags? = null,
    val mediation: ModelProviderBookingList.Mediation? = null,
    val customer:ModelProviderBookingList.Customer? = null,
    val reviewIsGiven :String? = null,
    val serviceRatingText :String? = null,
    val givenRating :Int ? = null,
    val bill:ModelProviderBookingList.Bill? = null,
    val cancellation:ModelProviderBookingList.Cancellation? = null,
    val reviewList: ModelGetRating.Data? =null,
    val addressList:ModelProviderBookingList.Address? = null,
    val extraHours :String ? = null,
    val hours :Double? = null,
    val providerImage :String?= null,
    val paymentType  :String? = null,
    val distance :String? = "",
    val customerLat:String? = null,
    val customerLong:String? = null,
    val workerDetails :ModelProviderBookingList.Worker? = null,
    val isInspection :Boolean?= null,
    val inspectionRate:Double?= null,
    val inspectionCalculatedAmount:Double? = null

)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JtProviderBookingsTabScreen(
    initialTabIndex: Int = 0,
    proBookingViewModel: JTProviderBookingTabScreenViewmodel = getKoinViewModel(),
    mainScreenViewModel: JTBtoMainScreenViewModel = getKoinViewModel(),
    navigateToOneOnOneChat: (String, String, Boolean, String, String?) -> Unit,
    navigateToProviderBookingInfoScreen: (BookingDataProvider,String) -> Unit,
    navigateProviderCancelReasonScreen:(String)->Unit,
    navigateToSendEstimationScreen:(String, scheduleTime:String, rate:String)->Unit,
    navigateToInspectionReportScreen:(String,String,String,String)-> Unit
) {
    val tabs = listOf(
        stringResource(Res.string.upcoming),
        stringResource(Res.string.processing),
        stringResource(Res.string.closed)
    )
    val uiState by proBookingViewModel.uiStates.collectAsState()

    var dialogType by remember { mutableStateOf<DialogType?>(null) }

    var jobTypeType by remember { mutableStateOf<JobType?>(null) }

    var selectedBookingId by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()

    var startOrEndJob by remember { mutableStateOf<JobType?>(null) }
    val map: MapNavigator = createMapNavigator()





    if (initialTabIndex != 0){
        LaunchedEffect(initialTabIndex) {
            proBookingViewModel.onTabSelected(initialTabIndex)
            mainScreenViewModel.resetProviderInitialTabToZero()
        }
    }

    LaunchedEffect(uiState.selectedTab) {
        when (uiState.selectedTab) {
            0 -> proBookingViewModel.loadInitialUpcomingBookings(PAGINATION_LIMIT.toString())
            1 -> proBookingViewModel.loadInitialOngoingBookings(PAGINATION_LIMIT.toString())
            2 -> proBookingViewModel.loadInitialClosedBookings(PAGINATION_LIMIT.toString())
        }
    }


    LaunchedEffect(listState, uiState.isLoadingMore, uiState.selectedTab) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val totalItems = listState.layoutInfo.totalItemsCount
                val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0

                val shouldLoadMore = totalItems > 0 && lastVisibleItemIndex >= totalItems - 3 && !uiState.isLoadingMore

                if (shouldLoadMore) {
                    when (uiState.selectedTab) {
                        0 -> proBookingViewModel.loadMoreUpcomingBookings(PAGINATION_LIMIT.toString())
                        1 -> proBookingViewModel.loadMoreOngoingBookings(PAGINATION_LIMIT.toString())
                        2 -> proBookingViewModel.loadMoreClosedBookings(PAGINATION_LIMIT.toString())
                    }
                }
            }
    }

    LaunchedEffect(startOrEndJob) {
        startOrEndJob?.let { jobType ->
            if (jobType == JobType.StartJob) {
                proBookingViewModel.startJob(bookingId = selectedBookingId ?: "")
                delay(500)
                proBookingViewModel.onTabSelected(1)
//                proBookingViewModel.loadInitialOngoingBookings(PAGINATION_LIMIT.toString())
            } else if (jobType == JobType.EndJob) {
                proBookingViewModel.endJob(bookingId = selectedBookingId ?: "")
                delay(500)
                proBookingViewModel.loadInitialOngoingBookings(PAGINATION_LIMIT.toString())
            }
            startOrEndJob = null
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JTColors.enableButton)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .height(110.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 17.dp,
                            start = 16.dp, end = 16.dp
                        )
                ) {
                    JtBtoCommonText(
                        stringResource(Res.string.booking),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 19.sp,
                        lineHeight = 12.sp,
                        color = JTColors.WhiteColor,
                        modifier = Modifier.clickable {}

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                CustomTabRow(
                    tabs = tabs,
                    selectedTabIndex = uiState.selectedTab,
                    onTabSelected = { index -> proBookingViewModel.onTabSelected(index) },
                )
            }

            if (uiState.services.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JTColors.WhiteColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    JtBtoCommonText(
                        text = stringResource(Res.string.no_bookings),
                        fontSize = 19.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    JtBtoCommonText(
                        text = stringResource(Res.string.your_bookings),
                        fontSize = 13.sp,
                        color = JTColors.textFieldColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )
                }
            } else {
                val isRefreshing by proBookingViewModel.isRefreshing.collectAsState()
                PullToRefreshBox(
                    state = rememberPullToRefreshState(),
                    modifier = Modifier.fillMaxSize(),
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        proBookingViewModel.refreshBookings(uiState.selectedTab)
                    }
                ){
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = JTColors.WhiteColor),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            uiState.services.sortedByDescending {
                                it.bookingId?.substringAfter("#")?.toLongOrNull() ?: 0L
                            }
                        ) { service ->
                            val workerLat = service.workerDetails?.workArea?.lat?.toDoubleOrNull()
                            val workerLng = service.workerDetails?.workArea?.lng?.toDoubleOrNull()
                            val customerLat = service.customerLat?.toDoubleOrNull()
                            val customerLng = service.customerLong?.toDoubleOrNull()
                            val pinCode = service.addressList?.address?.zip

                            val distanceInMiles = getFormattedDistanceInMiles(
                                lat1 = customerLat,
                                lon1 = customerLng,
                                lat2 = workerLat,
                                lon2 = workerLng
                            ) ?: "N/A"

                            ServiceCard(
                                serviceName = service.title ?: "",
                                status = (service.status
                                    ?: Status.OrderStatus.CONFIRMED).toString(),
                                date = formatDateString(service.scheduleDate ?: ""),
                                address = service.address ?: "",
                                imageUrl = service.image ?: "",
                                time = "${service.scheduleTime}",
                                onChatClick = {
                                    navigateToOneOnOneChat(
                                        service.customer?.fullName ?: "",
                                        "",
                                        false,
                                        service.customer?.profile ?: "",
                                        service.customer?.id
                                    )
                                },
                                serviceDate = service.scheduleDate ?: "",
                                onCardClick = {
//                                    val bookingData = BookingDataProvider(
//                                        status = (service.status
//                                            ?: Status.OrderStatus.CONFIRMED).toString(),
//                                        title = service.title ?: "",
//                                        address = service.address ?: "",
//                                        priceRange = service.priceRange.toString(),
//                                        image = service.image ?: "",
//                                        date = service.scheduleDate ?: "",
//                                        time = service.scheduleTime ?: "",
//                                        customerId = service.customerId ?: "",
//                                        workerName = service.workerName ?: "",
//                                        workerId = service.id ?: "",
//                                        percentage = service.percentage,
//                                        amount = service.amount,
//                                        ratings = service.ratings,
//                                        serviceRating = service.serviceRating,
//                                        flags = service.flags,
//                                        mediation = service.mediation,
//                                        customer = service.customer,
//                                        bookingId = service.bookingId,
//                                        reviewIsGiven = service.review,
//                                        serviceRatingText = service.serviceRatingText,
//                                        givenRating = service.givenRating,
//                                        bill = service.bill,
//                                        cancellation = service.cancellation,
//                                        reviewList = service.reviewList,
//                                        addressList = service.addressList,
//                                        extraHours = service.extraHours,
//                                        hours = service.hours,
//                                        providerImage = service.providerImage
//
//
//                                    )
                                    navigateToProviderBookingInfoScreen(BookingDataProvider(),service.bookingId ?: "")
                                },
                                userType = TYPE_PROVIDER,
                                onAccept = {
                                    dialogType = DialogType.Accept
                                },
                                onDecline = {
                                    dialogType = DialogType.Decline
                                },
                                flags = service.flags ?: ModelProviderBookingList.Flags(),
                                mediation = service.mediation
                                    ?: ModelProviderBookingList.Mediation(),
                                onCancelled = {
                                    navigateProviderCancelReasonScreen(service.bookingId ?: "")
                                },
                                sendEstimation = {
                                    navigateToSendEstimationScreen(
                                        service.bookingId ?: "",
                                        service.scheduleTime ?: "",
                                        service.costPerHour.toString()
                                    )
                                },
                                onStartJobClick = {
                                    selectedBookingId = service.bookingId
                                    jobTypeType = JobType.StartJob
                                },
                                onEndJobClick = {
                                    selectedBookingId = service.bookingId
                                    jobTypeType = JobType.EndJob
                                },
                                rating = service.serviceRatingValue?.toIntOrNull(),
                                miles = distanceInMiles,
                                onMapClick = {
                                    map.openGoogleMaps(
                                        latitude = customerLat?:0.0 ,
                                        longitude = customerLng?:0.0,
                                        pinCode = pinCode?:""
                                    )
                                },
                                navigateToInspectionReportScreen = {
                                    navigateToInspectionReportScreen(service.providerName?:"",service.title?:"",service.image?:"",service.bookingId?:"")
                                }
                            )
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(55.dp))
                        }
                    }
                }
            }
        }
        if (!uiState.message.isNullOrBlank()) {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 70.dp)){
                CommonSnackbar(
                    message = uiState.message?:"",
                    type = if (uiState.isSuccess) SnackbarType.SUCCESS else SnackbarType.ERROR,
                    onDismiss = { proBookingViewModel.clearMessage() }
                )
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
                    proBookingViewModel.acceptBooking(uiState.bookingId ?: "")
                    proBookingViewModel.onTabSelected(1)
                } else {
                    proBookingViewModel.declineBooking(uiState.bookingId?:"")
                }
                dialogType = null
            },
            onCancel = {
                dialogType = null
            }
        )
    }
    if (jobTypeType != null) {
        val isStart = jobTypeType is JobType.StartJob
        val isInspection = uiState.services.any { it.bookingId == selectedBookingId && it.flags?.isInspection == true }
        val message = when {
            isStart && isInspection -> stringResource(Res.string.start_inspection_Confirmation)
            !isStart && isInspection -> stringResource(Res.string.end_inspection_Confirmation)
            isStart -> stringResource(Res.string.start_Job_Confirmation)
            else -> stringResource(Res.string.end_Job_Confirmation)
        }

        val yesText = if (isStart)
            stringResource(Res.string.start_Job)
        else
            stringResource(Res.string.end_Job)

        CustomAlertDialog(
            showDialog = true,
            onDismiss = { jobTypeType = null },
            title = stringResource(Res.string.confirmation_Job),
            message = message,
            yes = true,
            no = true,
            yesText = yesText,
            noText = stringResource(Res.string.cancel),
            yesButtonColor = JTColors.enableButton,
            noButtonColor = Color.White,
            onConfirm = {
                startOrEndJob = jobTypeType
                jobTypeType = null
            },
            onCancel = {
                jobTypeType = null
            }
        )
    }

}


@Composable
fun RatingBadge(rating: Int, outOf: Int = 5) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xAA000000),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star Icon",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$rating/$outOf",
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun AwaitingConfirmationBoxCard(
    modifier: Modifier = Modifier,
    text:String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(25.dp)
            .background(
                color = Color(0xFFFFC64A),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Text(
            text = text,
            color = Color(0xFF29235C),
            fontSize = 10.sp,
            lineHeight = 10.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            maxLines = 1,
            modifier =Modifier.padding(7.dp,top = 5.dp, bottom = 5.dp),
        )
    }
}

@Composable
fun AwaitingConfirmationBox(
    modifier: Modifier = Modifier,
    text:String,
    ) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentHeight()
            .background(
                color = Color(0xFFFFC64A),
                shape = RoundedCornerShape(50)
            )
    ) {
        Text(
            text = text,
            color = Color(0xFF29235C),
            fontSize = 10.sp,
            lineHeight = 10.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            maxLines = 1,
            modifier =Modifier.padding(7.dp),

        )
    }
}

@Composable
fun StartServiceButton(
    onClick: () -> Unit,
    text: String,
    color: Color,
    detail:Boolean = false,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(if(detail)48.dp else 39.dp)
            .padding(horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(50),
        enabled = enable
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            maxLines = 1,
        )
    }
}

@Composable
fun CompletedButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    icon: Painter? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .height(27.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.size(15.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Black,
            fontSize = 11.sp,
            lineHeight = 11.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            maxLines = 1,
        )
    }
}

@Composable
fun ServiceCard(
    serviceName: String,
    status: String,
    date: String,
    address: String,
    imageUrl: String,
    onChatClick: () -> Unit,
    onCardClick: () -> Unit,
    time: String,
    userType: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onCancelled: () -> Unit,
    sendEstimation: () -> Unit,
    flags: ModelProviderBookingList.Flags,
    mediation:ModelProviderBookingList.Mediation,
    onStartJobClick:()->Unit,
    onEndJobClick:()->Unit,
    rating:Int? = null,
    miles  : String,
    onMapClick:()->Unit,
    serviceDate: String = "",
    navigateToInspectionReportScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .height(265.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(JTColors.WhiteColor)
    ) {
        var showStartJobButton by remember { mutableStateOf(false) }
        var showEndJobButton by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            val imageToLoad = if (imageUrl.isEmpty()) {
                painterResource(Res.drawable.image_1)
            } else {
                rememberAsyncImagePainter(model = imageUrl)
            }

            Image(
                painter = imageToLoad,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(Res.drawable.transparent_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                if (userType == TYPE_PROVIDER) {
                    if (rating != null && rating != 0) {
                        if (flags.type == COMPLETED) {
                            Box(modifier = Modifier.align(Alignment.End)) {
                                RatingBadge(rating = rating)
                            }
                        } else {
                            IconButton(
                                onClick = { onChatClick() },
                                modifier = Modifier.align(Alignment.End)
                                    .offset(y = (-10).dp, x = (10).dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.listchat_ico),
                                    contentDescription = null
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = { onChatClick() },
                            modifier = Modifier.align(Alignment.End)
                                .offset(y = (-10).dp, x = (10).dp)
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.listchat_ico),
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (flags.type.equals(COMPLETED, ignoreCase = true) &&
                    flags.isQuotationSent &&
                    flags.isQuotationAccepted &&
                    flags.paid &&
                    flags.isPaymentConfirmed &&
                    flags.jobStartedByWorker
                ) {
                    Row {
                        CompletedButton(
                            text = stringResource(Res.string.complete),
                            icon = painterResource(Res.drawable.completed_ico),
                            color = Color(0xFF7BD92F)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        if (mediation.mediationRaised.equals(
                                "y",
                                ignoreCase = true
                            ) && mediation.mediationAcceptAmount == null
                        ) {
                            CompletedButton(
                                text = stringResource(Res.string.mediation_Raised) + mediation.mediationAmount,
                                color = Color.White
                            )
                        }
                    }
                } else if (flags.type.equals(Cancelled, ignoreCase = true)) {
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

                Spacer(modifier = Modifier.height(8.dp))

                JtBtoCommonText(
                    text = serviceName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    JTColors.WhiteColor.copy(alpha = 0.15f),
                                    JTColors.lightPinkColor.copy(alpha = 0.15f),
                                )
                            ),
                        )
                ) {
                    Image(
                        painter = painterResource(Res.drawable.calendar_2_1),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp).padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    VerticalDivider(
                        modifier = Modifier
                            .height(32.dp)
                            .width(1.dp),
                        color = JTColors.WhiteColor.copy(0.2f),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        JtBtoCommonText(
                            text = stringResource(Res.string.scheduled_for),
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            fontFamily = JtBtoFonts.getInterLightFont()
                        )
                        Spacer(Modifier.height(4.dp))
                        JtBtoCommonText(
                            text = "$date, $time",
                            color = Color.White,
                            fontFamily = JtBtoFonts.getInterFont(),
                            fontSize = 13.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .height(55.dp)
                            .width(65.dp)
                            .background(
                                color = if (address.isBlank()) Color(0xFF4F4A5F)
                                else JTColors.BackGroundBlueColor,
                                shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                            )
                            .then(
                                if (address.isBlank()) Modifier else Modifier.clickable { onMapClick() }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(Res.drawable.miles_ico),
                                contentDescription = "Direction",
                                modifier = Modifier.size(16.dp),
                                colorFilter = if (address.isBlank())
                                    ColorFilter.tint(Color.Gray.copy(alpha = 0.6f))
                                else null
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            JtBtoCommonText(
                                text = if (address.isBlank()) "--" else "$miles/" + stringResource(Res.string.miles),
                                color = if (address.isBlank()) Color.Gray.copy(alpha = 0.6f) else JTColors.WhiteColor,
                                fontSize = 10.sp,
                                lineHeight = 10.sp,
                                fontFamily = JtBtoFonts.getInterFont()
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.location2_ico),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    JtBtoCommonText(
                        text = address.ifBlank { stringResource(Res.string.no_address) },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterLightFont()
                    )
                }
                if (status.equals(PENDING, ignoreCase = true)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Button(
                            onClick = onDecline,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF4F4A5F
                                )
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.decline),
                                color = Color.White,
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
                                .height(38.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.accept),
                                color = Color.White,
                                fontFamily = JtBtoFonts.getInterMediumFont(),
                                fontSize = 14.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
                if (status.equals(UPCOMING, ignoreCase = true) && flags.accepted && !flags.isQuotationSent) {
                    if (flags.isInspection) {
                        showStartJobButton = true
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .padding(1.dp)
                                .fillMaxWidth()
                                .offset(y = 10.dp),
                        ) {
                            Button(
                                onClick = onCancelled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4F4A5F)
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.cancel),
                                    color = Color.White,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 12.5.sp,
                                    lineHeight = 15.sp
                                )
                            }

                            Button(
                                onClick = sendEstimation,
                                colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.send_estimation),
                                    color = Color.White,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 12.5.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                val currentDate = LocalDate.now().toString()
                val enable = serviceDate == currentDate

                if (showStartJobButton) {
                    StartServiceButton(
                        modifier = Modifier.offset(y = 3.dp),
                        onClick = { if (enable) onStartJobClick() },
                        text = if (flags.isInspection)stringResource(Res.string.start_Inspection) else stringResource(Res.string.start_Service),
                        color = if (enable) Color(0xFF7BD92F) else  JTColors.lightGrayColor,
                    )
                }

                if (showEndJobButton) {
                    StartServiceButton(
                        modifier = Modifier.offset(y = (3).dp),
                        onClick = {  if (flags.isInspection) {
                            navigateToInspectionReportScreen()
                        } else {
                            onEndJobClick()
                        } },
                        text = if (flags.isInspection)stringResource(Res.string.end_Inspection) else stringResource(Res.string.end_Service),
                        color = Color(0xFFD92F32)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                if (flags.isInspection) {
                    when {
                        flags.type.equals(ONGOING, ignoreCase = true) &&
                             flags.jobStartedByWorker &&
                            !flags.jobEndedByWorker && flags.jobStartedByUser -> {
                            showEndJobButton = true
                        }

                        flags.type.equals(ONGOING, ignoreCase = true) &&
                                flags.jobStartedByWorker &&
                                !flags.jobEndedByWorker && !flags.jobStartedByUser -> {
                            AwaitingConfirmationBoxCard(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Waiting to Customer Confirm Start Inspection"
                            )
                        }
                    }
                }
                else if (flags.type == UPCOMING && flags.isQuotationSent && !flags.isQuotationAccepted) {
                    AwaitingConfirmationBoxCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.waiting_user_accept_estimation)
                    )
                } else if (flags.type == UPCOMING && flags.isQuotationSent && flags.isQuotationAccepted && !flags.isPaymentConfirmed) {
                    AwaitingConfirmationBoxCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.payment_Request_for_this_service)
                    )
                } else if (
                    flags.type == UPCOMING &&
                    flags.isQuotationSent &&
                    flags.isQuotationAccepted &&
                    flags.paid &&
                    flags.isPaymentConfirmed
                ) {
                    showStartJobButton = true
                } else if (
                    flags.type.equals(ONGOING, ignoreCase = true) &&
                    flags.isQuotationSent &&
                    flags.isQuotationAccepted &&
                    flags.paid &&
                    flags.isPaymentConfirmed &&
                    flags.jobStartedByWorker &&
                    flags.jobStartedByUser &&
                    flags.jobEndedByWorker
                ) {
                    AwaitingConfirmationBoxCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.awaiting_user_confirmation_to_end_job)
                    )
                } else if (
                    flags.type.equals(ONGOING, ignoreCase = true) &&
                    flags.isQuotationSent &&
                    flags.isQuotationAccepted &&
                    flags.paid &&
                    flags.isPaymentConfirmed &&
                    flags.jobStartedByWorker &&
                    flags.jobStartedByUser
                ) {
                    showEndJobButton = true
                } else if (
                    flags.type.equals(ONGOING, ignoreCase = true) &&
                    flags.isQuotationSent &&
                    flags.isQuotationAccepted &&
                    flags.paid &&
                    flags.isPaymentConfirmed &&
                    flags.jobStartedByWorker
                ) {
                    AwaitingConfirmationBoxCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.awaiting_user_confirmation_to_start_job)
                    )
                }
            }
        }
    }
}

