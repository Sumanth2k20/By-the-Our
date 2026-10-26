package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class JTActionSheetDialog (){
    fun show(
        title: String?= null,
        options: List<String>,
        onOptionSelected: (Int) -> Unit,
    )

    @Composable
    fun JTDialogContent()
    fun dismiss()
}

data class AlertButton(
    val text: String,
    val color: Color
)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class JTActionAlertBox (){
    fun show(
        title: String?= null,
        text: String?= null,
        buttons: List<AlertButton>,
        onOptionSelected: (Int) -> Unit,
    )

    @Composable
    fun JTAlertContent()
    fun dismiss()
}