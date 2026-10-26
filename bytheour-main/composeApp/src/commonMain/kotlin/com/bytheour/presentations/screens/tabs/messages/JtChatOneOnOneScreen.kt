package com.bytheour.presentations.screens.tabs.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.archived_chat
import bytheour.composeapp.generated.resources.chat_Support
import bytheour.composeapp.generated.resources.close_Ticket
import bytheour.composeapp.generated.resources.frame_2107
import bytheour.composeapp.generated.resources.how_can_i_help_you
import bytheour.composeapp.generated.resources.how_much_time_do_you_think_this_job_consume
import bytheour.composeapp.generated.resources.nums
import bytheour.composeapp.generated.resources.online
import bytheour.composeapp.generated.resources.others
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.room_id
import bytheour.composeapp.generated.resources.send_message
import bytheour.composeapp.generated.resources.sendmessage_ico
import bytheour.composeapp.generated.resources.this_job_on_certain_budget
import bytheour.composeapp.generated.resources.type_message
import bytheour.composeapp.generated.resources.un_archiveChat
import bytheour.composeapp.generated.resources.upload_img_ico
import coil3.compose.AsyncImage
import com.bytheour.data.request.CloseSupportTicketRequest
import com.bytheour.data.response.Message
import com.bytheour.utils.enums.MessageType
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.ChatSupportScreenViewModel
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneOnOneChatScreen(
    viewModel: OneOnOneChatScreenViewModel = getKoinViewModel(),
    chatSupportScreenViewModel: ChatSupportScreenViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    navigateToAttachment: (String) -> Unit,
    name: String,
    roomId: String,
    isArchive: Boolean,
    fromChatSupportId:String,
    profile:String,
    fromID: String
) {
    val message by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    var showDropdownMenu by remember { mutableStateOf(false) }
    val uiState by viewModel.uiStates.collectAsState()
    var showImagePicker by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val defaultsQuestions = listOf(
        stringResource(Res.string.how_can_i_help_you),
        stringResource(Res.string.this_job_on_certain_budget),
        stringResource(Res.string.how_much_time_do_you_think_this_job_consume),
        stringResource(Res.string.others)
    )

    LaunchedEffect(Unit) {
        if (roomId.isEmpty() && fromChatSupportId.isEmpty()){
            viewModel.createChatRoom(fromChatSupportId,profile,fromID)
            println("chatRoomLaunched:===================== ${fromChatSupportId}${profile}${fromID}")
        }else{
            viewModel.start(roomId,fromChatSupportId,profile,fromID)
            viewModel.callChatHistory()
        }
    }
    // Pagination
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
            totalItems > 0 && firstVisibleIndex <= 2
        }.collect { shouldLoadMore ->

        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = profile,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        error =painterResource(Res.drawable.provider_logo)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = uiState.username ?: name .ifBlank { stringResource(Res.string.chat_Support) },
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            color = JTColors.chatName,
                            fontWeight = FontWeight.W700,
                            fontFamily = JtBtoFonts.getOpenSansBold()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.size(8.dp),
                                colors = CardDefaults.cardColors(JTColors.OnlineGreenDot)
                            ) {

                            }

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = uiState.status ?: stringResource(Res.string.online),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = JTColors.OnlineTextColor,
                                fontFamily = JtBtoFonts.getOpenSansRegular()
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { showDropdownMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                ChatOptionsDropdownMenu(
                    expanded = showDropdownMenu,
                    onDismiss = { showDropdownMenu = false },
                    onArchivedChatClick = {
                        val archive = if (isArchive) 1 else 0
                        viewModel.makeChatArchive(roomId = roomId, unarchive = archive)
                        navigateBack()
                    },
                    roomId = uiState.roomId ?: stringResource(Res.string.nums),
                    isArchive = isArchive,
                    showCloseTickets = fromChatSupportId.isEmpty(),
                    onCloseTicketClick = {
                        chatSupportScreenViewModel.closeSupportTicket(
                            payload = CloseSupportTicketRequest(
                                supportId = fromChatSupportId
                            ),
                            onResult = navigateBack
                        )
                    }


                )
            },
            colors = TopAppBarDefaults.topAppBarColors(JTColors.WhiteColor)
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.Bottom
            ) {
                items(items = message) { message ->
                    ChatBubble(
                        message,
                        viewModel,
                        onImageClick = {
                            navigateToAttachment(it)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }

        if (uiState.showDefaultQues) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                DisplayDefaultQuestions(
                    questions = defaultsQuestions,
                    onTagClick = { messageText ->
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText, MessageType.TEXT.status)
                        }
                    }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().imePadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            JTBtoCustomTextField(
                value = messageText,
                name = stringResource(Res.string.send_message) + viewModel.getAccountType,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(0.85f),
                placeholder = stringResource(Res.string.type_message),
                shape = RoundedCornerShape(30.dp),
                trailingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.upload_img_ico),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            showImagePicker = true
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.width(4.dp))

            Image(
                painter = painterResource(Res.drawable.sendmessage_ico),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText, MessageType.TEXT.status)
                            messageText = ""
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (showImagePicker) {
        PermissionImagePicker(
            imageBitmap = uiState.imageBitmap,
            onImageSelected = {
                viewModel.updateImageBitmap(
                    imageBitmap = it,
                )
                showImagePicker = false
            },
            showBottomSheet = showImagePicker,
            onDismissRequest = {
                showImagePicker = false
            },
            filePathUri = {
                viewModel.updateImagePathUrl(it?:"")
            },
            byteArrayImage = {
                viewModel.updateByteArrayImage(it?:ByteArray(0))
                viewModel.sendChatImage(it?:ByteArray(0))
            }
        )
    }
}

@Composable
fun ChatBubble(
    message: Message,
    viewModel: OneOnOneChatScreenViewModel,
    onImageClick: (String) -> Unit
) {
    val isUser = message.sender == viewModel.getCurrentUserType
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(Res.drawable.frame_2107),
                contentDescription = "BtoIcon",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            if (message.type == MessageType.TEXT.status) {
                Column(
                    modifier = Modifier
                        .background(
                            color = if (isUser) JTColors.BackGroundBlueColor else JTColors.receiverMessageColor,
                            shape = RoundedCornerShape(
                                topStart = if (isUser) 24.dp else 0.dp,
                                topEnd = 24.dp,
                                bottomStart = if (isUser) 24.dp else 24.dp,
                                bottomEnd = if (isUser) 0.dp else 24.dp
                            )
                        )
                        .padding(12.dp)
                        .widthIn(0.dp, 250.dp)
                ) {
                    Text(
                        text = message.message ?: "",
                        color = if (isUser) Color.White else Color.Black
                    )
                }

                if (message.type == MessageType.IMAGE.status) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            if (message.type == MessageType.IMAGE.status){
                Card(
                    modifier = Modifier
                        .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    ),
                ){
                    AsyncImage(
                        model = message.message ?: "",
                        contentDescription = "Sent Image",
                        modifier = Modifier
                            .width(280.dp)
                            .height(170.dp)
                            .clickable { onImageClick(message.message?:"") },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun ChatOptionsDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onArchivedChatClick: () -> Unit,
    roomId: String,
    isArchive: Boolean,
    showCloseTickets:Boolean = true,
    onCloseTicketClick: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(180.dp)
            .background(Color.White, shape = RoundedCornerShape(13.dp)),
        shape = RoundedCornerShape(13.dp)
    ) {
        if (showCloseTickets){
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(13.dp))
            ) {
                Text(
                    text = if (isArchive)
                        stringResource(Res.string.un_archiveChat)
                    else
                        stringResource(Res.string.archived_chat),
                    fontSize = 14.sp,
                    color = JTColors.archivedTextColor,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onArchivedChatClick() }
                        .padding(10.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = JTColors.grayColor.copy(alpha = 0.3f)
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .background(color = JTColors.lightPink)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.room_id),
                        fontSize = 11.sp,
                        color = JTColors.grayColor,
                        fontWeight = FontWeight.W400,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = roomId,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }else{
            Text(
                text = stringResource(Res.string.close_Ticket),
                fontSize = 11.sp,
                color = JTColors.grayColor,
                fontWeight = FontWeight.W400,
                lineHeight = 20.sp,
                modifier = Modifier.clickable { onCloseTicketClick() }.padding(10.dp)
            )
        }

    }
}






@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayDefaultQuestions(
    questions: List<String>,
    onTagClick: (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = JTColors.ChatQuestionBackGround,
                RoundedCornerShape(topStart = 10.dp,
                    topEnd = 30.dp,
                    bottomStart = 30.dp,
                    bottomEnd = 10.dp
                )
            )
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            questions.forEach { question ->
                QuestionTag(
                    text = question,
                    onTagClick = {
                        onTagClick(question)
                    }
                )
            }
        }
    }
}


@Composable
fun QuestionTag(
    text: String,
    onTagClick: () -> Unit
){
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = JTColors.BackGroundBlueColor,
        tonalElevation = 0.dp,
        modifier = Modifier.padding(top = 2.dp)
            .clickable { onTagClick() }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = JTColors.WhiteColor,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}


