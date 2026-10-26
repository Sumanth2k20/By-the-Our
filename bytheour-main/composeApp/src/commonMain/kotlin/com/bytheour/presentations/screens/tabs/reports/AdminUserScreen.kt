package com.bytheour.presentations.screens.tabs.reports

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.all
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.booking_info
import bytheour.composeapp.generated.resources.checkbox_select
import bytheour.composeapp.generated.resources.checkbox_unselect
import bytheour.composeapp.generated.resources.complete
import bytheour.composeapp.generated.resources.contactNumber
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.delete_user
import bytheour.composeapp.generated.resources.device_type
import bytheour.composeapp.generated.resources.edit
import bytheour.composeapp.generated.resources.email_id
import bytheour.composeapp.generated.resources.name
import bytheour.composeapp.generated.resources.no
import bytheour.composeapp.generated.resources.ongoing
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.search_users
import bytheour.composeapp.generated.resources.status
import bytheour.composeapp.generated.resources.total_user
import bytheour.composeapp.generated.resources.up_coming
import bytheour.composeapp.generated.resources.user_details
import bytheour.composeapp.generated.resources.users
import bytheour.composeapp.generated.resources.voice_ico
import bytheour.composeapp.generated.resources.want_to_delete
import bytheour.composeapp.generated.resources.yes
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
fun AdminUsersScreen(
    viewModel: AdminUsersViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    navigateUserEditScreen: (String, String, String, String, String,String) -> Unit
) {
    val uiState by viewModel.uiStates.collectAsState()
    val selectedCount = uiState.contactList?.count { it.isSelected } ?: 0
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<ContactCardUiState?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var showVoiceSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

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
    if (showBottomSheet && selectedUser != null) {
        LaunchedEffect(selectedUser?.id) {
            selectedUser?.id?.let { id ->
                viewModel.adminGetUserDate(id = id)
            }
        }
    }

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
                        text = stringResource(Res.string.users),
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
                    placeholder = stringResource(Res.string.search_users),
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
                                append(stringResource(Res.string.total_user))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 12.sp,
                                    color = JTColors.BackGroundBlueColor
                                )
                            ) {
                                append("${uiState.totalCount}")
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

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = uiState.contactList?: emptyList(),
                        key = { it.id }
                    ) { user ->
                        UserDetailsCard(
                            name = "${user.firstName ?:"Invalid"} ${user.lastName ?:""}",
                            phoneNumber = user.phoneNumber,
                            isSelected = user.isSelected,
                            onCheckedChange = {
                                viewModel.onSelectCheckBox(user.id, it)
                            },
                            imagePainter = user.image?:"",
                            onCardClick = {
                                selectedUser = user
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
        if (selectedCount > 0) {
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
                title = stringResource(Res.string.delete_user),
                message = stringResource(Res.string.want_to_delete),
                onCancelClick = {
                    showDeleteConfirmation = false
                },
                onContinueClick = {
                    val selectedUserIds = uiState.contactList?.filter { it.isSelected }?.map { it.id } ?: emptyList()
                    selectedUserIds.forEach { userId ->
                        viewModel.adminDeleteUser(id = userId)
                    }
                    showDeleteConfirmation = false
                },
                cancelText = stringResource(Res.string.no),
                deleteText = stringResource(Res.string.yes)
            )
        }
        if (showBottomSheet && selectedUser != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedUser = null
                },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = JTColors.WhiteColor
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                UserBottomSheetDetails(
                    text = stringResource(Res.string.user_details),
                    name = "${selectedUser?.firstName ?: ""} ${selectedUser?.lastName ?: ""}",
                    contact = selectedUser?.phoneNumber ?: "",
                    email = selectedUser?.email ?: "",
                    deviceType = selectedUser?.deviceType ?: "",
                    statusChecked = selectedUser?.serviceStatus?:"",
                    onDelete = {
                        viewModel.adminDeleteUser(selectedUser?.id?:"")
                    },
                    onEdit = {
                        showBottomSheet = false
                        navigateUserEditScreen(
                            selectedUser?.id ?: "",
                            selectedUser?.firstName ?: "",
                            selectedUser?.lastName ?: "",
                            selectedUser?.email ?: "",
                            selectedUser?.phoneNumber ?: "",
                            selectedUser?.image?:""
                        )
                    },
                    ongoing = uiState.onGoing ?: 0,
                    completed = uiState.completed ?: 0,
                    upcoming = uiState.upComing ?: 0,
                    onStatusChange = {newChecked->
                        val status = if (newChecked) "y" else "n"
                        viewModel.userStatusChange(selectedUser?.id?:"", status = status)
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
fun UserDetailsCard(
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



@Composable
fun UserBottomSheetDetails(
    text: String,
    name: String,
    contact: String,
    email: String,
    deviceType: String,
    statusChecked: String,
    onStatusChange: (Boolean) -> Unit,
    completed: Int? =null,
    ongoing: Int?=null,
    upcoming: Int? =null,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    close:()-> Unit
) {
    var isChecked by remember { mutableStateOf(statusChecked.equals("y", ignoreCase = true)) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .fillMaxHeight(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text =text ,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.size(20.dp).clickable { close() }
            )

        }

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.name),
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterFont()
            )

            Text(
                text = name,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterMediumFont()
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.contactNumber),
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterFont()
            )

            Text(
                text = contact,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterMediumFont()
            )

        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.email_id),
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterFont()
            )

            Text(
                text = email,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterMediumFont()
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))


        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.device_type),
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterFont()
            )

            Text(
                text = deviceType,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getInterMediumFont()
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.status),
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    onStatusChange(newChecked)
                },
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), color = Color(0xFFE4E2EE))

        Column(modifier = Modifier) {
            BookingInfoCard(
                completed = completed.toString(),
                ongoing = ongoing.toString(),
                upcoming = upcoming.toString()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onDelete,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, JTColors.viewBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    JTColors.WhiteColor
                ),
                modifier = Modifier.width(140.dp).height(40.dp)
                    .weight(1f)
            ) {
                Text(text = stringResource(Res.string.delete))
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = onEdit,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    JTColors.BackGroundBlueColor
                ),
                modifier = Modifier.width(140.dp).height(40.dp)
                    .weight(1f)
            ) {
                Text(text = stringResource(Res.string.edit))
            }
        }
    }
}



@Composable
fun BookingInfoCard(
    completed: String,
    ongoing: String,
    upcoming: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE4E2EE))
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.booking_info),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth().background(color = JTColors.WhiteColor)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BookingStatItem(label = stringResource(Res.string.complete), count = completed)
                VerticalDivider()
                BookingStatItem(label = stringResource(Res.string.ongoing), count = ongoing)
                VerticalDivider()
                BookingStatItem(label = stringResource(Res.string.up_coming), count = upcoming)
            }
        }
    }
}

@Composable
fun BookingStatItem(label: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        Text(text = label, fontSize = 14.sp)
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(1.dp)
            .background(Color(0xFFDDDBEF))
    )
}


@Composable
fun CommonDialogBox(
    title: String,
    message: String?,
    onCancelClick: () -> Unit,
    onContinueClick: () -> Unit,
    cancelText: String,
    deleteText: String,
) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               if (title.isNotEmpty()) {
                   Text(
                       text = title,
                       fontWeight = FontWeight.Bold,
                       fontSize = 18.sp,
                       color = Color.Black
                   )

                   Spacer(modifier = Modifier.height(12.dp))
               }

                if (message != null) {
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = JTColors.BlackButtonColor,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(cancelText, color = JTColors.BlackButtonColor)
                    }

                    Button(
                        onClick = onContinueClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(deleteText, color = JTColors.WhiteColor)
                    }
                }
            }
        }
    }
}
