package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources._0_Miles
import bytheour.composeapp.generated.resources.average_hourly_rate
import bytheour.composeapp.generated.resources.current_radius
import bytheour.composeapp.generated.resources.delete
import bytheour.composeapp.generated.resources.delete_service
import bytheour.composeapp.generated.resources.delete_service_message
import bytheour.composeapp.generated.resources.enter_Description
import bytheour.composeapp.generated.resources.experience_years_for_this_service
import bytheour.composeapp.generated.resources.fixed_rates_can_also_be
import bytheour.composeapp.generated.resources.go_Back
import bytheour.composeapp.generated.resources.imageupload_ico
import bytheour.composeapp.generated.resources.miles
import bytheour.composeapp.generated.resources.minimum_hours
import bytheour.composeapp.generated.resources.minus_ico_serviceadd
import bytheour.composeapp.generated.resources.no
import bytheour.composeapp.generated.resources.note
import bytheour.composeapp.generated.resources.optional
import bytheour.composeapp.generated.resources.plus_ico_serviceadd
import bytheour.composeapp.generated.resources.select_your_plan
import bytheour.composeapp.generated.resources.service_Description
import bytheour.composeapp.generated.resources.service_in_list_msg
import bytheour.composeapp.generated.resources.service_in_list_title
import bytheour.composeapp.generated.resources.services
import bytheour.composeapp.generated.resources.set_radius
import bytheour.composeapp.generated.resources.set_radius_note
import bytheour.composeapp.generated.resources.submit
import bytheour.composeapp.generated.resources.yes
import bytheour.composeapp.generated.resources.your_premises
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.core.JTURLS.BASE_IMAGE_URL
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.CustomAlertDialog
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    viewModel: AddServiceScreenViewModel = getKoinViewModel(),
    navigateBack : () -> Unit,
    navigationValue : String,
    serviceId:String,
    serviceTitle:String,
    image:String,
    hourlyRate:String,
    minHours:String,
    experience:String,
    description:String,
    isPremiseAvailable:String,
    radius:String,
    id:String,
    navigatingtomainScreen:(Int)->Unit,
    navigateImageUploadScreen:(String,String,String)->Unit,
    navigateSelectPlanScreen:(String)->Unit,
){

    var showServiceErrorDialog by remember { mutableStateOf(false) }

    // No local mutable state for UI values
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    val previews by GlobalImageStore.imagePreviews.collectAsState()
    val pending by GlobalImageStore.pendingUploads.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDialog by remember { mutableStateOf(false) }



    LaunchedEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {

                    viewModel.loadServices(id)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
        }

    if (navigationValue == "1") {
        LaunchedEffect(Unit) {
            viewModel.imagePreviews(previews)
            viewModel.pendingUploads(pending)
        }
    }



    LaunchedEffect(Unit){
        viewModel.fetchGalleryBookings()
    }

    LaunchedEffect(uiState.serviceId) {
        if (navigationValue != "2" && uiState.serviceId == id && uiState.serviceId != null) {
            showServiceErrorDialog = true
        }
    }


    CustomAlertDialog(
        showDialog = showServiceErrorDialog,
        onDismiss = {
            showServiceErrorDialog = false
            navigateBack()
        },
        title = stringResource(Res.string.service_in_list_title),
        message = stringResource(Res.string.service_in_list_msg),
        yes = false,
        no = false,
        onConfirm = {
            showServiceErrorDialog = false
            navigateBack()
        },
        onCancel = { },
        changeText = true,
        changedText = stringResource(Res.string.go_Back)
    )


    val onUploadClick: () -> Unit = {
        val hasValidPlan = uiState.galleryBookings.size

        if (hasValidPlan != 0) {
            navigateImageUploadScreen(
                serviceId,
                if (navigationValue == "2") "tab" else "add",
                ""
            )
        } else {
            navigateSelectPlanScreen(serviceId)
        }
    }
    val onDeleteClick: () -> Unit = {
        viewModel.deleteService(
            serviceId,
            onSuccess = {
                navigateBack()
                GlobalImageStore.clearAll()
            }
        )
    }
    val onSubmitClick: () -> Unit = {
        viewModel.submitService(
            serviceId = id,
            pendingUploads = uiState.pendingUploads,
            onSuccess = {
                navigatingtomainScreen(1)
            }
        )
    }

    val maxRadius = 1000f
    if (navigationValue == "2") {
        LaunchedEffect(Unit) {
            viewModel.updateHourlyRate(hourlyRate)
            viewModel.updateMinHours(minHours.toIntOrNull() ?: 1)
            viewModel.updateExperience(experience)
            viewModel.updateDescription(description)
            viewModel.updateRadius(radius)
            viewModel.updatePremiseAvailable(isPremiseAvailable.equals("y", ignoreCase = true))
        }
    }

    Box(Modifier.fillMaxSize().
    padding(top = 32.dp)
        .background(Color.Transparent)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
                .verticalScroll(rememberScrollState())
        ) {
            // -------- Header Image with Title Overlay --------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // adjust height as needed
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = serviceTitle,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 16.dp, top = 10.dp)
                ) {
                    // Back button (left)
                    IconButton(
                        onClick = {
                            navigateBack()
                            GlobalImageStore.clearAll()
                                  },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    // Title (centered)
                    Text(
                        text = stringResource(Res.string.services),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Service Name at bottom
                Text(
                    text = serviceTitle,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0EFF3)),
            ) {
                ExpandableDescription(description)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.average_hourly_rate),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 13.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(16.dp),
                )
                OutlinedTextField(
                    value = uiState.hourlyRate ?: "",
                    onValueChange = { viewModel.updateHourlyRate(it) },
                    singleLine = true,
                    modifier = Modifier
                        .width(130.dp)
                        .padding(end = 16.dp, top = 16.dp),
                    prefix = {
                        Text(
                            text = "$",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        disabledBorderColor = Color.LightGray,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        cursorColor = Color.Black,
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                uiState.hourlyRateError?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        color = JTColors.ErrorMessage,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))


            Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF0EFF3)).padding(start = 16.dp,end = 16.dp, top = 5.dp, bottom = 5.dp)){
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFFF6A00), fontWeight = FontWeight.Bold)) {
                            append(stringResource(Res.string.note))
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF707070))){
                            append(stringResource(Res.string.fixed_rates_can_also_be))
                        }
                    },
                    fontSize = 11.sp,
                    fontFamily = JtBtoFonts.getInterRegular()
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.minus_ico_serviceadd),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            val current = uiState.minHours ?: 0
                            if (current > 0) viewModel.updateMinHours(current - 1)
                        }
                    )
                    Text(
                        text = "${uiState.minHours ?: 0}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Image(
                        painter = painterResource(Res.drawable.plus_ico_serviceadd),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            val current = uiState.minHours ?: 0
                            viewModel.updateMinHours(current + 1)
                        }
                    )
                }

            }

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                uiState.minHoursError?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        color = JTColors.ErrorMessage,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.experience_years_for_this_service),
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 13.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(16.dp),
                )

                OutlinedTextField(
                    value =  uiState.experience ?: "",
                    onValueChange = { it ->
                        // Allow only digits and restrict to 2 characters
                        val filtered = it.filter { it.isDigit() }.take(2)
                        viewModel.updateExperience(filtered)
                                    },
                    readOnly = false,
                    singleLine = true,
                    modifier = Modifier.width(130.dp).padding(end = 16.dp, top = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        disabledBorderColor = Color.LightGray,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        cursorColor = Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                uiState.experienceError?.let {errorMsg ->
                    Text(
                        text = errorMsg,
                        fontFamily = JtBtoFonts.getInterMediumFont(),
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                        color = JTColors.ErrorMessage,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3E2473))
                    .clickable { onUploadClick() }
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(9.dp)
                ) {
                    if (!uiState.galleryImages.isNullOrEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.galleryImages!!) { serverImage ->
                                Image(
                                    painter = rememberAsyncImagePainter(BASE_IMAGE_URL + serverImage.path),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            1.dp,
                                            color = Color.Transparent,
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    if (uiState.imagePreviews.isNotEmpty()){
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.imagePreviews) { serverImage ->
                                Image(
                                    bitmap = serverImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            1.dp,
                                            color = Color.Transparent,
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    DottedBorderBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                            .height(36.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 10.dp,
//                                    vertical = 10.dp
                                )
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(Res.drawable.imageupload_ico),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(7.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontFamily = JtBtoFonts.getInterRegular(),
                                            color = Color.White
                                        )) {
                                            append(stringResource(Res.string.select_your_plan))
                                        }
                                        append(" ")
                                        withStyle(style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontFamily = JtBtoFonts.getInterRegular(),
                                            color = Color(0xFFB9A9D9)
                                        )) {
                                            append(stringResource(Res.string.optional))
                                        }
                                    },
                                    lineHeight = 12.sp,
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            /* Description */
                Spacer(Modifier.height(15.dp))
            Column (modifier = Modifier.padding(16.dp)){
                Text(
                    stringResource(Res.string.service_Description),
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = JTColors.BackGroundBlueColor
                )
                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = uiState.description ?: "",
                    onValueChange = { viewModel.updateDescription(it) },
                    placeholder = {
                        Text(
                            stringResource(Res.string.enter_Description),
                            color = Color.LightGray,
                            fontFamily = JtBtoFonts.getInterRegular(),
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()

                        .heightIn(min = 120.dp),
                    singleLine = false,
                    maxLines = 6,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = JTColors.border,
                        unfocusedBorderColor =JTColors.border,
                        disabledBorderColor =JTColors.border,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        cursorColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

                /* Premises radio */
            Spacer(Modifier.height(16.dp))
            Surface(color = Color(0xFF3E2473)) { // Purple background
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(Res.string.your_premises),
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.isPremiseAvailable == true,
                            onClick = { viewModel.updatePremiseAvailable(true) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White
                            )
                        )
                        Text(
                            stringResource(Res.string.yes),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            modifier = Modifier.clickable { viewModel.updatePremiseAvailable(true) }
                        )

                        Spacer(Modifier.width(12.dp))

                        RadioButton(
                            selected = uiState.isPremiseAvailable == false,
                            onClick = { viewModel.updatePremiseAvailable(false) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White
                            )
                        )
                        Text(
                            stringResource(Res.string.no),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            modifier = Modifier.clickable { viewModel.updatePremiseAvailable(false) }
                        )
                    }
                }
            }
            uiState.premiseError?.let {
                Text(
                    text = it,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = JTColors.ErrorMessage,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }

            /* Radius slider */
            Spacer(Modifier.height(24.dp))

        // Title
            Column (modifier = Modifier.padding(16.dp)){
                Text(
                    stringResource(Res.string.set_radius),
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(8.dp))

                // Description
                Text(
                    stringResource(Res.string.set_radius_note),
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF707070)
                )

                Spacer(Modifier.height(12.dp))

                // Current radius label
                val radiusMiles = (uiState.radiusMiles?.toFloatOrNull() ?: 0f)
                Text(
                    stringResource(Res.string.current_radius)+"${radiusMiles.toInt()} " + stringResource(Res.string.miles),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont(),
                    color = Color(0xFF3E2473)
                )

                Spacer(Modifier.height(8.dp))

                // Slider with bubble tooltip
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(18.dp))
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Slider(
                            value = radiusMiles,
                            onValueChange = { viewModel.updateRadius(it.toString()) },
                            valueRange = 0f..maxRadius,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp), // reduce overall slider height
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Transparent, // disable default thumb color
                                activeTrackColor = Color(0xFF3E2473),
                                inactiveTrackColor = Color.LightGray
                            ),
                            thumb = {

                                Canvas(modifier = Modifier.size(20.dp)) {
                                    drawCircle(color = Color(0xFF3E2473), radius = size.minDimension / 2)
                                }
                            },
                            track = { sliderPositions ->
                                val fraction = (radiusMiles - 0f) / (maxRadius - 0f)
                                // Custom slim track
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(4.dp) // track thickness
                                ) {
                                    // inactive track
                                    Box(
                                        Modifier
                                            .matchParentSize()
                                            .background(Color.LightGray, RoundedCornerShape(50))
                                    )
                                    // active track
                                    Box(
                                        Modifier
                                            .fillMaxWidth(fraction)
                                            .height(4.dp)
                                            .background(Color(0xFF3E2473), RoundedCornerShape(50))
                                    )
                                }
                            }
                        )


                        Box(
                            modifier = Modifier
                                .offset(y = (-34).dp) // move above thumb
                                .background(Color(0xFFEDEDED), RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .height(20.dp)
                        ) {
                            Text(
                                "${radiusMiles.toInt()}" + stringResource(Res.string.miles),
                                color = JTColors.BackGroundBlueColor,
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getInterFont()
                            )
                        }
                    }

                    // Min/Max labels
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(Res.string._0_Miles),
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getInterFont(),
                            color = JTColors.BackGroundBlueColor
                        )

                        Text(
                            "${maxRadius.toInt()}" + stringResource(Res.string.miles),
                            fontSize = 12.sp,
                            fontFamily = JtBtoFonts.getInterFont(),
                            color = JTColors.BackGroundBlueColor
                        )
                    }

                    uiState.radiusError?.let {
                        Text(
                            text = it,
                            fontFamily = JtBtoFonts.getInterMediumFont(),
                            fontSize = 13.sp,
                            lineHeight = 13.sp,
                            color = JTColors.ErrorMessage,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

            }

                Spacer(Modifier.height(32.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

            if (navigationValue == "2") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            showDialog = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFF3B1970)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B1970))
                    ) {
                        Text(stringResource(Res.string.delete))
                    }

                    Button(
                        onClick = {
                            onSubmitClick()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B1970)),
                    ) {
                        Text(stringResource(Res.string.submit), color = Color.White)
                    }
                }
            }
            else if (navigationValue == "1"){
                Button(
                    onClick = {
                        onSubmitClick()
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B1970)),
                ) {
                    Text(stringResource(Res.string.submit), color = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
    if (uiState.message?.isNotEmpty() == true){
        Box(modifier = Modifier.padding(bottom = 65.dp)) {
            CommonSnackbar(
                message = uiState.message ?: "",
                type =
                    if (uiState.message!!.contains("successfully",ignoreCase = true)){
                        SnackbarType.SUCCESS
                    }else{
                        SnackbarType.ERROR
                    },
                autoDismissMillis = 1500,
                onDismiss = {
                    viewModel.clearError()

                }
            )
        }
    }
    if (showDialog){
        CustomAlertDialog(
            showDialog = showDialog,
            onDismiss = {showDialog = false},
            title = stringResource(Res.string.delete_service),
            message = stringResource(Res.string.delete_service_message),
            onCancel = { showDialog = false },
            onConfirm = {
                onDeleteClick()
                showDialog = false
            },
            yes = true,
            no = true
        )
    }

}



@Composable
fun DottedBorderBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    strokeWidth: Dp = 1.dp,
    dotSpacing: Float = 10f,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                val stroke = Stroke(
                    width = strokeWidth.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, dotSpacing))
                )
                drawRoundRect(
                    color = Color(0xFF9A82C8), // dotted border color
                    style = stroke,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
    ) {
        content()
    }
}


@Composable
fun ExpandableDescription(
    serviceDescription: String
) {
    var expanded by remember { mutableStateOf(false) }
    var isTextOverflow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = serviceDescription,
            fontFamily = JtBtoFonts.getInterRegular(),
            fontSize = 12.sp,
            lineHeight = 14.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = if (expanded) TextOverflow.Clip else TextOverflow.Ellipsis,
            color = Color(0xFF262626),
            onTextLayout = { result ->
                if (!expanded) {
                    isTextOverflow = result.hasVisualOverflow
                }
            }
        )

        if (isTextOverflow || expanded) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (expanded) "View Less" else "View More",
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                fontSize = 10.sp,
                lineHeight = 10.sp,
                color = Color(0xFF262626),
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}

