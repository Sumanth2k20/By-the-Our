package com.bytheour.presentations.screens.tabs.moreTab.documentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageFullScreen(
    image:String,
    onBackPress:()->Unit,
    description:String,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = description,
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPress()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(JTColors.blackColor)
            )
        }
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .background(Color.Black), // Background to match screenshot
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = image,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )
    }
    }
}
