package com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.address
import bytheour.composeapp.generated.resources.address_ico1
import bytheour.composeapp.generated.resources.afternoon
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.book_later
import bytheour.composeapp.generated.resources.book_now
import bytheour.composeapp.generated.resources.booking_past_time_error
import bytheour.composeapp.generated.resources.booking_summary
import bytheour.composeapp.generated.resources.calandar_leftarrow_ico
import bytheour.composeapp.generated.resources.calandar_rightarrow_ico
import bytheour.composeapp.generated.resources.calendar
import bytheour.composeapp.generated.resources.confirm_Book
import bytheour.composeapp.generated.resources.date
import bytheour.composeapp.generated.resources.dollar_ico
import bytheour.composeapp.generated.resources.estimated_Price
import bytheour.composeapp.generated.resources.evening
import bytheour.composeapp.generated.resources.go_Back
import bytheour.composeapp.generated.resources.go_back_Address
import bytheour.composeapp.generated.resources.morning
import bytheour.composeapp.generated.resources.no_AddressFound
import bytheour.composeapp.generated.resources.please_select
import bytheour.composeapp.generated.resources.request_inspection
import bytheour.composeapp.generated.resources.request_service
import bytheour.composeapp.generated.resources.select_date
import bytheour.composeapp.generated.resources.select_time
import bytheour.composeapp.generated.resources.service_Unavailable
import bytheour.composeapp.generated.resources.time_ico
import bytheour.composeapp.generated.resources.time_slot
import bytheour.composeapp.generated.resources.unavailable_at_your_location
import bytheour.composeapp.generated.resources.work_location_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.DayCustomTabRow
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.customcomponents.formatDate
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.withDayOfMonth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionDateOfService(
    selectionDateOfServiceViewModel :SelectionDateOfServiceViewModel = getKoinViewModel(),
    navigateToConfirmationScreen:(String,String,String,String)->Unit,
    navigateBack:()-> Unit,
    serviceId:String,
    title:String,
    description: String,
    imageUrl: String,
    navigateToInspectionBilling:(String,String,String,String)-> Unit,
    fromInspection: Boolean,
    inspectionBookingId :String,
    inspectionRate:String
) {
    val uiState by selectionDateOfServiceViewModel.uiState.collectAsState()
    var showConfirmationBottomSheet by remember { mutableStateOf(false) }
    var showDateAndTimeSlots by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        selectionDateOfServiceViewModel.getAddressList(limit = "10", offset = 0)
    }
    val defaultAddress = uiState.addressList.firstOrNull { it.defaultAddress == true }
    val fullAddress = defaultAddress?.let { "${it.flatNo}" } ?: "No address selected"
    LaunchedEffect(defaultAddress) {
        defaultAddress?.let {
            selectionDateOfServiceViewModel.getServiceDetails(
                serviceId = serviceId,
                lat = it.latitude ?: "",
                long = it.longitude ?: ""
            )
        }
    }
    var showNoAddressDialog by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isAddressFetchComplete) {
        if (uiState.isAddressFetchComplete && uiState.addressList.isEmpty()) {
            showNoAddressDialog = true
        } else {
            showNoAddressDialog = false
        }
    }


    LaunchedEffect(uiState.isNavigate, uiState.inspectionSuccess) {

        val bookingId = uiState.bookingId

        if (bookingId.isNullOrBlank() || bookingId == "0") {
            if (uiState.isNavigate == true || uiState.inspectionSuccess == true) {
                showConfirmationBottomSheet = false
                selectionDateOfServiceViewModel.showSnackBarMessage(
                    "Booking Not Created, Please Try Again"
                )
                selectionDateOfServiceViewModel.clearNavigationFlags()
            }
            return@LaunchedEffect
        }

        if (uiState.isNavigate == true) {
            showConfirmationBottomSheet = false
            navigateToConfirmationScreen(title, fullAddress, imageUrl, bookingId)
            selectionDateOfServiceViewModel.clearNavigationFlags()
        }

        if (uiState.inspectionSuccess == true) {
            showConfirmationBottomSheet = false
            navigateToInspectionBilling(title, fullAddress, imageUrl, bookingId)
            selectionDateOfServiceViewModel.clearNavigationFlags()
        }
    }

    val formattedTime = convertTo24HourWithSecondsKMP(uiState.selectedTime)


    LaunchedEffect(Unit) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val currentDate = now.date
        val currentTime = "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:00"

        // Set default date + time
        selectionDateOfServiceViewModel.onDateSelected(currentDate)
        selectionDateOfServiceViewModel.onTimeSelected(convertTo12HourFormat(currentTime))

        if (now.hour !in 8..<20) {
            showDateAndTimeSlots = true      // show Book Later UI
            selectionDateOfServiceViewModel.onTimeSelected("") // Clear time selection
        }
    }





    var showTimeError by remember { mutableStateOf(false) }
    var showServiceErrorDialog by remember { mutableStateOf(false) }

    if (uiState.showAlert == true) {
        showServiceErrorDialog = true
    }
    CustomAlertDialog(
        showDialog = showServiceErrorDialog,
        onDismiss = {
            showServiceErrorDialog = false
            navigateBack()
        },
        title = stringResource(Res.string.service_Unavailable),
        message =  stringResource(Res.string.unavailable_at_your_location),
        yes = false,
        no = false,
        onConfirm = {
            showServiceErrorDialog = false
            navigateBack()
        },
        onCancel = { },
        changeText = true,
        changedText = stringResource(Res.string.go_Back)
    )
    CustomAlertDialog(
        showDialog = showNoAddressDialog,
        onDismiss = {
            showNoAddressDialog = false
            navigateBack()
        },
        title = stringResource(Res.string.no_AddressFound),
        message = stringResource(Res.string.go_back_Address),
        yes = false,
        no = false,
        onConfirm = {
            showNoAddressDialog = false
            navigateBack()
        },
        onCancel = {},
        changeText = true,
        changedText = stringResource(Res.string.go_Back)
    )


    BookingSummaryBottomSheet(
        showBottomSheet = showConfirmationBottomSheet,
        onDismiss = { showConfirmationBottomSheet = false },
        onConfirm = {
            selectionDateOfServiceViewModel.bookingService(
                service = serviceId,
                hours = "1",
                date = uiState.selectedDate.toString(),
                time = formattedTime,
                timeZone = TimeZone.currentSystemDefault().id,
                address = defaultAddress?.id ?: "",
                longitude = defaultAddress?.longitude ?: "",
                latitude = defaultAddress?.latitude?:"",
                inspectionBookingId = if (fromInspection) inspectionBookingId else ""
            )
            showConfirmationBottomSheet = false
        },
        imageUrl = imageUrl,
        dayAndDate = uiState.selectedDate.toString(),
        timeslot = uiState.selectedTime ?: "",
        minRate = uiState.minRate,
        maxRate = uiState.maxRate,
        serviceName = title,
        serviceDesc = description,
        address = fullAddress,
        onInspectionClick = {
            selectionDateOfServiceViewModel.makeInspectionRequest(
                service = serviceId,
                address = defaultAddress?.id,
                date = uiState.selectedDate.toString(),
                time = formattedTime,
                timeZone = TimeZone.currentSystemDefault().id,
            )
        },
        fromInspection = fromInspection,
        inspectionRate = inspectionRate
    )

    LaunchedEffect(Unit) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentDate = now.date
        val currentTime = "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:00"

        selectionDateOfServiceViewModel.onDateSelected(currentDate)
        selectionDateOfServiceViewModel.onTimeSelected(convertTo12HourFormat(currentTime))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    JtBtoCommonText(
                        text = title,
                        fontSize = 15.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        color = JTColors.WhiteColor,
                        modifier = Modifier.padding(start = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(Res.drawable.arrowleft),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 10.dp).clickable { navigateBack() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = JTColors.enableButton
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                defaultAddress?.let {
                    AddressDetails(address = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                ServiceCard(
                    imageUrl = imageUrl,
                    serviceName = uiState.serviceTitle ?: title,
                    description = description,
                    minRate = uiState.minRate,
                    maxRate = uiState.maxRate,
                    fromInspection = fromInspection
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
//                    JtBtoCommonText(
//                        text = "Book Service",
//                        color = JTColors.enableButton,
//                        fontSize = 14.sp,
//                        fontFamily = JtBtoFonts.getInterMediumFont(),
//                        modifier = Modifier.padding(start = 18.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(10.dp))

                    if (!fromInspection){
                        JtBtoCommonText(
                            text = "Inspection Charges : $$inspectionRate",
                            color = JTColors.enableButton,
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            modifier = Modifier.padding(start = 18.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(0.95f)
                            .padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                    val hour = now.hour

                                    when {
                                        hour < 8 -> {
                                            selectionDateOfServiceViewModel.showSnackBarMessage("Cannot book before 8:00 AM")
                                            return@clickable
                                        }
                                        hour >= 20 -> {
                                            selectionDateOfServiceViewModel.showSnackBarMessage("Cannot book after 8:00 PM")
                                            return@clickable
                                        }
                                    }

                                    val currentDate = now.date
                                    val currentTime =
                                        "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:00"

                                    selectionDateOfServiceViewModel.onDateSelected(currentDate)
                                    selectionDateOfServiceViewModel.onTimeSelected(convertTo12HourFormat(currentTime))
                                    showDateAndTimeSlots = false
                                    showTimeError = false
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !showDateAndTimeSlots,
                                onClick = {
                                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                    val currentDate = now.date
                                    val currentTime = "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:00"

                                    selectionDateOfServiceViewModel.onDateSelected(currentDate)
                                    selectionDateOfServiceViewModel.onTimeSelected(convertTo12HourFormat(currentTime))
                                    showDateAndTimeSlots = false
                                    showTimeError = false
                                },
                                modifier = Modifier.scale(1.4f),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = JTColors.BackGroundBlueColor,
                                    disabledSelectedColor = JTColors.grayTextColor,
                                    disabledUnselectedColor = JTColors.grayTextColor,
                                    unselectedColor = JTColors.grayTextColor
                                )
                            )

                            Text(
                                text = stringResource(Res.string.book_now),
                                fontSize = 13.sp,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                                color = JTColors.BackGroundBlueColor,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }


                        Spacer(modifier = Modifier.width(10.dp))

                        Row(
                            modifier = Modifier
                                .clickable {
                                    showDateAndTimeSlots = true
                                    // Clear the time when switching to Book Later
                                    selectionDateOfServiceViewModel.onTimeSelected("")
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = showDateAndTimeSlots,
                                onClick = {
                                    showDateAndTimeSlots = true
                                    // Clear the time when switching to Book Later
                                    selectionDateOfServiceViewModel.onTimeSelected("")
                                },
                                modifier = Modifier.scale(1.4f),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = JTColors.BackGroundBlueColor,
                                    unselectedColor = JTColors.grayTextColor
                                )
                            )
                            Text(
                                text = stringResource(Res.string.book_later),
                                fontSize = 13.sp,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                                color = JTColors.BackGroundBlueColor,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    if (showDateAndTimeSlots) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = JTColors.border.copy(alpha = 40f)
                        )

                        JtBtoCommonText(
                            text = stringResource(Res.string.select_date),
                            color = JTColors.enableButton,
                            fontSize = 14.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            modifier = Modifier.padding(18.dp),
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = JTColors.border.copy(alpha = 40f)
                        )

                        HorizontalDatePicker(
                            onDateSelected = {
                                selectionDateOfServiceViewModel.onDateSelected(it)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = JTColors.border.copy(alpha = 40f)
                        )

                        JtBtoCommonText(
                            text = stringResource(Res.string.select_time),
                            color = JTColors.enableButton,
                            fontSize = 14.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            modifier = Modifier.padding(20.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = JTColors.border.copy(alpha = 40f)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        SelectTime(
                            uiState = uiState,
                            selectionDateOfServiceViewModel = selectionDateOfServiceViewModel
                        )

                        if (showTimeError) {
                            Text(
                                text = stringResource(Res.string.please_select),
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getInterRegular(),
                                modifier = Modifier.padding(start = 20.dp, top = 4.dp)
                            )
                        }


                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = JTColors.border.copy(alpha = 40f)
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        val isButtonEnabled = !showDateAndTimeSlots || !uiState.selectedTime.isNullOrBlank()
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                .background(color = JTColors.WhiteColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            JTBtoPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                name = if (fromInspection) stringResource(Res.string.request_service) else stringResource(Res.string.request_inspection),
                onClick = {
                    if (showDateAndTimeSlots && uiState.selectedTime.isNullOrBlank()) {
                        showTimeError = true
                    } else {
                        showConfirmationBottomSheet = true
                        showTimeError = false
                    }
                },
                buttonColors = if (isButtonEnabled) JTColors.enableButton else JTColors.disableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
        ) {
            if (uiState.message?.isNotBlank() == true) {
                CommonSnackbar(
                    message = uiState.message ?: "",
                    type = SnackbarType.ERROR,
                    onDismiss = {
                        selectionDateOfServiceViewModel.clearError()
                    }
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    imageUrl: String,
    serviceName: String,
    description: String,
    minRate: Int,
    maxRate: Int,
    fromInspection: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        shape = RoundedCornerShape(7.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(7.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = serviceName,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 15.sp,
                    color = Color.Black,
                    lineHeight = 12.sp
                )

                Text(
                    text = description,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    overflow = if (expanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    color = JTColors.grayTextColor,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                if (fromInspection){
                    Text(
                        text = "$$minRate/min - $$maxRate/max",
                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun AddressDetails(address: AddressDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = JTColors.addressGray)
            .border(width = 1.dp, color = JTColors.border.copy(alpha = 40f))
            .padding(12.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.work_location_ico),
            contentDescription = "",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            JtBtoCommonText(
                text = address.locationType ?: stringResource(Res.string.address),
                color = JTColors.black,
                fontSize = 14.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont()
            )
            Spacer(modifier = Modifier.height(3.dp))
            JtBtoCommonText(
                text = address.flatNo ?: "",
                color = JTColors.addressText,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getInterRegular()
            )
        }
    }
}

@Composable
fun HorizontalDatePicker(
    initialSelectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }
    var currentMonth by remember { mutableStateOf(selectedDate) }
    val daysInMonth = remember(currentMonth) { getDaysInMonth(currentMonth) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll to selected item after first composition
    LaunchedEffect(currentMonth) {
        val index = selectedDate.dayOfMonth - 1
        if (index in 0..<daysInMonth) {
            coroutineScope.launch {
                listState.scrollToItem(index)
            }
        }
    }

    Box(
        modifier = Modifier
            .background(JTColors.dateColor)
            .border(width = 1.dp, color = JTColors.dateColor)
    ) {
        Column(
            modifier = Modifier
                .background(JTColors.dateColor)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            MonthHeader(currentMonth) { newDate ->
                currentMonth = newDate
            }

            Spacer(modifier = Modifier.height(20.dp))

            DaysHorizontalList(
                month = currentMonth,
                daysInMonth = daysInMonth,
                selectedDate = selectedDate,
                initialSelectedDate = initialSelectedDate,
                listState = listState,
                onDateSelected = { date ->
                    selectedDate = date
                    onDateSelected(date)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun MonthHeader(
    currentMonth: LocalDate,
    onMonthChanged: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.calandar_leftarrow_ico),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onMonthChanged(currentMonth.minus(1, DateTimeUnit.MONTH))
                }
        )

        JtBtoCommonText(
            text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }}, ${currentMonth.year}",
            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            fontSize = 15.sp
        )

        Image(
            painter = painterResource(Res.drawable.calandar_rightarrow_ico),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onMonthChanged(currentMonth.plus(1, DateTimeUnit.MONTH))
                }
        )
    }
}

@Composable
private fun DaysHorizontalList(
    month: LocalDate,
    daysInMonth: Int,
    selectedDate: LocalDate,
    initialSelectedDate: LocalDate,
    listState: LazyListState,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = remember(month, daysInMonth) {
        (1..daysInMonth).map { day -> month.withDayOfMonth(day) }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 6.dp)
    ) {
        items(days) { date ->
            val isEnabled = date >= initialSelectedDate

            DayWithDateBox(
                date = date,
                isSelected = date == selectedDate,
                isEnabled = isEnabled,
                onClick = {
                    if (isEnabled) {
                        onDateSelected(date)
                    }
                }
            )
        }
    }
}

@Composable
private fun DayWithDateBox(
    date: LocalDate,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> JTColors.enableButton
        else -> JTColors.WhiteColor
    }
    val borderColor = if (isSelected) JTColors.enableButton else JTColors.border
    val textColor = when {
        isSelected -> Color.White
        !isEnabled -> JTColors.lightGrayColor // You can customize this
        else -> JTColors.black
    }

    Column(
        modifier = Modifier
            .width(48.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
            .clickable(enabled = isEnabled, onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        JtBtoCommonText(
            text = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        JtBtoCommonText(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 16.sp
        )
    }
}

private fun getDaysInMonth(date: LocalDate): Int {
    return when (date.month) {
        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        Month.FEBRUARY -> if ((date.year % 4 == 0 && date.year % 100 != 0) || (date.year % 400 == 0)) 29 else 28
        else -> TODO()
    }
}

@Composable
fun SelectTime(
    uiState: SelectionDateOfServiceViewModel.SelectUIState,
    selectionDateOfServiceViewModel: SelectionDateOfServiceViewModel
) {
    val tabs =
        listOf(
        stringResource(Res.string.morning),
        stringResource(Res.string.afternoon),
        stringResource(Res.string.evening)
    )
    val timeSlots = when (uiState.selectedTab) {
        0 -> listOf(
            "12:00 AM","1:00 AM","2:00 AM","3:00 AM","4:00 AM","5:00 AM",
            "6:00 AM","7:00 AM","8:00 AM","9:00 AM","10:00 AM","11:00 AM"
        )
        1 -> listOf(
            "12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM"
        )
        2 -> listOf(
            "6:00 PM","7:00 PM","8:00 PM","9:00 PM","10:00 PM","11:00 PM"
        )
        else -> emptyList()
    }

    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate = currentDateTime.date
    val currentTime = currentDateTime.time

    Box(
        modifier = Modifier
            .background(JTColors.dateColor)
            .border(width = 1.dp, color = JTColors.dateColor)
    ) {
        Column(
            modifier = Modifier
                .background(JTColors.dateColor)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DayCustomTabRow(
                tabs = tabs,
                selectedTabIndex = uiState.selectedTab,
                onTabSelected = { index -> selectionDateOfServiceViewModel.onTabSelected(index) },
                activeBackgroundColor = JTColors.WhiteColor,
                activeColor = JTColors.enableButton,
                inactiveBackgroundColor = JTColors.dayTabRow,
                inactiveColor = JTColors.enableButton,
                containerColor = JTColors.dayTabRow,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(timeSlots) { time ->
                    val slotTime = parseTime(time)
                    val isPast = currentDate == uiState.selectedDate && slotTime < currentTime
                    val errorMessage = stringResource(Res.string.booking_past_time_error)
                    SelectTimeSlot(
                        time = time,
                        isSelected = uiState.selectedTime == time,
                        isEnabled = !isPast,
                        onTimeSelected = {
                            selectionDateOfServiceViewModel.onTimeSelected(time)
                        },
                        onDisabledClick = {
                            selectionDateOfServiceViewModel.showSnackBarMessage(errorMessage)
                        }
                    )
                }
            }
        }
    }
}

fun parseTime(time: String): LocalTime {
    val parts = time.trim().split(" ")
    if (parts.size != 2) throw IllegalArgumentException("Invalid time format")

    val hm = parts[0].split(":")
    if (hm.size != 2) throw IllegalArgumentException("Invalid hour-minute format")

    val hour = hm[0].toInt()
    val minute = hm[1].toInt()
    val amPm = parts[1].uppercase()

    return when (amPm) {
        "AM" -> if (hour == 12) LocalTime(0, minute) else LocalTime(hour, minute)
        "PM" -> if (hour == 12) LocalTime(12, minute) else LocalTime(hour + 12, minute)
        else -> throw IllegalArgumentException("Invalid AM/PM format")
    }
}

fun convertTo12HourFormat(time24: String): String {
    val parts = time24.split(":")
    if (parts.size < 2) return time24
    var hour = parts[0].toInt()
    val minute = parts[1]
    val amPm = if (hour >= 12) "PM" else "AM"
    if (hour > 12) hour -= 12
    if (hour == 0) hour = 12
    return "$hour:$minute $amPm"
}

@Composable
fun SelectTimeSlot(
    time: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onTimeSelected: () -> Unit,
    onDisabledClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isEnabled && isSelected) JTColors.enableButton else JTColors.WhiteColor,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = if (isEnabled && isSelected) JTColors.enableButton else JTColors.border,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                if (isEnabled) {
                    onTimeSelected()
                } else {
                    onDisabledClick()
                }
            }
            .padding( 12.dp),
        contentAlignment = Alignment.Center
    ) {
        JtBtoCommonText(
            text = time,
            color = if (isEnabled && isSelected) JTColors.WhiteColor else JTColors.message,
            fontSize = 13.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onInspectionClick:()->Unit,
    imageUrl: String,
    dayAndDate:String,
    timeslot:String,
    minRate: Int,
    maxRate: Int,
    serviceName: String ,
    serviceDesc:String,
    address:String,
    fromInspection: Boolean,
    inspectionRate: String
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showBottomSheet) {
        ModalBottomSheet(
            dragHandle = null,
            sheetState = bottomSheetState,
            containerColor = Color.White,
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            tonalElevation = 8.dp,

        ) {
            BookingSummaryContent(
                onDismiss = onDismiss,
                onConfirm = onConfirm,
                imageUrl = imageUrl,
                dayAndDate = dayAndDate,
                timeslot = timeslot,
                serviceName = serviceName,
                serviceDesc = serviceDesc,
                address = address,
                minRate = minRate ,
                maxRate = maxRate,
                onInspectionClick = onInspectionClick,
                fromInspection = fromInspection,
                inspectionRate = inspectionRate
            )
        }
    }
}


@Composable
fun BookingSummaryContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onInspectionClick:() ->Unit,
    imageUrl: String,
    dayAndDate:String,
    timeslot:String,
    minRate: Int,
    maxRate: Int,
    serviceName: String ,
    serviceDesc:String,
    address:String,
    fromInspection: Boolean,
    inspectionRate: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 25.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.booking_summary),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                lineHeight = 12.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDismiss() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = JTColors.border.copy(alpha = 40f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    serviceName,
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    lineHeight = 12.sp
                )
                Text(
                    serviceDesc,
                    color =JTColors.grayTextColor,
                    fontSize = 11.sp,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    lineHeight = 11.sp,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = JTColors.border.copy(alpha = 40f)
        )
        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.calendar),
                    contentDescription = "Calendar Icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        stringResource(Res.string.date),
                        color = Color(0xFF5B5B5B),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = formatDate(dayAndDate)?:"",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }

            // Vertical Divider
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
                    .background(Color.LightGray)
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.time_ico),
                    contentDescription = "Time Icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(stringResource(Res.string.time_slot),
                        color = Color(0xFF5B5B5B),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = timeslot,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = JTColors.border.copy(alpha = 40f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (fromInspection){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.dollar_ico),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        stringResource(Res.string.estimated_Price),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF777777)
                    )
                    Text(
                        text = "$$minRate - $$maxRate",
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = JTColors.border.copy(alpha = 40f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.address_ico1),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    stringResource(Res.string.address),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = Color(0xFF777777)
                )
                Text(
                    address,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    fontSize = 12.sp,
                    lineHeight = 16.sp

                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = JTColors.border.copy(alpha = 40f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (fromInspection){
            JTBtoPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(45.dp),
                name = stringResource(Res.string.confirm_Book),
                onClick = {
                    onConfirm()
                },
                buttonColors = JTColors.enableButton ,
                selectedColor = JTColors.enableButton,
                textColor = Color.White,
            )
        } else{
//            JTBtoPrimaryButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//                    .height(45.dp),
//                name = stringResource(Res.string.confirm_Book),
//                onClick = {
//                    onConfirm()
//                },
//                buttonColors = JTColors.enableButton ,
//                selectedColor = JTColors.enableButton,
//                textColor = Color.White,
//            )
            JTBtoPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(45.dp),
                name = "Get Inspection @ $$inspectionRate",
                onClick = {
                    onInspectionClick()
                },
                buttonColors = JTColors.enableButton ,
                selectedColor = JTColors.enableButton,
                textColor = Color.White,
            )
        }
    }
}

fun convertTo24HourWithSecondsKMP(time12h: String?): String? {
    if (time12h.isNullOrBlank()) return null
    val parts = time12h.trim().split(" ")
    if (parts.size != 2) return null
    val timePart = parts[0]
    val amPm = parts[1].uppercase()
    val timeComponents = timePart.split(":")
    if (timeComponents.size != 2) return null
    var hour = timeComponents[0].toIntOrNull() ?: return null
    val minute = timeComponents[1].toIntOrNull() ?: return null
    if (amPm == "AM" && hour == 12) hour = 0
    if (amPm == "PM" && hour != 12) hour += 12

    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00"
}


fun isCurrentTimeRestricted(): Boolean {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = now.hour

    return hour !in 8..<20
}




