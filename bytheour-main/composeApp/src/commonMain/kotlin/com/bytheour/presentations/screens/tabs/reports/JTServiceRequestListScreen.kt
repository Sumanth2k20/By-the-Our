package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.category
import bytheour.composeapp.generated.resources.description
import bytheour.composeapp.generated.resources.edit
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.search_services
import bytheour.composeapp.generated.resources.serviceStatus
import bytheour.composeapp.generated.resources.service_Requests
import bytheour.composeapp.generated.resources.service_details
import bytheour.composeapp.generated.resources.title
import bytheour.composeapp.generated.resources.total_Services
import bytheour.composeapp.generated.resources.voice_ico
import bytheour.composeapp.generated.resources.workerName
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTServiceRequestListScreen(
    viewModel: JTServiceRequestListViewModel = getKoinViewModel(),
    navigateToServiceEditScreen:(
        id:Int,
        title:String,
        description:String,
        category: String,
        avatar:String,
        sequence:Int,
        serviceStatus:String
    ) ->Unit,
    navigateBack:()->Unit
){
    LaunchedEffect(Unit){
        viewModel.refreshData()
    }
    val uiState by viewModel.uiStates.collectAsState()

    var selectedService by remember { mutableStateOf<JTServiceRequestListViewModel.ServiceCardUiState?>(null) }
    var showServiceBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var showVoiceSearch by remember { mutableStateOf(false) }
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore && uiState.hasMoreData && !uiState.isLoadingMore) {
                    viewModel.loadMoreData()
                }
            }
    }
    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )
    Column(modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(135.dp)
                .background(color = JTColors.BackGroundBlueColor)
        ) {
            if(platform.equals("IOS", ignoreCase = true)){
                Spacer(modifier = Modifier.height(50.dp))
            }else{
                Spacer(modifier = Modifier.height(40.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                contentAlignment = Alignment.Center
            ) {
                JtBtoCommonText(
                    text = stringResource(Res.string.service_Requests),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    fontSize = 15.sp,
                    fontWeight = W700,
                    lineHeight = 12.sp,
                    color = JTColors.WhiteColor
                )
                Image(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { navigateBack() }
                        .padding(start = 16.dp, end = 8.dp)
                        .size(17.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            JTSearchTextField(
                value = uiState.search ?: "",
                placeholder = stringResource(Res.string.search_services),
                onValueChange = { viewModel.updateSearch(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                trailingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.voice_ico),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                            .clickable {
                                permission()
                            }
                    )
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Column (
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.total_Services)+" - ${uiState.totalCount}",
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getInterFont(),
                color = JTColors.BackGroundBlueColor,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(
                        items = uiState.serviceCardUiState,
                        key = { it.id }
                    ) { item ->
                        ServiceRequestCards(
                            status = item.serviceStatus,
                            title = item.title ?: "",
                            name = item.workerName ?: "",
                            imageRes = item.profile,
                            onClick = {
                                selectedService = item
                                showServiceBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
    }
    if (showServiceBottomSheet && selectedService != null) {
        ModalBottomSheet(
            onDismissRequest = { showServiceBottomSheet = false },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = JTColors.WhiteColor
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ServiceDetailsBottomSheet(
                title = selectedService?.title ?: "",
                workerName = selectedService?.workerName ?: "",
                description = selectedService?.description ?: "",
                category = selectedService?.category ?: "",
                serviceStatus = selectedService?.serviceStatus ?: "",
                onClose = { showServiceBottomSheet = false },
                onEditClick = {
                    showServiceBottomSheet = false
                    navigateToServiceEditScreen(
                        selectedService?.id ?: 0,
                        selectedService?.title ?: "",
                        selectedService?.description ?: "",
                        selectedService?.category ?: "",
                        selectedService?.profile ?: "",
                        selectedService?.sequence ?: 0,
                        selectedService?.serviceStatus ?: ""
                    )
                }
            )
        }
    }
    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
                    searchQuery = text
                    viewModel.updateSearch(text)
                }
                showVoiceSearch = false
            },
            onComplete = {
                showVoiceSearch = false
            },
            isListening = {
                showVoiceSearch = it
            }
        )
    }
}



@Composable
fun ServiceRequestCards(
    status: String,
    title: String,
    name: String,
    imageRes: String,
    onClick:()-> Unit,
) {
    data class StatusStyle(
        val bgColor: Color,
        val textColor: Color,
        val dotColor: Color
    )
    val (statusBgColor, statusTextColor, dotColor) = when (status) {
        "Pending" -> StatusStyle(Color(0xFFFFF3CD), Color(0xFF856404), Color(0xFFFFA500))
        "Approved" -> StatusStyle(Color(0xFFD4EDDA), Color(0xFF155724), Color.Green)
        "Rejected" -> StatusStyle(Color(0xFFF8D7DA), Color(0xFF721C24), Color.Red)
        else -> StatusStyle(Color.LightGray, Color.DarkGray, Color.Gray)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(8.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Background image
        if (imageRes.isBlank()) {
            Image(
                painter = painterResource(Res.drawable.image_1),
                contentDescription = "Service image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = "${JTURLS.BASE_IMAGE_URL}$imageRes",
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = statusBgColor,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = status,
                    color = statusTextColor,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont()
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp)
        ) {
            Text(
                text = title,
                color = JTColors.WhiteColor,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 16.sp,
                lineHeight = 16.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = name,
                    color = JTColors.WhiteColor,
                    fontFamily = JtBtoFonts.getInterFont(),
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ServiceDetailsBottomSheet(
    title: String,
    workerName: String,
    description: String,
    category: String,
    serviceStatus: String,
    onEditClick: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JTColors.WhiteColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.service_details),
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClose() }
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Text(
            text = stringResource(Res.string.title),
            color = JTColors.bottomSheetTitleColor,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )

        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Text(
            text = stringResource(Res.string.workerName),
            color = JTColors.bottomSheetTitleColor,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        Text(
            text = workerName,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.description),
            color = JTColors.bottomSheetTitleColor,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        Text(
            text = description,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.category),
            color = JTColors.bottomSheetTitleColor,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        Text(
            text = category,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = JtBtoFonts.getInterMediumFont(),
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.serviceStatus),
                color = JTColors.bottomSheetTitleColor,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterFont(),
            )
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color(0xFFFFF3CD),
                        shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = serviceStatus,
                    color = Color(0xFF856404),
                    fontSize = 12.sp,
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF321B6F))
        ) {
            Text(
                text = stringResource(Res.string.edit),
                color = Color.White,
            )
        }
    }
}
