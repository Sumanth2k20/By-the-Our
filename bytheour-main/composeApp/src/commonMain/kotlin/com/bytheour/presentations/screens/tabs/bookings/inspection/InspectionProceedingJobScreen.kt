package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.reports.CommonDialogBox
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.formatServerDateTimeTo12Hour
import com.bytheour.utils.getKoinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionProceedingJob(
    onBack: () -> Unit,
    navigateToInspectionDecline:(String)-> Unit,
    title:String,
    image:String,
    name :String,
    bookingId:String,
    bookingViewModel: JTBookingTabViewmodel = getKoinViewModel(),
    viewModel: InspectionReportScreenViewModel = getKoinViewModel(),
    navigateToBillingSummaryScreen:(String,Boolean)->Unit,
    ) {

    LaunchedEffect(Unit){ bookingViewModel.fetchBookingDetails(bookingId)}
    var showDialog by remember { mutableStateOf(false) }
    val bookingData by bookingViewModel.bookingData.collectAsState()

    val onYes: () -> Unit = {
        bookingViewModel.completeRequestBooking(
            booking = bookingId,
            onSuccess = {
                viewModel.acceptInspectionReport(
                    bookingId = bookingId,
                    onSuccess = {id ->
                        navigateToBillingSummaryScreen(id,true)
                    }
                )
            }
        )
    }
    val onNo: () -> Unit = { navigateToInspectionDecline(bookingId) }

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
                    IconButton(onClick = { onBack() }) {
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header (Provider info)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
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
                        model = image,
                        contentDescription = "",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(title = "What is the problem?", value = bookingData.problem?:"")
            Spacer(modifier = Modifier.height(12.dp))

            InfoCard(
                title = "Problem description",
                value = bookingData.problemDescription?:"",
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoCard(
                title = "Problem solution",
                value = bookingData.problemSolution?:""
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoCard(title = "Estimated cost", value = bookingData.estimationCost?:"")

            Spacer(modifier = Modifier.height(12.dp))

            val time = formatServerDateTimeTo12Hour(bookingData.availableTime?:"")
            InfoCard(title = "Date and Time that you are available", value = time)

            Spacer(modifier = Modifier.weight(1f))

            // Proceed with job
            Text(
                "Proceed with job?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onNo() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xF0EFEAFF),
                            contentColor = Color(0xFF2D1A6E)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        JtBtoCommonText(
                            text = "NO",
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            fontWeight = W700,
                            fontSize = 12.sp,
                            lineHeight = 14.sp

                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { onYes() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D1A6E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        JtBtoCommonText(
                            text = "YES",
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            fontWeight = W700,
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))
        }
        if (showDialog){
            CommonDialogBox(
                title = "",
                message = "Do you want to try with another provider?",
                onCancelClick = { showDialog = false },
                onContinueClick = {
                    showDialog = true
                },
                cancelText = "No",
                deleteText = "Yes",
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value)
        }
    }
}