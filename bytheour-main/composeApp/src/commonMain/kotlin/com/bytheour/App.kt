package com.bytheour

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bytheour.presentations.navigations.JTBtoNavGraph
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PlatformActivityContext
import org.jetbrains.compose.ui.tooling.preview.Preview

var showProgress = mutableStateOf(false)
@Composable
@Preview
fun App(
    mainActivity: PlatformActivityContext?,
    type:String? = null
) {
    MaterialTheme {
        JTBtoNavGraph(
            mainActivity = mainActivity,
            type = type ?: "",
        )
        AnimatedVisibility(
            visible = showProgress.value,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Dim background
                    .pointerInput(Unit) {
                        // Block all touch input to prevent background clicks
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(JTColors.enableButton)
                        .shadow(elevation = 0.dp, shape = RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = Color.White.copy(alpha = 0.6f),
                                strokeWidth = 4.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LoadingText()
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingText() {
    var dotCount by remember { mutableStateOf(0) }

    // Loop the dots every 500ms
    LaunchedEffect(Unit) {
        while (true) {
            dotCount = (dotCount + 1) % 4 // cycles between 0,1,2,3
            kotlinx.coroutines.delay(500)
        }
    }

    Text(
        text = "Loading" + ".".repeat(dotCount),
        fontSize = 14.sp,
        color = Color.White,
        fontFamily = JtBtoFonts.getOpenSansSemiBold()
    )
}
