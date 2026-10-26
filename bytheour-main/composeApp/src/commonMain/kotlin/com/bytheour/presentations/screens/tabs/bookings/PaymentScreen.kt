package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.add_new_card
import bytheour.composeapp.generated.resources.arrowleft
import bytheour.composeapp.generated.resources.card_select_ico
import bytheour.composeapp.generated.resources.debit_credit
import bytheour.composeapp.generated.resources.expires
import bytheour.composeapp.generated.resources.payment
import bytheour.composeapp.generated.resources.proceed_to_pay
import bytheour.composeapp.generated.resources.select_debit_credit
import bytheour.composeapp.generated.resources.total_payable
import com.bytheour.presentations.customcomponents.JTBtoPrimaryButton
import com.bytheour.utils.JTColors
import com.bytheour.utils.JtBtoCommonText
import com.bytheour.utils.JtBtoFonts
import com.bytheour.utils.getKoinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    addCardDetailsViewModel: AddCardDetailsViewModel = getKoinViewModel(),
    navigateToAddNewCard: () -> Unit,
    navigateBack: () -> Unit,
    bookingId: String
) {
    val uiState by addCardDetailsViewModel.uiStates.collectAsState()

    LaunchedEffect(Unit) {
        addCardDetailsViewModel.updateBookingId(bookingId)
        addCardDetailsViewModel.getCard()
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = JTColors.paymentBg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = {
                    JtBtoCommonText(
                        text = stringResource(Res.string.payment),
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
                        modifier = Modifier.padding(start = 10.dp).clickable { navigateBack() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = JTColors.enableButton
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (uiState.cardDetails.isNotEmpty()) {
                DebitOrCreditCards(
                    onCardSelected = {
                    },
                    uiState = uiState,
                    navigateToAddNewCard = navigateToAddNewCard
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxHeight(0.8f),
                    verticalArrangement = Arrangement.Center
                ) {
                    JTBtoPrimaryButton(
                        modifier = Modifier.fillMaxWidth(0.8f).height(40.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(
                                width = 1.dp,
                                color = JTColors.black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        name = stringResource(Res.string.add_new_card),
                        textColor = JTColors.enableButton,
                        onClick = {
                            navigateToAddNewCard()
                        },
                        buttonColors = JTColors.addCard,
                        selectedColor = JTColors.addCard,
                        isSelected = true
                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))
            if (uiState.cardDetails.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(100.dp)
                        .shadow(elevation = 0.5.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {

                        Column {
                            JtBtoCommonText(
                                text = stringResource(Res.string.total_payable),
                                fontSize = 12.sp,
                                color = JTColors.RadioText,
                                fontFamily = JtBtoFonts.getOpenSansRegular(),
                            )
                            JtBtoCommonText(
                                text = "\$5,00.64",
                                fontSize = 14.sp,
                                color = JTColors.RadioText,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))


                        JTBtoPrimaryButton(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            name = stringResource(Res.string.proceed_to_pay),
                            onClick = {
                            },
                            buttonColors = JTColors.enableButton,
                            selectedColor = JTColors.enableButton,
                            textColor = Color.White,

                            )

                    }

                }
            }

        }
    }
}

@Composable
fun DebitOrCreditCards(
    onCardSelected: (Int) -> Unit,
    uiState: AddCardDetailsViewModel.UIStates,
    navigateToAddNewCard: () -> Unit

) {
    var selectedCard by remember { mutableIntStateOf(0) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = JTColors.WhiteColor
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Column(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
            JtBtoCommonText(
                text = stringResource(Res.string.debit_credit),
                fontSize = 15.sp,
                color = JTColors.enableButton,
                fontFamily = JtBtoFonts.getOpenSansSemiBold(),
                modifier = Modifier.padding(start = 16.dp)
            )
            JtBtoCommonText(
                text = stringResource(Res.string.select_debit_credit),
                fontSize = 11.sp,
                color = JTColors.disableButton,
                fontFamily = JtBtoFonts.getOpenSansRegular(),
                modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = JTColors.border)
            LazyColumn {
                items(uiState.cardDetails) { card ->
                    CardItem(
                        isSelected = uiState.cardDetails.indexOf(card) == selectedCard,
                        onClick = {
                            selectedCard = uiState.cardDetails.indexOf(card)
                            onCardSelected(selectedCard)
                        },
                        cardUIState = card,
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = JTColors.border,
                        thickness = 1.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            JTBtoPrimaryButton(
                modifier = Modifier.fillMaxWidth(0.8f).height(40.dp)
                    .align(Alignment.CenterHorizontally),
                name = stringResource(Res.string.add_new_card),
                textColor = JTColors.enableButton,
                onClick = {
                    navigateToAddNewCard()
                },
                buttonColors = JTColors.addCard,
                selectedColor = JTColors.addCard,
                isSelected = true
            )
        }


    }
}

@Composable
fun CardItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    cardUIState: AddCardDetailsViewModel.CardUIState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JtBtoCommonText(
            text = cardUIState.cardType ?: "",
            fontSize = 12.sp,
            color = JTColors.disableButton,
            fontFamily = JtBtoFonts.getOpenSansSemiBold(),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {

            JtBtoCommonText(
                text = "**** **** ****" + cardUIState.lastFourDigits,
                fontSize = 12.sp,
                color = JTColors.disableButton,
                fontFamily = JtBtoFonts.getOpenSansSemiBold()
            )
            JtBtoCommonText(
                text = stringResource(Res.string.expires) + cardUIState.cardExpiry,
                fontSize = 12.sp,
                color = JTColors.disableButton,
                fontFamily = JtBtoFonts.getOpenSansRegular()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isSelected) {
            Image(
                painter = painterResource(Res.drawable.card_select_ico),
                contentDescription = null
            )
        }
    }
}
