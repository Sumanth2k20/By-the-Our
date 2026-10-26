package com.bytheour.presentations.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.later
import bytheour.composeapp.generated.resources.update
import bytheour.composeapp.generated.resources.update_available
import bytheour.composeapp.generated.resources.update_available_message
import com.bytheour.utils.AlertButton
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTActionAlertBox
import com.bytheour.utils.JTColors
import com.bytheour.utils.JTVideoPlayer
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getAppVersion
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.openPlayStore
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTSplashScreen(
    navigateToWellComeScreen:()-> Unit,
    navigateToLocationScreen:()->Unit,
    splashViewmodel:JTSplashViewmodel = getKoinViewModel(),
){
    val uiState by splashViewmodel.uiState.collectAsState()
    val actionAlertBox = remember { JTActionAlertBox() }
    val currentAppVersion = getAppVersion()
    println("currentAppVersion is $currentAppVersion")
    val text = stringResource(Res.string.update_available_message)
    val title = stringResource(Res.string.update_available)
    val later = stringResource(Res.string.later)
    val update = stringResource(Res.string.update)


    LaunchedEffect(uiState.destination, uiState.showUpdateDialog) {
        if (uiState.showUpdateDialog) return@LaunchedEffect

        when (uiState.destination) {
            JTSplashViewmodel.SplashDestination.LOCATION -> navigateToLocationScreen()
            JTSplashViewmodel.SplashDestination.WELCOME -> navigateToWellComeScreen()
            else -> {}
        }
    }

    LaunchedEffect(Unit){
        delay(2000)
        splashViewmodel.getAppStatus()
        splashViewmodel.appVersion(currentAppVersion)
    }


//    LaunchedEffect(Unit) {
//        delay(2000)
//        throw RuntimeException("🔥 Forced Crash from Splash Screen Test")
//    }

    uiState.openStoreUrl?.let { url ->
        LaunchedEffect(url) {
            openPlayStore(appPackageName = url)
            splashViewmodel.resetOpenStoreUrl()
        }
    }

    println("  ========================= ${uiState.showUpdateDialog}")
    if (uiState.showUpdateDialog){
        actionAlertBox.show(
            title = title,
            text = text,
            buttons = listOf(
                AlertButton(later, JTColors.blackColor),
                AlertButton(update, JTColors.BackGroundBlueColor)
            ),
            onOptionSelected = { index ->
                when (index) {
                    0 -> splashViewmodel.onCancelUpdate()
                    1 -> splashViewmodel.onUpdateClick()
                }
            }

        )
    }
    Box(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier
            .fillMaxSize()
            .background(color = JTColors.BackGroundBlueColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            JTVideoPlayer(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f))
        }
        uiState.errorMessage?.let { message ->
            CommonSnackbar(
                message = message,
                type = SnackbarType.ERROR,
                onDismiss = {
                    splashViewmodel.clearError()
                }
            )
        }
    }


    actionAlertBox.JTAlertContent()
}