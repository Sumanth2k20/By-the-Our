package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.avg
import bytheour.composeapp.generated.resources.bto_transparenttext
import bytheour.composeapp.generated.resources.chat
import bytheour.composeapp.generated.resources.chat_ico
import bytheour.composeapp.generated.resources.directions_ico
import bytheour.composeapp.generated.resources.experience
import bytheour.composeapp.generated.resources.filter_ico
import bytheour.composeapp.generated.resources.great
import bytheour.composeapp.generated.resources.honesty_rating
import bytheour.composeapp.generated.resources.hr
import bytheour.composeapp.generated.resources.miles
import bytheour.composeapp.generated.resources.min_Booking_Hrs
import bytheour.composeapp.generated.resources.minbookinghr_ico
import bytheour.composeapp.generated.resources.no_Service_Name
import bytheour.composeapp.generated.resources.other_Services
import bytheour.composeapp.generated.resources.phone_call
import bytheour.composeapp.generated.resources.poor
import bytheour.composeapp.generated.resources.price_per_hour
import bytheour.composeapp.generated.resources.priceperhr_ico
import bytheour.composeapp.generated.resources.project_complete
import bytheour.composeapp.generated.resources.projectcomplete_ico
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.review_rating
import bytheour.composeapp.generated.resources.service_Rating
import bytheour.composeapp.generated.resources.years
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings.ReviewFilterBottomSheet
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getFormattedDistanceInMiles
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProviderProfileScreen(
    bookingData: BookingData,
    navigateBack: () -> Unit,
    viewModel: ViewProviderProfileViewModel = getKoinViewModel(),
    navigateToChatScreen: () ->Unit,
) {
    val uiState by viewModel.uiStates.collectAsState()
    val lazyListState = rememberLazyListState()
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getWorkersSpecificServices(
            workerId = bookingData.workerId ?: ""
        )
        viewModel.getWorkersRating(
            ratings = "0",
            workerId = bookingData.workerDetails?.id ?: ""
        )
    }

    if (showFilterBottomSheet) {
        ReviewFilterBottomSheet(
            onDismiss = { showFilterBottomSheet = false },
            onApply = { selectedHonestRatings ->
                viewModel.getWorkersRating(
                    ratings = selectedHonestRatings.joinToString(","),
                    workerId = bookingData.workerDetails?.id ?: ""
                )
                showFilterBottomSheet = false
            },
            onClear = {
                showFilterBottomSheet = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = bookingData.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable { navigateBack() },
                        tint = JTColors.WhiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JTColors.BackGroundBlueColor
                )
            )

        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            WorkerHeaderSection(bookingData) {
                navigateToChatScreen()
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                RatingSection(
                    serviceRating = bookingData.workerDetails?.portfolio?.serviceRating?:0.0f,
                    honestyRating = bookingData.workerDetails?.portfolio?.ratings?:0.0f,
                    experience = bookingData.workerExperience ?: 0,
                    type = bookingData.workerDetails?.portfolio?.serviceRatingText?:"",
                    honestType = bookingData.workerDetails?.portfolio?.type?:"",
                )
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoCard(
                    icon = Res.drawable.projectcomplete_ico,
                    label = stringResource(Res.string.project_complete),
                    value = bookingData.workerDetails?.portfolio?.complted?:""
                )
                InfoCard(
                    icon = Res.drawable.priceperhr_ico,
                    label = stringResource(Res.string.price_per_hour),
                    value = "${bookingData.costPerHour}/" +stringResource(Res.string.hr)
                )
                InfoCard(
                    icon = Res.drawable.minbookinghr_ico,
                    label = stringResource(Res.string.min_Booking_Hrs),
                    value = "${bookingData.minHourTime}/"+stringResource(Res.string.hr)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            Text(
                text = stringResource(Res.string.other_Services),
                fontFamily = JtBtoFonts.getInterBoldFont(),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(uiState.serviceDetails) { service ->
            ServiceItem(service = service)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.review_rating),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(Res.drawable.filter_ico),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                        .padding(end = 10.dp)
                        .clickable { showFilterBottomSheet = true }
                )
            }
        }

        items(uiState.workerRatings) { rating ->
            RatingItemColumn(
                rating = rating,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun ServiceItem(
    service: WorkerServiceItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(JTColors.WhiteColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = service.image,
                contentDescription = service.title,
                modifier = Modifier
                    .size(44.dp)
                    .border(
                        width = 2.dp,
                        color = JTColors.lightGrayColor,
                        shape = CircleShape
                    )
                    .padding(2.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Res.drawable.provider_logo),
                error = painterResource(Res.drawable.provider_logo)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.title,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = JTColors.lightGrayColor, fontFamily = JtBtoFonts.getInterFont())) {
                            append("Minimum Booking Hours: ")
                        }
                        withStyle(style = SpanStyle(color = JTColors.BackGroundBlueColor, fontFamily = JtBtoFonts.getInterBoldFont())) {
                            append("${service.minHour} hr${if (service.minHour > 1) "s" else ""}")
                        }
                    },
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${service.rate}",
                    fontSize = 16.sp,
                    color = JTColors.blackColor,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont()
                )
            }
        }
    }
}

@Composable
fun WorkerHeaderSection(bookingData: BookingData,navigateToChatScreen: () -> Unit) {

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = bookingData.workerDetails?.profile,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            placeholder = painterResource(Res.drawable.provider_logo),
            error = painterResource(Res.drawable.provider_logo)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            // Name + Verified Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${bookingData.workerDetails?.firstName ?: ""} ${bookingData.workerDetails?.lastName ?: ""}".trim(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = JTColors.blackColor
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.phone_call),
                    contentDescription = "",
                    tint = JTColors.BackGroundBlueColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = bookingData.workerDetails?.contact?.number ?: "",
                    fontSize = 12.sp,
                    color = JTColors.blackColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(Res.drawable.directions_ico),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = distanceInMiles + stringResource(Res.string.miles),
                    fontSize = 12.sp,
                    color = JTColors.blackColor
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navigateToChatScreen()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F2177)), // Purple background
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.chat_ico),
                contentDescription = stringResource(Res.string.chat),
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(Res.string.chat),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun RatingItemColumn(
    rating: WorkerRatingItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image with border
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                AsyncImage(
                    model = rating.userProfile,
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape),
                    placeholder = painterResource(Res.drawable.provider_logo),
                    error = painterResource(Res.drawable.provider_logo)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${rating.userFirstName} ${rating.userLastName}",
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D1E7C),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = if (rating.serviceTitle.isNullOrBlank()) stringResource(Res.string.no_Service_Name) else rating.serviceTitle,
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = JTColors.BackGroundBlueColor,
                        modifier = Modifier
                            .background(Color(0xFFF2F0FA), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                }

                Row {
                    Text(
                        text = stringResource(Res.string.service_Rating),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = rating.type,
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2D1E7C)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomSheetRatingRow(rating = rating.rating.toFloat())
                }

                rating.review?.let { reviewText ->
                    if (reviewText.isNotBlank()) {
                        Text(
                            text = reviewText,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFF4A4A4A),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RatingSection(
    serviceRating: Float,
    honestyRating: Float,
    experience: Int,
    type: String,
    honestType: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F8FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.service_Rating),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = type,
                        fontSize = 12.sp,
                        color = JTColors.BackGroundBlueColor,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }
                ProfileRatingRow(serviceRating)
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color(0xFFE0E0E0))
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.honesty_rating),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = honestType,
                        fontSize = 12.sp,
                        color = JTColors.BackGroundBlueColor,
                        fontFamily = JtBtoFonts.getInterMediumFont()
                    )
                }
                ProfileRatingRow(honestyRating)
            }
        }

        // Experience Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0EEF7))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.experience),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "$experience" + stringResource(Res.string.years),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}


@Composable
fun ProfileRatingRow(rating: Float) {
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
                modifier = Modifier.size(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${rating.toInt()}/5 ($label)",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun InfoCard(
    icon: DrawableResource,
    label: String,
    value: String,
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .border(1.dp,JTColors.notification_border, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(JTColors.timeSlotCardColor),
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.bto_transparenttext),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.8f)
                    .size(55.dp).align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3D1D77)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    letterSpacing = 0.sp,
                    lineHeight = 12.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )

                Text(
                    text = value,
                    fontSize = 15.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D1D77)
                )
            }
        }
    }
}
