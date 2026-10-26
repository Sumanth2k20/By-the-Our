package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.calendar
import bytheour.composeapp.generated.resources.card_select_ico
import bytheour.composeapp.generated.resources.dollar_ico
import bytheour.composeapp.generated.resources.send_estimate_description
import bytheour.composeapp.generated.resources.send_estimate_enter_fixed_price
import bytheour.composeapp.generated.resources.send_estimate_enter_working_hours
import bytheour.composeapp.generated.resources.send_estimate_estimated_working_hours
import bytheour.composeapp.generated.resources.send_estimate_fixed_price
import bytheour.composeapp.generated.resources.send_estimate_note_message
import bytheour.composeapp.generated.resources.send_estimate_note_prefix
import bytheour.composeapp.generated.resources.send_estimate_price_per_hour
import bytheour.composeapp.generated.resources.send_estimate_submit
import bytheour.composeapp.generated.resources.send_estimate_title
import bytheour.composeapp.generated.resources.total_Price
import bytheour.composeapp.generated.resources.workinghours_ico
import com.bytheour.utils.JTColors
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.FIXED
import com.bytheour.utils.HOUR
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendEstimateScreen(
    viewModel: EstimateViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    bookingId: String,
    time:String,
    cost:String
) {
    val uiState by viewModel.uiStates.collectAsState()

    println("Time =============================== $time")
    println("cost =============================== $cost")
    val displayedCost = remember(cost) { (cost.toDoubleOrNull() ?: 0.0).let { kotlin.math.floor(it * 10) / 10 }.toString() }


    LaunchedEffect(cost) {
        viewModel.setBaseCost(displayedCost)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.send_estimate_title),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(JTColors.enableButton),
                navigationIcon = {
                  Image(
                      painter = painterResource(Res.drawable.arrowleft),
                      contentDescription = "Back",
                      modifier = Modifier
                          .padding(16.dp)
                          .clickable { navigateBack() }
                  )
                }
            )
        },
        bottomBar = {
            val isSubmitEnabled = when (uiState.priceType) {
                HOUR -> uiState.hourlyHours.isNotBlank()
                FIXED -> uiState.fixedHours.isNotBlank() && uiState.fixedPrice.isNotBlank()
                else -> false
            }
            Button(
                onClick = {
                    val timeRange = time
                    val parts = timeRange.split(" - ")
                    val startTimeString = parts[0].trim()

                    val startparsTime24hr = convertTo24HourWithSecondsKMP(startTimeString)

                    val estimatedHours = uiState.hourlyHours.toIntOrNull() ?: 0
                    val fixedHours = uiState.fixedHours.toIntOrNull() ?: 0


                    val startLocalTime = LocalTime.parse(startparsTime24hr.toString())

                    val newHour = (startLocalTime.hour + estimatedHours) % 24
                    val endLocalTimeManual = LocalTime(
                        hour = newHour,
                        minute = startLocalTime.minute,
                        second = startLocalTime.second,
                    )

                    val startTimes = listOf(startLocalTime.toString())

                    val endTimes = listOf(endLocalTimeManual.toString())

                    val newHourFixed = (startLocalTime.hour + fixedHours) % 24
                    val endLocalTimeManualFixed = LocalTime(
                        hour = newHourFixed,
                        minute = startLocalTime.minute,
                        second = startLocalTime.second,
                    )

                    val endTimesFixed = listOf(endLocalTimeManualFixed.toString())


                    when (uiState.priceType) {
                        HOUR -> {
                            viewModel.sendEstimation(
                                bookingId = bookingId,
                                estimatedHours = uiState.hourlyHours,
                                priceType = HOUR,
                                startTimes = startTimes,
                                endTimes = endTimes,
                                onSuccess = { navigateBack() }

                            )
                        }
                        FIXED -> {
                            viewModel.sendFixedEstimation(
                                bookingId = bookingId,
                                estimatedHours = uiState.fixedHours,
                                priceType = FIXED,
                                startTimes = startTimes,
                                endTimes = endTimesFixed,
                                fixedPrice = uiState.fixedPrice,
                                onSuccess = { (navigateBack()) }
                            )
                        }
                    }
                },
                enabled = isSubmitEnabled,
                colors = ButtonDefaults.buttonColors(JTColors.enableButton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(45.dp)
            ) {
                Text(
                    stringResource(Res.string.send_estimate_submit),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    ) { padding ->
        Column (
            modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        )
        {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F4FB))
            ) {
                Column {
                    // Description text
                    Text(
                        text = stringResource(Res.string.send_estimate_description),
                        color = Color.Gray,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        fontSize = 11.sp,
                        lineHeight = 13.sp,
                        modifier = Modifier.padding(
                            top = 16.dp,
                            end = 16.dp,
                            start = 16.dp,
                            bottom = 16.dp
                        )
                    )
                    Column {
                        HorizontalDivider(
                            color = Color(0xFFEAEAEA),
                            modifier = Modifier.fillMaxWidth()
                        )
                        PriceTypeCard(
                            title = stringResource(Res.string.send_estimate_estimated_working_hours),
                            selected = uiState.priceType == HOUR,
                            onClick = { viewModel.selectPriceType(HOUR) }
                        ) {
                            Column(
                                Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 14.dp,
                                    bottom = 25.dp
                                )
                            ) {
                                WorkingHoursPriceCard(
                                    hours = uiState.hourlyHours,
                                    onHoursChange = { viewModel.updateHourlyHours(it) },
                                    pricePerHour = displayedCost
                                )
                            }

                        }

                        HorizontalDivider(
                            color = Color(0xFFEAEAEA),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Fixed Price Option
                        PriceTypeCard(
                            title = stringResource(Res.string.send_estimate_fixed_price),
                            selected = uiState.priceType == FIXED,
                            onClick = { viewModel.selectPriceType(FIXED) }
                        ) {

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.background(Color(0xFFE8E7EB))
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color(0xFFFF9800),
                                                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                                                fontSize = 11.sp,

                                                )
                                        ) {
                                            append(stringResource(Res.string.send_estimate_note_prefix))
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color.Black,
                                                fontFamily = JtBtoFonts.getInterRegular(),
                                                fontSize = 11.sp,
                                            )
                                        ) {
                                            append(stringResource(Res.string.send_estimate_note_message))
                                        }

                                    },
                                    style = TextStyle(
                                        lineHeight = 11.sp // <-- Set your desired line height here
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Column(modifier = Modifier.padding(16.dp)) {
                                WorkingHoursInputField(
                                    hours = uiState.fixedHours,
                                    onHoursChange = { viewModel.updateFixedHours(it) },
                                    painter = painterResource(Res.drawable.workinghours_ico),
                                    placeholder = stringResource(Res.string.send_estimate_enter_working_hours)
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                WorkingHoursInputField(
                                    hours = uiState.fixedPrice,
                                    onHoursChange = { viewModel.updateFixedPrice(it) },
                                    painter = painterResource(Res.drawable.dollar_ico),
                                    placeholder = stringResource(Res.string.send_estimate_enter_fixed_price),
                                )
                            }

                        }
                        HorizontalDivider(
                            color = Color(0xFFEAEAEA),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (uiState.message.isNotBlank()) {
        CommonSnackbar(
            message = uiState.message,
            type = if (uiState.isSuccess) SnackbarType.SUCCESS else SnackbarType.ERROR,
            onDismiss = { viewModel.clearMessage() }
        )
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

    when (amPm) {
        "AM" -> if (hour == 12) hour = 0
        "PM" -> if (hour != 12) hour += 12
        else -> return null
    }

    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00"
}


@Composable
fun PriceTypeCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
        Column(modifier = Modifier.clickable { onClick()}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.White),
            ) {
                Text(
                    title,
                    modifier = Modifier.weight(1f).padding(16.dp),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 13.sp,
                    )
                if (selected) {
                    Image(
                        painter = painterResource(Res.drawable.card_select_ico),
                        contentDescription = null,
                    )
                }else{
                    Box(modifier = Modifier.clip(CircleShape)
                        .background(color = JTColors.notification_border)
                        .size(18.dp))
                }
                Spacer(Modifier.width(16.dp))
            }
                if (selected) {
                    content()
                }

        }

}

@Composable
fun WorkingHoursPriceCard(
    hours: String,
    onHoursChange: (String) -> Unit,
    pricePerHour: String,
) {
    val hoursInt = hours.toIntOrNull() ?: 0
    val rateDouble = pricePerHour.toDoubleOrNull() ?: 0.0
    val totalCost = rateDouble * hoursInt
    val isChanged = hours.isNotEmpty()

    val purpleColor = Color(0xFF3B2270)
    val cornerRadius = 16.dp

    Surface(
        shape = RoundedCornerShape(cornerRadius),
        shadowElevation = 2.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.calendar), // Use appropriate icon
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Borderless input field
                BasicTextField(
                    value = hours,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..24) { // Restrict input between 0 and 24
                                onHoursChange(newValue)
                            }
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    decorationBox = { innerTextField ->
                        if (hours.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.send_estimate_enter_working_hours),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Bottom: Price display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purpleColor)
                    .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isChanged) stringResource(Res.string.total_Price) else stringResource(Res.string.send_estimate_price_per_hour),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (isChanged) "$$totalCost" else "$$pricePerHour",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}


@Composable
fun WorkingHoursInputField(
    hours: String,
    onHoursChange: (String) -> Unit,
    painter: Painter,
    placeholder:String,
) {

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White)
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter, // Replace with your actual resource
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = hours,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        val intValue = newValue.toIntOrNull() ?: 0
                        if (intValue in 0..24) { // Restrict input between 0 and 24
                            onHoursChange(newValue)
                        }
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                decorationBox = { innerTextField ->
                    if (hours.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

