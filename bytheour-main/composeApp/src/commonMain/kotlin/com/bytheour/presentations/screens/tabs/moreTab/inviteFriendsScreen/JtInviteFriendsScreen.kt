package com.bytheour.presentations.screens.tabs.moreTab.inviteFriendsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.frame_427319932
import bytheour.composeapp.generated.resources.invite
import bytheour.composeapp.generated.resources.invite_Friends
import bytheour.composeapp.generated.resources.invite_Now
import bytheour.composeapp.generated.resources.invite_Your_Friends
import bytheour.composeapp.generated.resources.share_the_convenience
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreenViewModel
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.ShareServiceProvider
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JtInviteFriendsScreen(
    onBackClick:() -> Unit,
    moreTabScreenViewModel: JTMoreTabScreenViewModel = getKoinViewModel()
) {
    var isBackArrowClicked by remember { mutableStateOf(false) }

    val uiState by moreTabScreenViewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        moreTabScreenViewModel.getInviteCode()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.invite_Friends),
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            },
            isBackArrowClicked = isBackArrowClicked,
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Main invite card
            Card(
                colors = CardDefaults.cardColors(Color(0xFFF7F7F7)),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth().height(461.dp).width(336.dp)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.invite),
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    JtBtoCommonText(
                        text = stringResource(Res.string.invite_Your_Friends),
                        fontSize = 23.sp,
                        lineHeight = 20.sp,
                        maxLines = 2,
                        fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    JtBtoCommonText(
                        text = stringResource(Res.string.share_the_convenience),
                        fontSize = 13.sp,
                        lineHeight = 14.sp,
                        maxLines = 10,
                        fontFamily = JtBtoFonts.getOpenSansRegular(),
                        fontWeight = W400,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val title = uiState.inviteText?:""
                            val url = uiState.inviteLink?:""
                            val code = uiState.inviteCode?:""
                            val shareService = ShareServiceProvider().getShareService()
                            shareService.share(title, url,code)
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.height(45.dp).width(170.dp),
                        colors = ButtonDefaults.buttonColors(JTColors.enableButton)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        JtBtoCommonText(
                            stringResource(Res.string.invite_Now),
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            fontFamily = JtBtoFonts.getOpenSansRegular(),
                            fontWeight = W400,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Image(
                painter = painterResource(Res.drawable.frame_427319932),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
