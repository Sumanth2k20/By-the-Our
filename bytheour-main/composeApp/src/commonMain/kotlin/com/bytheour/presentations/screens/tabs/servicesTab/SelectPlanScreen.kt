package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.minus_ico_serviceadd
import bytheour.composeapp.generated.resources.monthly_Storage_fee
import bytheour.composeapp.generated.resources.plus_ico_serviceadd
import bytheour.composeapp.generated.resources.proceed_to_pay
import bytheour.composeapp.generated.resources.select_the_number_of_pictures
import bytheour.composeapp.generated.resources.select_your_plan
import com.bytheour.data.model.response.GalleryPlan
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.bookings.PayCard
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlanScreen(
    onBackClick: () -> Unit,
    navigateToimageUploadScreen: (String) -> Unit,
    serviceid: String,
    viewModel: SelectPlanViewModel = getKoinViewModel()
) {
    val uiState by viewModel.uiStates.collectAsState()

    if (uiState.navigateBack){
        navigateToimageUploadScreen(serviceid)

    }


    var selectedPlan by remember { mutableStateOf<GalleryPlan?>(null) }
    var customPictures by remember { mutableStateOf(0) }

    val onProceedClick: () -> Unit = {
        viewModel.updatedSelectedPlan(selectedPlan,customPictures)
        viewModel.onPayNow()
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.select_your_plan),
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onScreenExit()
                        onBackClick() }) {
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
        bottomBar = {
            Button(
                onClick = { onProceedClick() },
                enabled = selectedPlan != null || customPictures > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JTColors.enableButton
                )
            ) {
                Text(
                    stringResource(Res.string.proceed_to_pay),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                stringResource(Res.string.monthly_Storage_fee),
                fontFamily = JtBtoFonts.getInterFont(),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            uiState.plans.forEach { plan ->
                if (plan != null) {
                    // Special handling for "$2 per Picture"
                    if (plan.customId == "C6PsW3KrjBmM") {
                        CustomPictureOption(
                            plan = plan,
                            customPictures = customPictures,
                            onDecrease = {
                                if (customPictures > 1) customPictures--
                                selectedPlan = null
                            },
                            onIncrease = {
                                customPictures++
                                selectedPlan = null
                            }
                        )
                    } else {
                        PlanOption(
                            text = "${plan.name} - $${plan.amount}",
                            value = plan.customId?:"",
                            selectedPlan = selectedPlan?.customId,
                        ) {
                            selectedPlan = plan
                            customPictures = 0
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Note Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF2E5), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))) {
                            append("Note\n")
                        }
                        append("Earn ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))) {
                            append("\$1000")
                        }
                        append("This Month Across All Services And Receive Unlimited Free Pictures Next Month\n")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Card Row
            PayCard(selectedCard = uiState.cardNumber, onSelect = {
                viewModel.onSelectCard()
            })

        }
    }
}

@Composable
fun PlanOption(
    text: String,
    value: String,
    selectedPlan: String?,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSelect(value) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 13.sp,
            )
            if (selectedPlan == value) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
            }
        }
    }
}

@Composable
fun CustomPictureOption(
    plan: GalleryPlan,
    customPictures: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "${plan.name}",
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    fontSize = 13.sp,
                )
                Text(
                    stringResource(Res.string.select_the_number_of_pictures),
                    fontFamily = JtBtoFonts.getInterFont(),
                    fontSize = 9.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.minus_ico_serviceadd),
                    contentDescription = "Decrease",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onDecrease() }
                )
                Text(
                    customPictures.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(Res.drawable.plus_ico_serviceadd),
                    contentDescription = "Increase",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onIncrease() }
                )
            }
        }
    }
}
