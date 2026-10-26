package com.bytheour.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


data class NavItem(
    val selectedIcon: DrawableResource,
    val unSelectedIcon: DrawableResource,
    val title: String
)
@Composable
fun JTBottomNavigationBar(
    navItem: List<NavItem>,
    selectedIndex: Int,
    onNavItemClicked: (Int) -> Unit,
    disabledIndexes: Set<Int> = emptySet()
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.97f)
            .height(70.dp)
            .background(
                color = JTColors.enableButton,
                shape = RoundedCornerShape(50.dp)
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItem.forEachIndexed { index, navItem ->
                val isDisabled = disabledIndexes.contains(index)
                Column(
                    modifier = Modifier
                        .alpha(if (isDisabled) 0.4f else 1f)
                        .clickable(
                            enabled = !isDisabled,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNavItemClicked(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val image: Painter = if (selectedIndex == index) {
                        painterResource(navItem.selectedIcon)
                    } else {
                        painterResource(navItem.unSelectedIcon)
                    }

                    Image(
                        painter = image,
                        contentDescription = navItem.title,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(Modifier.height(3.dp))

                    navItem.title.let {
                        JtBtoCommonText(
                            text = it,
                            fontSize = 10.sp,
                            fontWeight = W600,
                            lineHeight = 12.sp,
                            fontFamily = if (selectedIndex == index) JtBtoFonts.getOpenSansSemiBold() else JtBtoFonts.getOpenSansRegular(),
                            color = if (selectedIndex == index) JTColors.WhiteColor else JTColors.bottomBarTextUnselectedColor
                        )
                    }
                    if (selectedIndex == index) {
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .width(20.dp)
                                .background(JTColors.WhiteColor, shape = RoundedCornerShape(1.dp))
                        )
                    }else{
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .width(20.dp)
                                .background(JTColors.enableButton, shape = RoundedCornerShape(1.dp))
                        )
                    }

                }
            }
        }
    }
}
