package com.bytheour.presentations.screens.wellComeScreens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.access_network
import bytheour.composeapp.generated.resources.across
import bytheour.composeapp.generated.resources.bytheourlogo
import bytheour.composeapp.generated.resources.find_local
import bytheour.composeapp.generated.resources.locationicon
import bytheour.composeapp.generated.resources.next
import bytheour.composeapp.generated.resources.no_providers
import bytheour.composeapp.generated.resources.onboard_bg
import bytheour.composeapp.generated.resources.professionals
import bytheour.composeapp.generated.resources.skip
import bytheour.composeapp.generated.resources.the_easiest_way
import bytheour.composeapp.generated.resources.worldwide
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTWelcomeScreens(
    navigateToLocationEnableScreen:()-> Unit,
    viewModel: JTWelcomeViewModel = getKoinViewModel()
) {
    val uiState by viewModel.uiStates.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = uiState.currentPage,
        pageCount = { 2 }
    )



    Column(modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> FirstWelcomeScreen(
                    pagerState,
                    viewModel = viewModel,
                    navigateToLocationEnableScreen
                )
                1 -> SecondWelcomeScreen(
                    pagerState,
                    navigateToLocationEnableScreen
                )
            }
        }
    }
}

@Composable
fun FirstWelcomeScreen(
    pagerState: PagerState,
    viewModel: JTWelcomeViewModel,
    navigateToLocationEnableScreen: () -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(JTColors.WhiteColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToLocationEnableScreen() }
                .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ){
                JtBtoCommonText(
                    text = stringResource(Res.string.skip),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable { navigateToLocationEnableScreen() }
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(Res.drawable.bytheourlogo),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp).width(168.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(Res.drawable.locationicon),
                contentDescription = null,
                modifier = Modifier
                    .height(245.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(45.dp))

            Column (modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                JtBtoCommonText(
                    text = stringResource(Res.string.find_local),
                    fontSize = 26.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold()
                )

                JtBtoCommonText(
                    text = stringResource(Res.string.professionals),
                    fontSize = 26.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold()
                )

                Spacer(modifier = Modifier.height(15.dp))

                JtBtoCommonText(
                    text = stringResource(Res.string.the_easiest_way),
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                    color = JTColors.grayTextColor,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            JTBtoPrimaryButton(
                name = stringResource(Res.string.next),
                onClick = {
                    val nextPage = pagerState.currentPage + 1
                    if (nextPage < pagerState.pageCount) {
                        viewModel.onNextPage(pagerState.pageCount)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                buttonColors = JTColors.BlackButtonColor,
                textColor = JTColors.WhiteColor,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                iconColor = JTColors.WhiteColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalPagerIndicator(
                pagerCount = 2,
                currentPage = pagerState.currentPage,
                activeColor = Color.Black,
                inactiveColor = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SecondWelcomeScreen(
    pagerState: PagerState,
    navigateToLocationEnableScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = JTColors.BackGroundBlueColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.onboard_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.no_providers),
                fontSize = 26.sp,
                color = Color.White,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                textAlign = TextAlign.Center
            )
            JtBtoCommonText(
                text = stringResource(Res.string.worldwide),
                fontSize = 26.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            JtBtoCommonText(
                text = stringResource(Res.string.access_network),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            JtBtoCommonText(
                text = stringResource(Res.string.across),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                color = Color.White,
            )
        }

        // Fixed bottom section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            JTBtoPrimaryButton(
                name = stringResource(Res.string.next),
                onClick = {
                    navigateToLocationEnableScreen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                textColor = Color.Black,
                buttonColors = Color.White,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                iconColor = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalPagerIndicator(
                pagerCount = 2,
                currentPage = pagerState.currentPage,
                activeColor = JTColors.WhiteColor,
                inactiveColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}



@Composable
fun HorizontalPagerIndicator(
    pagerCount: Int,
    currentPage: Int,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerCount) { pageIndex ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (pageIndex == currentPage) activeColor else inactiveColor)
            )
        }
    }
}