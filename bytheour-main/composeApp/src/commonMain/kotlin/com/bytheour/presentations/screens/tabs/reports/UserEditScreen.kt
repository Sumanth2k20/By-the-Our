package com.bytheour.presentations.screens.tabs.reports

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.camera_ico1
import bytheour.composeapp.generated.resources.cancel
import bytheour.composeapp.generated.resources.contactNumber
import bytheour.composeapp.generated.resources.edit_user
import bytheour.composeapp.generated.resources.email_address
import bytheour.composeapp.generated.resources.enter_contact_number
import bytheour.composeapp.generated.resources.enter_email
import bytheour.composeapp.generated.resources.enter_first_name
import bytheour.composeapp.generated.resources.enter_last_name
import bytheour.composeapp.generated.resources.firstName
import bytheour.composeapp.generated.resources.lastName
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.save
import coil3.compose.AsyncImage
import com.bytheour.core.JTURLS
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.PermissionImagePicker
import com.bytheour.utils.getKoinViewModel
import com.bytheour.utils.platform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun UserEditScreen(
    viewModel: UserEditScreenViewModel = getKoinViewModel(),
    navigateBack:()-> Unit,
    id :String,
    firstName:String,
    lastName:String,
    email:String,
    phoneNumber:String,
    image: String
){

    val uiState by viewModel.uiStates.collectAsState()

    var showImagePicker by remember { mutableStateOf(false) }


    LaunchedEffect(Unit){
        viewModel.adminGetUserDate(id)
    }

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(color = Color(0xFFF7F7F7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = JTColors.BackGroundBlueColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(platform.equals("IOS", ignoreCase = true)){
                    Spacer(modifier = Modifier.height(50.dp))
                }else{
                    Spacer(modifier = Modifier.height(30.dp))
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            navigateBack()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = stringResource(Res.string.edit_user),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { showImagePicker = true }
                        .background(Color.Gray),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    when {
                        uiState.imageBitmap != null -> {
                            Image(
                                bitmap = uiState.imageBitmap!!,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                        image.isBlank() -> {
                            Image(
                                painter = painterResource(Res.drawable.provider_logo),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                        else -> {
                            AsyncImage(
                                model = "${JTURLS.BASE_IMAGE_URL}$image",
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                    }
//                    if (image.isBlank()) {
//                        Image(
//                            painter = painterResource(Res.drawable.america),
//                            contentDescription = "Avatar",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(CircleShape),
//                            contentScale = ContentScale.Crop
//                        )
//                    } else {
//                        AsyncImage(
//                            model = "${JTURLS.BASE_IMAGE_URL}$image",
//                            contentDescription = "Avatar",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(CircleShape),
//                            contentScale = ContentScale.Crop
//                        )
//                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(27.dp)
                            .background(Color.Black.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { showImagePicker = true }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.camera_ico1),
                                contentDescription = "Camera Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(bottom = 10.dp)
                                    .clickable { showImagePicker = true }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.firstName?:firstName,
                    color = JTColors.WhiteColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.firstName),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = uiState.firstName?:firstName,
                    onValueChange = { viewModel.updateName(it) },
                    placeholder = {
                        Text(text = stringResource(Res.string.enter_first_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = JTColors.outerColor,
                        unfocusedPlaceholderColor = JTColors.outerColor,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = JTColors.border,
                        unfocusedIndicatorColor = JTColors.border,
                        disabledContainerColor = JTColors.WhiteColor,
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text =  stringResource(Res.string.lastName),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = uiState.lastName ?: lastName,
                    onValueChange = { viewModel.updateLastName(it) },
                    placeholder = {
                        Text(text = stringResource(Res.string.enter_last_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = JTColors.outerColor,
                        unfocusedPlaceholderColor = JTColors.outerColor,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = JTColors.border,
                        unfocusedIndicatorColor = JTColors.border,
                        disabledContainerColor = JTColors.WhiteColor,
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(Res.string.email_address),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = uiState.email?:email,
                    onValueChange = { viewModel.updateEmail(it) },
                    placeholder = {
                        Text(text = stringResource(Res.string.enter_email))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = JTColors.outerColor,
                        unfocusedPlaceholderColor = JTColors.outerColor,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = JTColors.border,
                        unfocusedIndicatorColor = JTColors.border,
                        disabledContainerColor = JTColors.WhiteColor,
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(Res.string.contactNumber),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterRegular(),
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(7.dp))
                OutlinedTextField(
                    value = uiState.contactNumber?:phoneNumber,
                    onValueChange = { viewModel.updateContactNumber(it) },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.enter_contact_number),
                            color = JTColors.outerColor,
                            fontSize = 13.sp,
                            fontFamily = JtBtoFonts.getInterMediumFont()
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = JTColors.outerColor,
                        unfocusedPlaceholderColor = JTColors.outerColor,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = JTColors.border,
                        unfocusedIndicatorColor = JTColors.border,
                        disabledContainerColor = JTColors.WhiteColor,
                    )
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth()
                    .height(80.dp)
                    .background(color = JTColors.WhiteColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) {
                    OutlinedButton(
                        onClick = {
                            navigateBack()
                        },
                        modifier = Modifier.weight(1f).height(45.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(stringResource(Res.string.cancel), color = JTColors.BlackButtonColor)
                    }
                    Button(
                        onClick = {
                            viewModel.adminUserEdit(
                                id = id,
                                firstName = uiState.firstName?:firstName,
                                lastName = uiState.lastName?:lastName,
                                profile = uiState.avatar?:"",
                                email = uiState.email?:"",
                                onSuccess = { navigateBack() }
                            )
                        },
                        modifier = Modifier.weight(1f).height(45.dp),
                        colors = ButtonDefaults.buttonColors(JTColors.BackGroundBlueColor),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(stringResource(Res.string.save), color = JTColors.WhiteColor)
                    }
                }

            }
        }
        if (showImagePicker) {
            PermissionImagePicker(
                imageBitmap = uiState.imageBitmap,
                onImageSelected = {
                   viewModel.updateImageBitmap(it)
                    showImagePicker = false
                },
                showBottomSheet = showImagePicker,
                onDismissRequest = {
                    showImagePicker = false
                },
                filePathUri = {
                    showImagePicker = false
                    viewModel.onAvatarFileNameChange(it?:"")
                },
                byteArrayImage = {
                    viewModel.imageByteArrayChange(it?:ByteArray(0))
                    viewModel.uploadUserImage(it?:ByteArray(0))
                }

            )
        }
    }
}


