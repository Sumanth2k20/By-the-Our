package com.bytheour.presentations.screens.tabs.moreTab.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.get_notifications_on_rating_Skill
import bytheour.composeapp.generated.resources.inbox_messages
import bytheour.composeapp.generated.resources.notifications
import bytheour.composeapp.generated.resources.promotions_and_tips
import bytheour.composeapp.generated.resources.ratings_reminders
import bytheour.composeapp.generated.resources.receive_notifications
import bytheour.composeapp.generated.resources.there_are_discounts
import bytheour.composeapp.generated.resources.we_have_updates_about_your_account
import bytheour.composeapp.generated.resources.your_account
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource


@Composable
fun JTNotificationScreen(
    onBackClick: () -> Unit,
    viewModel: JTNotificationScreenViewModel = getKoinViewModel()
) {

    val uiState  =  viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.notifications),
            onBackClick = { onBackClick() },
            showBackButton = true
        )

        HorizontalDivider(color = JTColors.border, thickness = 1.dp)

        // Notification toggles separately
        NotificationToggleRow(
            title = stringResource(Res.string.inbox_messages),
            description = stringResource(Res.string.receive_notifications),
            isEnabled = uiState.value.inboxMessages,
            onToggle = { viewModel.toggleInboxMessages(it)}
        )
        HorizontalDivider(color = JTColors.border, thickness = 1.dp)

        NotificationToggleRow(
            title = stringResource(Res.string.ratings_reminders),
            description = stringResource(Res.string.get_notifications_on_rating_Skill),
            isEnabled = uiState.value.ratingsReminders,
            onToggle = { viewModel.toggleRatingsReminders(it) }
        )


        NotificationToggleRow(
            title = stringResource(Res.string.promotions_and_tips),
            description = stringResource(Res.string.there_are_discounts),
            isEnabled = uiState.value.promotionsAndTips,
            onToggle = { viewModel.togglePromotionsAndTips(it) }
        )


        NotificationToggleRow(
            title = stringResource(Res.string.your_account),
            description = stringResource(Res.string.we_have_updates_about_your_account),
            isEnabled = uiState.value.accountUpdates,
            onToggle = { viewModel.toggleAccountUpdates(it)}
        )

    }
}


@Composable
fun NotificationToggleRow(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            JtBtoCommonText(
                text = title,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = Color(0xFF1B2431)
            )
            Spacer(modifier = Modifier.height(4.dp))

            JtBtoCommonText(
                text = description,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = W400,
                textAlign = TextAlign.Left,
                maxLines = 2,
                color = Color(0xFF777777)
            )
        }

         val customSwitchWidth = 40.dp
         val customSwitchHeight = 33.dp

        Switch(
            modifier = Modifier.padding(end = 16.dp).requiredSize(customSwitchWidth,customSwitchHeight),
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = JTColors.enableButton,
                uncheckedBorderColor = JTColors.enableButton,
                uncheckedTrackColor = Color.White,
                uncheckedThumbColor = JTColors.enableButton
            )
        )
    }
}
