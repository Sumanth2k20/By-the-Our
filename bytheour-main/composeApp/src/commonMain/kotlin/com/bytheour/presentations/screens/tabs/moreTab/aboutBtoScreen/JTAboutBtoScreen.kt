package com.bytheour.presentations.screens.tabs.moreTab.aboutBtoScreen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.aboutUsAtOur
import bytheour.composeapp.generated.resources.aboutUsDescription1
import bytheour.composeapp.generated.resources.aboutUsDescription3
import bytheour.composeapp.generated.resources.aboutUsDescription4
import bytheour.composeapp.generated.resources.aboutUsHeading
import bytheour.composeapp.generated.resources.aboutUsHeading1
import bytheour.composeapp.generated.resources.aboutUsHeading2
import bytheour.composeapp.generated.resources.aboutUsHeading3
import bytheour.composeapp.generated.resources.aboutUsHeading4
import bytheour.composeapp.generated.resources.aboutUsMainDescription
import bytheour.composeapp.generated.resources.aboutUsMainHeading
import bytheour.composeapp.generated.resources.aboutUsSubDescription1
import bytheour.composeapp.generated.resources.aboutUsSubDescription2
import bytheour.composeapp.generated.resources.aboutUsSubDescription3
import bytheour.composeapp.generated.resources.aboutUsSubDescription4
import bytheour.composeapp.generated.resources.aboutUsSubHeading1
import bytheour.composeapp.generated.resources.aboutUsSubHeading2
import bytheour.composeapp.generated.resources.aboutUsSubHeading3
import bytheour.composeapp.generated.resources.aboutUsSubHeading4
import bytheour.composeapp.generated.resources.aboutUsVisitWebSiteText
import bytheour.composeapp.generated.resources.aboutUsWeBelieve
import bytheour.composeapp.generated.resources.customisabletask_ico
import bytheour.composeapp.generated.resources.hourlyschedule_ico
import bytheour.composeapp.generated.resources.visit_Site
import bytheour.composeapp.generated.resources.your_Time_Your_Schedule_BTO
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.utils.JTWebViewScreen
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JtAboutScreen(
    onBackClick:()-> Unit
) {

    var showWebView by remember { mutableStateOf(false) }
    var isBackArrowClicked by remember { mutableStateOf(false) }




    Column{
        if (showWebView){
            JTWebViewScreen(
                title = stringResource(Res.string.aboutUsMainHeading),
                url = "https://bytheour.com/",
                onBackPress = {showWebView = false},
                showDownBox = false
            )
        }
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.aboutUsHeading),
            showBackButton = true,
            isBackArrowClicked = isBackArrowClicked,
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { AboutBTOSection(
                onViewSiteClick = {
                    showWebView = true
                }
            ) }
            item { PurposeCard() }
            item { KeyPrinciplesSection() }
            item {
                CommitmentSection(imageUrl = "https://avatars.mds.yandex.net/i?id=bfec37451ebc9610a06cfbbff2e8af4dceb5b653-11936956-images-thumbs&n=13")
            }
            item {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton)
                ) {
                    JtBtoCommonText(
                        text = stringResource(Res.string.your_Time_Your_Schedule_BTO),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = W700,
                        lineHeight = 16.sp,
                        fontFamily = JtBtoFonts.getInterFont()
                    )
                }
            }
            item {
                CommunitySection(imageUrl = "https://www.shutterstock.com/image-photo/two-businessmen-discuss-cooperation-meet-600nw-2233871393.jpg")
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    JtBtoCommonText(
                        text = stringResource(Res.string.aboutUsVisitWebSiteText),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp,
                        color = Color(0xFF1C1C1C),
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )

                    Text(
                        text = "https://bytheour.com/",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E3AF2),
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable{
                            showWebView = true
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun AboutBTOSection(
    onViewSiteClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            JtBtoCommonText(
                text = stringResource(Res.string.aboutUsMainHeading),
                fontSize = 12.sp,
                fontWeight = W700,
                lineHeight = 16.sp,
                color = Color(0xFF303030),
                fontFamily = JtBtoFonts.getInterFont()

            )
            Spacer(modifier = Modifier.height(8.dp))
            JtBtoCommonText(
                text = stringResource(Res.string.aboutUsMainDescription),
                fontSize = 10.sp,
                fontWeight = W400,
                lineHeight = 13.sp,
                maxLines = 10,
                textAlign = TextAlign.Left,
                color = Color(0xFF313131),
                fontFamily = JtBtoFonts.getOpenSansRegular()
            )
        }

        // Image & Button
        Column(
            modifier = Modifier
                .width(140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
               model = "https://t4.ftcdn.net/jpg/03/28/49/35/360_F_328493598_yMp446SUpiIGYQQKydLKVo8aoFA7DPJ2.jpg",
                contentDescription = "User Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {onViewSiteClick()},
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(31.dp).width(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton) // Purple shade
            ) {
                JtBtoCommonText(
                    text = stringResource(Res.string.visit_Site),
                    fontSize = 12.sp,
                    fontWeight = W700,
                    lineHeight = 14.sp,
                    color = Color.White,
                    fontFamily = JtBtoFonts.getOpenSansRegular()
                )
            }
        }
    }
}


@Composable
fun PurposeCard() {
    Card(
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            JtBtoCommonText(
                stringResource(Res.string.aboutUsHeading1),
                fontSize = 12.sp,
                fontWeight = W700,
                lineHeight = 16.sp,
                color = Color(0xFF303030),
                fontFamily = JtBtoFonts.getInterFont()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle()){
                        append(stringResource(Res.string.aboutUsAtOur))
                    }
                    withStyle(style = SpanStyle(color = JTColors.blackLight,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 10.sp)){
                        append(stringResource(Res.string.aboutUsWeBelieve))
                    }
                    withStyle(style = SpanStyle()){
                        append( stringResource(Res.string.aboutUsDescription1)
                        )
                    }

                },
                fontSize = 10.sp,
                fontWeight = W400,
                lineHeight = 13.sp,
                maxLines = 10,
                color = Color(0xFF313131),
                fontFamily = JtBtoFonts.getOpenSansRegular()
            )
        }
    }
}

@Composable
fun KeyPrinciplesSection() {
    val principles = listOf(
        KeyPrinciple(
            stringResource(Res.string.aboutUsSubHeading1),
            stringResource(Res.string.aboutUsSubDescription1),
            painterResource(Res.drawable.hourlyschedule_ico)
        ),
        KeyPrinciple(
            stringResource(Res.string.aboutUsSubHeading2),
            stringResource(Res.string.aboutUsSubDescription2),
            painterResource(Res.drawable.customisabletask_ico),
            ),
        KeyPrinciple(
            stringResource(Res.string.aboutUsSubHeading3),
            stringResource(Res.string.aboutUsSubDescription3),
            painterResource(Res.drawable.hourlyschedule_ico)
        ),
        KeyPrinciple(
            stringResource(Res.string.aboutUsSubHeading4),
            stringResource(Res.string.aboutUsSubDescription4),
            painterResource(Res.drawable.customisabletask_ico)
        )
    )

    Column {
        JtBtoCommonText(
            stringResource(Res.string.aboutUsHeading2),
            fontSize = 12.sp,
            fontWeight = W700,
            lineHeight = 16.sp,
            color = Color(0xFF202020),
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(principles) { principle ->
                KeyPrincipleCard(principle)
            }
        }
    }
}

// Data class to simplify item definition
data class KeyPrinciple(
    val title: String,
    val description: String,
    val icon: Painter
)

@Composable
fun KeyPrincipleCard(principle: KeyPrinciple) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .height(220.dp)
            .background(Color(0xFFD9CDFF), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Icon(
            principle.icon,
            contentDescription = null,
            tint = Color.Black)
        Spacer(Modifier.height(8.dp))
        JtBtoCommonText(
            principle.title,
            fontSize = 12.sp,
            fontWeight = W700,
            lineHeight = 16.sp,
            color = Color(0xFF202020),
            fontFamily = JtBtoFonts.getInterFont()
        )
        Spacer(Modifier.height(4.dp))
        Text(
            principle.description,
            fontSize = 10.sp,
            fontWeight = W400,
            lineHeight = 13.sp,
            maxLines = 10,
            textAlign = TextAlign.Left,
            color = Color(0xFF1C1C1C),
            fontFamily = JtBtoFonts.getOpenSansRegular()
        )
    }
}


@Composable
fun CommitmentSection(imageUrl: String) {

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Commitment Image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )
                }

                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(Res.string.aboutUsHeading3),
                        fontSize = 12.sp,
                        fontWeight = W700,
                        lineHeight = 16.sp,
                        color = Color(0xFF202020),
                        fontFamily = JtBtoFonts.getInterFont()
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        stringResource(Res.string.aboutUsDescription3),
                        fontSize = 10.sp,
                        fontWeight = W400,
                        lineHeight = 13.sp,
                        maxLines = 10,
                        textAlign = TextAlign.Left,
                        color = Color(0xFF1C1C1C),
                        fontFamily = JtBtoFonts.getOpenSansRegular()
                    )
                }
            }

}

@Composable
fun CommunitySection(imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        JtBtoCommonText(
            text = stringResource(Res.string.aboutUsHeading4),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            color = Color(0xFF202020),
            fontFamily = JtBtoFonts.getInterFont()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Paragraph + Image Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            JtBtoCommonText(
                text = stringResource(Res.string.aboutUsDescription4),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp,
                textAlign = TextAlign.Left,
                maxLines = 10,
                color = Color(0xFF1C1C1C),
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            AsyncImage(
                model = imageUrl,
                contentDescription = "Commitment Image",
                modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }



        // Website Lin
    }
}



