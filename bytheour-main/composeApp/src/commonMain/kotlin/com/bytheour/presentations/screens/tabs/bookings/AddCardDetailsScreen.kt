package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.add_card
import bytheour.composeapp.generated.resources.add_card_details
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.calendar_2_1
import bytheour.composeapp.generated.resources.cardNumber
import bytheour.composeapp.generated.resources.cvc
import bytheour.composeapp.generated.resources.dots
import bytheour.composeapp.generated.resources.expiry_date
import bytheour.composeapp.generated.resources.mm_yy
import bytheour.composeapp.generated.resources.zero
import com.bytheour.presentations.customcomponents.JTBtoCustomTextField
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDetailsScreen(
    addCardDetailsViewModel:AddCardDetailsViewModel = getKoinViewModel(),
    navigateToCardList:()->Unit
) {
    val uiState by addCardDetailsViewModel.uiStates.collectAsState()
    Column (modifier = Modifier.fillMaxSize()
        .background(color = JTColors.WhiteColor),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = {
                JtBtoCommonText(
                    text = stringResource(Res.string.add_card_details),
                    fontSize = 15.sp,
                    fontFamily = JtBtoFonts.getOpenSansBold(),
                    color = JTColors.WhiteColor,
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(Res.drawable.arrowleft),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = JTColors.enableButton
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        JTBtoCustomTextField(
            name = stringResource(Res.string.cardNumber),
            value = uiState.cardNumber ?: "",
            onValueChange = { addCardDetailsViewModel.updateCard(it) },
            placeholder = stringResource(Res.string.zero),
            errorMessage = uiState.errorMessageCardNumber,
            modifier = Modifier
                .padding(12.dp)

        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        )
        {
            JTBtoCustomTextField(
                name = stringResource(Res.string.expiry_date),
                value = uiState.expiry ?: "",
                onValueChange = { addCardDetailsViewModel.updateExpiry(it) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(Res.string.mm_yy),
                errorMessage = uiState.errorMessageExpiry,
                keyboardType = KeyboardType.Number,
                trailingIcon = {
                    Icon(painter = painterResource(Res.drawable.calendar_2_1),
                        contentDescription = null,
                        )
                },
            )
            JTBtoCustomTextField(
                name = stringResource(Res.string.cvc),
                value = uiState.cvc ?: "",
                onValueChange = { addCardDetailsViewModel.updateCvc(it) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(Res.string.dots),
                errorMessage = uiState.errorMessageCvc,
                keyboardType = KeyboardType.Number,
                passwordText = true
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
                .shadow(elevation = 0.5.dp),
            contentAlignment = Alignment.Center
        )
        {
            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(0.9f),
                name =
                stringResource(Res.string.add_card),
                onClick = {
                    if (addCardDetailsViewModel.validation()){
                        navigateToCardList()
                    }
                },
                buttonColors = JTColors.enableButton,
                selectedColor = JTColors.enableButton,
                textColor = Color.White,

            )
            Spacer(modifier = Modifier.height(22.dp))

        }
    }
}