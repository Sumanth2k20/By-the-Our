package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.group_ico
import bytheour.composeapp.generated.resources.manage_services
import bytheour.composeapp.generated.resources.service_added
import bytheour.composeapp.generated.resources.services
import bytheour.composeapp.generated.resources.services_Request
import bytheour.composeapp.generated.resources.services_Request_in_last_month
import bytheour.composeapp.generated.resources.servicesrequest_ico
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTServicesScreen(
    viewModel: ServiceScreenViewModel = getKoinViewModel(),
    navigateToServiceRequestScreen:()->Unit,
    navigateToManageServiceScreen:() ->Unit,
    navigateBack:() ->Unit
){
    val uiState  by  viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getAdminServiceRequest("10","0")
        viewModel.getAdminServiceManage("10","0")
    }

    Column (Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.services),
                    fontFamily = JtBtoFonts.getOpenSansExtraBold(),
                    fontSize = 15.sp,
                    modifier = Modifier,
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { navigateBack() }
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9FB))
                .padding(16.dp)
        ) {
            listOf(
                ReportItem(
                    stringResource(Res.string.services_Request),
                    uiState.serviceRequestCount ?: 0,
                    stringResource(Res.string.services_Request_in_last_month),
                    painterResource(Res.drawable.servicesrequest_ico),
                    onClick = { navigateToServiceRequestScreen() }
                ),
                ReportItem(
                    stringResource(Res.string.manage_services),
                    uiState.serviceManageCount ?: 0,
                    stringResource(Res.string.service_added),
                    painterResource(Res.drawable.group_ico),
                    onClick = {navigateToManageServiceScreen()}
                ),
            )
        }
    }
}