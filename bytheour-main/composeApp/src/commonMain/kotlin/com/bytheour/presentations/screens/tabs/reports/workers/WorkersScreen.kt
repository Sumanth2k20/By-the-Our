package com.bytheour.presentations.screens.tabs.reports.workers

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.all
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.checkbox_select
import bytheour.composeapp.generated.resources.checkbox_unselect
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.delete_worker
import bytheour.composeapp.generated.resources.filter_ico
import bytheour.composeapp.generated.resources.in_progress
import bytheour.composeapp.generated.resources.manage_workers
import bytheour.composeapp.generated.resources.no
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.rejected
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_worker
import bytheour.composeapp.generated.resources.total_workers
import bytheour.composeapp.generated.resources.verified
import bytheour.composeapp.generated.resources.voice_ico
import bytheour.composeapp.generated.resources.want_to_delete
import bytheour.composeapp.generated.resources.worker_details
import bytheour.composeapp.generated.resources.yes
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.reports.CommonDialogBox
import com.bytheour.presentations.screens.tabs.reports.UserBottomSheetDetails
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import com.bytheour.utils.usePermission
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWorkersScreen(
    viewModel: WorkersScreenViewModel = getKoinViewModel(),
    navigateBack:()->Unit,
    navigateWorkerEditScreen:(String, String, String, String, String, String, String, String)-> Unit,
    editWorkerViewModel: EditWorkerDetailsScreenViewModel = getKoinViewModel(),
) {
    val uiState by viewModel.uiStates.collectAsState()
    val editUiState by editWorkerViewModel.uiStates.collectAsState()
    val selectedCount = uiState.contactList?.count { it.isSelected } ?: 0
    val isDropdownExpanded by remember { derivedStateOf { viewModel.isDropdownExpanded } }
    val lazyListState = rememberLazyListState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedWorker by remember { mutableStateOf<ContactWorkerCardUiState?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showVoiceSearch by remember { mutableStateOf(false) }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisibleIndex >= totalItems - 3 && totalItems > 0
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !uiState.isLoading && uiState.hasMoreData) {
                    viewModel.loadMoreWorkers()
                }
            }
    }
    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )
    val filteredList = remember(uiState.contactList, uiState.search) {
        val contacts = uiState.contactList.orEmpty()
        if (uiState.search.isNullOrBlank()) {
            contacts
        } else {
            contacts.filter { user ->
                val searchTerm = uiState.search?:""
                "${user.firstName} ${user.lastName}".lowercase().contains(searchTerm) ||
                        user.number?.lowercase()?.contains(searchTerm) == true ||
                        user.emailId?.lowercase()?.contains(searchTerm) == true
            }
        }
    }
    LaunchedEffect(Unit){
        if (uiState.contactList.isNullOrEmpty()) {
            viewModel.fetchAdminWorkersList(
                limit = PAGINATION_LIMIT.toString(),
                offset = "0",
                documentStatus = uiState.currentFilter,
                searchKey = uiState.search,
                isRefresh = true
            )
        }
    }
    if (showBottomSheet && selectedWorker != null) {
        LaunchedEffect(selectedWorker?.id) {
            selectedWorker?.id?.let { id ->
                editWorkerViewModel.fetchAdminWorkerData(id = id)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF7F7F7))
                .padding(bottom = if (selectedCount > 0) 70.dp else 0.dp)
        ){
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
                        text = stringResource(Res.string.manage_workers),
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

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    JTSearchTextField(
                        value = uiState.search ?: "",
                        placeholder = stringResource(Res.string.search_worker),
                        onValueChange = { viewModel.updateSearch(it) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
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
                        leadingIcon = {
                            Image(
                                painter = painterResource(Res.drawable.search_ico),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                    Box {
//                        Box(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .background(
//                                    color = JTColors.filterBackGround,
//                                    shape = CircleShape
//                                )
//                                .clickable { viewModel.toggleDropdown() },
//                            contentAlignment = Alignment.Center
//                        ) {
                            Image(
                                painter = painterResource(Res.drawable.filter_ico),
                                contentDescription = "Filter Icon",
                                modifier = Modifier.size(20.dp)
//                                    .background(
//                                        color = JTColors.filterBackGround,
//                                        shape = CircleShape
//                                    )
                                    .clickable { viewModel.toggleDropdown() },
                            )
//                        }

                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { viewModel.dismissDropdown() },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.fetchAdminWorkersList(
                                        limit = PAGINATION_LIMIT.toString(),
                                        offset = "0",
                                        documentStatus = null,
                                        searchKey = uiState.search,
                                        isRefresh = true
                                    )
                                    viewModel.dismissDropdown()
                                },
                                text = {
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.all),
                                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                                        fontSize = 15.sp
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onDropdownItemSelected("process")
                                    viewModel.dismissDropdown()
                                },
                                text = {
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.in_progress),
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        fontSize = 12.sp
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onDropdownItemSelected("verified")
                                    viewModel.dismissDropdown()
                                },
                                text = {
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.verified),
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        fontSize = 12.sp
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onDropdownItemSelected("rejected")
                                    viewModel.dismissDropdown()
                                },
                                text = {
                                    JtBtoCommonText(
                                        text = stringResource(Res.string.rejected),
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        fontSize = 12.sp
                                    )
                                }
                            )
                        }
                    }
                }

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
                                append(stringResource(Res.string.total_workers))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 12.sp,
                                    color = JTColors.BackGroundBlueColor
                                )
                            ) {
                                append(":${uiState.totalCount}")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = JtBtoFonts.getOpenSansBold(),
                                        fontSize = 12.sp,
                                        color = JTColors.BackGroundBlueColor
                                    )
                                ) {
                                    append(stringResource(Res.string.all))
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                                        fontSize = 12.sp,
                                        color = JTColors.BackGroundBlueColor
                                    )
                                ) {
                                    append("(${selectedCount}/${uiState.totalCount})")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = if (uiState.selectAll) {
                                painterResource(Res.drawable.checkbox_select)
                            } else {
                                painterResource(Res.drawable.checkbox_unselect)
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    viewModel.onSelectAllUsers(!uiState.selectAll)
                                }
                        )
                    }
                }

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList) { worker ->
                        WorkersDetailsCard(
                            name = "${worker.firstName ?:""} ${worker.lastName ?:""}",
                            phoneNumber = worker.number?:"",
                            isSelected = worker.isSelected,
                            imagePainter = worker.profileImage?:"",
                            onCheckedChange = {
                                viewModel.onSelectCheckBox(worker.id, it)
                            },
                            onCardClick = {
                                selectedWorker = worker
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
        if (selectedCount > 0 ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
                    .background(JTColors.BackGroundBlueColor, shape = RoundedCornerShape(30.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = JTColors.WhiteColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            showDeleteConfirmation = true
                        }
                )
            }
        }
        if (showDeleteConfirmation) {
            CommonDialogBox(
                title = stringResource(Res.string.delete_worker),
                message = stringResource(Res.string.want_to_delete),
                onCancelClick = {
                    showDeleteConfirmation = false
                },
                onContinueClick = {
                    val selectedUserIds = uiState.contactList?.filter { it.isSelected }?.map { it.id } ?: emptyList()
                    selectedUserIds.forEach { userId ->
                        viewModel.adminDeleteSelectedWorkers(id = userId)
                    }
                    showDeleteConfirmation = false
                },
                cancelText = stringResource(Res.string.no),
                deleteText = stringResource(Res.string.yes)
            )
        }
        if (showBottomSheet && selectedWorker != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedWorker = null
                },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = JTColors.WhiteColor
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                UserBottomSheetDetails(
                    text = stringResource(Res.string.worker_details),
                    name = "${selectedWorker?.firstName ?: ""} ${selectedWorker?.lastName ?: ""}",
                    contact = selectedWorker?.number ?: "",
                    email = selectedWorker?.emailId ?: "",
                    deviceType = selectedWorker?.accountType ?: "",
                    statusChecked = selectedWorker?.serviceStatus?:"",
                    onDelete = {
                        showBottomSheet = false
                        viewModel.adminDeleteSelectedWorkers(selectedWorker?.id?:"")
                    },
                    onEdit = {
                        showBottomSheet = false
                        navigateWorkerEditScreen(
                            selectedWorker!!.id,
                            selectedWorker!!.firstName?:"",
                            selectedWorker!!.lastName?:"",
                            selectedWorker!!.number?:"",
                            selectedWorker!!.emailId?:"",
                            selectedWorker!!.profileImage?:"",
                            selectedWorker!!.accountType?:"",
                            selectedWorker!!.documentStatus?:""
                        )
                    },
                    ongoing = editUiState.onGoing ?: 0,
                    completed = editUiState.completed ?: 0,
                    upcoming =editUiState.upComing ?: 0,
                    onStatusChange = {newChecked->
                        val status = if (newChecked) "y" else "n"
                        viewModel.workerStatusChange(selectedWorker?.id?:"", status = status)
                    },
                    close = {
                        showBottomSheet = false
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
}


@Composable
fun WorkersDetailsCard(
    name: String,
    phoneNumber: String,
    imagePainter: String,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onCardClick:()-> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = JTColors.WhiteColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.clickable { onCardClick() }
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                if (imagePainter.isNotBlank()) {
                    AsyncImage(
                        model = "${JTURLS.BASE_IMAGE_URL}$imagePainter",
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.provider_logo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    lineHeight = 13.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = phoneNumber,
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF7F7F7F)
                )
            }

            Image(
                painter = if (isSelected) {
                    painterResource(Res.drawable.checkbox_select)
                } else {
                    painterResource(Res.drawable.checkbox_unselect)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onCheckedChange(!isSelected) }
            )
        }
    }
}
