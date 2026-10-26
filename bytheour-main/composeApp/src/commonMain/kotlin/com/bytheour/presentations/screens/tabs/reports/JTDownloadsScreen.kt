package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.android_ico
import bytheour.composeapp.generated.resources.downloads
import bytheour.composeapp.generated.resources.ios_ico
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JtAdminDownloadsScreen(
    viewModel: JtDownLoadsScreenViewModel = getKoinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState  by  viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    JtBtoCommonText(
                        stringResource(Res.string.downloads),
                    fontWeight = FontWeight.Bold)
                        },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.White)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFFAFAFC))
                    .padding(16.dp)
            ) {
                DownloadCard(
                    icon = painterResource(Res.drawable.ios_ico),
                    platform = "IOS",
                    downloads = uiState.iosDownload?:0
                )

                Spacer(modifier = Modifier.height(16.dp))

                DownloadCard(
                    icon = painterResource(Res.drawable.android_ico),
                    platform = "Android",
                    downloads = uiState.androidDownload?:0
                )
            }
        }
    )
}

@Composable
fun DownloadCard(
    icon: Painter,
    platform: String,
    downloads: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF0F0F0), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = icon,
                        contentDescription = "$platform Icon",
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    JtBtoCommonText(
                        text = platform,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 18.sp
                    )
                    JtBtoCommonText(
                        text = "Downloads",
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            JtBtoCommonText(
                text = downloads.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }
}


