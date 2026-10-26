package com.bytheour.presentations.screens.tabs.explore

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import bytheour.composeapp.generated.resources.autoDetailService
import bytheour.composeapp.generated.resources.book_now
import bytheour.composeapp.generated.resources.categories
import bytheour.composeapp.generated.resources.equipment_rentals
import bytheour.composeapp.generated.resources.facility_rentals
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.mobileMechanics
import bytheour.composeapp.generated.resources.no_default_address
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.roadside
import bytheour.composeapp.generated.resources.search_ico
import bytheour.composeapp.generated.resources.search_lawn
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.view_all
import bytheour.composeapp.generated.resources.voice_ico
import coil3.compose.AsyncImage
import com.bytheour.presentations.customcomponents.JTSearchTextField
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTAddressListViewModel
import com.bytheour.presentations.screens.wellComeScreens.HorizontalPagerIndicator
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.AUTO_DETAILING
import com.bytheour.utils.AUTO_DETAILING_ID
import com.bytheour.utils.AppFeatures
import com.bytheour.utils.EQUIPMENT_RENTALS_ID
import com.bytheour.utils.FACILITY_RENTALS_ID
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.MOBILE_MECHANIC
import com.bytheour.utils.MOBILE_MECHANIC_ID
import com.bytheour.utils.PermissionType
import com.bytheour.utils.ROAD_SIDE_ASSISTANCE
import com.bytheour.utils.ROAD_SIDE_ASSISTANCE_Id
import com.bytheour.utils.fetchLocationDetails
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import com.bytheour.utils.usePermission
import com.russhwolf.settings.Settings
import dev.jordond.compass.geolocation.Geolocator
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JtExploreTabScreen(
    viewModel: ExploreTabViewModel = getKoinViewModel(),
    addressViewModel: JTAddressListViewModel = getKoinViewModel(),
    navigateToAddressScreen:(Boolean)-> Unit,
    navigateToNotificationListScreen:()->Unit,
    navigateToCategoryScreen:()->Unit,
    navigateToRentalsScreen:(String,String)-> Unit,
    navigateToAvailableServices:(String,String)-> Unit,
){
    val search by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val addressState by addressViewModel.uiStates.collectAsState()

    LaunchedEffect(Unit) {
        fetchLocationDetails(
            geoLocator = Geolocator(),
            onSuccess = { flatNo, address, landmark, pinCode, country, _, _ ->
                viewModel.updateFullAddress(flatNo, address, landmark, pinCode,country)
            },
            onFailure = { message ->
                println("Message: $message")
            }
        )
        addressViewModel.loadInitialAddressList("50")
    }
    val requestNotificationPermission = usePermission(
        permissionType = PermissionType.NOTIFICATION,
        onPermissionGranted = {
            println("Notification permission granted")
        }
    )

    LaunchedEffect(Unit) {
        if (!uiState.notificationPermissionRequested) {
            requestNotificationPermission()
            viewModel.setNotificationPermissionRequested()
        }
    }
    LaunchedEffect(Unit){
        viewModel.getCategories()
        viewModel.getTrendingServices()
        viewModel.fetchEquipmentRentals(
            categoryId = EQUIPMENT_RENTALS_ID,
            limit = "10",
            offset = "0"
        )

        viewModel.fetchFacilityRentals(
            categoryId = FACILITY_RENTALS_ID,
            limit = "10",
            offset = "0"
        )
    }


    Column(modifier = Modifier.fillMaxSize()
        .background(color = JTColors.WhiteColor)
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = JTColors.BackGroundBlueColor)
        ) {
            if(platform.equals("IOS", ignoreCase = true)){
                Spacer(modifier = Modifier.height(45.dp))
            }else{
                Spacer(modifier = Modifier.height(35.dp))
            }
            val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
            val userInfo = sharedPreferenceUseCase.getUserInfo()
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .clickable { navigateToAddressScreen(false) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userInfo?.profile.isNullOrBlank()){
                    Image(
                        painter = painterResource(Res.drawable.provider_logo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape),
                    )
                } else {
                    AsyncImage(
                        model = userInfo?.profile,
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(Res.drawable.image_1),
                    )
                }

                Spacer(modifier = Modifier.width(3.dp))

                val defaultAddressItem = addressState.addressList.find { it.defaultAddress == true }
                val stateText = defaultAddressItem?.state ?: uiState.landmark?.takeIf { it.isNotBlank() }
                val fullAddress = defaultAddressItem?.fullAddress

                val currentLocation = listOfNotNull(
                    uiState.flatNo?.takeIf { it.isNotBlank() },
                    uiState.address?.takeIf { it.isNotBlank() },
                    uiState.country?.takeIf { it.isNotBlank() }
                ).joinToString(", ").takeIf { it.isNotBlank() }
                val displayAddress = fullAddress ?: currentLocation?.takeIf { it.isNotBlank() }

                val isInvalidAddress = displayAddress.isNullOrBlank() || displayAddress.contains("null", ignoreCase = true)
                if (!isInvalidAddress) {
                    Text(
                        text = buildAnnotatedString {
                            if (!stateText.isNullOrBlank()) {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                                        fontSize = 14.sp,
                                        color = JTColors.WhiteColor.copy(alpha = 0.9f)
                                    )
                                ) {
                                    append(stateText)
                                }
                                append("\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = JTColors.WhiteColor.copy(alpha = 0.8f)
                                )
                            ) {
                                append(displayAddress)
                            }
                        },
                        maxLines = 2,
                        lineHeight = 19.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2.5f)
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.no_default_address),
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontSize = 12.sp,
                        color = JTColors.WhiteColor.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2.5f)
                    )
                }


                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    tint = JTColors.WhiteColor,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Bottom)
                        .padding(bottom = 2.dp)
                        .weight(1f)
                        .clickable { navigateToAddressScreen(false) }
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "",
                    tint = JTColors.WhiteColor,
                    modifier = Modifier.clickable {
                        navigateToNotificationListScreen()
//                        NotificationService().showNotification(
//                            title = "Test Notification",
//                            body = "This is a local notification triggered from Compose.",
//                            imageUrl = "https://picsum.photos/450/300?grayscale",
//                            type = "local-test"
//                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            JTSearchTextField(
                value = search,
                onValueChange = {},
                modifier = Modifier.clickable { navigateToAddressScreen(true) }
                    .fillMaxWidth(0.95f).align(alignment = Alignment.CenterHorizontally),
                placeholder = stringResource(Res.string.search_lawn),
                enabled = false,
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
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (AppFeatures.TRENDING_SERVICES_HOME){
                TrendingServicesSection(
                    services = uiState.trendingServices,
                    navigateToAvailableServices = navigateToAvailableServices
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val filteredBanners = bannerList.filter { banner ->
                uiState.trendingServices.any { trending ->
                    trending.title?.replace(" ", "").equals(banner.title.replace(" ", ""), ignoreCase = true)
                }
            }
            BannerImages(
                categories = uiState.categories,
                banners = filteredBanners,
                onImageClick = { bannerId, bannerTitle ->

                    val service = uiState.trendingServices.find { trending ->
                        trending.title
                            ?.replace(" ", "").equals(
                                bannerTitle
                                    .replace(" ", ""), ignoreCase = true)
                    }
                    service?.let {
                        navigateToAvailableServices(it.id ?: "", it.title ?: "")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CategoriesSection(
                categories = uiState.categories,
                onViewAllClick = {
                    navigateToCategoryScreen()
                },
                navigateToAvailableServices = navigateToAvailableServices
            )

            Spacer(modifier = Modifier.height(20.dp))

// As per now the these section need to hide

//            EquipmentRentalsSection(
//                services = uiState.equipmentRentals,
//                onServiceClick = {},
//                onViewAllClick = {
//                    navigateToRentalsScreen(EQUIPMENT_RENTALS_ID, EQUIPMENT_RENTALS)
//
//                }
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            FacilityRentalsSection(
//                services = uiState.facilityRentals,
//                onViewAllClick = {
//                    navigateToRentalsScreen(FACILITY_RENTALS_ID, FACILITY_RENTALS)
//                }
//            )

//As per now the Images is hide

//            Image(
//                painter = painterResource(Res.drawable.discount_banner),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp)
//            )
//            Spacer(modifier = Modifier.height(30.dp))

//            Image(
//                painter = painterResource(Res.drawable.lowerPrice_banner),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp)
//            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun BannerImages(
    categories: List<ExploreTabViewModel.Categories>,
    banners: List<BannerItem>,
    onImageClick: (String,String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 8.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp
            ) { page ->
                val banner = banners[page]
                Image(
                    painter = painterResource(banner.image),
                    contentDescription = null,
                    contentScale =
                        if (categories.size <= 3) {
                            ContentScale.Fit
                        }else{
                            ContentScale.Crop
                        },

                    modifier =
                        if (categories.size <= 3) {
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onImageClick(banner.id, banner.title) }
                        } else{
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onImageClick(banner.id, banner.title) }
                        }

                )
            }
        }

        HorizontalPagerIndicator(
            pagerCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(bottom = 8.dp),
            activeColor = JTColors.BackGroundBlueColor,
            inactiveColor = JTColors.pageIndicatorColor
        )
    }
}



@Composable
fun EquipmentRentalsSection(
    services: List<ExploreTabViewModel.EquipmentRentals>,
    onServiceClick: (String?) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.equipment_rentals),
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 15.sp,
                color = JTColors.blackColor
            )

            TextButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = JTColors.BackGroundBlueColor
                )
            ) {
                Text(
                    text = stringResource(Res.string.view_all),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 14.sp,
                    color = JTColors.viewAllTextColor
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint =  JTColors.viewAllTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (services.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(services) { service ->
                    EquipmentRentalCard(
                        image = service.image?:"",
                        title = service.title?:"",
                        description = service.description?:"",
                        id = service.id?:"",
                        onClick = { onServiceClick(service.id) }
                    )
                }
            }
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun EquipmentRentalCard(
    image: String,
    title: String,
    id:String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.image_1)
            )

            Image(
                painter = painterResource(Res.drawable.transparent_bg), // Replace with your drawable
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.9f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 5.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = title,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    fontSize = 14.sp,
                    color = Color.White,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = description,
                    fontFamily = JtBtoFonts.getInterFont(),
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 11.sp
                )

                Spacer(modifier = Modifier.height(7.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.book_now),
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        fontSize = 10.sp,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))
            }
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun CategoryCard(
    image:String,
    title: String,
    id:String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .height(100.dp).width(100.dp)
            .clickable { onClick()},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = title,
                maxLines = 2,
                fontSize = 10.sp,
                lineHeight = 10.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<ExploreTabViewModel.Categories>,
    onViewAllClick: () -> Unit,
    navigateToAvailableServices: (String, String) -> Unit,
    rows: Int = 3
) {
    val cardHeight = 90.dp
    val verticalSpacing = 12.dp
    val totalHeight = (cardHeight * rows) + (verticalSpacing * (rows - 1))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.categories),
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 15.sp,
                color = JTColors.blackColor
            )

            TextButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = JTColors.BackGroundBlueColor
                )
            ) {
                Text(
                    text = stringResource(Res.string.view_all),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 14.sp,
                    color = JTColors.viewAllTextColor
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = JTColors.viewAllTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (categories.isNotEmpty()) {
            if (categories.size <= 3) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categories.forEach { category ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            CategoryCard(
                                image = category.image ?: "",
                                title = category.title ?: "",
                                id = category.id ?: "",
                                onClick = {
                                    navigateToAvailableServices(
                                        category.id ?: "",
                                        category.title ?: ""
                                    )
                                }
                            )
                        }
                    }
                }

            } else {

                LazyHorizontalGrid(
                    rows = GridCells.Fixed(rows),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(totalHeight)
                ) {
                    items(categories) { category ->
                        CategoryCard(
                            image = category.image ?: "",
                            title = category.title ?: "",
                            id = category.id ?: "",
                            onClick = {
                                navigateToAvailableServices(
                                    category.id ?: "",
                                    category.title ?: ""
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FacilityRentalsSection(
    services: List<ExploreTabViewModel.FacilityRentals>,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.facility_rentals),
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 15.sp,
                color = JTColors.blackColor
            )

            TextButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = JTColors.BackGroundBlueColor
                )
            ) {
                Text(
                    text = stringResource(Res.string.view_all),
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    fontSize = 14.sp,
                    color = JTColors.viewAllTextColor
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint =  JTColors.viewAllTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (services.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(services) { services ->
                    FacilityRentalsCard(
                        image = services.image ?: "",
                        title = services.title ?: "",
                        onClick = {  }
                    )
                }
            }
        }
    }
}

@Composable
fun FacilityRentalsCard(
    image: String,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {

        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(Res.drawable.image_1),
            error = painterResource(Res.drawable.image_1)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startX = 0f,
                        endX = Float.POSITIVE_INFINITY
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getInterBoldFont(),
                modifier = Modifier
                    .graphicsLayer(rotationZ = (-90f))
                    .offset(x = (-35).dp)
                    .padding(top = 85.dp),
                maxLines = 1,

            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.White,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}

@Composable
fun TrendingServicesCard(
    image: String,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E0FF)), // Light purple background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            AsyncImage(
                model = image,
                contentDescription = title,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TrendingServicesSection(
    services: List<ExploreTabViewModel.TrendingServices>,
    navigateToAvailableServices:(String,String)-> Unit
) {
    Column (modifier = Modifier
        .fillMaxWidth()
    ){
        if (services.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(services) { service ->
                    TrendingServicesCard(
                        image = service.image?:"",
                        title = service.title?:"",
                        onClick = { navigateToAvailableServices(service.id?:"",service.title?:"") }
                    )
                }
            }
        }
    }
}



data class BannerItem(
    val image: DrawableResource,
    val id: String,
    val title: String
)

val bannerList = listOf(
    BannerItem(Res.drawable.mobileMechanics, MOBILE_MECHANIC_ID, MOBILE_MECHANIC),
    BannerItem(Res.drawable.roadside, ROAD_SIDE_ASSISTANCE_Id, ROAD_SIDE_ASSISTANCE),
    BannerItem(Res.drawable.autoDetailService, AUTO_DETAILING_ID, AUTO_DETAILING),)
