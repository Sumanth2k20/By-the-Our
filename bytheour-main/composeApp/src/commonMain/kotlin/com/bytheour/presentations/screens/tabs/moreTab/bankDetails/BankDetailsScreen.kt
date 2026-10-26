package com.bytheour.presentations.screens.tabs.moreTab.bankDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.account_holder_name
import bytheour.composeapp.generated.resources.account_number_last4
import bytheour.composeapp.generated.resources.bank_details
import bytheour.composeapp.generated.resources.bank_name
import bytheour.composeapp.generated.resources.na
import bytheour.composeapp.generated.resources.remove
import bytheour.composeapp.generated.resources.route_number
import bytheour.composeapp.generated.resources.status
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.utils.JTColors
import com.bytheour.utils.CommonSnackbar
import com.bytheour.utils.JTWebViewScreen
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.SnackbarType
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDetailsScreen(
    viewModel: BankDetailsViewModel = getKoinViewModel(),
    onBackPress: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getBankDetailsOrStripeConnect()
    }

    val uiState by viewModel.uiState.collectAsState()
    var showWebView by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (!uiState.hasBankDetails && uiState.link.isNotEmpty()) {
            showWebView = true
        }
    }

    Box {
        Scaffold(
            containerColor = JTColors.iosWhite,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.bank_details),
                            color = JTColors.WhiteColor,
                            fontFamily = JtBtoFonts.getOpenSansBold(),
                            fontSize = 15.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackPress() }) {
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
        ) { innerPadding ->
            when {
                uiState.hasBankDetails && uiState.bankAccounts.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.bankAccounts.forEach { account ->
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                JTBtoCustomTextField(
                                    name = stringResource(Res.string.account_holder_name),
                                    value = account.accountHolderName.ifEmpty { stringResource(Res.string.na) },
                                    placeholder = stringResource(Res.string.account_holder_name),
                                    onValueChange = {},
                                    enable = false
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                JTBtoCustomTextField(
                                    name = stringResource(Res.string.account_number_last4),
                                    value = account.last4Digits.ifEmpty { "****" },
                                    placeholder = stringResource(Res.string.account_number_last4),
                                    onValueChange = {},
                                    enable = false
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                JTBtoCustomTextField(
                                    name = stringResource(Res.string.route_number),
                                    value = account.routingNumber.ifEmpty { "N/A" },
                                    placeholder =  stringResource(Res.string.route_number),
                                    onValueChange = {},
                                    enable = false
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                JTBtoCustomTextField(
                                    name = stringResource(Res.string.bank_name),
                                    value = account.bankName.ifEmpty { "N/A" },
                                    placeholder = stringResource(Res.string.bank_name),
                                    onValueChange = {},
                                    enable = false
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                JTBtoCustomTextField(
                                    name = stringResource(Res.string.status),
                                    value = uiState.accountStatus,
                                    placeholder = stringResource(Res.string.status),
                                    onValueChange = {},
                                    enable = false
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.deleteBankDetailsOrStripeConnect()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = JTColors.enableButton),
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(stringResource(Res.string.remove), color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showWebView && uiState.link.isNotEmpty()) {
            JTWebViewScreen(
                title = "Stripe Connect",
                url = uiState.link,
                onBackPress = { showWebView = false },
                showDownBox = false,
            )
        }

        if (uiState.message?.isNotEmpty() == true){
            CommonSnackbar(
                message = uiState.message?:"",
                type = if (uiState.message!!.contains("Successfully",ignoreCase = true)){
                        SnackbarType.SUCCESS
                    }else{
                        SnackbarType.ERROR
                    },
                autoDismissMillis = 1500,
                onDismiss = {}
            )
        }
    }
}

//Routing Number: 110000000
//Account Number: 000123456789