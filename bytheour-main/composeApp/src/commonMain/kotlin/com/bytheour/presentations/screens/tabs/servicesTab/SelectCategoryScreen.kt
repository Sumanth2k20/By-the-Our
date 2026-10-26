package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.no_services_found
import bytheour.composeapp.generated.resources.search_services
import bytheour.composeapp.generated.resources.select_A_Category
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.utils.JTColors
import com.bytheour.showProgress
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionType
import com.bytheour.utils.VoiceSearch
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryScreen(
    viewModel: CategoriesViewModel= getKoinViewModel(),
    navigateToServicesScreen:(String,String)-> Unit,
    navigateBack:()->Unit,
//    navigateToAddServiceScreen:(
//        navigationValue:String,
//        String,
//        String,
//        String,
//        String,
//        String,
//        String
//    )-> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    var showVoiceSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getCategories()
        viewModel.loadServicesWorker()
    }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == uiState.searchServices.lastIndex) {
                    viewModel.loadMoreSearchResults()
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
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.background(JTColors.enableButton)){
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.select_A_Category),
                            fontFamily = JtBtoFonts.getOpenSansBold(),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = JTColors.enableButton, // Purple background
                        titleContentColor = Color.White
                    )
                )

                JTSearchTextField(
                    value = uiState.search ?: "",
                    placeholder = stringResource(Res.string.search_services),
                    onValueChange = { viewModel.updateSearch(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Search,
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


        // Search Bar


//        // Search results section
//        if (uiState.searchServices.isNotEmpty() && uiState.search?.isNotEmpty() == true) {
//            LazyColumn {
//                items(uiState.searchServices) { serviceResult ->
//                    ServicesCard1(
//                        image = serviceResult.image ?: "",
//                        title = serviceResult.title ?: "",
//                        id = serviceResult.id ?: "",
//                        desc = serviceResult.description?:"",
//                        onServiceClick = {
//                            navigateToAddServiceScreen(
//                                "1",
//                                serviceResult.id ?: "",
//                                serviceResult.title ?: "",
//                                serviceResult.description ?: "",
//                                serviceResult.image?:"",
//                                serviceResult.catId?:"",
//                                serviceResult.iD?:""
//                            )
//                        },
//                    )
//                }
//                if (uiState.isLoadingMore) {
//                    item {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .padding(16.dp)
//                                .fillMaxWidth()
//                                .wrapContentWidth(Alignment.CenterHorizontally)
//                        )
//                    }
//                }
//            }
//        } else if (uiState.search?.isNotEmpty() == true) {

        val filteredServices =
            if (uiState.search.isNullOrBlank()) {
                uiState.services
            } else {
                uiState.services.filter { service ->
                    service.title?.contains(uiState.search!!, ignoreCase = true) == true ||
                            service.dec?.contains(uiState.search!!, ignoreCase = true) == true
                }
            }


        if (filteredServices.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier.padding(12.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(filteredServices) { _, item ->
                    CategoryCard1(
                        icon = item.image ?: "",
                        title = item.title ?: "",
                        onClick = {
                            navigateToServicesScreen(item.id ?: "", item.title ?: "")
                        }
                    )
                }
            }
        } else if(!showProgress.value && filteredServices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_services_found),
                    fontSize = 16.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    color = Color.Gray
                )
            }
        }
    }
    if (showVoiceSearch) {
        VoiceSearch(
            onResult = { recognizedText ->
                recognizedText?.let { text ->
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
fun CategoryCard1(
    title: String,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    badgeBackground: Color = Color(0xFFE4F5FF),
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .height(155.dp)
            .clickable { onClick() },
//            .height(140.dp)
//            .width(140.dp),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement =Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(badgeBackground, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = icon,
                        contentDescription = title,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = title,
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        overflow = TextOverflow.Visible,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        color = Color(0xFF2C2C2C)
                    )

//                    val count = subtitle.substringBefore(" ").trim()
//                    Text(
//                        text = buildAnnotatedString {
//                            withStyle(
//                                style = SpanStyle(
//                                    color = Color.Gray,
//                                    fontSize = 13.sp,
//                                    fontFamily = JtBtoFonts.getOpenSansRegular()
//                                )
//                            ) {
//                                append("Bookings - ")
//                            }
//                            withStyle(
//                                style = SpanStyle(
//                                    color = JTColors.enableButton,
//                                    fontSize = 13.sp,
//                                    fontFamily = JtBtoFonts.getOpenSansRegular()
//                                )
//                            ) {
//                                append(count)
//                            }
//                        },
//                        lineHeight = 13.sp,
//                        maxLines = 1
//                    )

                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

