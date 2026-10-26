package com.bytheour.presentations.screens.tabs.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.archived_chat
import bytheour.composeapp.generated.resources.no_data
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource


@Composable
fun ArchivedChatScreen(
    chatListViewModel: JTChatListViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    navigateToOneOnOneChat: (String, String, Boolean,String, String?) -> Unit,
) {
    val chatList by chatListViewModel.chatList.collectAsState()
    val uiState by chatListViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        chatListViewModel.refreshArchivedChats(PAGINATION_LIMIT.toString())
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
                    chatListViewModel.loadMoreArchivedChats(PAGINATION_LIMIT.toString())
                }
            }
    }

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
            IconButton(
                onClick = { navigateBack() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
            JtBtoCommonText(
                text = stringResource(Res.string.archived_chat),
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


        Box(modifier = Modifier.fillMaxSize()) {
            if (chatList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    JtBtoCommonText(
                        text = stringResource(Res.string.no_data),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = chatList,
                    ) { item ->
                        ChatSupportList(
                            name = item.name,
                            message = item.message ?: item.roomId, // Use message if available, fallback to roomId
                            time = item.date,
                            profile = item.profile,
                            onClick = {
                                navigateToOneOnOneChat(
                                    item.name ?: "",
                                    item.roomId ?: "",
                                    item.isArchive ?: true,
                                    item.profile?:"",
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