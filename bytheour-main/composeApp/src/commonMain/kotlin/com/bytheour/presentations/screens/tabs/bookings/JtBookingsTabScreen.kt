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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.amount_refunded
import bytheour.composeapp.generated.resources.booking
import bytheour.composeapp.generated.resources.calendar_2_1
import bytheour.composeapp.generated.resources.cancelled
import bytheour.composeapp.generated.resources.cancelled_ico
import bytheour.composeapp.generated.resources.closed
import bytheour.composeapp.generated.resources.complete
import bytheour.composeapp.generated.resources.confirmed
import bytheour.composeapp.generated.resources.confirmed_ico
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.in_progres
import bytheour.composeapp.generated.resources.inprogress_ico
import bytheour.composeapp.generated.resources.listchat_ico
import bytheour.composeapp.generated.resources.location2_ico
import bytheour.composeapp.generated.resources.mediation_Raised
import bytheour.composeapp.generated.resources.mediation_accepted
import bytheour.composeapp.generated.resources.mediation_declined
import bytheour.composeapp.generated.resources.no_bookings
import bytheour.composeapp.generated.resources.processing
import bytheour.composeapp.generated.resources.scheduled_for
import bytheour.composeapp.generated.resources.searching_for_provider
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.upcoming
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.presentations.customcomponents.CustomTabRow
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.RatingBadge
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.Status
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getKoinViewModel
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data class BookingData(
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
    val costPerHour :String? = null,
    val cancelBy :String? = null,
    val reason :String? = null,
    val bookingId :String? = null,
    val mediationRaise :String? = null,
    val extraHours :String ? = null,
    val hours :Double? = null,
    val paymentType  :String? = null,
    val cancelAfterTime :String? = null,
    val isQuotationAccepted:Boolean? = null,
    val isPaymentConfirmed :Boolean? = null,
    val startedByUser: Boolean? = null,
    val workEndByProvider :Boolean? = null,
    val reviewIsGiven :String? = null,
    val serviceRatingText :String? = null,
    val givenRating :Int ? = null,
    val mediationAmount: String? = null,
    val isQuotationSent :Boolean? = null,
    val providerStartTime:String? = null,
    val providerEndTime:String? = null,
    val mediationStatus :String? = null,
    val mediationAcceptAmount :String? = null,
    val rating :Float? = null,
    val perHourAmount :Float? = null,
    val reviewTaken :Int ? = null,
    val reviewList: ModelGetRating.Data? =null,
    val providerImage:String? = null,
    val providerVerified:String? = null,
    val workerDetails: ModelProviderBookingList.Worker? = null,
    val serviceId:String? = null,
    val amountProceed:String?= null,
    val workerExperience :Int? = null,
    val minHourTime :Int? = null,
    val customerLat:String? = null,
    val customerLong:String? = null,
    val isInspection:Boolean? = null,
    val isInspectionRate :Double? = null,
    val inspectionId :String? = null,
    val problem :String?= null,
    val problemDescription :String?= null,
    val problemSolution :String? = null,
    val estimationCost:String? = null,
    val availableTime:String? = null,
    val serviceDescription:String? = null
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JtBookingsTabScreen(
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    navigateToOneOnOneChat: (String, String, Boolean,String,String?) -> Unit,
    navigateToBookingInfoScreen: (BookingData,String) -> Unit,
) {
    val tabs = listOf(
        stringResource(Res.string.upcoming),
        stringResource(Res.string.processing),
        stringResource(Res.string.closed)
    )
    val uiState by bookingViewModel.uiStates.collectAsState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        bookingViewModel.onTabSelected(uiState.selectedTab)
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                if (totalItems > 0 &&
                    lastVisibleItemIndex >= totalItems - 3 &&
                    !uiState.isLoadingMore &&
                    uiState.hasMoreData) {

                    when (uiState.selectedTab) {
                        0 -> bookingViewModel.loadMoreUpComingList()
                        1 -> bookingViewModel.loadMoreOnGoingList()
                        2 -> bookingViewModel.loadMoreClosedBookings()
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = JTColors.enableButton)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                JtBtoCommonText(
                    stringResource(Res.string.booking),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 19.sp,
                    lineHeight = 12.sp,
                    color = JTColors.WhiteColor,
                )
                Spacer(Modifier.weight(1f))

//                Box(                         //filter icon is hide for now
//                    modifier = Modifier
//                        .height(30.dp)
//                        .width(44.dp)
//                        .border(
//                            width = 1.dp,
//                            color = JTColors.WhiteColor,
//                            shape = RoundedCornerShape(30.dp)
//                        )
//                        .clickable {},
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.filter),
//                        contentDescription = "",
//                        tint = JTColors.WhiteColor,
//                        modifier = Modifier.clickable {}
//                    )
//                }

            }

            Spacer(modifier = Modifier.weight(1f))

            CustomTabRow(
                tabs = tabs,
                selectedTabIndex = uiState.selectedTab,
                onTabSelected = { index -> bookingViewModel.onTabSelected(index) },
            )
        }

        if (uiState.services.isEmpty() && !uiState.isLoadingMore) {
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
            }
        } else {
            val isRefreshing by bookingViewModel.isRefreshing.collectAsState()
            PullToRefreshBox(
                state = rememberPullToRefreshState(),
                modifier = Modifier.fillMaxSize(),
                isRefreshing = isRefreshing,
                onRefresh = {
                    bookingViewModel.refreshBookings(uiState.selectedTab)
                }
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JTColors.WhiteColor),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(uiState.services.sortedByDescending {
                        it.bookingId?.substringAfter("#")?.toLongOrNull() ?: 0L
                    }) { service ->
                        ServiceCard(
                            serviceName = service.title ?: "",
                            status = (service.status?.status ?: Status.OrderStatus.CONFIRMED).toString(),
                            date = formatDateString(service.scheduleDate ?: ""),
                            address = service.address ?: "N/A",
                            imageUrl = service.image ?: "",
                            id = service.bookingId ?: "",
                            time = "${service.scheduleTime}",
                            onChatClick = {
                                navigateToOneOnOneChat(
                                    service.workerName ?: "",
                                    "",
                                    false,
                                    service.providerImage ?: "",
                                     service.workerId
                                )
                            },
                            mediationAmount = service.mediationAmount ?: "",
                            amountProceed = service.amountProceed?:"",
                            onCardClick = {
//                                val bookingData = BookingData(
//                                    status = (service.status
//                                        ?: Status.OrderStatus.CONFIRMED).toString(),
//                                    title = service.title ?: "",
//                                    address = service.address ?: " Hyderabad",
//                                    priceRange = service.priceRange.toString(),
//                                    image = service.image ?: "",
//                                    date = service.scheduleDate ?: "",
//                                    time = service.scheduleTime ?: "",
//                                    customerId = service.customerId ?: "",
//                                    workerName = service.workerName ?: "",
//                                    workerId = service.id ?: "",
//                                    percentage = service.percentage,
//                                    amount = service.amount,
//                                    ratings = service.ratings,
//                                    serviceRating = service.serviceRating,
//                                    cancelBy = service.cancelBy,
//                                    reason = service.reason,
//                                    bookingId = service.bookingId,
//                                    mediationRaise = service.mediationRaised,
//                                    hours = service.hours,
//                                    extraHours = service.extraHours,
//                                    paymentType = service.paymentType,
//                                    cancelAfterTime = service.cancelAfterTime,
//                                    isQuotationAccepted = service.isQuotationAccepted,
//                                    isPaymentConfirmed = service.isPaymentConfirmed,
//                                    startedByUser = service.startedByUser,
//                                    workEndByProvider = service.workEndByProvider,
//                                    reviewIsGiven = service.reviewIsGiven,
//                                    serviceRatingText = service.serviceRatingText,
//                                    givenRating = service.givenRating,
//                                    mediationAmount = service.mediationAmount,
//                                    isQuotationSent = service.isQuotationSent,
//                                    providerStartTime = service.providerStartTime,
//                                    providerEndTime = service.providerEndTime,
//                                    mediationStatus = service.mediationStatus,
//                                    mediationAcceptAmount = service.mediationAcceptAmount,
//                                    rating = service.rating,
//                                    costPerHour = service.costPerHour,
//                                    perHourAmount = service.perHourAmount,
//                                    reviewTaken = service.reviewTaken,
//                                    reviewList = service.reviewList,
//                                    providerImage = service.providerImage,
//                                    providerVerified = service.providerVerified,
//                                    workerDetails = service.workerDetails,
//                                    serviceId = service.serviceId,
//                                    amountProceed = service.amountProceed
//                                )
                                navigateToBookingInfoScreen(BookingData(),service.bookingId?:"")
                            },
                            mediationRaised = service.mediationRaised ?: "",
                            mediationStatus = service.mediationStatus,
                            rating = service.reviewList?.taken?.rating,
                            isRatingGiven = service.reviewList?.taken != null,
                            mediationAcceptAmount = service.mediationAcceptAmount
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(55.dp))
                    }
                }
            }
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun ServiceCard(
    id:String,
    serviceName: String,
    status: String,
    date: String,
    address: String,
    imageUrl: String,
    onChatClick:()-> Unit,
    onCardClick:()-> Unit,
    time:String,
    mediationAmount:String,
    mediationRaised :String,
    mediationStatus: String?,
    rating: Int? = null,
    isRatingGiven: Boolean = false,
    amountProceed: String,
    mediationAcceptAmount: String?
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

//            if (status == Status.OrderStatus.SEARCHING.status) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 70.dp)
//                        .align(Alignment.TopCenter),
//                    contentAlignment = Alignment.TopCenter
//                ) {
//                    SearchAnimation(
//                        modifier = Modifier.size(40.dp)
//                    )
//                }
//            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                when(status){
                    Status.OrderStatus.SEARCHING.status -> {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp),
                        ) {
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
                    }
                    Status.OrderStatus.CONFIRMED.status ->{
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        ){
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

                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                painter = painterResource(Res.drawable.listchat_ico),
                                contentDescription = "",
                                modifier = Modifier.clickable { onChatClick() }
                            )
                        }
                    }
                    Status.OrderStatus.IN_PROGRESS.status -> {
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        ){
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

                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(Res.drawable.listchat_ico),
                                contentDescription = "",
                                modifier = Modifier.clickable { onChatClick() }
                            )

                        }
                    }
                    Status.OrderStatus.COMPLETED.status -> {
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        ){
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

                            Spacer(modifier = Modifier.weight(1f))

                            if (isRatingGiven && rating != null) {
                                RatingBadge(rating = rating)
                            } else {
                                Image(
                                    painter = painterResource(Res.drawable.listchat_ico),
                                    contentDescription = "",
                                    modifier = Modifier.clickable { onChatClick() }
                                )
                            }
                        }
                    }
                    Status.OrderStatus.CANCELLED.status -> {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp),
                        ) {
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
                }
                Spacer(modifier = Modifier.weight(1f))
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp,bottom = 10.dp)
                ){
                    when (status) {
                        Status.OrderStatus.COMPLETED.status -> {
                            if (amountProceed == "y") {
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
                                        text = stringResource(Res.string.amount_refunded)+ mediationAmount,
                                        color = JTColors.BackGroundBlueColor,
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                        fontSize = 11.sp
                                    )
                                }
                            } else if (mediationRaised == "y" && mediationStatus == "n") {
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
                                        text = stringResource(Res.string.mediation_Raised)+ mediationAmount,
                                        color = JTColors.BackGroundBlueColor,
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                        fontSize = 11.sp
                                    )
                                }
                            } else if (mediationStatus == "y" && mediationRaised == "y") {
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
                                            if (mediationAcceptAmount.equals("0")){
                                                stringResource(Res.string.mediation_declined)
                                            }else{
                                                stringResource(Res.string.mediation_accepted)
                                            },
                                        color = JTColors.BackGroundBlueColor,
                                        fontFamily = JtBtoFonts.getInterMediumFont(),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                    }
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
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        JTColors.WhiteColor.copy(alpha = 0.13f),
                                        JTColors.lightPinkColor.copy(alpha = 0.17f),
                                    )
                                ),
                            )
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.calendar_2_1),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                                .padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        VerticalDivider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = JTColors.WhiteColor.copy(0.2f),
                            thickness = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            JtBtoCommonText(
                                text = stringResource(Res.string.scheduled_for),
                                color = Color.LightGray,
                                fontSize = 11.sp,
                                fontFamily = JtBtoFonts.getInterLightFont()
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            JtBtoCommonText(
                                text = "$date, $time",
                                color = Color.White,
                                fontFamily = JtBtoFonts.getInterFont(),
                                fontSize = 14.sp,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.location2_ico),
                            contentDescription = "location",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        JtBtoCommonText(
                            text = address,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getInterLightFont()
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            if (status == Status.OrderStatus.SEARCHING.status) {
                AwaitingConfirmationBoxCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    text = "Waiting for a technician to accept the inspection"
                )
            }
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
