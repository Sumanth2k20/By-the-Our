package com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.apply_filter
import bytheour.composeapp.generated.resources.clear
import bytheour.composeapp.generated.resources.current_local
import bytheour.composeapp.generated.resources.filter
import bytheour.composeapp.generated.resources.honesty_rating
import bytheour.composeapp.generated.resources.no_ratings
import bytheour.composeapp.generated.resources.note
import bytheour.composeapp.generated.resources.note_please
import bytheour.composeapp.generated.resources.provider_location
import bytheour.composeapp.generated.resources.review_and_rating
import bytheour.composeapp.generated.resources.review_given
import bytheour.composeapp.generated.resources.review_taken
import bytheour.composeapp.generated.resources.service_rating
import bytheour.composeapp.generated.resources.sort_location
import bytheour.composeapp.generated.resources.sort_rating
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.ProviderReviewAndRatingBottomSheet
import com.bytheour.showProgress
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.StarRating
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getKoinViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAndRatingsScreen(
    onBackPress: () -> Unit,
    viewModel: ReviewsAndDetailScreenViewmodel = getKoinViewModel(),
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedReview by remember { mutableStateOf<ReviewsAndDetailScreenViewmodel.ReviewItem?>(null) }
    val sharedpref = SharedPreferenceUseCase(Settings())
    val role = sharedpref.getUser()
    var isBackArrowClicked by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.fetchReviews(
            ratingType = "taken",
            offset = "0",
            isInitialLoad = true
        )
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex == totalItems - 2
        }
            .distinctUntilChanged()//prevents duplicate triggers when condition stays true
            .collect { shouldLoadMore ->
                if (shouldLoadMore) {
                    viewModel.loadMoreServices()
                }
            }
    }

    if (showFilterBottomSheet) {

        ReviewFilterBottomSheet(
            onDismiss = { showFilterBottomSheet = false },
            onApply = { selectedHonestRatings ->
                viewModel.fetchReviews(
                    ratingType = if (uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.TAKEN) "taken" else "given",
                    honestRatings = selectedHonestRatings.joinToString(","),
                    offset = "0",
                    isInitialLoad = true
                )
                showFilterBottomSheet = false
            },
            onClear = {
                viewModel.clearFilters()
                showFilterBottomSheet = false
            }
        )
    }





    Column(Modifier.fillMaxSize()
        .background(color = JTColors.enableButton)
        .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onBackPress()
                    isBackArrowClicked = true
                },
                enabled = !isBackArrowClicked
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = JTColors.WhiteColor
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                JtBtoCommonText(
                    stringResource(Res.string.review_and_rating),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 15.sp,
                    color = JTColors.WhiteColor
                )
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(44.dp)
                    .border(
                        width = 1.dp,
                        color = JTColors.WhiteColor,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clickable { showFilterBottomSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.filter),
                    contentDescription = "",
                    tint = JTColors.WhiteColor
                )
            }
        }


        Column(
            Modifier.fillMaxSize()
                .background(color = JTColors.WhiteColor)
        ) {


            Box(modifier = Modifier.fillMaxWidth()){

                val reviews =
                    if (uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.TAKEN)
                        uiState.reviewsTaken
                    else
                        uiState.reviewsGiven

                if (!showProgress.value && reviews.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.no_ratings),
                            color = JTColors.blackLight,
                            fontFamily = JtBtoFonts.getInterRegular(),
                            fontSize = 11.sp,
                            lineHeight = 11.sp
                        )
                    }
                } else {
                    LazyColumn(state = lazyListState) {
                        if (uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.GIVEN) {
                            item {
                                NoteBox()
                            }
                        } else {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        itemsIndexed(reviews) { index, review ->
                            RatingCard(
                                review = review,
                                onEditClick = {
                                    selectedReview = review
                                    showBottomSheet = true
                                },
                                uiState = uiState
                            )

                            if (index < reviews.lastIndex) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(0.9f),
                                    )
                                }
                            }
                        }
                    }
                }



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .offset(y = (-20).dp)
                        .padding(horizontal = 16.dp)
                        .background(
                            color = JTColors.WhiteColor,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabItem(
                        stringResource(Res.string.review_taken),
                        uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.TAKEN
                    ) {
                        viewModel.selectTab(ReviewsAndDetailScreenViewmodel.ReviewTab.TAKEN)
                    }
                    TabItem(
                        stringResource(Res.string.review_given),
                        uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.GIVEN
                    ) {
                        viewModel.selectTab(ReviewsAndDetailScreenViewmodel.ReviewTab.GIVEN)
                    }
                }
            }



        }

    }
    if (showBottomSheet && selectedReview != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                selectedReview = null
            },
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
                    serviceName = selectedReview!!.serviceTitle,
                    bookingId = selectedReview!!.bookingId,
                    providerName =
                    if(role == TYPE_CUSTOMER){
                        selectedReview!!.workerName
                    }else{
                        selectedReview!!.userName
                    },

                    onSubmitClick = { serviceRating, _, review ->
                        bookingViewModel.updateRating(
                            bookingId = selectedReview!!.id,
                            serviceRating = serviceRating ?: "",
                            rating = serviceRating ?: "",
                            review = review,
                            onSuccess = {
                                showBottomSheet = false
                                selectedReview = null
                            }
                        )

                    },
                    profileImage = selectedReview!!.userProfile,
                    onCloseClick = {
                        showBottomSheet = false
                        selectedReview = null
                    },
//                    showMediation = false,
//                    onMediationClick = {},
                    address = selectedReview!!.bookingDate
                )
            }
        }
    }
}

@Composable
fun NoteBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = JTColors.brown,
            )
            .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 12.dp)

    ) {
        Column {
            Text(
                text = stringResource(Res.string.note),
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                color = JTColors.purpleLight,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.note_please),
                color = JTColors.blackLight,
                fontFamily = JtBtoFonts.getInterRegular(),
                fontSize = 11.sp,
                lineHeight = 11.sp
            )
        }
    }
}

@Composable
fun RowScope.TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (isSelected) JTColors.enableButton else JTColors.WhiteColor
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            color = if (isSelected) JTColors.WhiteColor else JTColors.enableButton
        )
    }
}


@Suppress("UNUSED_PARAMETER")
@Composable
fun RatingCard(
    review: ReviewsAndDetailScreenViewmodel.ReviewItem,
    onEditClick: () -> Unit,
    uiState: ReviewsAndDetailScreenViewmodel.UiState,
) {
    val sharedpref = SharedPreferenceUseCase(Settings())

    val role = sharedpref.getUser()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            width = 2.dp,
                            color = JTColors.image_border,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(review.userProfile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(49.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column {
                    if (role.equals(TYPE_CUSTOMER, ignoreCase = true)){
                        Text(review.workerName, fontSize = 13.sp, fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            color = JTColors.enableButton)
                    }else{
                        Text(review.userName, fontSize = 13.sp, fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            color = JTColors.enableButton)
                    }
                    if (review.serviceTitle.isNotEmpty()){
                        Row {
                            Box(modifier = Modifier.background(color = JTColors.timeSlotCardColor,
                                shape = RoundedCornerShape(10.dp)).padding(2.dp)){
                                Text(
                                    review.serviceTitle,
                                    color = JTColors.enableButton,
                                    fontSize = 11.sp,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    modifier = Modifier.padding(2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                review.date,
                                fontFamily = JtBtoFonts.getInterRegular(),
                                color = Color(0xFF979797),
                                fontSize = 11.sp
                            )
                        }
                    }

                }
            }

            Spacer(Modifier.height(8.dp))
            RatingService(review = review, uiState = uiState)

            if (!review.review.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    review.review,
                    fontSize = 11.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                )
            }
        }

    }
}
@Composable
fun RatingService(
    review: ReviewsAndDetailScreenViewmodel.ReviewItem,
    uiState: ReviewsAndDetailScreenViewmodel.UiState
) {
    val sharedpref = SharedPreferenceUseCase(Settings())
    val role = sharedpref.getUser()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = JTColors.timeSlotCardColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        if (uiState.selectedTab == ReviewsAndDetailScreenViewmodel.ReviewTab.TAKEN ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (role ==TYPE_PROVIDER){
                    Column {
                        Row {
                            Text(
                                stringResource(Res.string.service_rating) + ": ",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getInterRegular()
                            )
                            Text(
                                review.giveServiceRatingText ?: "",
                                color = JTColors.enableButton,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getInterMediumFont()
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StarRating(review.serviceRating.toDouble())
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(0.9f),
                        color = JTColors.grey_divider.copy(alpha = 0.4f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }



                Column {
                    Row {
                        Text(
                            stringResource(Res.string.honesty_rating) + ": ",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getInterRegular()
                        )
                        Text(
                            review.type,
                            color = JTColors.enableButton,
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont()
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StarRating(review.honestRating.toDouble())
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.honesty_rating) + ": ",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular()
                )
                Text(
                    review.type,
                    color = JTColors.enableButton,
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarRating(review.honestRating.toDouble())
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFilterBottomSheet(
    onDismiss: () -> Unit,
    onApply: (selectedRatings: Set<Int>
//              , selectedLocation: String?
    ) -> Unit,
    onClear: () -> Unit
) {
    val ratingOptions = listOf(0, 3, 5, 2, 4, 1)
    val ratingLabels = mapOf(
        0 to "All",
        1 to "Bad",
        2 to "Poor",
        3 to "Average",
        4 to "Great",
        5 to "Excellent",
    )

    val selectedRatings = rememberSaveable { mutableStateOf(setOf<Int>()) }
    val selectedLocation = rememberSaveable { mutableStateOf<String?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = JTColors.WhiteColor,
        dragHandle = null
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(Res.string.filter),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.RadioText,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp).clickable { onDismiss()  }
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.sort_rating),
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = JTColors.enableButton
            )

            Spacer(Modifier.height(8.dp))

            ratingOptions.chunked(2).forEach { rowItems ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { rating ->
                        val isChecked = selectedRatings.value.contains(rating)
                        Row(
                            Modifier
                                .weight(1f)
                                .clickable {
                                    selectedRatings.value = if (isChecked) {
                                        selectedRatings.value - rating
                                    } else {
                                        selectedRatings.value + rating
                                    }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null
                            )
                            Text(
                                text = "⭐ ${ratingLabels[rating]}",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier
                    .height(0.5.dp)
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                color = JTColors.grayTextColor.copy(alpha = 40f)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text =stringResource(Res.string.sort_location),
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = JTColors.enableButton
            )

            Spacer(Modifier.height(8.dp))

            val locationOptions = listOf(stringResource(Res.string.current_local), stringResource(Res.string.provider_location))
            locationOptions.forEach { location ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { selectedLocation.value = location },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLocation.value == location,
                        onClick = { selectedLocation.value = location }
                    )
                    Text(
                        text = location,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                JTBtoPrimaryButton(
                    name = stringResource(Res.string.clear),
                    onClick = {
                        selectedRatings.value = emptySet()
                        selectedLocation.value = null
                        onClear()
                    },
                    buttonColors = JTColors.WhiteColor,
                    selectedColor = JTColors.WhiteColor,
                    textColor = JTColors.enableButton,
                    modifier = Modifier.weight(1f)
                        .border(1.dp, color = JTColors.viewBorder,
                        shape = RoundedCornerShape(30.dp)
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                JTBtoPrimaryButton(
                    name = stringResource(Res.string.apply_filter),
                    onClick = {
                        onApply(selectedRatings.value,
//                        selectedLocation.value
                        )
                    },
                    buttonColors = JTColors.enableButton,
                    selectedColor = JTColors.enableButton,
                    textColor = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}