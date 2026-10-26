package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.confirm_location
import bytheour.composeapp.generated.resources.fetchingAddress
import bytheour.composeapp.generated.resources.loadingAddress
import bytheour.composeapp.generated.resources.location_ico
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_text
import bytheour.composeapp.generated.resources.select_location
import bytheour.composeapp.generated.resources.voice_ico
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.presentations.customcomponents.LocationBottomSheet
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.GoogleMaps
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SheetMode
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.fetchLocationDetails
import com.bytheour.utils.getKoinViewModel
import dev.jordond.compass.geolocation.Geolocator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTSelectLocation(
    latitude: String,
    longitude: String,
    selectLocationViewModel: JTSelectLocationViewModel = getKoinViewModel(),
    googleViewmodel: JTGoogleViewmodel = getKoinViewModel(),
    navigateToGoogleSearch:(String) ->Unit,
    navigateBack:()-> Unit,
    fromService:String,
    navigateToWebView:(String,String)-> Unit,
) {

    println("fromService=========================$fromService")

    val uiState by selectLocationViewModel.uiStates.collectAsState()
    val googleLocationUiState by googleViewmodel.uiState.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }

    val lat by LatLngObject.latitude.collectAsState()
    val lng by LatLngObject.longitude.collectAsState()

    LaunchedEffect(lat, lng) {
        if (lat.isNotEmpty() && lng.isNotEmpty()) {
            googleViewmodel.updateCoordinatesToString(lat, lng)
            LatLngObject.clear() // Clear AFTER usage
        }
    }

    LaunchedEffect(uiState.isNavigate){
        if (uiState.isNavigate){
            if (fromService.equals("fromService", ignoreCase = true)){
                selectLocationViewModel.verifyAccountWithStripe()
                selectLocationViewModel.updateNavigation(false)
            }else{
                navigateBack()
                selectLocationViewModel.updateNavigation(false)
            }

        }
    }
    LaunchedEffect(googleLocationUiState) {
        googleLocationUiState.address?.let { selectLocationViewModel.updateAddress(it) }
        googleLocationUiState.city?.let { selectLocationViewModel.updateCity(it) }
        googleLocationUiState.state?.let { selectLocationViewModel.updateState(it) }
        googleLocationUiState.pinCode?.let { selectLocationViewModel.updateZipCode(it) }
        googleLocationUiState.latitude.let { selectLocationViewModel.updateLatitude(it.toString()) }
        googleLocationUiState.longitude.let { selectLocationViewModel.updateLongitude(it.toString()) }
    }

    if (uiState.webUrl != null){
        navigateToWebView(uiState.webUrl ?: "","Upload Documents")
        selectLocationViewModel.updateWebUrl()
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        if (!showBottomSheet) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 16.dp)
                        .clickable {
                            navigateBack()//---------------
                        }
                )

                Spacer(modifier = Modifier.width(10.dp))


                JtBtoCommonText(
                    text = stringResource(Res.string.select_location),
                    fontSize = 16.sp,
                    color = JTColors.chatSupport,
                    fontFamily = JtBtoFonts.getOpenSansBold()
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

        }

        CurrentLocationFetching(
            viewmodel = selectLocationViewModel,
            googleViewmodel = googleViewmodel,
            navigateToGoogleSearch =navigateToGoogleSearch,
            latitude,longitude
        )

        Spacer(modifier = Modifier.weight(1f))

        AddressCard(
            googleViewmodel = googleViewmodel
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        )
        {
            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(0.9f),
                name =
                stringResource(Res.string.confirm_location),
                onClick = {

                    showBottomSheet = true

                },
                buttonColors = JTColors.enableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White
            )
            Spacer(modifier = Modifier.height(22.dp))

        }


    }
    if (showBottomSheet) {
        LocationBottomSheet(
            onDismiss = { showBottomSheet = false },
            selectLocationViewModel = selectLocationViewModel ,
            onClick = { selectedType->
                selectLocationViewModel.addAddress(
                    house ="" ,
                    name =selectedType ,
                    addressLine = uiState.address ?: googleLocationUiState.address.orEmpty(),
                    city = uiState.city ?: googleLocationUiState.city.orEmpty(),
                    state = uiState.state ?: googleLocationUiState.state.orEmpty(),
                    zipCode = uiState.zipCode ?: googleLocationUiState.pinCode.orEmpty(),
                    longitude = uiState.longitude ?: googleLocationUiState.longitude.toString(),
                    latitude = uiState.latitude ?: googleLocationUiState.latitude.toString(),
                    id = uiState.message,
                )
                showBottomSheet = false
            },
            mode = SheetMode.ADD,
            uiStates =uiState
        )
    }
    if (uiState.message.isNotEmpty()){
        CommonSnackbar(
            message = uiState.message,
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 1500,
            onDismiss = {
            }
        )
    }


}

@Composable
fun CurrentLocationFetching(
    viewmodel: JTSelectLocationViewModel,
    googleViewmodel: JTGoogleViewmodel,
    navigateToGoogleSearch: (String) -> Unit,
    latitude: String,
    longitude: String
) {

    val uiState by viewmodel.uiStates.collectAsState()
    val googleUiState by googleViewmodel.uiState.collectAsState()

    // Decide which coordinates should be shown
    val latToShow = if (googleUiState.latitude == 0.0) latitude else googleUiState.latitude.toString()
    val lngToShow = if (googleUiState.longitude == 0.0) longitude else googleUiState.longitude.toString()
    var hasInitialLocationChanged by remember { mutableStateOf(false) }
    LaunchedEffect(latToShow, lngToShow) {
        if (latToShow.isNotEmpty() && lngToShow.isNotEmpty()) {
            googleViewmodel.updateCoordinates(
                latToShow.toDoubleOrNull() ?: 0.0,
                lngToShow.toDoubleOrNull() ?: 0.0
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight(0.78f)
            .fillMaxWidth()
    ) {
        key(latToShow to lngToShow) {
            GoogleMaps(
                latitude = latToShow,
                longitude = lngToShow,
                onLocationChange = { lat, lng ->
                    if (!hasInitialLocationChanged) {
                        // Ignore first callback from map initialization
                        hasInitialLocationChanged = true
                    } else {
                        println("User moved marker: Latitude = $lat, Longitude = $lng")
                        googleViewmodel.updateCoordinates(
                            lat.toDoubleOrNull() ?: 0.0,
                            lng.toDoubleOrNull() ?: 0.0
                        )
                    }
                },
                onMarkerClick = { _, _ -> }
            )
        }


        JTSearchTextField(
            value = uiState.search ?: "",
            onValueChange = { viewmodel.updateSearch(it) },
            placeholder = stringResource(Res.string.search_text),
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .offset(y = 30.dp)
                .height(53.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(48.dp),
                    spotColor = JTColors.grayTextColor
                )
                .clip(RoundedCornerShape(48.dp))
                .background(color = JTColors.WhiteColor)
                .clickable {
                    navigateToGoogleSearch(uiState.search?:"")
                },

            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.search_ico),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(Res.drawable.voice_ico),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navigateToGoogleSearch(uiState.search?:"")
                        }
                )
            },
        )
    }

}

@Composable
fun AddressCard(
    googleViewmodel :JTGoogleViewmodel,
    coordinates: Pair<Double, Double>? = null
) {
    val googleLocationUiState by googleViewmodel.uiState.collectAsState()

    LaunchedEffect(coordinates) {
        coordinates?.let { (lat, lng) ->
            googleViewmodel.updateCoordinates(lat, lng)
        }
    }

    LaunchedEffect(Unit) {
        if (coordinates == null && googleLocationUiState.latitude == 0.0 && googleLocationUiState.longitude == 0.0) {
            fetchLocationDetails(
                geoLocator = Geolocator(),
                onSuccess = { flatNo, address, landmark, pinCode, country,latitude,longitude  ->
                    googleViewmodel.updateFullAddress(flatNo, address, landmark, pinCode, country,latitude,longitude)
                },
                onFailure = { message ->
                    println("Message: $message")
                }
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(elevation = 4.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = JTColors.otpBox),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(Res.drawable.location_ico),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                JtBtoCommonText(
                    text = googleLocationUiState.landmark ?: stringResource(Res.string.loadingAddress),
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.locationType
                )

                val addressText = if (googleLocationUiState.isLocationFetch) {
                    stringResource(Res.string.fetchingAddress)

                } else {
                    listOfNotNull(
                        googleLocationUiState.flatNo,
                        googleLocationUiState.address,
                        googleLocationUiState.landmark,
                        googleLocationUiState.country,
                        googleLocationUiState.pinCode
                    ).takeIf { it.isNotEmpty() }?.joinToString(", ")
                }

                if (!googleLocationUiState.isLocationFetch && !addressText.isNullOrBlank()) {
                    JtBtoCommonText(
                        text = addressText,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        maxLines = 1,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = JTColors.addressText,
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

