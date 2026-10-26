package com.bytheour.presentations.screens.tabs.moreTab.mediationGuideline

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.mediation_Guidelines
import bytheour.composeapp.generated.resources.thank_you_for_choosing_the_by_the_our
import bytheour.composeapp.generated.resources.tick_ico
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun JtMediationGuidelinesScreen(
    onBackClick: () -> Unit,
    viewModel: JTMediationGuidelinesViewModel = getKoinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }



    LaunchedEffect (Unit){
        viewModel.getMediationGuidelines()
    }





    Column(modifier = Modifier.fillMaxSize()) {
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.mediation_Guidelines),
            isBackArrowClicked = isBackArrowClicked,
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
            JtBtoCommonText(
                text = uiState.introText,
                fontSize = 10.sp,
                fontWeight = W400,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                lineHeight = 13.sp,
                textAlign = TextAlign.Left,
                maxLines = 15
            )

            Spacer(modifier = Modifier.height(24.dp))

            uiState.guidelines.forEach {
                GuidelineItem(title = it.title, description = it.description)
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(Modifier.fillMaxWidth(), color = JTColors.border, thickness = 2.dp)
            Spacer(modifier = Modifier.height(25.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.thank_you_for_choosing_the_by_the_our),
                fontSize = 10.sp,
                fontWeight = W700,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                lineHeight = 16.sp,
                color = JTColors.enableButton,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GuidelineItem(title: String, description: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.tick_ico),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(4.dp))
            JtBtoCommonText(
                text = "$title:",
                fontSize = 10.sp,
                fontWeight = W700,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                lineHeight = 16.sp,
                color = JTColors.enableButton
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 10.sp,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            lineHeight = 16.sp,
            color = Color(0xFF313131)
        )
    }
}
