package com.bytheour.presentations.screens.services

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.no_services
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_services
import bytheour.composeapp.generated.resources.voice_ico
import com.bytheour.presentations.screens.tabs.servicesTab.ServicesCard1
import com.bytheour.showProgress
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableServices(
    navigateBack: () -> Unit,
    viewModel: TrendingServicesViewModel = getKoinViewModel(),
    id: String,
    title: String,
    navigateToSelectionScreen:(String,String,String,String,Boolean,String)-> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadInitialServices(catId = id)
    }

    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var searchQuery = uiState.search ?: ""
    var showVoiceSearch by remember { mutableStateOf(false) }

    val displayServices = if (searchQuery.isNotBlank()) {
        uiState.searchServices
    } else {
        uiState.services
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 2 && lastVisibleIndex == totalItems - 2
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore) {
                    if (searchQuery.isNotBlank()) {
                        viewModel.loadMoreSearchItem()
                    } else {
                        viewModel.loadMoreServices()
                    }
                }
            }
    }

    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                JtBtoCommonText(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = JTColors.WhiteColor
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = JTColors.WhiteColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = uiState.search ?: "",
                onValueChange = { viewModel.updateSearch(it) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search_services),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.search_ico),
                        contentDescription = "",
                        tint = Color.Black
                    )
                },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                modifier = Modifier.fillMaxWidth(0.9f),
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        if ( uiState.services.isNotEmpty()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                itemsIndexed(uiState.services) { index, serviceResult ->
                    Column {
                        ServicesCard1(
                            image = serviceResult.image ?: "",
                            title = serviceResult.title ?: "",
                            desc = serviceResult.description ?: "",
                            onServiceClick = {
                                navigateToSelectionScreen(
                                    serviceResult.id ?: "",
                                    serviceResult.title ?: "",
                                    serviceResult.description ?: "",
                                    serviceResult.image ?: "",
                                    false,
                                    (serviceResult.inspectionRate?:0.0).toString()
                                )
                            },
                        )
                        if (index < displayServices.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        } else if (!showProgress.value &&  uiState.services.isEmpty()) {
            JtBtoCommonText(
                text = if (searchQuery.isNotBlank()) "No services found" else stringResource(Res.string.no_services),
                fontSize = 16.sp,
                color = JTColors.BlackButtonColor,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                textAlign = TextAlign.Center
            )
        }
    }

    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
                    searchQuery = text
                    viewModel.updateSearch(text)
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

