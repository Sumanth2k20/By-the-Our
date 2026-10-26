package com.bytheour

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.about_current_location
import bytheour.composeapp.generated.resources.about_current_location_info
import bytheour.composeapp.generated.resources.enable_location
import bytheour.composeapp.generated.resources.enable_location_info
import bytheour.composeapp.generated.resources.learn_more
import bytheour.composeapp.generated.resources.locationlayer
import bytheour.composeapp.generated.resources.ok_i_understand
import bytheour.composeapp.generated.resources.uses_your_location
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.JtRequestLocationPermission
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JtLocationEnableScreen(
    navigateToUserTypeScreen: () -> Unit = {}
){

    JtRequestLocationPermission { granted ->
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(40.dp))
        Column (modifier = Modifier
            .fillMaxWidth(0.9f)
        ){
            JtBtoCommonText(
                text = stringResource(Res.string.enable_location),
                fontSize = 26.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            JtBtoCommonText(
                text = stringResource(Res.string.enable_location_info),
                fontSize = 17.sp,
                lineHeight = 17.sp,
                maxLines = 2,
                fontFamily = JtBtoFonts.getInterMediumFont(),
                textAlign = TextAlign.Start,
                color = JTColors.grayTextColor
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        Image(
            painter = painterResource(Res.drawable.locationlayer),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        JtBtoCommonText(
            text = stringResource(Res.string.uses_your_location),
            fontSize = 15.sp,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            modifier = Modifier.padding(start = 10.dp),
            minLines = 3,
            maxLines = 3,
            lineHeight = 19.sp,
            textAlign = TextAlign.Start,
            color = JTColors.grayTextColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        JtBtoCommonText(
            text = stringResource(Res.string.learn_more),
            fontSize = 20.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.align(Alignment.Start)
                .padding(start = 10.dp).clickable { showBottomSheet = true }
        )

        Spacer(modifier = Modifier.height(20.dp))

        JTBtoPrimaryButton(
            name = stringResource(Res.string.ok_i_understand),
            onClick = {
                navigateToUserTypeScreen()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            buttonColors = JTColors.blackColor,
            textColor = Color.White,
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
    if (showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = JTColors.WhiteColor
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showBottomSheet = false },
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

                ContentItem(
                    title = stringResource(Res.string.about_current_location),
                    description = stringResource(Res.string.about_current_location_info)
                )
            }
        }
    }
}


@Composable
fun ContentItem(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(8.dp),
            colors = CardDefaults.cardColors(JTColors.BackGroundBlueColor)
        ) {}
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = JTColors.BackGroundBlueColor,
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = JTColors.blackColor,
                    lineHeight = 20.sp
                )
            )
        }
    }
}