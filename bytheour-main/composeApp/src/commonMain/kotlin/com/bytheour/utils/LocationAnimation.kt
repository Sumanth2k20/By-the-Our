package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bytheour.presentations.screens.location.JTLocationFetchingViewModel

@Composable
expect fun LocationAnimation(uiState: JTLocationFetchingViewModel.LocationUiSate)


@Composable
expect fun SearchAnimation(modifier: Modifier)