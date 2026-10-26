package com.bytheour.presentations.screens.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.current_Location
import bytheour.composeapp.generated.resources.delivery_service
import bytheour.composeapp.generated.resources.fetching
import bytheour.composeapp.generated.resources.fetching_your_location
import com.bytheour.device
import com.bytheour.getDeviceToken
import com.bytheour.osName
import com.bytheour.platform
import com.bytheour.utils.JTColors
import com.bytheour.sdkVersionName
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.LocationAnimation
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.fetchLocationDetails
import com.bytheour.utils.getKoinViewModel
import com.russhwolf.settings.Settings
import dev.jordond.compass.geolocation.Geolocator
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource


@Composable
fun JTLocationFetchingScreen(
    viewModel: JTLocationFetchingViewModel = getKoinViewModel(),
    navigateToMainScreen: () -> Unit,
//    isAdmin:Boolean
) {
    LaunchedEffect(Unit){
            getDeviceToken {
                viewModel.deviceToken(
                    device = device,
                    token = it?:"",
                    type =  if(platform.equals("Android", ignoreCase = true)){
                        "android"
                    }else{
                        "ios"
                    },
                    os = osName,
                    appVersion = sdkVersionName,
                    version = 1


                )
            }
        delay(5000)
        navigateToMainScreen()
    }

    val uiState by viewModel.uiState.collectAsState()
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LocationAnimation(uiState = uiState)

            Box(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 130.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (uiState.isLocationFetch) {
                        JtBtoCommonText(
                            text = if (sharedPreferenceUseCase.getUser() == TYPE_CUSTOMER) stringResource(
                                Res.string.delivery_service
                            ) else stringResource(Res.string.current_Location),
                            fontSize = 18.sp,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            color = JTColors.black,
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        MinimalLocationDisplay(viewModel = viewModel)
                    } else {
                        JtBtoCommonText(
                            text = stringResource(Res.string.fetching),
                            fontSize = 18.sp,
                            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                            color = JTColors.black,
                        )

                    }
                }
            }
        }
}

@Composable
fun MinimalLocationDisplay(viewModel: JTLocationFetchingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    LaunchedEffect(Unit) {
        fetchLocationDetails(
            geoLocator = Geolocator(),
            onSuccess = { flatNo, address, landmark, pinCode,country,latitude,longitude  ->
                viewModel.updateFullAddress(flatNo, address, landmark, pinCode,country,latitude,longitude)
                sharedPreferenceUseCase.putLocation(latitude?:0.0, longitude?:0.0)
            },
            onFailure = { message ->
                println("Message: $message")
            }
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        JtBtoCommonText(
            text = uiState.address ?: stringResource(Res.string.fetching_your_location),
            fontSize = 15.sp,
            fontFamily = JtBtoFonts.getOpenSansBold(),
            color = JTColors.black
        )

        Spacer(modifier = Modifier.height(4.dp))

        JtBtoCommonText(
            text = uiState.landmark ?: "",
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            color = JTColors.lightBlack,
            modifier = Modifier
                .fillMaxWidth(),
            overflow = TextOverflow.Ellipsis
        )

        JtBtoCommonText(
            text = uiState.country ?: "",
            fontSize = 14.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            color = JTColors.lightBlack,
            modifier = Modifier
                .fillMaxWidth(),
            lineHeight = 18.sp,
            softWrap = true,
            overflow = TextOverflow.Ellipsis
        )
    }
}




