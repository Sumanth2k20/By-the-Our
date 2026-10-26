package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.no_services
import bytheour.composeapp.generated.resources.no_services_found
import bytheour.composeapp.generated.resources.requests
import bytheour.composeapp.generated.resources.search_users
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.showProgress
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectServicesScreen(
    viewModel: ProviderSelectServiceScreenViewModel = getKoinViewModel(),
    serviceViewModel: ServiceRequestScreenViewModel = getKoinViewModel(),
    id: String,
    title :String,
    navigateBack: () -> Unit,
    navigateToAddServiceScreen:(
        navigationValue:String,
            String,
            String,
            String,
            String,
            String,
            String
            )-> Unit
){
    LaunchedEffect(Unit) {
        viewModel.loadInitialServices(catId = id)
    }

    val uiState by viewModel.uiState.collectAsState()
    val serviceState by serviceViewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val searchListState = rememberLazyListState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showVoiceSearch by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex == totalItems - 2
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !uiState.isSearchMode) {
                    viewModel.loadMoreServices()
                }
            }
    }

    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
        onPermissionGranted = {
            showVoiceSearch = true
        }
    )
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.background(JTColors.enableButton)) {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = title,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                                fontSize = 16.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navigateBack()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = JTColors.enableButton,
                            titleContentColor = Color.White
                        )
                    )

                    JTSearchTextField(
                        value = uiState.search ?: "",
                        placeholder = stringResource(Res.string.search_users),
                        onValueChange = { viewModel.updateSearch(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.padding(10.dp)
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
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterFont(),
                                fontSize = 14.sp,
                                color = JTColors.enableButton
                            )
                        ) {
                            append("Total Services - ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterBoldFont(),
                                fontSize = 14.sp,
                                color = JTColors.enableButton
                            )
                        ) {
                            append("${uiState.count}")
                        }
                    }
                )


                Button(
                    onClick = {
                        showBottomSheet = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JTColors.enableButton
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = JTColors.iosWhite
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(Res.string.requests),
                        color = JTColors.iosWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            if (uiState.isSearchMode && uiState.searchServices.isNotEmpty()) {
                LazyColumn(
                    state = searchListState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    itemsIndexed(uiState.searchServices) { index, serviceResult ->
                        ServicesCard1(
                            image = serviceResult.image ?: "",
                            title = serviceResult.title ?: "",
                            desc = serviceResult.description ?: "",
                            onServiceClick = {
                                navigateToAddServiceScreen(
                                    "1",
                                    serviceResult.id ?: "",
                                    serviceResult.title ?: "",
                                    serviceResult.description ?: "",
                                    serviceResult.image ?: "",
                                    serviceResult.catId ?: "",
                                    serviceResult.iD ?: ""
                                )
                            },
                        )
                    }
                }
            } else if ( !showProgress.value &&  uiState.isSearchMode && uiState.searchServices.isEmpty() && uiState.search?.isNotEmpty() == true) {
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
            } else if (!uiState.isSearchMode) {
                Spacer(modifier = Modifier.height(10.dp))

                if (uiState.services.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        itemsIndexed(uiState.services) { index, serviceResult ->
                            Column {
                                ServicesCard1(
                                    image = serviceResult.image ?: "",
                                    title = serviceResult.title ?: "",
                                    desc = serviceResult.description ?: "",
                                    onServiceClick = {
                                        navigateToAddServiceScreen(
                                            "1",
                                            serviceResult.id ?: "",
                                            serviceResult.title ?: "",
                                            serviceResult.description ?: "",
                                            serviceResult.image ?: "",
                                            serviceResult.catId ?: "",
                                            serviceResult.iD ?: ""
                                        )
                                    },
                                )
                            }
                        }
                    }
                } else if (!showProgress.value && uiState.services.isEmpty()){
                    JtBtoCommonText(
                        text = stringResource(Res.string.no_services),
                        fontSize = 16.sp,
                        color = JTColors.BlackButtonColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (serviceState.message?.isNotEmpty() == true){
            CommonSnackbar(
                message = serviceState.message?:"",
                type = SnackbarType.SUCCESS,
                autoDismissMillis = 1500,
                onDismiss = {}
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = null,
            tonalElevation = 8.dp
        ) {
            ServiceRequestScreen(
                catId = id,
                onBackClick = { showBottomSheet = false }
            )
        }
    }
    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
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

@Composable
fun ServicesCard1(
    image: String,
    title: String,
    onServiceClick: () -> Unit,
    desc:String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onServiceClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xFF7F7F7F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = JtBtoFonts.getInterRegular(),
                )

            }
        }
    }
}