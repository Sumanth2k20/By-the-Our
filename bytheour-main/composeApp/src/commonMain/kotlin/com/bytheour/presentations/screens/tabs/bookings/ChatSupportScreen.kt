package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.chat_Support
import bytheour.composeapp.generated.resources.no_more_messages
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.send_message
import bytheour.composeapp.generated.resources.sendmessage_ico
import bytheour.composeapp.generated.resources.support_id
import bytheour.composeapp.generated.resources.type_message
import bytheour.composeapp.generated.resources.upload_img_ico
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSupportScreen(
    navigateBack: () -> Unit,
    viewModel: ChatSupportViewModel = getKoinViewModel(),
    bookingId: String
) {
    var messageText by remember { mutableStateOf("") }
    val uiState by viewModel.uiStates.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.addTicketSupport(bookingId)
    }

    // Handle pagination when scrolling to the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.messages.size - 3 && // Load more when 3 items from end
                    uiState.hasMoreData &&
                    !uiState.isLoadingMore) {
                    viewModel.loadMoreMessages()
                }
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
                    Image(
                        painter = painterResource(Res.drawable.provider_logo),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = stringResource(Res.string.chat_Support),
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            color = JTColors.chatName,
                            fontWeight = FontWeight.W700,
                            fontFamily = JtBtoFonts.getOpenSansBold()
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    viewModel.refreshMessages()
                }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(JTColors.WhiteColor)
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.support_id)+ (uiState.supportId ?: ""),
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(
                    count = uiState.messages.size,
                    key = { index -> "${uiState.messages[index].id}_$index" }
                ) {}
                if (!uiState.hasMoreData && uiState.messages.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_more_messages),
                                fontSize = 12.sp,
                                color = Color.Gray,
                            )
                        }
                    }
                }
            }
        }

        // Message input section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            JTBtoCustomTextField(
                value = messageText,
                name = stringResource(Res.string.send_message),
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
                        modifier = Modifier.clickable {}
                    )
                }
            )

            Spacer(modifier = Modifier.width(4.dp))

            Image(
                painter = painterResource(Res.drawable.sendmessage_ico),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable{}
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}