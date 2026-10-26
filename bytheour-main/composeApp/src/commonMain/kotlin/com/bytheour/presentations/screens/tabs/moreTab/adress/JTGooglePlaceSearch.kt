package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_text
import bytheour.composeapp.generated.resources.voice_ico
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.API_KEY
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTGooglePlaceSearch(
    addressListViewmodel: JTAddressListViewModel = getKoinViewModel(),
    searchText: String,
    navigateBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val textFieldFocusState = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val uiState by addressListViewmodel.uiStates.collectAsState()
    var showVoiceSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(searchText) }

    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )

    LaunchedEffect(expanded) {
        if (expanded) {
            textFieldFocusState.requestFocus()
            keyboardController?.show()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.selectedLocation?.navigateToMark == true) {
            LatLngObject.update(
                uiState.selectedLocation?.latitude.toString(),
                uiState.selectedLocation?.longitude.toString()
            )
            navigateBack()
        }
    }

    val places = addressListViewmodel.places
    LaunchedEffect(places.size) {
        if (places.isNotEmpty() && searchQuery.length >= 4) {
            expanded = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back arrow icon
            Icon(
                painter = painterResource(Res.drawable.arrowleft),
                contentDescription = "Back",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        navigateBack()
                    },
                tint = Color.Black
            )

            // Custom Dropdown Box
            Box(
                modifier = Modifier.weight(1f)
            ) {
                JTSearchTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query

                        scope.launch {
                            delay(50)
                            textFieldFocusState.requestFocus()
                            keyboardController?.show()
                        }

                        if (query.isEmpty()) {
                            addressListViewmodel.clearPredictions()
                            expanded = false
                        } else if (query.length >= 4) {
                            scope.launch {
                                addressListViewmodel.fetchPlacePredictions(query, API_KEY)
                                // Wait for the state to update
                                delay(300)
                                expanded = addressListViewmodel.places.isNotEmpty()
                            }
                        } else {
                            expanded = false
                            addressListViewmodel.clearPredictions()
                        }
                    },
                    placeholder = stringResource(Res.string.search_text),
                    modifier = Modifier
                        .focusRequester(textFieldFocusState)
                        .fillMaxWidth()
                        .height(53.dp)
                        .padding(horizontal = 15.dp)
                        .background(color = JTColors.WhiteColor)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                keyboardController?.show()
                                if (searchQuery.length >= 4 && addressListViewmodel.places.isNotEmpty()) {
                                    expanded = true
                                }
                            }
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
                            modifier = Modifier.size(20.dp)
                                .clickable {
                                    permission()
                                }
                        )
                    },
                )

                    DropdownMenu(
                        expanded = expanded && places.isNotEmpty(),
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(color = Color.White)
                            .width(400.dp)

                    ) {
                        places.forEach { prediction ->
                            DropdownMenuItem(
                                text = {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        JtBtoCommonText(
                                            text = prediction.mainText,
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        JtBtoCommonText(
                                            text = prediction.secondaryText,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                },
                                onClick = {
                                    searchQuery = prediction.description
                                    expanded = false
                                    scope.launch {
                                        addressListViewmodel.fetchPlaceDetails(
                                            prediction.placeId,
                                            API_KEY
                                        )
                                        addressListViewmodel.clearPredictions()
                                    }
                                    keyboardController?.hide()
                                },
                                modifier = Modifier
                                    .background(color = Color.White)
                                    .fillMaxWidth()
                            )
                            if (prediction != places.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Color.LightGray,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }

            }
        }

        if (showVoiceSearch) {
            VoiceSearch(
                onResult = { recognizedText ->
                    recognizedText?.let { text ->
                        searchQuery = text
                        addressListViewmodel.updateSearch(text)
                        if (text.length >= 3) {
                            addressListViewmodel.fetchPlacePredictions(text, API_KEY)
                        }
                    }
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
    }
}
