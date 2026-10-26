package com.bytheour.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource
import platform.Foundation.setValue
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIColor

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class JTActionSheetDialog {

    private var _isVisible by mutableStateOf(false)
    private var _options by mutableStateOf<List<String>>(emptyList())
    private var _onOptionSelected: ((Int) -> Unit)? = null
    actual fun show(
        title: String?,
        options: List<String>,
        onOptionSelected: (Int) -> Unit,
    ) {
        _options = options
        _onOptionSelected = onOptionSelected
        _isVisible = true
    }
    actual fun dismiss() {
        _isVisible = false
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    actual fun JTDialogContent() {
        if (!_isVisible) return

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { dismiss() },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.Transparent,
            modifier = Modifier.padding(10.dp),
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 4.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = JTColors.iosWhite, shape = RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        _options.forEachIndexed { index, option ->
                            Text(
                                text = option,
                                color = JTColors.iosButton,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        _onOptionSelected?.invoke(index)
                                        dismiss()
                                    }
                                    .padding(vertical = 16.dp, horizontal = 20.dp)
                            )
                            if (index != _options.lastIndex) {
                                Divider(color = Color.LightGray, thickness = 1.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = JTColors.iosButton1,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { dismiss() }
                            .padding(16.dp)
                    )
                }
            }
        }
    }


}

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
    fun colorToUIColor(color: Color): UIColor {
        return UIColor(
            red = color.red.toDouble(),
            green = color.green.toDouble(),
            blue = color.blue.toDouble(),
            alpha = color.alpha.toDouble()
        )
    }

     private fun jTShowAlert() {
        val alertController = UIAlertController.alertControllerWithTitle(
            _title,
            _text,
            UIAlertControllerStyleAlert
        )

        _buttons.forEachIndexed { index, button ->
            val action = UIAlertAction.actionWithTitle(
                button.text,
                UIAlertActionStyleDefault
            ) { _ ->
                _onOptionSelected?.invoke(index)
                _isVisible = false
            }

            // Set title text color using KVC (Key-Value Coding)
            action.setValue(colorToUIColor(button.color), forKey = "titleTextColor")

            alertController.addAction(action)
        }
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.presentViewController(alertController, animated = true, completion = null)
    }

    @Composable
    actual fun JTAlertContent(){
        if (_isVisible) {
            jTShowAlert()
        }

    }


    actual fun dismiss() {
        _isVisible = false
    }
}