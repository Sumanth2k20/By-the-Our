package com.bytheour.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class JTActionSheetDialog {

    private var _isVisible by mutableStateOf(false)
    private var _title by mutableStateOf<String?>(null)
    private var _options by mutableStateOf<List<String>>(emptyList())
    private var _onOptionSelected: ((Int) -> Unit)? = null
    private var _onCancel: (() -> Unit)? = null

    actual fun show(
        title: String?,
        options: List<String>,
        onOptionSelected: (Int) -> Unit,
    ) {
        _title = title
        _options = options
        _onOptionSelected = onOptionSelected
        _isVisible = true
    }

    actual fun dismiss() {
        _isVisible = false
    }

    @Composable
    actual fun JTDialogContent() {
        if (!_isVisible) return // Do nothing if the dialog is not visible

        Dialog(onDismissRequest = {
            _onCancel?.invoke()
            dismiss()
        }) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = _title ?: "",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )

                    _options.forEachIndexed { index, option ->
                        Text(
                            text = option,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    _onOptionSelected?.invoke(index)
                                    dismiss()
                                }
                                .padding(12.dp)
                        )
                        if (index != _options.lastIndex) {
                            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.cancel),
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dismiss()
                            }
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class JTActionAlertBox {
    private var _isVisible by mutableStateOf(false)
    private var _title by mutableStateOf<String?>(null)
    private var _text by mutableStateOf<String?>(null)
    private var _buttons by mutableStateOf<List<AlertButton>>(emptyList())
    private var _onOptionSelected: ((Int) -> Unit)? = null

    actual fun show(
        title: String?,
        text: String?,
        buttons: List<AlertButton>,
        onOptionSelected: (Int) -> Unit
    ) {
        _title = title
        _text = text
        _buttons = buttons
        _onOptionSelected = onOptionSelected
        _isVisible = true
    }

    @Composable
    actual fun JTAlertContent() {
        if (_isVisible) {
            Dialog(onDismissRequest = { dismiss() }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = _title ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = _text ?: "",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 24.dp),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            _buttons.forEachIndexed { index, button ->
                                val isPrimary = index == 1 // assuming "Accept" is at index 1

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (isPrimary) button.color else Color.White
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isPrimary) button.color else Color.Gray,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable {
                                            _onOptionSelected?.invoke(index)
                                            dismiss()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = button.text,
                                        color = if (isPrimary) Color.White else button.color,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    actual fun dismiss() {
        _isVisible = false
    }
}
