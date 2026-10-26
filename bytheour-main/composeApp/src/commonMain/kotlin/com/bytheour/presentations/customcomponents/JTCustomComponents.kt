package com.bytheour.presentations.customcomponents

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.address_details
import bytheour.composeapp.generated.resources.camera
import bytheour.composeapp.generated.resources.city
import bytheour.composeapp.generated.resources.gallery
import bytheour.composeapp.generated.resources.home_location
import bytheour.composeapp.generated.resources.home_location_selected
import bytheour.composeapp.generated.resources.other_location
import bytheour.composeapp.generated.resources.other_location_selected
import bytheour.composeapp.generated.resources.others
import bytheour.composeapp.generated.resources.save_address_details
import bytheour.composeapp.generated.resources.save_as
import bytheour.composeapp.generated.resources.state
import bytheour.composeapp.generated.resources.street_details
import bytheour.composeapp.generated.resources.upload
import bytheour.composeapp.generated.resources.work_location
import bytheour.composeapp.generated.resources.work_location_selected
import bytheour.composeapp.generated.resources.zip_code
import com.bytheour.platform
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.tabs.moreTab.adress.AddressCard
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTAddressListViewModel
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTGoogleViewmodel
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTSelectLocationViewModel
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.LocationType
import com.bytheour.utils.SheetMode
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTBtoCustomTextField(
    name: String? = null,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    shape: Shape = RoundedCornerShape(8.dp), // 👈 New parameter
    fontSize: TextUnit? = null,
    enable: Boolean? = true,
    textFieldModifier: Modifier = Modifier,
    maxLines:Int = 1,
    isShowBorderError: Boolean = false,
    textStyle: TextStyle = TextStyle(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    passwordText: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.padding(start = 4.dp)) {
        if (name != null) {
            JtBtoCommonText(
                text = name ,
                fontSize = fontSize?:13.sp,
                fontFamily = JtBtoFonts.getInterRegular(),
                color = JTColors.textFieldTextColor
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (enable != null) {
            OutlinedTextField(
                textStyle = textStyle,
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    JtBtoCommonText(
                        text = placeholder,
                        color = JTColors.textFieldColor,
                        fontSize = 14.sp,
                    )
                },
                shape = shape,
                modifier = textFieldModifier.fillMaxWidth(),
                maxLines = maxLines,
                visualTransformation = if (passwordText) PasswordVisualTransformation() else visualTransformation,
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = JTColors.outerColor,
                    unfocusedPlaceholderColor = JTColors.outerColor,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = JTColors.border,
                    unfocusedIndicatorColor = JTColors.border,
                    disabledContainerColor = Color.White,
                    disabledPlaceholderColor = JTColors.outerColor,
                    disabledIndicatorColor = JTColors.border,
                ),
                isError = if (isShowBorderError) errorMessage != null else false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions ?: KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                enabled = enable,
            )
        }


        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                maxLines = 5,
                modifier = Modifier.fillMaxSize()
                    .padding(top = 4.dp),
                fontFamily = JtBtoFonts.getOpenSansRegular(),
            )
        }
    }
}



@Composable
fun JTBtoPrimaryButton(
    modifier: Modifier = Modifier,
    name:String,
    onClick :()-> Unit,
    buttonColors: Color,
    textColor: Color,
    icon: ImageVector? = null,
    iconColor : Color?= null,
    showInfoIcon:Boolean?= false,
    selectedColor: Color = buttonColors,
    borderColor:Color?= null,
    isSelected: Boolean = false,
    enable:Boolean?= true
) {
    Box (
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(55.dp)
            .background(
                color = if (isSelected) selectedColor else buttonColors,
                shape = RoundedCornerShape(30.dp)
            )
            .border(1.dp, color = borderColor ?: Color.Unspecified, shape = RoundedCornerShape(30.dp))
            , contentAlignment = Alignment.Center
    ){
        Button(onClick = {
            if (enable == true){
                onClick()
            }
        },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(0.9f))
        {

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                JtBtoCommonText(
                    text = name,
                    fontSize = 13.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    textAlign = TextAlign.Center,
                    color = textColor
                )
                if (showInfoIcon == true){
                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = icon ?: Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = iconColor ?: Color.Unspecified
                    )
                }
            }
        }
    }
}

@Composable
fun JTBtoSocialHandleButtons(
    onClick: () -> Unit,
    image: Painter,
    text:String,
    modifier: Modifier =Modifier
){
    Button(
        onClick = {
            onClick()
        },
        modifier.height(55.dp),
        colors = ButtonDefaults.buttonColors(
            Color.White,
        ),
        border = BorderStroke( width = 1.dp,JTColors.border),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter =image,
                contentDescription = "",
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(8.dp))

            JtBtoCommonText(
                text,
                color = JTColors.socialSignUpText, fontSize = 14.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraBottomSheetContent(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    galleryIconPainter: DrawableResource,
    cameraIconPainter: DrawableResource,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxHeight(0.27f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                Text(
                    text = stringResource(Res.string.upload),
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                     imageVector = Icons.Default.Clear,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            scope.launch { sheetState.hide() }
                            onDismiss()
                        },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(galleryIconPainter),
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable { onGalleryClick() }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = stringResource(Res.string.gallery),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(cameraIconPainter),
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                onCameraClick()
                            }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = stringResource(Res.string.camera),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

}

@Suppress("UNUSED_PARAMETER")
@Composable
fun JTSearchTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    modifier: Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                JtBtoCommonText(
                    placeholder,
                    color = JTColors.textFieldColor,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    overflow = TextOverflow.Ellipsis,
                    letterSpacing = 0.sp,
                )
            },
            modifier = Modifier.fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(48.dp),
                    spotColor = JTColors.grayTextColor
                ),
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(48.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.White
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions =  KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    onDismiss: () -> Unit,
    address: JTAddressListViewModel.UIStates? = null,
    uiStates: JTSelectLocationViewModel.UIState? = null,
    selectLocationViewModel: JTSelectLocationViewModel,
    googleViewmodel: JTGoogleViewmodel = getKoinViewModel(),
    onClick: (String) -> Unit,
    mode: SheetMode,
) {
    val googleUiState by googleViewmodel.uiState.collectAsState()

    // Initialize values based on mode
    val addressInit = if (mode == SheetMode.EDIT) address?.landmark else googleUiState.address
    val stateInit = if (mode == SheetMode.EDIT) address?.state else googleUiState.state
    val cityInit = if (mode == SheetMode.EDIT) address?.city else googleUiState.city
    val pinInit = if (mode == SheetMode.EDIT) address?.zip else googleUiState.pinCode
    val locationTypeInit = if (mode == SheetMode.EDIT) {
        address?.locationType ?: LocationType.HOME.type
    } else {
        LocationType.HOME.type // Default to HOME for adding new address
    }

    // Use remember to maintain stable state across recompositions
    val selectedType = remember(address?.locationType, mode) {
        mutableStateOf(locationTypeInit)
    }

    val types = listOf(LocationType.WORK.type, LocationType.HOME.type, LocationType.OTHER.type)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Text field states with proper initialization
    val addressTextState = remember(addressInit) {
        mutableStateOf(TextFieldValue(addressInit ?: "", TextRange(addressInit?.length ?: 0)))
    }
    val stateTextState = remember(stateInit) {
        mutableStateOf(TextFieldValue(stateInit ?: "", TextRange(stateInit?.length ?: 0)))
    }
    val cityTextState = remember(cityInit) {
        mutableStateOf(TextFieldValue(cityInit ?: "", TextRange(cityInit?.length ?: 0)))
    }
    val pinCodeTextState = remember(pinInit) {
        mutableStateOf(TextFieldValue(pinInit ?: "", TextRange(pinInit?.length ?: 0)))
    }

    // Custom name state for OTHER type - initialize properly for edit mode
    val customNameState = remember(address?.locationType, mode) {
        val initialCustomName = if (mode == SheetMode.EDIT &&
            address?.locationType != null &&
            address.locationType != LocationType.HOME.type &&
            address.locationType != LocationType.WORK.type &&
            address.locationType != LocationType.OTHER.type) {
            // This is a custom location type
            address.locationType
        } else ""
        mutableStateOf(TextFieldValue(initialCustomName, TextRange(initialCustomName.length)))
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = JTColors.WhiteColor,
        modifier = Modifier,
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            if (mode != SheetMode.EDIT) {
                AddressCard(googleViewmodel = googleViewmodel)
                HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))
                Spacer(modifier = Modifier.height(22.dp))
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth().alpha(0.5f))
            Spacer(modifier = Modifier.height(22.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.address_details),
                fontSize = 16.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = JTColors.code,
                modifier = Modifier.padding(10.dp)
            )

            // Address field
            JTBtoCustomTextField(
                name = stringResource(Res.string.street_details),
                value = addressTextState.value.text,
                onValueChange = { newText ->
                    addressTextState.value = TextFieldValue(newText, TextRange(newText.length))
                    selectLocationViewModel.updateAddress(newText)
                },
                modifier = Modifier.padding(10.dp),
                fontSize = 10.sp,
                placeholder = "",
                errorMessage = uiStates?.addressErrorMessage,
                keyboardType = KeyboardType.Text,
            )

            // State field
            JTBtoCustomTextField(
                name = stringResource(Res.string.state),
                value = stateTextState.value.text,
                onValueChange = { newText ->
                    stateTextState.value = TextFieldValue(newText, TextRange(newText.length))
                    selectLocationViewModel.updateState(newText)
                },
                modifier = Modifier.padding(10.dp),
                fontSize = 10.sp,
                placeholder = "",
                errorMessage = uiStates?.stateErrorMessage,
                keyboardType = KeyboardType.Text
            )

            // City field
            JTBtoCustomTextField(
                name = stringResource(Res.string.city),
                value = cityTextState.value.text,
                onValueChange = { newText ->
                    cityTextState.value = TextFieldValue(newText, TextRange(newText.length))
                    selectLocationViewModel.updateCity(newText)
                },
                modifier = Modifier.padding(10.dp),
                fontSize = 10.sp,
                placeholder = "",
                errorMessage = uiStates?.cityErrorMessage,
                keyboardType = KeyboardType.Text
            )

            // Pin Code field
            JTBtoCustomTextField(
                name = stringResource(Res.string.zip_code),
                value = pinCodeTextState.value.text,
                onValueChange = { newText ->
                    pinCodeTextState.value = TextFieldValue(newText, TextRange(newText.length))
                    selectLocationViewModel.updateZipCode(newText)
                },
                modifier = Modifier.padding(10.dp),
                fontSize = 10.sp,
                placeholder = "",
                errorMessage = uiStates?.zipErrorMessage,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.save_as),
                fontSize = 10.sp,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                color = JTColors.save,
                modifier = Modifier.padding(start = 10.dp)
            )

            // Location type selection chips
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                types.forEach { type ->
                    val isSelected = when {
                        // For HOME and WORK, simple equality check
                        type == LocationType.HOME.type || type == LocationType.WORK.type -> {
                            selectedType.value == type
                        }
                        // For OTHER type
                        type == LocationType.OTHER.type -> {
                            selectedType.value == LocationType.OTHER.type ||
                                    // Also select OTHER if it's a custom type (not HOME/WORK/OTHER)
                                    (selectedType.value != LocationType.HOME.type &&
                                            selectedType.value != LocationType.WORK.type &&
                                            selectedType.value != LocationType.OTHER.type &&
                                            selectedType.value.isNotBlank())
                        }
                        else -> false
                    }

                    val textColor = if (isSelected) JTColors.enableButton else JTColors.BlackButtonColor
                    val image = when (type) {
                        LocationType.HOME.type -> {
                            if (isSelected) painterResource(Res.drawable.home_location_selected)
                            else painterResource(Res.drawable.home_location)
                        }
                        LocationType.WORK.type -> {
                            if (isSelected) painterResource(Res.drawable.work_location_selected)
                            else painterResource(Res.drawable.work_location)
                        }
                        LocationType.OTHER.type -> {
                            if (isSelected) painterResource(Res.drawable.other_location_selected)
                            else painterResource(Res.drawable.other_location)
                        }
                        else -> painterResource(Res.drawable.other_location)
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedType.value = type
                            selectLocationViewModel.cleartypeError()
                            // Clear custom name when switching away from OTHER
                            if (type != LocationType.OTHER.type) {
                                customNameState.value = TextFieldValue("")
                            }
                        },
                        modifier = Modifier
                            .width(82.dp)
                            .height(29.dp),
                        label = {
                            JtBtoCommonText(
                                text = type,
                                fontSize = 10.sp,
                                color = textColor,
                                fontFamily = JtBtoFonts.getOpenSansBold()
                            )
                        },
                        leadingIcon = {
                            Image(
                                painter = image,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = JTColors.BlackButtonColor.copy(alpha = 0.15f),
                            selectedContainerColor = JTColors.enableButton.copy(alpha = 0.15f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = selectedType.value == type,
                            selected = selectedType.value == type,
                            borderColor = JTColors.BlackButtonColor,
                        )
                    )
                }
            }

            // Show type error
            uiStates?.typeErrorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Custom name field for OTHER type
            if (selectedType.value == LocationType.OTHER.type ||
                (selectedType.value != LocationType.HOME.type &&
                        selectedType.value != LocationType.WORK.type &&
                        selectedType.value != LocationType.OTHER.type &&
                        selectedType.value.isNotBlank())) {
                JTBtoCustomTextField(
                    name = stringResource(Res.string.others),
                    value = customNameState.value.text,
                    onValueChange = { newText ->
                        customNameState.value = TextFieldValue(newText, TextRange(newText.length))
                        selectLocationViewModel.clearOtherTypeError()
                        // Update selectedType to the custom name if it's not empty
                        if (newText.isNotBlank()) {
                            selectedType.value = newText
                        } else {
                            // If user clears the field, revert to OTHER
                            selectedType.value = LocationType.OTHER.type
                        }
                    },
                    modifier = Modifier.padding(10.dp),
                    fontSize = 10.sp,
                    placeholder = "",
                    errorMessage = uiStates?.otherTypeErrorMessage,
                    keyboardType = KeyboardType.Text
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .shadow(1.dp),
                contentAlignment = Alignment.Center
            ) {
                JTBtoPrimaryButton(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    name = stringResource(Res.string.save_address_details),
                    onClick = {
                        // Determine the final location type name
                        val finalLocationTypeName = when {
                            selectedType.value == LocationType.HOME.type -> LocationType.HOME.type
                            selectedType.value == LocationType.WORK.type -> LocationType.WORK.type
                            selectedType.value == LocationType.OTHER.type -> {
                                // Use custom name if available, otherwise use "Other"
                                customNameState.value.text.ifBlank { LocationType.OTHER.type }
                            }
                            else -> {
                                // This handles custom types (when selectedType contains custom name)
                                selectedType.value.ifBlank { LocationType.OTHER.type }
                            }
                        }

                        // Pass the appropriate value for validation
                        val typeForValidation = if (selectedType.value == LocationType.OTHER.type ||
                            (selectedType.value != LocationType.HOME.type &&
                                    selectedType.value != LocationType.WORK.type &&
                                    selectedType.value != LocationType.OTHER.type)) {
                            selectedType.value
                        } else {
                            selectedType.value
                        }

                        // Pass the custom name for validation (for OTHER type)
                        val customNameForValidation = if (selectedType.value == LocationType.OTHER.type) {
                            customNameState.value.text
                        } else if (selectedType.value != LocationType.HOME.type &&
                            selectedType.value != LocationType.WORK.type &&
                            selectedType.value != LocationType.OTHER.type) {
                            selectedType.value // This is already the custom name
                        } else {
                            ""
                        }

                        if (selectLocationViewModel.validateLocationForm(
                                typeForValidation,
                                customNameForValidation
                            )) {
                            onClick(finalLocationTypeName)
                        }
                    },
                    buttonColors = JTColors.enableButton,
                    selectedColor = JTColors.enableButton,
                    textColor = Color.White
                )
                Spacer(modifier = Modifier.height(22.dp))
            }
        }
    }
}

@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabRowHeight: Dp = 48.dp,
    activeColor: Color = JTColors.WhiteColor,
    inactiveColor: Color = JTColors.WhiteColor.copy(alpha = 0.5f),
    fontSize: TextUnit = 14.sp
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
            .fillMaxWidth()
            .height(tabRowHeight),
        containerColor = JTColors.enableButton,
        contentColor = activeColor,
        indicator = { tabPositions ->
            val currentTabPosition = tabPositions[selectedTabIndex]
            Box(
                Modifier
                    .tabIndicatorOffset(currentTabPosition)
                    .height(4.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White, RoundedCornerShape(1.dp))
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier.height(tabRowHeight)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = title,
                        color = if (index == selectedTabIndex) activeColor else inactiveColor,
                        fontSize = fontSize,
                        fontFamily = if (index == selectedTabIndex) JtBtoFonts.getOpenSansSemiBold() else JtBtoFonts.getOpenSansRegular()
                    )
                }
            }
        }
    }
}

@Composable
fun DayCustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabRowHeight: Dp = 38.dp,
    activeColor: Color = JTColors.WhiteColor,
    inactiveColor: Color = JTColors.enableButton,
    activeBackgroundColor: Color = JTColors.enableButton,
    inactiveBackgroundColor: Color = JTColors.TabRow,
    containerColor:Color =JTColors.TabRow,
    fontSize: TextUnit = 13.sp
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(tabRowHeight)
            .clip(RoundedCornerShape(42.dp)),
        containerColor = containerColor,
        indicator = { tabPositions ->
            Box(Modifier)
        },
        divider = {
            Box(Modifier)
        },
        tabs = {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(42.dp))
                        .background(
                            if (selectedTabIndex == index) activeBackgroundColor
                            else inactiveBackgroundColor
                        )
                ) {
                    Text(
                        text = title,
                        color = if (index == selectedTabIndex) activeColor else inactiveColor,
                        fontSize = fontSize,
                        fontFamily = if (index == selectedTabIndex) JtBtoFonts.getOpenSansSemiBold() else JtBtoFonts.getOpenSansRegular(),
                        modifier = Modifier
                            .wrapContentSize()
                            .then(
                                if (platform.equals("ios", ignoreCase = true)) {
                                    Modifier.padding(top = 3.dp, bottom = 7.dp)
                                } else {
                                    Modifier.padding(3.dp)
                                }
                            ),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

fun formatDate(dateString: String): String? {
    return try {
        // Parse ISO date (yyyy-MM-dd)
        val localDate = LocalDate.parse(dateString) // 2025-09-01

        // Day of week short name
        val dayOfWeek = localDate.dayOfWeek.name.take(3).lowercase()
            .replaceFirstChar { it.titlecase() } // "Mon"

        // Day with suffix
        val day = localDate.dayOfMonth
        val suffix = when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }

        // Month full name
        val month = localDate.month.name.lowercase()
            .replaceFirstChar { it.titlecase() } // "September"

        return "$dayOfWeek, $day$suffix $month"
    }
    catch (e:Exception){
        null
    }
}


