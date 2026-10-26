package com.bytheour.utils


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.map_bg
import bytheour.composeapp.generated.resources.vector
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bytheour.R
import com.bytheour.presentations.screens.location.JTLocationFetchingViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun LocationAnimation(uiState: JTLocationFetchingViewModel.LocationUiSate) {


    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.location)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.6f
    )


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.paint(
            painter = painterResource(Res.drawable.map_bg),
            contentScale = ContentScale.FillBounds
        )
            .fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },

            )
        Icon(painter = painterResource(Res.drawable.vector),
            contentDescription = "")
    }

}

@Composable
actual fun SearchAnimation(modifier: Modifier) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.searching_provider)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(55.dp)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}