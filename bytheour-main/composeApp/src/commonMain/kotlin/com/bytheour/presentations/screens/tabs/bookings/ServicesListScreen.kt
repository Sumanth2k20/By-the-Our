package com.bytheour.presentations.screens.tabs.bookings

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.frame_427319932
import bytheour.composeapp.generated.resources.lawn_services
import bytheour.composeapp.generated.resources.maintain_lawn
import bytheour.composeapp.generated.resources.no_of_services
import bytheour.composeapp.generated.resources.provider_logo
import bytheour.composeapp.generated.resources.start_your_lawn
import bytheour.composeapp.generated.resources.view_all
import com.bytheour.utils.JTColors
import com.bytheour.presentations.screens.wellComeScreens.HorizontalPagerIndicator
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ServiceListScreen(
    viewModel: ServicesListViewModel = getKoinViewModel()
){
    val pagerState = rememberPagerState(initialPage = 1, pageCount = {3})
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Box(modifier = Modifier.height(250.dp)) {
                HorizontalPager(state = pagerState) {
                    Image(
                        painter = painterResource(Res.drawable.frame_427319932),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Overlay content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, top = 48.dp, end = 16.dp)
                ) {
                    IconButton(onClick = {  }) {
                       Icon(
                           imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                           contentDescription = ""
                       )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.start_your_lawn),
                        fontSize = 14.sp,
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold()
                    )

                    Text(
                        text = stringResource(Res.string.maintain_lawn),
                        fontSize = 14.sp,
                        color = JTColors.WhiteColor,
                        maxLines = 2,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold()
                    )
                }

                HorizontalPagerIndicator(
                    pagerCount = 3,
                    currentPage = pagerState.currentPage,
                    activeColor = JTColors.WhiteColor,
                    inactiveColor = JTColors.BlackButtonColor,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Column (modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp)
        ){
            Text(
                text = stringResource(Res.string.lawn_services),
                fontSize = 15.sp,
                fontFamily = JtBtoFonts.getOpenSansBold()
            )

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "4.85",
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold()
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(Res.string.no_of_services),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold()
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column (modifier = Modifier.fillMaxWidth()
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(uiState) {
                    ServiceCard(
                        image = painterResource(Res.drawable.provider_logo),
                        title = it
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {  }
            ){
                Text(
                    text = stringResource(Res.string.view_all),
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    image:Painter,
    title:String,
){
    Column(
        modifier = Modifier
            .height(100.dp)
            .width(65.dp)
            .background(JTColors.WhiteColor, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height(5.dp))
        Card(modifier = Modifier.width(80.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = JTColors.WhiteColor)
        ) {
            Image(
                painter = image,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            lineHeight = 13.sp,
            letterSpacing = 0.5.sp,
            minLines = 2,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}
