package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.addservice_ico
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.common_pricing
import bytheour.composeapp.generated.resources.common_pricing_note
import bytheour.composeapp.generated.resources.enter_Price
import bytheour.composeapp.generated.resources.hello
import bytheour.composeapp.generated.resources.minimum_booking_hours
import bytheour.composeapp.generated.resources.minimum_hours
import bytheour.composeapp.generated.resources.minus_ico_serviceadd
import bytheour.composeapp.generated.resources.note
import bytheour.composeapp.generated.resources.plus_ico_serviceadd
import bytheour.composeapp.generated.resources.proceeding_to_add_service
import bytheour.composeapp.generated.resources.save
import bytheour.composeapp.generated.resources.services_ico
import bytheour.composeapp.generated.resources.txt_msg_service
import bytheour.composeapp.generated.resources.your_services
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.reports.CommonDialogBox
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ServicesTabScreen(
    viewModel: ServiceTabScreenViewModel = getKoinViewModel(),
    navigateToCategoryScreen:()-> Unit,
    navigateToSelectLocation :(String, String,String) ->Unit,
    navigateToWebView:(String,String) ->Unit,
    navigateToAddServiceScreen:(
        navigationValue:String,
        serviceId:String,
        serviceTitle:String,
        serviceDescription:String,
        serviceImage:String,
        hourlyRate:String,
        minHours:String,
        experience:String,
        dec:String,
        isPermission:String,
        catId:String,
        radius:String,
        id:String
            )->Unit

) {
    val uiState by viewModel.uiStates.collectAsState()
    val listState = rememberLazyListState()
    var showAlert by remember { mutableStateOf(false) }
    var isUserSwitchAction by remember { mutableStateOf(false) }
    var showAlertNavigation by remember { mutableStateOf(false) }

    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val getUserInfo = sharedPreferenceUseCase.getUserInfo()
    val getlat = sharedPreferenceUseCase.getLat()
    val getLng = sharedPreferenceUseCase.getLng()


    LaunchedEffect(Unit) {
        viewModel.loadInitialServices()
        viewModel.getProfile()
        viewModel.verifyAccountWithStripe()
    }


    val actionButtonClick:()->Unit = {
        if(uiState.isVerified){
                navigateToCategoryScreen()
        }else if(getUserInfo?.flags?.address == false || getUserInfo?.flags?.documents == false){
            showAlertNavigation = true
        }else{
            navigateToCategoryScreen()
        }
    }

    if (showAlertNavigation){
        CommonDialogBox(
            title = "",
            message = stringResource(Res.string.proceeding_to_add_service),
            onCancelClick = {
                showAlertNavigation = false
            },
            onContinueClick = {
                if(getUserInfo?.flags?.address == false){
                    navigateToSelectLocation(
                       getlat.toString(),getLng.toString(),"fromService"
                    )
                    showAlertNavigation = false
                }else if(getUserInfo?.flags?.documents == false){
                    if (uiState.webUrl != null){
                        navigateToWebView(uiState.webUrl ?: "","Upload Documents")
                        viewModel.updateWebUrl()
                        showAlertNavigation = false
                    }
                    showAlertNavigation = false
                }else{
                    navigateToCategoryScreen()
                    showAlertNavigation = false
                }
            },
            cancelText = "Cancel",
            deleteText = "Continue",

        )
    }

//    if (uiState.webUrl != null){
//        navigateToWebView(uiState.webUrl ?: "","Upload Documents")
//        viewModel.updateWebUrl()
//        showAlertNavigation = false
//    }
    fun onCommonPricingSwitchChanged(checked: Boolean) {
        isUserSwitchAction = true
        viewModel.toggleCommonPricing(checked)

        if (checked) {
            // Show dialog when enabling common pricing
            showAlert = true
        } else {
            // Disable common pricing via API
            viewModel.submitCommonRate(
                common = "0",
                commonHours = "1",
                commonPrice = null,
                gocommonRate = false,
                onSuccess =  {
                    viewModel.loadInitialServices()
                }
            )
        }
    }


    // If backend updates the switch, don't show dialog
    LaunchedEffect(uiState.isCommonPricingEnabled) {
        if (!isUserSwitchAction) {
            // Just update the switch, no dialog
            showAlert = false
        }
        isUserSwitchAction = false // Reset after handling
    }

    CommonPricingDialog(
        visible = showAlert,
        onDismiss = {
            showAlert = false
            // Optionally, turn switch OFF if dialog is dismissed without saving
            viewModel.toggleCommonPricing(false)
        },
        onSave = { price, minHr ->
            viewModel.submitCommonRate(
                common = "1",
                commonHours = minHr.toString(),
                commonPrice = price.toString(),
                gocommonRate = true,
                onSuccess = {
                    showAlert = false
                    viewModel.toggleCommonPricing(true)
                }
            )
//            viewModel.loadInitialServices()
        },
        initialPrice = uiState.commonPrice ?: "",
        initialMinHours = uiState.commonHours ?: ""
    )



    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIdx ->
                if (lastVisibleIdx == uiState.services.lastIndex) {
                    viewModel.loadMoreServices()
                }
            }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDEDED))
        ) {
            // Top Profile Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3D217A))
                    .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Image
                    if(uiState.profile.isNullOrBlank()){
                        Image(
                            painter = painterResource(Res.drawable.services_ico), // replace with uiState.profileImage
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }else{
                        AsyncImage(
                            model = uiState.profile,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(Res.string.hello),
                            fontSize = 13.sp,
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            color = Color.White,
                            lineHeight = 13.sp
                        )
                        Text(
                            text = uiState.userName.ifEmpty { "N/A" },
                            fontSize = 16.sp,
                            fontFamily = JtBtoFonts.getOpenSansBold(),
                            color = Color.White,
                            lineHeight = 15.sp
                        )
                    }
                }
                if(uiState.services.isNotEmpty()) {
                    IconButton(onClick = { navigateToCategoryScreen() }) {
                        Image(
                            painter = painterResource(Res.drawable.addservice_ico),
                            contentDescription = "",
                        )
                    }
                }
            }

            if(uiState.services.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                actionButtonClick()
                            },
                            containerColor = Color(0xFF3D217A),
                            contentColor = Color.White
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.addservice_ico),
                                contentDescription = "Add Service"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(Res.string.txt_msg_service),
                            fontSize = 13.sp,
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            color = Color.Black,
                            lineHeight = 13.sp
                        )
                    }
                }
            }else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFE8E7EB))
                        .padding(7.dp)
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(Res.string.common_pricing),
                            color = JTColors.enableButton,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            modifier = Modifier.padding(start = 35.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = stringResource(Res.string.note),
                                        color = Color(0xFFF97316),
                                        fontSize = 12.sp,
                                        fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(Res.string.common_pricing_note),
                                        color = Color(0xFF707070),
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp,
                                        fontFamily = JtBtoFonts.getInterRegular(),
                                    )
                                }
                            }
                            Switch(
                                checked = uiState.isCommonPricingEnabled,
                                onCheckedChange = { onCommonPricingSwitchChanged(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF3D217A),
                                    uncheckedThumbColor = Color.Gray,
                                    uncheckedTrackColor = Color.White
                                ),
                            )
                        }

                        if (uiState.isCommonPricingEnabled && !uiState.commonPrice.isNullOrBlank()) {
                            Row {
                                Text(
                                    text = "Minimum hour booking : ${uiState.commonHours}Hr",
                                    color = Color(0xFF9E9E9E),
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 14.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(start = 34.dp)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "$${uiState.commonPrice}",
                                    color = JTColors.BackGroundBlueColor,
                                    fontFamily = JtBtoFonts.getInterMediumFont(),
                                    fontSize = 15.sp,
                                    lineHeight = 15.sp,
                                )
                            }
                        }
                    }
                }

                JtBtoCommonText(
                    text = stringResource(Res.string.your_services),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    lineHeight = 12.sp,
                    color = JTColors.BackGroundBlueColor
                )

                LazyColumn {
                    items(uiState.services) { service ->
                        ServiceItemCard(
                            service = service,
                            onCardClick = {
                                navigateToAddServiceScreen(
                                    "2",
                                    service.id ?: "",
                                    service.name,
                                    service.description ?: "",
                                    service.imageRes,
                                    service.price,
                                    service.minHours,
                                    service.experience ?: "",
                                    service.editDesc ?: "",
                                    service.isPermissions ?: "",
                                    service.catId ?: "",
                                    service.radius ?: "",
                                    service.iD ?: ""
                                )
                            }
                        )
                    }
                    item {
                        Spacer(Modifier.height(90.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItemCard(
    service: Service,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
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
                    model = service.imageRes,
                    contentDescription = service.name,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontSize = 13.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF7F7F7F),
                                fontSize = 11.sp,
                                fontFamily = JtBtoFonts.getInterRegular(),
                            )
                        ) {
                            append(stringResource(Res.string.minimum_booking_hours))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = JTColors.enableButton,
                                fontSize = 11.sp,
                                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                            )
                        ) {
                            append("${service.minHours} Hrs")
                        }
                    }
                )

            }

            // Price
            JtBtoCommonText(
                text = "$${service.price}",
                fontSize = 13.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
            )
        }
    }
}



@Composable
fun CommonPricingDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSave: (price: Double, minHours: Int) -> Unit,
    initialPrice: String = "",
    initialMinHours: String = ""
) {
    if (!visible) return

    var priceText by remember { mutableStateOf(initialPrice) }
    var minHours by remember { mutableStateOf(maxOf(1, initialMinHours.toIntOrNull() ?: 1)) }
    val focus = LocalFocusManager.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // ---------- TITLE ----------
                Text(
                    text = stringResource(Res.string.common_pricing),
                    fontSize = 16.sp,
                    lineHeight = 12.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = Color(0xFF313131)
                )

                // ---------- NOTE ----------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 20.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFF97316),
                                fontWeight = FontWeight.SemiBold
                            )
                        ) { append(stringResource(Res.string.note)) }

                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF9E9E9E)
                            )
                        ) {
                            append(stringResource(Res.string.common_pricing_note))
                        }
                    }.also { text ->
                        Text(text = text,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            fontFamily = JtBtoFonts.getInterRegular(),
                        )
                    }
                }

                // ---------- PRICE ----------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.enter_Price),
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { new ->
                            if (new.isEmpty() || new.matches("""\d*\.?\d{0,2}""".toRegex()))
                                priceText = new
                        },
                        modifier = Modifier.width(120.dp),
                        placeholder = { Text(stringResource(Res.string.enter_Price), fontSize = 13.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        prefix = { Text("$", color = Color.Black) },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = JTColors.border,
                            unfocusedIndicatorColor =JTColors.border,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedPlaceholderColor = JTColors.WhiteColor,
                            unfocusedPlaceholderColor = JTColors.grayColor,
                            focusedTextColor = JTColors.blackColor,
                            unfocusedTextColor = JTColors.blackColor,
                            focusedLabelColor = JTColors.border,
                            unfocusedLabelColor = JTColors.border
                        )
                        
                    )

                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = JTColors.border)
                Spacer(Modifier.height(10.dp))
                // ---------- MIN HOURS ----------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.minimum_hours),
                        fontSize = 14.sp,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(width = 1.dp, color = JTColors.border, RoundedCornerShape(5.dp)).padding(5.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.minus_ico_serviceadd),
                            contentDescription = "",
                            modifier = Modifier.clickable { if (minHours > 1) minHours-- }
                        )
                        Text(
                            text = minHours.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Image(
                            painter = painterResource(Res.drawable.plus_ico_serviceadd),
                            contentDescription = "",
                            modifier = Modifier.clickable { minHours++ }
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = JTColors.border)
//
                Spacer(Modifier.height(20.dp))
                // ---------- ACTIONS ----------
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            focus.clearFocus()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFFC2C2C2)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B1970))
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }

                    Button(
                        onClick = {
                            focus.clearFocus()
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            onSave(price, minHours)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B1970)),
                        enabled = priceText.isNotBlank()
                    ) {
                        Text(stringResource(Res.string.save), color = Color.White)
                    }
                }
            }
        }
    }
}
