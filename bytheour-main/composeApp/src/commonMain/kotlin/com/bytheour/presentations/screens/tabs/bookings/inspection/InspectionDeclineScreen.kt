package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.reports.CommonDialogBox
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionDeclineScreen(
    onBackClick:()-> Unit,
    viewModel: InspectionDeclineViewModel = getKoinViewModel(),
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    navigateToSelectionDateScreen:(String,String,String,String,Boolean,String?)->Unit,
    bookingId:String,
    navigateToMainScreen:()->Unit
) {
    LaunchedEffect(Unit){
        bookingViewModel.fetchBookingDetails(bookingId)
    }
    val uiState by viewModel.uiStates.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val bookingData by bookingViewModel.bookingData.collectAsState()
    var isYesClicked by remember { mutableStateOf(false) }


    LaunchedEffect(Unit){
        if (uiState.isNavigate == false){
            navigateToSelectionDateScreen(
                bookingData.serviceId ?: "",
                bookingData.title,
                bookingData.problemDescription?:"",
                bookingData.image,
                true,
                bookingId
            )
        }
    }

    val reasonOptions = listOf(
        "Price",
        "The time the provider is available to do it",
        "Other"
    )

    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
            .background(JTColors.WhiteColor),
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Inspection Decline Report",
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
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.background(JTColors.WhiteColor)
            ) {
                OutlinedTextField(
                    value = uiState.reason ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("what didn’t you like about the quote") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    isError = uiState.reasonError != null,
                    supportingText = {
                        uiState.reasonError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp)
                        }
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = JTColors.grayColor,
                        unfocusedIndicatorColor =JTColors.grayColor,
                        focusedContainerColor = JTColors.WhiteColor,
                        unfocusedContainerColor = JTColors.WhiteColor
                    )
                )

                // Dropdown items
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = JTColors.WhiteColor
                ) {
                    reasonOptions.forEach { reason ->
                        DropdownMenuItem(
                            text = { Text(reason) },
                            onClick = {
                                viewModel.onReasonChanged(reason)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            if (uiState.reason == "Other") {
                OutlinedTextField(
                    value = uiState.other ?: "",
                    onValueChange = { viewModel.onOtherChanged(it) },
                    label = { Text("Provide Specific Reason") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.otherError != null,
                    supportingText = {
                        uiState.otherError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }


            Spacer(modifier = Modifier.weight(1f))

            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                name = "Submit Report",
                onClick = {
                   showDialog = true
                },
                buttonColors = JTColors.enableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White
            )
        }
    }
    if (uiState.message?.isNotEmpty() == true) {
        CommonSnackbar(
            message = uiState.message ?: "",
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 2000L,
            onDismiss = {
                if (isYesClicked) {
                    navigateToSelectionDateScreen(
                        bookingData.serviceId ?: "",
                        bookingData.title,
                        bookingData.problemDescription ?: "",
                        bookingData.image,
                        true,
                        bookingId
                    )
                } else {
                    navigateToMainScreen()
                }
                showDialog = false
            }
        )
    }

    if (showDialog){
        CommonDialogBox(
            title = bookingData.title,
            message = "Would you like to try a different technician?",
            onCancelClick = {
                isYesClicked = false
                bookingViewModel.completeRequestBooking(
                    booking = bookingId,
                    onSuccess = {
                        viewModel.declineInspectionReport(
                            bookingId,
                            reason = uiState.reason?:"",
                        )
                    }
                )
                showDialog = false
            },
            onContinueClick = {
                isYesClicked = true
                bookingViewModel.completeRequestBooking(
                    booking = bookingId,
                    onSuccess = {
                        viewModel.declineInspectionReport(
                            bookingId,
                            reason = uiState.reason?:"",
                        )
                    }
                )
                showDialog = false
            },
            cancelText = "No",
            deleteText = "Yes",
        )
    }
}