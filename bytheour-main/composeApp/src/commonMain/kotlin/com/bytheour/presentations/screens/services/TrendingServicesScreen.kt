package com.bytheour.presentations.screens.services

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.diagnosticService
import bytheour.composeapp.generated.resources.emergencyRoadside
import bytheour.composeapp.generated.resources.exterior
import bytheour.composeapp.generated.resources.fleetService
import bytheour.composeapp.generated.resources.interior
import bytheour.composeapp.generated.resources.maintenance
import bytheour.composeapp.generated.resources.moduleProgramming
import bytheour.composeapp.generated.resources.no_services
import bytheour.composeapp.generated.resources.no_services_found
import bytheour.composeapp.generated.resources.popular_searches
import bytheour.composeapp.generated.resources.search_for_services
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.specializedRepairs
import bytheour.composeapp.generated.resources.trending_services
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.screens.tabs.servicesTab.ServicesCard1
import com.bytheour.utils.AppFeatures
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TrendingServices(
    viewModel: TrendingServicesViewModel = getKoinViewModel(),
    navigateToServicesScreen:(String,String)-> Unit,
    navigateBack:()-> Unit,
    navigateToSelectionScreen:(String, String, String, String, Boolean, String)-> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var showVoiceSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.getTrendingServices()
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
    val tags = listOf(
        stringResource(Res.string.moduleProgramming),
        stringResource(Res.string.interior),
        stringResource(Res.string.exterior),
        stringResource(Res.string.specializedRepairs),
        stringResource(Res.string.maintenance),
        stringResource(Res.string.diagnosticService),
        stringResource(Res.string.emergencyRoadside),
        stringResource(Res.string.fleetService)
    )

    Column (
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrowleft),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navigateBack() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = uiState.search ?: "",
                onValueChange = { viewModel.updateSearchAndSearch(it) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search_for_services),
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
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
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
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.updateSearchAndSearch(
                            keyword = uiState.search?.trim() ?: ""
                        )
                    }
                ),
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

        if(
            uiState.searchServices.isNotEmpty()
            && uiState.search?.isNotEmpty() == true ){
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn (state = listState, modifier = Modifier.fillMaxHeight(0.6f)){
                    items(uiState.searchServices) { serviceResult ->
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
                    }
                }
            }
        }
        else if (uiState.search?.isNotEmpty() == true) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_services_found),
                    fontSize = 16.sp,
                    lineHeight = 13.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.trending_services),
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            fontSize = 16.sp,
            lineHeight = 16.sp,
            color = JTColors.BlackButtonColor,
            modifier = Modifier.align(Alignment.Start)
                .padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (AppFeatures.TRENDING_SERVICES_SEARCH){
            if (uiState.services.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(uiState.services) { serviceResult ->
                        TrendingServicesCard(
                            image = serviceResult.image?:"",
                            title = serviceResult.title?:"",
                            onServiceClick = {
                                navigateToServicesScreen(serviceResult.id?:"",serviceResult.title?:"")
                            },
                            id = serviceResult.id?:""
                        )
                    }
                }
            } else {
                JtBtoCommonText(
                    text = stringResource(Res.string.no_services),
                    fontSize = 16.sp,
                    color = JTColors.BlackButtonColor,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
        HorizontalDivider(color = JTColors.DividerColor, thickness = 4.dp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(5.dp))
        if (AppFeatures.POPULAR_SEARCHES){
            PopularSearchesSection(
                tags = tags,
                onTagClick = { selectedTag ->
                    viewModel.updateSearchAndSearch(selectedTag)
                }
            )
        }
    }
    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
                    searchQuery = text
                    viewModel.updateSearchAndSearch(text)
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


@Composable
fun TrendingServicesCard(
    image: String,
    title: String,
    onServiceClick: () -> Unit,
    id:String
) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .clickable { onServiceClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = image,
            contentDescription = title,
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 13.sp,
            lineHeight = 13.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopularSearchesSection(
    tags: List<String>,
    onTagClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(start = 15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(Res.string.popular_searches),
            fontSize = 18.sp,
            fontFamily = JtBtoFonts.getOpenSansBold()
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                PopularSearchChip(
                    text = tag,
                    onTagClick = onTagClick
                )
            }
        }
    }
}

@Composable
fun PopularSearchChip(
    text: String,
    onTagClick: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        modifier = Modifier.padding(top = 2.dp)
            .border(1.dp, color = JTColors.border, RoundedCornerShape(50.dp))
            .clickable { onTagClick(text) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontFamily = JtBtoFonts.getOpenSansRegular()
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = JTColors.blackColor,
                modifier = Modifier.size(16.dp).graphicsLayer { rotationZ = -50f }
            )
        }
    }
}
