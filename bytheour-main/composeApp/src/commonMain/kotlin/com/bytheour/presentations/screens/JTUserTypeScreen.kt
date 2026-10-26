package com.bytheour.presentations.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.customer_logo
import bytheour.composeapp.generated.resources.i_am
import bytheour.composeapp.generated.resources.i_am_looking_for_services
import bytheour.composeapp.generated.resources.i_offer_professional_services
import bytheour.composeapp.generated.resources.looking_for_service
import bytheour.composeapp.generated.resources.much_to_explore
import bytheour.composeapp.generated.resources.next
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.select_userType
import bytheour.composeapp.generated.resources.service_provider
import bytheour.composeapp.generated.resources.tickmark
import bytheour.composeapp.generated.resources.welcome
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTUserTypeScreen(
    viewModel: JTUserTypeViewModel = getKoinViewModel(),
    navigateToLoginScreen: (isProvider: Boolean) -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(JTColors.BackGroundBlueColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp,end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.welcome),
                fontSize = 26.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color =  JTColors.WhiteColor,
                modifier = Modifier.align(Alignment.Start)
            )

            JtBtoCommonText(
                text = stringResource(Res.string.much_to_explore),
                fontSize = 14.sp,
                color =  JTColors.WhiteColor,
                modifier = Modifier.align(Alignment.Start)
            )


            Spacer(modifier = Modifier.height(50.dp))


            JtBtoCommonText(
                text = stringResource(Res.string.i_am),
                fontSize = 26.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color =  JTColors.WhiteColor,
                modifier = Modifier.align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(20.dp))

            ServiceInfoBox(
                title = stringResource(Res.string.looking_for_service),
                subtitle = stringResource(Res.string.i_am_looking_for_services),
                profileLogo = painterResource(Res.drawable.customer_logo),
                tickImage = painterResource(Res.drawable.tickmark),
                isSelected = selectedIndex == 0,
                onClick = {
                    selectedIndex = 0
                    showError = false
                    viewModel.updateSelectedType(TYPE_CUSTOMER)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            ServiceInfoBox(
                title = stringResource(Res.string.service_provider),
                subtitle = stringResource(Res.string.i_offer_professional_services),
                profileLogo = painterResource(Res.drawable.provider_logo),
                tickImage = painterResource(Res.drawable.tickmark),
                isSelected = selectedIndex == 1,
                onClick = {
                    selectedIndex = 1
                    showError = false
                    viewModel.updateSelectedType(TYPE_PROVIDER)
                }
            )

            if (showError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(Res.string.select_userType),
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            JTBtoPrimaryButton(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (selectedIndex == null) {
                        showError = true
                    } else {
                        showError = false
                        navigateToLoginScreen(selectedIndex == 1)
                    }
                },
                name = stringResource(Res.string.next),
                textColor = JTColors.blackColor,
                buttonColors = JTColors.WhiteColor
            )
        }
    }
}


@Composable
fun ServiceInfoBox(
    title: String,
    subtitle: String,
    profileLogo: Painter,
    tickImage: Painter,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(90.dp)
            .border(
                1.5.dp,
                JTColors.WhiteColor,
                RoundedCornerShape(6.dp)
            )
            .background(if (isSelected) JTColors.WhiteColor.copy(alpha = 0.13f) else JTColors.BackGroundBlueColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = profileLogo,
                    contentDescription = null,
                    modifier = Modifier,
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                JtBtoCommonText(
                    text = title,
                    fontSize = 18.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    color =  JTColors.WhiteColor,
                )

                JtBtoCommonText(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getOpenSansRegular(),
                    color =  JTColors.WhiteColor,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (isSelected){
                Image(
                    painter = tickImage,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(50.dp)
                )
            }
        }
    }
}
