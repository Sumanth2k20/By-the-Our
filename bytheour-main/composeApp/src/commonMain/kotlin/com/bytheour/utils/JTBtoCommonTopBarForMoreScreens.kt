package com.bytheour.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JtBtoCommonTopAppBarForMoreScreens(
    title: String,
    showBackButton: Boolean = true,
    isBackArrowClicked: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(Color.White),
        title = {
            JtBtoCommonText(
                text = title,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                fontSize = 15.sp,
                fontWeight = W700,
                lineHeight = 12.sp
            )
        },
        navigationIcon = {
            if (showBackButton) {
                Icon(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(enabled = !isBackArrowClicked) { onBackClick() }
                        .padding(start = 6.dp, end = 8.dp)
                        .size(19.dp)
                )
            }
        },
    )
}

