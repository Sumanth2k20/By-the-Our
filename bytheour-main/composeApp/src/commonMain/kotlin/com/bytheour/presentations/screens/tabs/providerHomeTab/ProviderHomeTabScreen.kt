package com.bytheour.presentations.screens.tabs.providerHomeTab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.accept
import bytheour.composeapp.generated.resources.address
import bytheour.composeapp.generated.resources.address_ico1
import bytheour.composeapp.generated.resources.booking_ID
import bytheour.composeapp.generated.resources.btologo
import bytheour.composeapp.generated.resources.celeb
import bytheour.composeapp.generated.resources.complete_lead
import bytheour.composeapp.generated.resources.completedleads_ico
import bytheour.composeapp.generated.resources.current_assigned_leads
import bytheour.composeapp.generated.resources.current_balance
import bytheour.composeapp.generated.resources.currentbalance_ico
import bytheour.composeapp.generated.resources.decline
import bytheour.composeapp.generated.resources.earningsthismonth_ico
import bytheour.composeapp.generated.resources.estimated_Hours
import bytheour.composeapp.generated.resources.hello
import bytheour.composeapp.generated.resources.hour
import bytheour.composeapp.generated.resources.info_black_ico
import bytheour.composeapp.generated.resources.inspectionId
import bytheour.composeapp.generated.resources.inspectionRate
import bytheour.composeapp.generated.resources.inspection_Details
import bytheour.composeapp.generated.resources.miles_ico
import bytheour.composeapp.generated.resources.month_earnings
import bytheour.composeapp.generated.resources.na
import bytheour.composeapp.generated.resources.new_assigned_leads
import bytheour.composeapp.generated.resources.no_address
import bytheour.composeapp.generated.resources.privacy_ico
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.remain_unverified
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.total_earnings
import bytheour.composeapp.generated.resources.total_lead
import bytheour.composeapp.generated.resources.total_withDraw
import bytheour.composeapp.generated.resources.totalearnings_ico
import bytheour.composeapp.generated.resources.totalleads_ico
import bytheour.composeapp.generated.resources.totalwithdrawals_ico
import bytheour.composeapp.generated.resources.undraw_real_time_analytics_50za_1
import bytheour.composeapp.generated.resources.upload_documents
import bytheour.composeapp.generated.resources.you_have_a
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.screens.JTBtoMainScreenViewModel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.DateTimeCard
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.AppFeatures
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.MapNavigator
import com.bytheour.utils.PermissionType
import com.bytheour.utils.createMapNavigator
import com.bytheour.utils.formatDateString
import com.bytheour.utils.getFormattedDistanceInMiles
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import com.bytheour.utils.usePermission
import com.russhwolf.settings.Settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashBoardScreen(
    viewModel: ProviderHomeViewModel = getKoinViewModel(),
    mainScreenViewModel:JTBtoMainScreenViewModel = getKoinViewModel(),
    navigateToNotificationListScreen:()->Unit,
    navigateToWebView:(String,String)-> Unit,
){

    val uiState by viewModel.uiStates.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAddServiceDialogue by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue ->
            sheetValue != SheetValue.Hidden
        }
    )

    val requestNotificationPermission = usePermission(
        permissionType = PermissionType.NOTIFICATION,
        onPermissionGranted = {
            println("Notification permission granted")
        }
    )

    LaunchedEffect(Unit) {
        if (!uiState.notificationPermissionRequested) {
            requestNotificationPermission()
            viewModel.setNotificationPermissionRequested()
        }
    }
    LaunchedEffect(Unit){
        viewModel.getProfile()
        viewModel.getProviderDashBoardDetails()
        viewModel.getProviderBookingRequest()
        viewModel.getWorkerServices()
    }

    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val defaultAddress = sharedPreferenceUseCase.getDefaultAddress()

    println("Provider Default Address =================== $defaultAddress")
    val parts = defaultAddress.split(",").map { it.trim() }
    val defaultLng = parts.getOrNull(parts.size - 2)?.toDoubleOrNull()
    val defaultLat = parts.getOrNull(parts.size - 1)?.toDoubleOrNull()
    println("Default Latitude:===================== $defaultLat")
    println("Default Longitude:===================== $defaultLng")
    val customerLat = uiState.customerLat?.toDoubleOrNull()
    val customerLong = uiState.customerLong?.toDoubleOrNull()
    val distanceInMiles = getFormattedDistanceInMiles(
        lat1 = customerLat,
        lon1 = customerLong,
        lat2 = defaultLat,
        lon2 = defaultLng
    ) ?: "N/A"
    val map: MapNavigator = createMapNavigator()



    LaunchedEffect(uiState.showAddServiceDialog){
        if (uiState.showAddServiceDialog){
            showAddServiceDialogue = true
        }
    }


    LaunchedEffect(uiState.notAssigned, uiState.accepted) {
        if (uiState.notAssigned.equals("Request", ignoreCase = true) && uiState.accepted == false) {
            showBottomSheet = true
            coroutineScope.launch {
                    bottomSheetState.show()
            }
        } else {
            showBottomSheet = false
        }
    }

    if (!showAddServiceDialogue){
        LaunchedEffect(Unit) {
            while (true) {
                viewModel.getProviderBookingRequest()
                delay(3000)
                if (uiState.accepted == true){
                    showBottomSheet= false
                    bottomSheetState.hide()
                }
            }
        }
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
            },
            sheetState = bottomSheetState,
            containerColor = JTColors.WhiteColor,
            dragHandle = null,
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp,
            ),
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false
            )
        ) {
            BookingRequestBottomSheet(
                bookingId = uiState.id ?: "",
                serviceTitle = uiState.serviceName ?: stringResource(Res.string.na),
                serviceSubtitle = uiState.description ?: "",
                date = formatDateString(uiState.date ?: stringResource(Res.string.na)),
                timeSlot = uiState.time ?: stringResource(Res.string.na),
                priceRange = uiState.estimation?:"",
                address = uiState.address ?: "",
                onAccept = {
                    viewModel.acceptBookingRequest(
                        uiState.id ?: "",
                        onSuccess = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                                showBottomSheet = false
                                viewModel.clearState()
                                mainScreenViewModel.onNavItemClicked(1)
                                mainScreenViewModel.setProviderInitialTab(0)
                            }
                        }
                    )
                },
                onDecline = {
                    viewModel.rejectBookingRequest(
                        uiState.id ?: "",
                        onSuccess = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                                showBottomSheet = false
                                viewModel.clearState()
                            }
                        }
                    )
                },
                imagePainter = uiState.profile ?: "",
                isInspection = uiState.isInspection?:true,
                inspectionRate = uiState.inspectionCalculatedAmount?:0.0,
                onInfoClick = {},
                problem = uiState.problem?:"",
                problemDescription = uiState.problemDescription?:"",
                problemSolution = uiState.solution?:"",
                rejectReason = uiState.reason?:"",
                cost = uiState.estimatedCost.toString(),
                time = uiState.availableTime?:"",
                hasInspectionReport = uiState.hasInspectionReport,
                mile = distanceInMiles,
                onDirectionClick = {
                    map.openGoogleMaps(
                        latitude = customerLat?:0.0 ,
                        longitude = customerLong?:0.0,
                        pinCode = uiState.pinCode?:""
                    )
                }
            )
        }
    }


   val onAddServiceClick: () -> Unit = {
       mainScreenViewModel.onNavItemClicked(3)
       mainScreenViewModel.setProviderInitialTab(0)
   }

    if (showAddServiceDialogue){
        Box(
            modifier = Modifier
                .paint(painterResource(Res.drawable.undraw_real_time_analytics_50za_1))
                .fillMaxSize()
                .background(JTColors.enableButton),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.btologo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp).width(168.dp)
                        .align(Alignment.CenterHorizontally)

                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle text
                Text(
                    text = "List your services to attract customers.\n" +
                            "Don't see your service? Request it and\n" +
                            "we'll approve it within 24 hours.",
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                JTBtoPrimaryButton(
                    name = "Add Service",
                    onClick = {
                        onAddServiceClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    buttonColors = JTColors.WhiteColor,
                    textColor = JTColors.BackGroundBlueColor,
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    iconColor = JTColors.WhiteColor
                )
            }
        }
    }else{
        Column (
            modifier = Modifier.fillMaxSize()
                .background(JTColors.WhiteColor)
        ){
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = JTColors.BackGroundBlueColor)
            ) {
                if(platform.equals("IOS", ignoreCase = true)){
                    Spacer(modifier = Modifier.height(50.dp))
                }else{
                    Spacer(modifier = Modifier.height(40.dp))
                }

                Row (modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                    ) {
                        if(!uiState.profile.isNullOrEmpty()){
                            AsyncImage(
                                model = uiState.userProfile,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }else{
                            Image(
                                painter = painterResource(Res.drawable.provider_logo),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = stringResource(Res.string.hello),
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            fontFamily = JtBtoFonts.getInterRegular(),
                            color = JTColors.WhiteColor.copy(alpha = 0.5f)
                        )
                        Text(
                            text = uiState.userName?:"".ifEmpty { stringResource(Res.string.na)},
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            color = JTColors.WhiteColor
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.clickable {

                            navigateToNotificationListScreen()
                        }.padding(end = 10.dp)
                    )

                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(Color(0xFFDED7F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("The ")
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                                    color = Color(0xFF2C1975) // Dark text color for highlight
                                )
                            ) {
                                append("First Three Completed")
                            }
                            append(" Jobs Are Complimentary")
                        },
                        color = Color(0xFF2C1975),
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getInterFont(),
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                val totalEarnings = uiState.totalEarnings?.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0
                val monthEarnings = uiState.monthEarnings?.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0

                data class DashItem(
                    val title: String,
                    val amount: String,
                    val image:Painter,
                    val showDollar:Boolean
                )

                val items = listOf(
                    DashItem(
                        title = stringResource(Res.string.total_earnings),
                        amount = roundToTwoDecimals(totalEarnings).toString(),
                        image = painterResource(Res.drawable.totalearnings_ico),
                        showDollar = true
                    ),
                    DashItem(
                        title = stringResource(Res.string.month_earnings),
                        amount = roundToTwoDecimals(monthEarnings).toString(),
                        image = painterResource(Res.drawable.earningsthismonth_ico),
                        showDollar = true

                    ),
                    DashItem(
                        title = stringResource(Res.string.total_withDraw),
                        amount = uiState.withdrawAmount ?: "",
                        image = painterResource(Res.drawable.totalwithdrawals_ico),
                        showDollar = true
                    ),
                    DashItem(
                        title = stringResource(Res.string.current_balance),
                        amount = uiState.currentBalance ?: "",
                        image = painterResource(Res.drawable.currentbalance_ico),
                        showDollar = true
                    ),
                    DashItem(
                        title = stringResource(Res.string.total_lead),
                        amount = uiState.totalLead ?: "",
                        image = painterResource(Res.drawable.totalleads_ico),
                        showDollar = false
                    ),
                    DashItem(
                        title = stringResource(Res.string.complete_lead),
                        amount = uiState.completedLead ?: "",
                        image = painterResource(Res.drawable.completedleads_ico),
                        showDollar = false
                    )
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        val shape = when (index) {
                            0 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 20.dp)
                            1 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                            2 -> RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 20.dp)
                            3 -> RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                            4 -> RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
                            5 -> RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
                            else -> RoundedCornerShape(20.dp)
                        }
                        DashBoardCard(
                            modifier = Modifier
                                .height(80.dp)
                                .fillMaxWidth(),
                            title = item.title,
                            amount = item.amount,
                            image = item.image,
                            shape =shape,
                            showDollar = item.showDollar
                        )
                    }
                }
                Spacer(Modifier.height(25.dp))
            }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            LeadsCard(
                count = uiState.currentAssignedLeads ?: "0",
                title = stringResource(Res.string.current_assigned_leads),
                onClick = {
                    mainScreenViewModel.onNavItemClicked(1)
                    mainScreenViewModel.setProviderInitialTab(0)
                }
            )
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray.copy(alpha = 0.5f))
            LeadsCard(
                count = uiState.newAssignedLeads ?: "0",
                title = stringResource(Res.string.new_assigned_leads),
                onClick = {
                    mainScreenViewModel.onNavItemClicked(1)
                    mainScreenViewModel.setProviderInitialTab(0)
                }
            )
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray.copy(alpha = 0.5f))
            if (uiState.verificationStatus=="process") {
                LeadsCard(
                    count = null,
                    title = stringResource(Res.string.remain_unverified),
                    onClick = {
                        viewModel.verifyAccountWithStripe()
                    }
                )
                HorizontalDivider(
                    Modifier.fillMaxWidth(),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }

        }
    }
    if (uiState.webUrl != null){
        navigateToWebView(uiState.webUrl ?: "",stringResource(Res.string.upload_documents))
        viewModel.updateWebUrl()
    }
}

}

@Composable
fun LeadsCard(
    count: String?,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(JTColors.WhiteColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Number inside rounded box
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFEFEF)),
                contentAlignment = Alignment.Center
            ) {
                if (count!=null) {
                    Text(
                        text = count,
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        color = JTColors.BackGroundBlueColor
                    )
                }else {
                    Image(
                        painter = painterResource(Res.drawable.privacy_ico),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title text
            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                color = JTColors.BlackButtonColor,
                modifier = Modifier.weight(1f)
            )

            // Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = JTColors.BackGroundBlueColor
            )
        }
    }
}


@Composable
fun DashBoardCard(
    modifier: Modifier,
    title: String,
    amount: String,
    image: Painter,
    shape: Shape,
    showDollar:Boolean = true
) {
    Card(
        shape = shape,
        modifier = modifier,
        colors = CardDefaults.cardColors(JTColors.WhiteColor),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(start = 12.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                )
                Text(
                    text = if (showDollar){
                        "$$amount"
                    }else{
                        amount
                    },
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                )
            }
        }
    }
}

fun roundToTwoDecimals(value: Double): Double {
    return kotlin.math.round(value * 100) / 100.0
}


@Composable
fun BookingRequestBottomSheet(
    bookingId: String,
    serviceTitle: String,
    serviceSubtitle: String,
    date: String,
    timeSlot: String,
    priceRange: String,
    address: String,
    imagePainter: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isInspection:Boolean,
    inspectionRate :Double,
    onInfoClick:()-> Unit,
    problem :String,
    problemDescription:String,
    problemSolution :String,
    rejectReason :String,
    cost :String,
    time :String,
    mile:String,
    hasInspectionReport: Boolean,
    onDirectionClick :()-> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JTColors.WhiteColor)
            .padding(bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
                .clip(RoundedCornerShape(bottomStart = 220.dp, bottomEnd = 220.dp))
                .background(JTColors.ErrorMessage),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.celeb),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(Res.string.you_have_a),
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.WhiteColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (isInspection)"Inspection request !!" else "Booking request !!" ,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.WhiteColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Gray.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isInspection) {
                            stringResource(Res.string.inspectionId) + " " + bookingId
                        } else {
                            stringResource(Res.string.booking_ID) + " " + bookingId
                        },
                        fontSize = 12.sp,
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getInterFont(),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Start)) {
                AsyncImage(
                    model = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = serviceTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = serviceSubtitle,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = JTColors.grayTextColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray)
            Spacer(modifier = Modifier.height(10.dp))
            DateTimeCard(
                date = date,
                time = timeSlot,
                isForBottomSheet = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray)
            Spacer(modifier = Modifier.height(10.dp))

            if (isInspection && AppFeatures.INSPECTION_RATE) {
                val iconPainter = painterResource(Res.drawable.time_ico)
                InfoRow(
                    icon = iconPainter,
                    label = stringResource(Res.string.inspectionRate),
                    value = "$inspectionRate" + stringResource(Res.string.hour)
                )
            }

            if (!isInspection) {
                val iconPainter = painterResource(Res.drawable.time_ico)

                InfoRow(
                    icon = iconPainter,
                    label = stringResource(Res.string.estimated_Hours),
                    value = priceRange + stringResource(Res.string.hour)
                )
                HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.address_ico1),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        stringResource(Res.string.address),
                        fontSize = 11.sp,
                        color = JTColors.textFieldColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        lineHeight = 11.sp
                    )
                    Text(
                        address.ifEmpty { stringResource(Res.string.no_address) },
                        fontSize = 13.sp,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        lineHeight = 19.sp,
                        color = JTColors.RadioText
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onDirectionClick() }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.miles_ico),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Text(
                        text = "$mile/miles",
                        fontSize = 11.sp,
                        color = JTColors.textFieldColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        lineHeight = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray)
            Spacer(modifier = Modifier.height(14.dp))

            if (hasInspectionReport) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isExpanded = !isExpanded
                                onInfoClick()
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.info_black_ico),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = stringResource(Res.string.inspection_Details),
                                fontSize = 11.sp,
                                color = JTColors.textFieldColor,
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                                lineHeight = 11.sp
                            )
                        }
                        Image(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "null",
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            DetailItem(label = "Problem", value = problem)
                            Spacer(modifier = Modifier.height(12.dp))

                            DetailItem(label = "Problem Description", value = problemDescription)
                            Spacer(modifier = Modifier.height(12.dp))

                            DetailItem(label = "Problem Solution", value = problemSolution)
                            Spacer(modifier = Modifier.height(12.dp))

                            DetailItem(label = "EstimationCost", value = cost)
                            Spacer(modifier = Modifier.height(12.dp))

                            DetailItem(label = "availableTime", value = time)
                            Spacer(modifier = Modifier.height(12.dp))

                            DetailItem(label = "Cancel Reason", value = rejectReason)
                        }
                    }
                    HorizontalDivider(Modifier.fillMaxWidth(), color = Color.LightGray)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onDecline,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 8.dp),
                border = BorderStroke(1.dp, JTColors.BackGroundBlueColor)
            ) {
                Text(stringResource(Res.string.decline), color = JTColors.BackGroundBlueColor)
            }

            Button(
                onClick = onAccept,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JTColors.BackGroundBlueColor)
            ) {
                Text(stringResource(Res.string.accept), color = Color.White)
            }
        }
    }
}


@Composable
fun DetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = JTColors.textFieldColor,
            fontFamily = JtBtoFonts.getOpenSansSemiBold()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun InfoRow(
    icon: Painter,
    label: String,
    value: String?= null,
    onClick: () -> Unit = {},
    trailIcon :Painter?= null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
        )

        Column {
            Text(
                label,
                fontSize = 11.sp,
                color =JTColors.textFieldColor,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                lineHeight = 11.sp
            )
            Text(
                value?:"",
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                lineHeight = 19.sp,
                color = JTColors.RadioText
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        trailIcon?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
        }
    }
}