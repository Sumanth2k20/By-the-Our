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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bytheour.utils.JTColors
import com.bytheour.utils.FACILITY_RENTALS
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.getKoinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRentalsScreen(
    viewModel: ExploreTabViewModel = getKoinViewModel(),
    navigateBack: () -> Unit,
    id: String,
    title: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyGridState()

    LaunchedEffect(id) {
        if (title.equals(FACILITY_RENTALS, ignoreCase = true)) {
            viewModel.loadFacilityRentals(categoryId = id)
        } else {
            viewModel.loadEquipmentRentals(categoryId = id)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }.collect { shouldLoadMore ->
            if (title.equals(FACILITY_RENTALS, ignoreCase = true)) {
                if (shouldLoadMore && uiState.hasMoreFacilityData && !uiState.isLoadingMoreFacility) {
                    viewModel.loadMoreFacilityRentals()
                }
            } else {
                if (shouldLoadMore && uiState.hasMoreEquipmentData && !uiState.isLoadingMoreEquipment) {
                    viewModel.loadMoreEquipmentRentals()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                JtBtoCommonText(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = JTColors.WhiteColor
                )
            },
            navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
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

        if (title.equals(FACILITY_RENTALS, ignoreCase = true)) {
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(uiState.facilityRentals) {
                    FacilityRentalsCard(
                        image = it.image ?: "",
                        title = it.title ?: "",
                        onClick = {  }
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(uiState.equipmentRentals) {
                    EquipmentRentalCard(
                        image = it.image ?: "",
                        title = it.title ?: "",
                        id = it.id ?: "",
                        description = it.description ?: "",
                        onClick = { }
                    )
                }
            }
        }
    }
}

