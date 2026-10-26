package com.bytheour.presentations.screens.tabs.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.categories
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerCategoriesScreen(
    viewModel: ExploreTabViewModel = getKoinViewModel(),
    navigateToAvailabilityScreen:(String,String)-> Unit,
    navigateBack:()-> Unit
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getCategories()
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ){
        CenterAlignedTopAppBar(
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.categories),
                    fontWeight = FontWeight.Bold,
                    color = JTColors.WhiteColor
                )
            },
            navigationIcon = {
                IconButton(onClick = {navigateBack()}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = JTColors.WhiteColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(JTColors.BackGroundBlueColor)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)
        ) {
            items(uiState.categories) { category ->
                CategoryCard(
                    image = category.image ?: "",
                    title = category.title ?: "",
                    id = category.id?:"",
                    onClick = { navigateToAvailabilityScreen(category.id?:"",category.title?:"") }
                )
            }
        }
    }
}


