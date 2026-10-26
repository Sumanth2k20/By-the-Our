package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.android
import bytheour.composeapp.generated.resources.android_ico
import bytheour.composeapp.generated.resources.bookings_in_last_month
import bytheour.composeapp.generated.resources.downloads
import bytheour.composeapp.generated.resources.downloads_caps
import bytheour.composeapp.generated.resources.downloads_ico
import bytheour.composeapp.generated.resources.downloads_last_month
import bytheour.composeapp.generated.resources.group_14
import bytheour.composeapp.generated.resources.ios
import bytheour.composeapp.generated.resources.ios_ico
import bytheour.composeapp.generated.resources.manage_services
import bytheour.composeapp.generated.resources.reports
import bytheour.composeapp.generated.resources.service_added
import bytheour.composeapp.generated.resources.servicerequest_ico
import bytheour.composeapp.generated.resources.services
import bytheour.composeapp.generated.resources.services_Request
import bytheour.composeapp.generated.resources.services_Request_in_last_month
import bytheour.composeapp.generated.resources.services_caps
import bytheour.composeapp.generated.resources.services_ico
import bytheour.composeapp.generated.resources.undraw_real_time_analytics_50za_1
import bytheour.composeapp.generated.resources.users_cap
import bytheour.composeapp.generated.resources.users_ico
import bytheour.composeapp.generated.resources.users_in_last_month
import bytheour.composeapp.generated.resources.workers_caps
import bytheour.composeapp.generated.resources.workers_ico
import bytheour.composeapp.generated.resources.workers_in_last_month
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTAdminReportsScreen(
    viewModel: JtAdminReportsScreenViewModel = getKoinViewModel(),
    navigateToUserScreen:()-> Unit,
    navigateToWorkersScreen:() -> Unit,
    navigateToServiceRequestScreen:()->Unit,
    navigateToManageServiceScreen:() ->Unit,
) {
    val uiState  by  viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDownloadBottomSheet by remember { mutableStateOf(false) }
    var showServiceBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.fetchDownLoadsCount()
    }

    LaunchedEffect(Unit){
        viewModel.getAdminServiceRequest(PAGINATION_LIMIT.toString(),"0")
        viewModel.getAdminServiceManage(PAGINATION_LIMIT.toString(),"0")
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Your existing Column with gradient background
        Column(
            Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF301E69),
                            Color(0xFF0E042D)
                        )
                    )
                )
        ) {
            if(platform.equals("IOS", ignoreCase = true)){
                Spacer(modifier = Modifier.height(50.dp))
            }else{
                Spacer(modifier = Modifier.height(40.dp))
            }

            JtBtoCommonText(
                text = stringResource(Res.string.reports),
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 19.sp,
                modifier = Modifier.padding(start = 18.dp),
                color = JTColors.WhiteColor
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val reportItems = listOf(
                    ReportItem(
                        stringResource(Res.string.downloads_caps),
                        uiState.downloadsCount ?: 0,
                        stringResource(Res.string.downloads_last_month),
                        painterResource(Res.drawable.downloads_ico),
                        onClick = { showDownloadBottomSheet = true }
                    ),
                    ReportItem(
                        stringResource(Res.string.users_cap),
                        uiState.usersCount ?: 0,
                        stringResource(Res.string.users_in_last_month),
                        painterResource(Res.drawable.users_ico),
                        onClick = { navigateToUserScreen() }
                    ),
                    ReportItem(
                        stringResource(Res.string.services_caps),
                        uiState.servicesCount ?: 0,
                        stringResource(Res.string.bookings_in_last_month),
                        painterResource(Res.drawable.workers_ico),
                        onClick = { showServiceBottomSheet = true }
                    ),
                    ReportItem(
                        stringResource(Res.string.workers_caps),
                        uiState.workersCount ?: 0,
                        stringResource(Res.string.workers_in_last_month),
                        painterResource(Res.drawable.users_ico),
                        onClick = { navigateToWorkersScreen() }
                    )
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    itemsIndexed(reportItems) { index, item ->
                        val shape = when (index) {
                            0 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 20.dp)
                            1 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                            2 -> RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
                            3 -> RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
                            else -> RoundedCornerShape(20.dp)
                        }
                        ReportCard(item, shape)
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(Res.drawable.undraw_real_time_analytics_50za_1),
                contentDescription = "",
                modifier = Modifier
                    .size(320.dp)
                    .alpha(0.3f),
            )
        }
    }
    if (showDownloadBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showDownloadBottomSheet = false },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = JTColors.WhiteColor
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            DownloadBottomSheetContent(
                iosCount = uiState.iosDownload?:0,
                androidCount = uiState.androidDownload?:0,
                onCloseClick = { showDownloadBottomSheet = false }
            )
        }
    }
    if (showServiceBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showServiceBottomSheet = false },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = JTColors.WhiteColor
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ServicesBottomSheetContent(
                onManageServicesClick = {
                    navigateToManageServiceScreen()
                },
                onServiceRequestClick = {
                    navigateToServiceRequestScreen()
                },
                onCloseClick = { showServiceBottomSheet = false },
                serviceCount = uiState.serviceRequestCount?:0 ,
                requestCount = uiState.serviceManageCount?:0,
            )
        }
    }
}

data class ReportItem(
    val title: String,
    val countText: Int,
    val description: String,
    val iconRes: Painter,
    val onClick: ()-> Unit ,
)

@Composable
fun ReportCard(
    item: ReportItem,
    shape: Shape
) {
    Card(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.group_14),
                contentDescription = "Background Logo",
                modifier = Modifier
                    .align(Alignment.TopEnd).padding(end = 10.dp)
                    .size(70.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color(0xFFE4F5FF), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = item.iconRes,
                        contentDescription = item.title,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                JtBtoCommonText(
                    text = item.title,
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = JTColors.BackGroundBlueColor,
                                fontSize = 11.sp,
                                fontFamily = JtBtoFonts.getOpenSansBold()
                            )
                        ) {
                            append("${item.countText} ")
                        }
                        withStyle(
                            SpanStyle(
                                color = JTColors.ReportsDescriptionColor,
                                fontSize = 11.sp,
                                fontFamily = JtBtoFonts.getInterFont()
                            )
                        ) {
                            append(item.description)
                        }
                    },
                    lineHeight = 11.sp
                )
            }
        }
    }
}


@Composable
fun DownloadBottomSheetContent(
    iosCount: Int,
    androidCount: Int,
    onCloseClick: () -> Unit,
){
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.downloads),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.size(20.dp).clickable { onCloseClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(
                painter = painterResource(Res.drawable.ios_ico),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = stringResource(Res.string.ios),
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    color = JTColors.BackGroundBlueColor,
                    fontFamily = JtBtoFonts.getInterFont()
                )

                Text(
                    text = stringResource(Res.string.downloads),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = JTColors.ReportsDescriptionColor,
                    fontFamily = JtBtoFonts.getInterFont()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(
                        color = JTColors.CountBackGroundColor.copy(0.3f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$iosCount",
                    color = JTColors.BackGroundBlueColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))


        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f))
        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(
                painter = painterResource(Res.drawable.android_ico),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = stringResource(Res.string.android),
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    color = JTColors.BackGroundBlueColor,
                    fontFamily = JtBtoFonts.getInterFont()
                )

                Text(
                    text = stringResource(Res.string.downloads),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = JTColors.ReportsDescriptionColor,
                    fontFamily = JtBtoFonts.getInterFont()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(
                        color = JTColors.CountBackGroundColor.copy(0.3f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$androidCount",
                    color = JTColors.BackGroundBlueColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }

}


@Composable
fun ServicesBottomSheetContent(
    onServiceRequestClick: () -> Unit,
    onManageServicesClick: () -> Unit,
    onCloseClick: () -> Unit,
    requestCount:Int,
    serviceCount:Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JTColors.WhiteColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row (modifier = Modifier.fillMaxWidth(0.95f),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(Res.string.services),
                color = JTColors.blackColor,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.size(20.dp).clickable { onCloseClick()  }
            )

        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clickable { onServiceRequestClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(Res.drawable.services_ico),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.services_Request),
                    fontSize = 16.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.BackGroundBlueColor
                )
                Text(
                    text = stringResource(Res.string.services_Request_in_last_month),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterFont(),
                    color = JTColors.ReportsDescriptionColor

                )
            }

            Box(
                modifier = Modifier
                    .background(JTColors.CountBackGroundColor,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$serviceCount",
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = JTColors.BackGroundBlueColor,
                    fontSize = 26.sp
                )

            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clickable { onManageServicesClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(Res.drawable.servicerequest_ico),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.manage_services),
                    fontSize = 16.sp,
                    fontFamily = JtBtoFonts.getInterBoldFont(),
                    color = JTColors.BackGroundBlueColor
                )
                Text(
                    text = stringResource(Res.string.service_added),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterFont(),
                    color = JTColors.ReportsDescriptionColor
                )
            }
            Box(
                modifier = Modifier
                    .background(JTColors.CountBackGroundColor,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$requestCount",
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = JTColors.BackGroundBlueColor,
                    fontSize = 26.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
