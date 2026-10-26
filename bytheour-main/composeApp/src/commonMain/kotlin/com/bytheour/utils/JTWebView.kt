package com.bytheour.utils

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.accept
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.decline
import bytheour.composeapp.generated.resources.either_url
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTWebViewScreen(
    title: String? = null,
    url: String? = null,
    htmlFileName: String? = null,
    onBackPress: () -> Unit,
    onAcceptClick:() -> Unit = {},
    onDeclineClick:() -> Unit = {},
    showCrossButton: Boolean = false,
    showDownBox: Boolean = true,
    showSkipButton: Boolean = false,
    onSkipClick: () -> Unit = {}
) {
    val webViewState =
        when {
            htmlFileName != null -> rememberWebViewStateWithHTMLFile(htmlFileName)
            url != null -> rememberWebViewState(url)
            else -> error(stringResource(Res.string.either_url))
        }

    val webViewNavigator = rememberWebViewNavigator()

    // Web settings
    webViewState.webSettings.apply {
        zoomLevel = 1.0
        isJavaScriptEnabled = true
        logSeverity = KLogSeverity.Debug
        allowFileAccessFromFileURLs = true
        allowUniversalAccessFromFileURLs = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
            allowFileAccess = true
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .height(60.dp)
                .background(JTColors.WhiteColor)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                if (showCrossButton){
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.clickable { onBackPress() }
                    )
                }else {
                    Icon(
                        painter = painterResource(Res.drawable.arrowleft),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.clickable { onBackPress() }
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                JtBtoCommonText(
                    text = title ?: "",
                    fontSize = 16.sp,
                    color = JTColors.BlackButtonColor,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    modifier = Modifier.padding(start = 12.dp)
                )
                if (showSkipButton){
                    Spacer(modifier = Modifier.weight(1f))
                    JtBtoCommonText(
                        text = "Skip",
                        fontSize = 16.sp,
                        color = JTColors.BlackButtonColor,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        modifier = Modifier.padding(16.dp).clickable { onSkipClick() }
                    )
                }

            }
        }


        // WebView + Bottom Bar
        Box(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize()) {
                WebView(
                    state = webViewState,
                    navigator = webViewNavigator,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
//                    captureBackPresses = true
                )


                if(showDownBox) {
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .height(70.dp)
                                .fillMaxWidth()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                JtBtoCommonText(
                                    text = stringResource(Res.string.decline),
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 15.sp,
                                    color = JTColors.enableButton,
                                    modifier = Modifier.padding(start = 15.dp)
                                        .clickable { onDeclineClick() }
                                )

                                JtBtoCommonText(
                                    text = stringResource(Res.string.accept),
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    fontSize = 15.sp,
                                    color = JTColors.enableButton,
                                    modifier = Modifier.padding(end = 15.dp)
                                        .clickable { onAcceptClick() }
                                )

                            }
                        }

                    }
                }

            }
        }
    }
}
