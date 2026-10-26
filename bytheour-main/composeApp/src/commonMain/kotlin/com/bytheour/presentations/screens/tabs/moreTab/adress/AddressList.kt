package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.add_address
import bytheour.composeapp.generated.resources.addaddress_ico
import bytheour.composeapp.generated.resources.addresses
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.continues
import bytheour.composeapp.generated.resources.current_location
import bytheour.composeapp.generated.resources.currentlocation_ico
import bytheour.composeapp.generated.resources.default
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.deleteText
import bytheour.composeapp.generated.resources.deleteTitle
import bytheour.composeapp.generated.resources.edit
import bytheour.composeapp.generated.resources.home_ico
import bytheour.composeapp.generated.resources.location_ico
import bytheour.composeapp.generated.resources.more_ico
import bytheour.composeapp.generated.resources.no_address
import bytheour.composeapp.generated.resources.not_serviceable
import bytheour.composeapp.generated.resources.saved_add
import bytheour.composeapp.generated.resources.select_address
import bytheour.composeapp.generated.resources.work_ico
import com.bytheour.presentations.customcomponents.LocationBottomSheet
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.location.JTLocationFetchingViewModel
import com.bytheour.utils.AlertButton
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTActionAlertBox
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.LocationType
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.SheetMode
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.fetchLocationDetails
import com.bytheour.utils.getKoinViewModel
import dev.jordond.compass.geolocation.Geolocator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTAddressList(
    addressListViewmodel: JTAddressListViewModel = getKoinViewModel(),
    viewModel: JTLocationFetchingViewModel = getKoinViewModel(),
    selectLocationViewModel: JTSelectLocationViewModel = getKoinViewModel(),
    fromHomeScreen: Boolean? = null,
    navigateToSelectLocation :(String, String) ->Unit,
    navigateToMoreTab :() ->Unit,
    navigateToTrendingServices :() ->Unit
) {
    val uiState by addressListViewmodel.uiStates.collectAsState()
    var showVoiceSearch by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isBackArrowClicked by remember { mutableStateOf(false) }

    val locationUiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        addressListViewmodel.loadInitialAddressList(PAGINATION_LIMIT.toString())
        fetchLocationDetails(
            geoLocator = Geolocator(),
            onSuccess = { flatNo, address, landmark, pinCode,country,latitude,longitude ->
                viewModel.updateFullAddress(flatNo, address, landmark, pinCode,country,latitude,longitude)
            },
            onFailure = { message ->
                println("Message: $message")
            }
        )
    }

    LaunchedEffect(listState, uiState.isLoadingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val totalItems = listState.layoutInfo.totalItemsCount
                val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0
                if (totalItems > 0 && lastVisibleItemIndex >= totalItems - 3 && !uiState.isLoadingMore) {
                    addressListViewmodel.loadMoreAddresses(PAGINATION_LIMIT.toString())
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navigateToMoreTab()
                    isBackArrowClicked = true
                },
                enabled = !isBackArrowClicked
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            JtBtoCommonText(
                text = stringResource(
                    if (fromHomeScreen == true) Res.string.select_address else Res.string.addresses
                ),
                fontSize = 16.sp,
                color = JTColors.chatSupport,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))

        Spacer(modifier = Modifier.height(22.dp))

        Column(verticalArrangement = Arrangement.Center) {

                CurrentLocation(
                    locationUiState,
                    navigateToSelectLocation = {lat,long-> navigateToSelectLocation(lat,long) }
                )

            Spacer(modifier = Modifier.height(22.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 4.dp, color = JTColors.otpBox
            )
            Spacer(modifier = Modifier.height(22.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.saved_add),
                fontSize = 12.sp,
                color = JTColors.addressText,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 16.dp)

            )


            Spacer(modifier = Modifier.height(6.dp))
            if (uiState.addressList.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    items(uiState.addressList) { addressResult ->
                        AddressList(
                            locationType = addressResult.locationType,
                            fullAddress = addressResult.fullAddress,
                            defaultAddress = addressResult.defaultAddress,
                            notServiceable = addressResult.notServiceable,
                            selectLocationViewModel = selectLocationViewModel,
                            viewModel = viewModel,
                            onAddressClick = {
                                addressListViewmodel.defaultAddress(id = addressResult.id ?: "")
                            },
                            onDeleted = {
                                addressListViewmodel.deleteAddress(addressResult.id ?: "")
                            },
                            addressResult = addressResult
                        )
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 4.dp, color = JTColors.otpBox
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clickable {
                                    navigateToSelectLocation(locationUiState.latitude.toString(), locationUiState.longitude.toString())
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.addaddress_ico),
                                contentDescription = "",
                                modifier = Modifier.size(32.dp),
                            )
                            JtBtoCommonText(
                                text = stringResource(Res.string.add_address),
                                fontSize = 12.sp,
                                color = JTColors.enableButton,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                                modifier = Modifier,
                                textAlign = TextAlign.Center

                            )

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp), color = JTColors.otpBox
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (fromHomeScreen == true) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = { navigateToTrendingServices() },
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.continues)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    JtBtoCommonText(
                        text = stringResource(Res.string.no_address),
                        fontSize = 16.sp,
                        color = JTColors.BlackButtonColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        textAlign = TextAlign.Center
                    )
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 4.dp, color = JTColors.otpBox
            )

            if(uiState.addressList.isEmpty()){
                Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable {
                        navigateToSelectLocation(locationUiState.latitude.toString(), locationUiState.longitude.toString())
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.addaddress_ico),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp),
                )
                JtBtoCommonText(
                    text = stringResource(Res.string.add_address),
                    fontSize = 12.sp,
                    color = JTColors.enableButton,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    modifier = Modifier,
                    textAlign = TextAlign.Center

                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp), color = JTColors.otpBox
            )
            Spacer(modifier = Modifier.height(8.dp))

            }

        }
    }
    if (showVoiceSearch) {
        VoiceSearch (
            onResult = {
                showVoiceSearch = false
            },
            onComplete = {
                showVoiceSearch = false
            },
            isListening = {
                showVoiceSearch = it
            }
        )
    }
    if (uiState.message.isNotEmpty()){
        CommonSnackbar(
            message = uiState.message,
            type = SnackbarType.SUCCESS,
            autoDismissMillis = 1500,
            onDismiss = {
                addressListViewmodel.clearMessage()
            }
        )
    }
}

@Composable
fun AddressList(
    addressListViewmodel: JTAddressListViewModel = getKoinViewModel(),
    locationType: String? = null,
    fullAddress:String? = null,
    defaultAddress: Boolean? = null,
    notServiceable: Boolean? = false,
    selectLocationViewModel: JTSelectLocationViewModel,
    viewModel :JTLocationFetchingViewModel,
    addressResult:JTAddressListViewModel.UIStates,
    onAddressClick:()-> Unit,
    onDeleted: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val imageTint = if (notServiceable == true) {
        JTColors.serviceableBox
    } else {
        Color.Unspecified
    }
    val actionAlertBox = remember { JTActionAlertBox() }
    val text = stringResource(Res.string.deleteText)
    val title = stringResource(Res.string.deleteTitle)
    val delete = stringResource(Res.string.delete)
    val cancel = stringResource(Res.string.cancel)
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(color = imageTint),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (locationType) {
                LocationType.WORK.type -> {
                    Image(
                        painter = painterResource(Res.drawable.work_ico),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                LocationType.HOME.type -> {
                    Image(
                        painter = painterResource(Res.drawable.home_ico),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(Res.drawable.location_ico),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.padding(top = 10.dp,
                bottom =10.dp )
            ) {
                Row {
                    (if (!locationType.isNullOrEmpty()) {
                        locationType
                    } else {
                        addressResult.state
                    })?.let {
                        JtBtoCommonText(
                            text = it,
                            fontSize = if (defaultAddress == true) {
                                13.sp
                            }else{
                                12.sp
                            },
                            color = if (defaultAddress == true) JTColors.enableButton else JTColors.blackColor,
                            lineHeight = 16.sp,
                            fontFamily = JtBtoFonts.getOpenSansBold()
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    JtBtoCommonText(
                        text = if (defaultAddress == true) {
                            stringResource(Res.string.default)
                        } else {
                            ""
                        },
                        fontSize = 13.sp,
                        color = JTColors.enableButton,
                        fontFamily = JtBtoFonts.getOpenSansBold()
                    )

                }
                Row {
                    JtBtoCommonText(
                        text = "$fullAddress",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        maxLines = 5,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = JTColors.addressText,
                        modifier = Modifier.fillMaxWidth(0.9f)
                            .clickable {
                                onAddressClick()
                            }
                            .weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(color = Color.White))
                    {
                        Image(
                            painter = painterResource(Res.drawable.more_ico),
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                showMenu = true

                            }.size(20.dp)
                        )
                        DropdownMenu(
                            expanded = showMenu ,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(Res.string.edit),
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        fontSize = 12.sp,
                                        color = JTColors.dropDownText,
                                        textAlign = TextAlign.Start,
                                        fontFamily = JtBtoFonts.getOpenSansSemiBold()
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    selectLocationViewModel.updateAddress(if (!addressResult.landmark.isNullOrEmpty()) {
                                        addressResult.landmark
                                    } else {
                                        addressResult.name ?: ""
                                    },)
                                    selectLocationViewModel.updateState(addressResult.state?:"")
                                    selectLocationViewModel.updateCity(addressResult.city?:"")
                                    selectLocationViewModel.updateZipCode(addressResult.zip?:"")
                                    selectLocationViewModel.updateLatitude(addressResult.lat?:"")
                                    println("lat -------------------------${addressResult.lat}")
                                    selectLocationViewModel.updateLongitude(addressResult.lag?:"")
                                    println("long -------------------------${addressResult.lag}")

                                    showBottomSheet =true
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(Res.string.delete),
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        fontSize = 12.sp,
                                        color = JTColors.dropDownText,
                                        textAlign = TextAlign.Start,
                                        fontFamily = JtBtoFonts.getOpenSansSemiBold()
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    actionAlertBox.show(
                                        title = title,
                                        text = text,
                                        buttons = listOf(
                                            AlertButton(cancel, JTColors.blackColor),
                                            AlertButton(delete, JTColors.BackGroundBlueColor)
                                        ),
                                        onOptionSelected = { index ->
                                            when(index){
                                                0 ->{}
                                                1 -> { onDeleted() }
                                            }
                                        }
                                    )

                                },
                            )

                        }
                    }
                }



                if(notServiceable == true){
                    JtBtoCommonText(
                        text = stringResource(Res.string.not_serviceable),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        color = JTColors.serviceableText,
                        modifier = Modifier.fillMaxWidth(0.9f)
                            .clickable {
                                showMenu =true
                            },
                        textAlign = TextAlign.Start
                    )
                }


            }



            actionAlertBox.JTAlertContent()


        }

        Spacer(modifier = Modifier.height(7.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = JTColors.otpBox)


    }
    if (showBottomSheet) {
        LocationBottomSheet(
            onDismiss = { showBottomSheet = false },
            selectLocationViewModel = selectLocationViewModel,
            onClick = { selectedType->
                val state = selectLocationViewModel.uiStates.value

                selectLocationViewModel.addAddress(
                    house = "",
                    name = selectedType,
                    addressLine = state.address ?: addressResult.landmark.orEmpty(), // prefer updated value
                    city = state.city ?: addressResult.city.orEmpty(),
                    state = state.state ?: addressResult.state.orEmpty(),
                    zipCode = state.zipCode ?: addressResult.zip.orEmpty(),
                    longitude = state.longitude ?: addressResult.lag.orEmpty(),
                    latitude = state.latitude ?: addressResult.lat.orEmpty(),
                    id = addressResult.id.orEmpty(),
                    onSuccessful = {
                        showBottomSheet = false
                        addressListViewmodel.loadInitialAddressList(PAGINATION_LIMIT.toString())
                    }
                )
            },
            mode = SheetMode.EDIT,
            address = addressResult,
        )
    }}


@Composable
fun CurrentLocation(
    locationUiState: JTLocationFetchingViewModel.LocationUiSate,
    navigateToSelectLocation: (String, String) -> Unit
) {

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navigateToSelectLocation(locationUiState.latitude.toString(), locationUiState.longitude.toString())
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.currentlocation_ico),
                contentDescription = "",
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))

            Column {
                JtBtoCommonText(
                    text = stringResource(Res.string.current_location),
                    fontSize = 12.sp,
                    color = JTColors.enableButton,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    modifier = Modifier
                        .clickable {
                            navigateToSelectLocation(locationUiState.latitude.toString(), locationUiState.longitude.toString())
                        },
                    textAlign = TextAlign.Center,

                    )
                JtBtoCommonText(
                    text = listOf(
                        locationUiState.flatNo,
                        locationUiState.address,
                        locationUiState.landmark,
                        locationUiState.country,
                        locationUiState.pinCode
                    ).filter { !it.isNullOrBlank() }
                        .joinToString(","),
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    maxLines = 5,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    color = JTColors.addressText,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable {
                            navigateToSelectLocation(locationUiState.latitude.toString(), locationUiState.longitude.toString())
                        },
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp), color = JTColors.otpBox
        )
    }
}