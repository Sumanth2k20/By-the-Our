package com.bytheour.presentations.screens.tabs.moreTab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.policy
import com.bytheour.utils.JTColors
import com.bytheour.utils.FormattedTextDisplay
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource


@Composable
fun PrivacyAndPolicyScreen(
    onBackClick: () -> Unit,
    viewModel: PrivacyAndPolicyViewModel = getKoinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getPrivacyAndPolicy()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.policy),
            isBackArrowClicked = isBackArrowClicked,
            showBackButton = true,
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            }
        )
        HorizontalDivider(Modifier.fillMaxWidth(), color = JTColors.border)

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FormattedTextDisplay(uiState.policyText)
        }
    }
}




