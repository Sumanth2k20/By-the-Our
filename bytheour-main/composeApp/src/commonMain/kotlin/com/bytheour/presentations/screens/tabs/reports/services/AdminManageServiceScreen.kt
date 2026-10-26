package com.bytheour.presentations.screens.tabs.reports.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.Sequence
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.category
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.delete_service
import bytheour.composeapp.generated.resources.delete_service_message
import bytheour.composeapp.generated.resources.description
import bytheour.composeapp.generated.resources.edit
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.manage_Services
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_services
import bytheour.composeapp.generated.resources.serviceStatus
import bytheour.composeapp.generated.resources.service_details
import bytheour.composeapp.generated.resources.title
import bytheour.composeapp.generated.resources.total_Services
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.CustomAlertDialog
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
fun AdminManageServiceScreen(
    viewModel: AdminManageServiceScreenViewModel = getKoinViewModel(),
    navigateBack:()->Unit,
    navigateToManageServiceEditScreen:(
        id:Int,
        title:String,
        description:String,
        category: String,
        avatar:String,
        sequence:Int,
        serviceStatus:String,
        categoryId:Int
    ) ->Unit,
) {

    LaunchedEffect(Unit){
        viewModel.refreshData()
    }

    val uiState by viewModel.uiStates.collectAsState()
    val selectedCount = uiState.contactList?.count { it.isSelected } ?: 0

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedUser by remember {
        mutableStateOf<ContactCardUiState?>(null)
    }

    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var showVoiceSearch by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF7F7F7))
                .padding(bottom = if (selectedCount > 0) 70.dp else 0.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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
                        text = stringResource(Res.string.manage_Services),
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
                    leadingIcon = {
                        Image(
                            painter = painterResource(Res.drawable.search_ico),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        Image(
                            painter = painterResource(Res.drawable.voice_ico),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                                .clickable {
                                    permission()
                                }
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                                    fontSize = 12.sp,
                                    color = JTColors.BackGroundBlueColor
                                )
                            ) {
                                append(stringResource(Res.string.total_Services))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 12.sp,
                                    color = JTColors.BackGroundBlueColor
                                )
                            ) {
                                append(" - ${uiState.totalCount}")
                            }
                        }
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.contactList?: emptyList(),
                            key = { it.id }
                        ) { user ->
                            ServiceManageCards(
                                status = user.serviceStatus?:"",
                                title = user.title ?: "",
                                imageRes = user.image?:"",
                                category = user.categoryName?:"",
                                onCardClick = {
                                    selectedUser = user
                                    showBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }
        }
        if (showBottomSheet && selectedUser != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = JTColors.WhiteColor
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                selectedUser?.let { user ->
                    ServiceManageDetailsBottomSheet(
                        title = user.title ?: "",
                        description = user.description ?: "",
                        category = user.categoryName ?: "",
                        serviceStatus = user.serviceStatus?:"",
                        sequence = user.sequence ?: 0,
                        onClose = { showBottomSheet = false },
                        onStatusChange = { newChecked ->
                            val status = if (newChecked) "y" else "n"
                            viewModel.serviceStatusChange(user.id, status = status)
                        },
                        onEditClick = {
                            showBottomSheet = false
                            navigateToManageServiceEditScreen(
                                user.id.toInt(),
                                user.title?:"",
                                user.description?:"",
                                user.categoryName?:"",
                                user.image?:"",
                                user.sequence?: 0,
                                user.serviceStatus?:"",
                                user.categoryId?:0
                            )
                        },
                        onDeleteClick = {
                            showDialog = true
                        }
                    )
                }
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
    if (showDialog && selectedUser != null){
        CustomAlertDialog(
            showDialog = showDialog,
            onDismiss = {showDialog = false},
            title = stringResource(Res.string.delete_service),
            message = stringResource(Res.string.delete_service_message),
            onCancel = { showDialog = false },
            onConfirm = {
                selectedUser?.let { user ->
                    viewModel.adminDeleteService(user.id)
                }
            },
            yes = true,
            no = true
        )
    }
}



@Composable
fun ServiceManageCards(
    status: String,
    title: String,
    imageRes: String,
    category:String,
    onCardClick: () -> Unit,
) {
    data class StatusStyle(
        val label: String,
        val bgColor: Color,
        val textColor: Color,
        val dotColor: Color
    )

    val statusStyle = when (status.lowercase()) {
        "y" -> StatusStyle("", Color.Transparent, Color.Transparent, Color.Transparent)
        "n" -> StatusStyle("N/A", Color.LightGray, Color(0xFF1D4100), Color.Gray)
        else -> StatusStyle("Unknown", Color.LightGray, Color.DarkGray, Color.Gray)
    }

    val (label, bgColor, textColor, dotColor) = statusStyle


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(8.dp)
            .clickable { onCardClick() }
            .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            model = "${JTURLS.BASE_IMAGE_URL}$imageRes",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            placeholder = painterResource(Res.drawable.image_1),
            error = painterResource(Res.drawable.image_1),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = bgColor,
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
                    text = label,
                    color = textColor,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont()
                )
            }
        }
        Column(
            modifier = Modifier.padding(start = 10.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = title,
                color = JTColors.WhiteColor,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 16.sp,
                lineHeight = 16.sp,
            )

            Text(
                text = category,
                color = JTColors.WhiteColor,
                fontFamily = JtBtoFonts.getInterFont(),
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
fun ServiceManageDetailsBottomSheet(
    title: String,
    sequence: Int,
    description: String,
    category: String,
    serviceStatus: String,
    onEditClick: () -> Unit,
    onStatusChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    onDeleteClick:()-> Unit
) {
    var isChecked by remember { mutableStateOf(serviceStatus.equals("y", ignoreCase = true)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(JTColors.WhiteColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.service_details),
                fontSize = 16.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.title),
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))


        Text(
            text = stringResource(Res.string.category),
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.description),
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = stringResource(Res.string.Sequence),
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sequence.toString().padStart(3, '0'),
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getInterMediumFont()
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.serviceStatus),
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = JtBtoFonts.getInterFont()
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    onStatusChange(newChecked)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF321B6F))
            )

        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {onDeleteClick() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF321B6F))
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = Color(0xFF321B6F)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onEditClick() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF321B6F))
            ) {
                Text(
                    text = stringResource(Res.string.edit),
                    color = Color.White
                )
            }
        }
    }
}
