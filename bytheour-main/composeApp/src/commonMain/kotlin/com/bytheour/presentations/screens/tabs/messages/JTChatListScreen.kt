package com.bytheour.presentations.screens.tabs.messages

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.archived_chat
import bytheour.composeapp.generated.resources.chat
import bytheour.composeapp.generated.resources.no_chat
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.search_chat
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.showProgress
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTChatListScreen(
    chatListViewModel: JTChatListViewModel = getKoinViewModel(),
    navigateToOneOnOneChat: (String, String, Boolean, String, String?) -> Unit,
    navigateToArchiveChat:()-> Unit
) {
    val chatList by chatListViewModel.chatList.collectAsState()
    val uiState by chatListViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var showVoiceSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        chatListViewModel.getChatRoomsList()
    }
    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            JtBtoCommonText(
                stringResource(Res.string.chat),
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 19.sp,
                lineHeight = 12.sp,
                fontWeight = W700,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        JTSearchTextField(
            value = uiState.search ?: "",
            onValueChange = { chatListViewModel.updateSearch(it) },
            placeholder = stringResource(Res.string.search_chat),
            enabled = true,
            modifier = Modifier
                .fillMaxWidth(0.95f).align(alignment = Alignment.CenterHorizontally)
                .border(1.dp, shape = RoundedCornerShape(48.dp), color = JTColors.grayTextColor.copy(alpha = 0.4f)),

            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.search_ico),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    chatListViewModel.updateSearch(
                        uiState.search?:""
                    )
                }
            ),
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
        Spacer(modifier = Modifier.height(12.dp))

        Box (modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(30.dp)
            .clickable { navigateToArchiveChat() }
            .align(Alignment.CenterHorizontally)
            .background(JTColors.BackGroundBlueColor,
                shape = RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = stringResource(Res.string.archived_chat),
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                fontSize = 12.sp,
                color = JTColors.WhiteColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 10.dp)
            )
        }


        Box(modifier = Modifier.fillMaxSize()) {
            if (!showProgress.value) {
                if (chatList.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.no_chat),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold()
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(chatList) { item ->
                            ChatSupportList(
                                name = item.name,
                                message = item.roomId,
                                time = item.date,
                                profile = item.profile,
                                onClick = {
                                    navigateToOneOnOneChat(
                                        item.name ?: "",
                                        item.roomId ?: "",
                                        item.isArchive ?: false,
                                        item.profile ?: "",
                                        ""
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
                    chatListViewModel.updateSearch(text)
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
fun ChatSupportList(
    onClick: () -> Unit,
    name: String? = null,
    message: String? = null,
    time: String? = null,
    badge: String? = null,
    profile: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        // Profile image
        Box(contentAlignment = Alignment.Center) {
            if (!profile.isNullOrEmpty()) {
                AsyncImage(
                    model = profile,
                    contentDescription = "",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.provider_logo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Middle column takes remaining space
        Column(
            modifier = Modifier.weight(1f)
        ) {
            JtBtoCommonText(
                text = name ?: "",
                fontSize = 16.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                color = JTColors.chatName
            )
            Spacer(modifier = Modifier.height(4.dp))
            JtBtoCommonText(
                text = message ?: "",
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                color = JTColors.message,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1, // keep one line
                textAlign = TextAlign.Start
            )
        }

        // Right column (time + badge)
        Column(
            horizontalAlignment = Alignment.End, // align text to right
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text  = time?:"",
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = JTColors.message,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!badge.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(18.dp)
                        .background(color = JTColors.enableButton),
                    contentAlignment = Alignment.Center
                ) {
                    JtBtoCommonText(
                        text = badge,
                        color = JTColors.WhiteColor,
                        fontSize = 12.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold()
                    )
                }
            }
        }
    }
}
