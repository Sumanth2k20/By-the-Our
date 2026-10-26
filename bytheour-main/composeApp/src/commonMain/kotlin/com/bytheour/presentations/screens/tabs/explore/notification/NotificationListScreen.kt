package com.bytheour.presentations.screens.tabs.explore.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.notifications
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource


@Composable
fun NotificationListScreen(
    viewModel: NotificationListScreenViewModel = getKoinViewModel(),
    navigateBack:()->Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize()
        .background(color = JTColors.enableButton)
        .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = JTColors.WhiteColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            JtBtoCommonText(
                stringResource(Res.string.notifications),
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 15.sp,
                color = JTColors.WhiteColor,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .align(alignment = Alignment.CenterVertically)

            )
        }
        Column(modifier =  Modifier.fillMaxSize())
        {
            val groupedNotifications = remember(uiState.notifications) {
                uiState.notifications.groupBy { viewModel.categorizeDate(it.timestamp) }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(JTColors.CancelScreenBackGroundColor),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                groupedNotifications.forEach { (dateCategory, notifications) ->
                    item {
                        Text(
                            text = dateCategory,
                            color = JTColors.blackLight,
                            fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    itemsIndexed(notifications) { index, notification ->
                        NotificationCard(notification)

                        val absoluteIndex = uiState.notifications.indexOf(notification)
                        if (absoluteIndex == uiState.notifications.lastIndex && !uiState.endReached) {
                            LaunchedEffect(Unit) {
                                viewModel.loadNotifications()
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun NotificationCard(notification: NotificationItem) {
    Box(modifier = Modifier.fillMaxWidth()
        .zIndex(16f)
        .padding(horizontal = 16.dp, vertical = 10.dp)
        .border(0.5.dp, color = JTColors.notification_border.copy(5f),
            shape = RoundedCornerShape(12.dp))
        ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = JTColors.WhiteColor, shape = RoundedCornerShape(12.dp))
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = JTColors.image_border,
                        shape = CircleShape
                    )
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = notification.imageUrl ?: "https://via.placeholder.com/64",
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.type,
                    color = JTColors.enableButton,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    color = JTColors.notification_text,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    fontWeight = FontWeight.W400,
                    letterSpacing = 0.42.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = notification.timeAgo,
                fontSize = 14.sp,
                color = JTColors.notification_time,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }

}
