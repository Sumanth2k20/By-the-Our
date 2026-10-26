package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.calendar_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.JTProviderBookingTabScreenViewmodel
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.WheelDateTimePickerBottomSheet
import com.bytheour.utils.formatTo12Hour
import com.bytheour.utils.formatTo24Hour
import com.bytheour.utils.getCurrentDateTimeIn24hrs
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.parse12HourToLocalDateTime
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionReportScreen(
    providerName: String,
    onBackClick: () -> Unit,
    viewModel: InspectionReportScreenViewModel = getKoinViewModel(),
    proBookingViewModel: JTProviderBookingTabScreenViewmodel = getKoinViewModel(),
    serviceName :String,
    serviceImage:String,
    bookingId:String,
    navigateToMainScreen:(Boolean)-> Unit
) {
    LaunchedEffect(Unit) { proBookingViewModel.fetchBookingDetails(bookingId) }
    val uiState by viewModel.uiStates.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Inspection Report",
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
            )
        },
        containerColor = JTColors.WhiteColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Provider Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = serviceImage,
                        contentDescription = "",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(serviceName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp)

                    Text(text = "Technician: $providerName",
                        fontSize = 14.sp,
                        color = Color.Gray)
                }
            }

            Text(
                text = "Submit Inspection Details",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Problem Input
            OutlinedTextField(
                value = uiState.problem ?: "",
                onValueChange = { viewModel.onProblemChanged(it) },
                label = { Text("What is the problem?") },
                placeholder = { Text("Service Problem...") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.problemError != null,
                supportingText = {
                    uiState.problemError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

// Description
            OutlinedTextField(
                value = uiState.problemDescription ?: "",
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text("Problem description") },
                placeholder = { Text("Details about the issue...") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.descriptionError != null,
                supportingText = {
                    uiState.descriptionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

// Solution
            OutlinedTextField(
                value = uiState.solution ?: "",
                onValueChange = { viewModel.onSolutionChanged(it) },
                label = { Text("Problem solution") },
                placeholder = { Text("Recommended steps to fix...") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.solutionError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    uiState.solutionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

// Estimated Cost
            OutlinedTextField(
                value = uiState.estimatedCost ?: "",
                onValueChange = { it ->
                    val filtered = it.filter { it.isDigit() }
                    viewModel.onEstimatedCostChanged(filtered)
                },
                label = { Text("Estimated cost") },
                leadingIcon = {
                    Text("$", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.estimatedCostError != null,
                supportingText = {
                    uiState.estimatedCostError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                }
            )

            val currentDateTime = remember { getCurrentDateTimeIn24hrs() }

            OutlinedTextField(
                value = selectedDateTime.ifEmpty { currentDateTime },
                onValueChange = {},
                label = { Text("Date and Time that you are available") },
                placeholder = { Text("Time...") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                   Image(
                       painter = painterResource(Res.drawable.calendar_ico),
                       contentDescription = "null",
                       modifier = Modifier.size(25.dp).clickable { showDatePicker = true }
                   )
                },
                readOnly = true
            )


            Spacer(Modifier.weight(1f))


            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                name = "Submit Report",
                onClick = {
                    viewModel.makeSubmitInspectionReport(
                        bookingId = bookingId,
                        problem = uiState.problem?:"",
                        problemDescription = uiState.problemDescription?:"",
                        solution = uiState.solution?:"",
                        estimatedCost = uiState.estimatedCost?:"",
                        time = uiState.time ?: getCurrentDateTimeIn24hrs(),
                        onSuccess = {
                            proBookingViewModel.endJob(
                                bookingId,
                                onSuccess = {
                                    navigateToMainScreen(true)
                                }
                            )
                        }
                    )

                },
                buttonColors = JTColors.enableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White
            )
        }
    }
    if (uiState.message?.isNotEmpty() == true){
        CommonSnackbar(
            message = uiState.message?:"",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 1500,
            onDismiss = {
                navigateToMainScreen(true)
            }
        )
    }
    if (showDatePicker) {
        WheelDateTimePickerBottomSheet(
            showDatePicker = showDatePicker,
            onDoneClick = { dateTimeString ->
                val parsedDateTime = parse12HourToLocalDateTime(dateTimeString)
                selectedDateTime = formatTo12Hour(parsedDateTime)
                val formattedForApi = formatTo24Hour(parsedDateTime)
                viewModel.onTimeChanged(formattedForApi)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

}

