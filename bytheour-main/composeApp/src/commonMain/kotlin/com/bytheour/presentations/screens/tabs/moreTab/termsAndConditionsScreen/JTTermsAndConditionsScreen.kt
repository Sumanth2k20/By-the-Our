package com.bytheour.presentations.screens.tabs.moreTab.termsAndConditionsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.tcDescription4
import bytheour.composeapp.generated.resources.tcDescription5
import bytheour.composeapp.generated.resources.tcHeading1
import bytheour.composeapp.generated.resources.tcHeading2
import bytheour.composeapp.generated.resources.tcHeading3
import bytheour.composeapp.generated.resources.tcHeading4
import bytheour.composeapp.generated.resources.tcHeading5
import bytheour.composeapp.generated.resources.tcMainDescription
import bytheour.composeapp.generated.resources.tcMainHeading
import bytheour.composeapp.generated.resources.tcSubDescription1
import bytheour.composeapp.generated.resources.tcSubDescription2
import bytheour.composeapp.generated.resources.tcSubDescription3
import bytheour.composeapp.generated.resources.tcSubDescription3_2
import bytheour.composeapp.generated.resources.terms_Conditions
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import org.jetbrains.compose.resources.stringResource

@Composable
fun JtTermsAndConditionsScreen(onBackClick: () -> Unit) {

    var isBackArrowClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Custom Top Bar
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.terms_Conditions),
            showBackButton = true,
            isBackArrowClicked = isBackArrowClicked,
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            }
        )

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        // Content
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            JtBtoCommonText(
                text = stringResource(Res.string.tcMainHeading),
                fontSize = 14.sp,
                lineHeight = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                fontWeight = W700,
                color = Color(0xFF313131),
            )
            Spacer(modifier = Modifier.height(5.dp))

            SectionBody(stringResource(Res.string.tcMainDescription))

            SectionHeader(stringResource(Res.string.tcHeading1))
            SectionBody(stringResource(Res.string.tcSubDescription1))

            SectionHeader(stringResource(Res.string.tcHeading2))
            SectionBody(stringResource(Res.string.tcSubDescription2))

            SectionHeader(stringResource(Res.string.tcHeading3))
            SectionBody(stringResource(Res.string.tcSubDescription3))
            SectionBody(stringResource(Res.string.tcSubDescription3_2))

            SectionHeader(stringResource(Res.string.tcHeading4))
            SectionBody(stringResource(Res.string.tcDescription4))

            SectionHeader(stringResource(Res.string.tcHeading5))
            SectionBody(stringResource(Res.string.tcDescription5))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    JtBtoCommonText(
        text = title,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        fontFamily = JtBtoFonts.getOpenSansRegular(),
        fontWeight = W700,
        color = Color(0xFF301E69),
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

@Composable
fun SectionBody(text: String) {
    JtBtoCommonText(
        text = text,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        fontFamily = JtBtoFonts.getOpenSansRegular(),
        fontWeight = W400,
        color = Color(0xFF313131),
        maxLines = 10,
        textAlign = TextAlign.Left
    )
}
