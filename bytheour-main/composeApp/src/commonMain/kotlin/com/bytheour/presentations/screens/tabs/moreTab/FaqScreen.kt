package com.bytheour.presentations.screens.tabs.moreTab


import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.contact_us_at
import bytheour.composeapp.generated.resources.faq
import bytheour.composeapp.generated.resources.have_more_questions
import bytheour.composeapp.generated.resources.here_to_help
import bytheour.composeapp.generated.resources.minus_ico
import bytheour.composeapp.generated.resources.plus_ico
import bytheour.composeapp.generated.resources.total_FAQs
import com.bytheour.utils.JTColors
import com.bytheour.utils.INFO_URL
import com.bytheour.utils.JtBtoCommonTopAppBarForMoreScreens
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun FAQScreen(
    viewModel: FaqViewModel = getKoinViewModel(),
    onBackClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadFaq()
    }

    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore && uiState.hasMoreData && !uiState.isLoadingMore) {
                    viewModel.loadMoreData()
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top AppBar
        JtBtoCommonTopAppBarForMoreScreens(
            title = stringResource(Res.string.faq),
            showBackButton = true,
            isBackArrowClicked = isBackArrowClicked,
            onBackClick = {
                isBackArrowClicked = true
                onBackClick()
            }
        )

        HorizontalDivider(Modifier.fillMaxWidth(), color = JTColors.border)

        Column(
            modifier = Modifier
                .background(JTColors.faqBackGround)
                .fillMaxSize()
        ) {
            if (uiState.totalCount != null && uiState.totalCount!! > 0) {
                Text(
                    text = stringResource(Res.string.total_FAQs)+"${uiState.totalCount}",
                    fontSize = 12.sp,
                    fontFamily = JtBtoFonts.getInterFont(),
                    color = JTColors.BackGroundBlueColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(JTColors.faqBackGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = uiState.faqList,
                        key = { "${it.question}_${it.answer}" }
                    ) { item ->
                        ExpandableSection(
                            title = item.question,
                            content = item.answer,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .background(Color.Transparent)
                        ) {
                            Text(
                                text = stringResource(Res.string.have_more_questions),
                                fontWeight = FontWeight.W700,
                                fontSize = 10.sp,
                                lineHeight = 16.sp,
                                color = JTColors.BackGroundBlueColor,
                                textAlign = TextAlign.Start
                            )
                            val annotatedText = buildAnnotatedString {
                                val displayText = INFO_URL
                                val messageStart = stringResource(Res.string.contact_us_at)
                                val messageEnd = stringResource(Res.string.here_to_help)

                                append(messageStart)
                                pushStringAnnotation(tag = "URL", annotation = displayText)
                                withStyle(style = SpanStyle(color = JTColors.BackGroundBlueColor, fontWeight = FontWeight.W600)) {
                                    append(displayText)
                                }
                                pop()

                                append(messageEnd)
                            }
                            Text(
                                text = annotatedText,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    lineHeight = 13.sp,
                                    color = JTColors.RadioText,
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val icon = if (isExpanded) {
        painterResource(Res.drawable.minus_ico)
    } else {
        painterResource( Res.drawable.plus_ico)
    }

    Card(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .padding(top = 5.dp, bottom = 5.dp)
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(JTColors.WhiteColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp, start = 9.dp),
                    color = JTColors.BackGroundBlueColor,
                )
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Top),
                    painter = icon,
                    contentDescription = null
                )
            }

            AnimatedVisibility(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
                visible = isExpanded
            ) {
                Box(
                    modifier.wrapContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = content,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.W400,
                        color = JTColors.RadioText,
                    )
                }
            }
        }
    }
}
