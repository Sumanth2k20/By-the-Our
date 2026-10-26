package com.bytheour.presentations.screens.tabs.moreTab.documentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.camera_ico1
import bytheour.composeapp.generated.resources.documents
import bytheour.composeapp.generated.resources.no_documents
import coil3.compose.AsyncImage
import com.bytheour.utils.JTColors
import com.bytheour.showProgress
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentationScreen(
    viewModel: DocumentationScreenViewModel = getKoinViewModel(),
    onBackPress: () -> Unit,
    navigateToImageFullScreen: (String,String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBackArrowClicked by remember { mutableStateOf(false) }


    LaunchedEffect(Unit){
        viewModel.fetchDocuments()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.documents),
                        color = JTColors.WhiteColor,
                        fontFamily = JtBtoFonts.getOpenSansBold(),
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        enabled = !isBackArrowClicked,
                        onClick = {
                        isBackArrowClicked = true
                        onBackPress()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
            )
        }
    ) { padding ->
        if (!showProgress.value && uiState.documents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_documents),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = JtBtoFonts.getInterMediumFont()
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.documents) { doc ->
                    DocumentCard(
                        imagePath = doc.file,
                        imageDescription = doc.description,
                        onClick = {
                            navigateToImageFullScreen(
                                doc.file,
                                doc.description
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentCard(
    imagePath: String,
    imageDescription: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = imagePath,
                contentDescription = null,
                modifier = Modifier

                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.camera_ico1)
            )
            Text(
                text = imageDescription,
                modifier = Modifier
                    .padding(8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = JtBtoFonts.getInterBoldFont(),
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

