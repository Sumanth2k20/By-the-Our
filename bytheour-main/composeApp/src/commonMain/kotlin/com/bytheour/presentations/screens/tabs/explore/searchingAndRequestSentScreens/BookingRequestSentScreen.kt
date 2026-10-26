package com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.booking
import bytheour.composeapp.generated.resources.bookingId
import bytheour.composeapp.generated.resources.booking_Request
import bytheour.composeapp.generated.resources.booking_Request_Sent
import bytheour.composeapp.generated.resources.congratulations_splash
import bytheour.composeapp.generated.resources.image_1
import bytheour.composeapp.generated.resources.notify_you_shortly_once
import bytheour.composeapp.generated.resources.thank_You
import bytheour.composeapp.generated.resources.thankyou_ico
import bytheour.composeapp.generated.resources.to_track_your_booking
import bytheour.composeapp.generated.resources.transparent_bg
import bytheour.composeapp.generated.resources.view_Bookings
import bytheour.composeapp.generated.resources.visit
import bytheour.composeapp.generated.resources.your_booking_request_has_been_sent
import coil3.compose.rememberAsyncImagePainter
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoFonts
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun BookingConfirmationScreen(
    serviceName:String,
    address: String,
    imageUrl:String,
    bookingId: String,
    navigateToMainScreen: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JTColors.WhiteColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().background(JTColors.WhiteColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                val imageToLoad = if (imageUrl.isEmpty()) {
                    painterResource(Res.drawable.image_1)
                } else {
                    rememberAsyncImagePainter(model = imageUrl)
                }

                Image(
                    painter = imageToLoad,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(Res.drawable.transparent_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top navigation and title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        text = stringResource(Res.string.booking_Request),
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        textAlign = TextAlign.Center
                    )

                }

                // Title + Booking ID
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        serviceName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterRegular(),
                                color = JTColors.bookingId
                            )){
                                append(text = stringResource(Res.string.bookingId))
                            }
                            withStyle(style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterBoldFont(),
                                color = JTColors.WhiteColor
                            )){
                                append(text = bookingId)
                            }
                        },
                    )
                }
            }

            Column (
                modifier = Modifier
                    .weight(1f) // This makes the column take remaining space
                    .verticalScroll(rememberScrollState())
            ){
                // Bottom content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        stringResource(Res.string.booking_Request_Sent),
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp
                    )
                    Text(
                        address,
                        color = Color(0xFF777777),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = JtBtoFonts.getInterRegular(),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = JTColors.border.copy(alpha = 40f)
                )
                Column(
                    modifier = Modifier.background(Color(0xFFFBFBFF)).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.congratulations_splash),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                        Image(
                            painter = painterResource(Res.drawable.thankyou_ico),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Text(
                        stringResource(Res.string.thank_You),
                        color = JTColors.enableButton,
                        fontSize = 22.sp,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                    )
                    Text(
                        stringResource(Res.string.your_booking_request_has_been_sent),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = JTColors.border.copy(alpha = 40f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        stringResource(Res.string.notify_you_shortly_once),
                        color = Color(0xFF1C1C1C),
                        fontFamily = JtBtoFonts.getInterRegular(),
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterRegular()
                            ),
                            ) {
                                append(stringResource(Res.string.visit))
                            }

                            withStyle(style = SpanStyle(fontFamily = JtBtoFonts.getInterBoldFont(), fontSize = 14.sp)) {
                                append(stringResource(Res.string.booking))
                            }
                            withStyle(style = SpanStyle(
                                fontFamily = JtBtoFonts.getInterRegular())
                            ) {
                                append(stringResource(Res.string.to_track_your_booking))
                            }
                        },
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        JTBtoPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            name = stringResource(Res.string.view_Bookings),
            onClick = {
                navigateToMainScreen(true)
            },
            buttonColors =  JTColors.enableButton ,
            selectedColor = JTColors.enableButton,
            textColor = Color.White,
        )
    }
}