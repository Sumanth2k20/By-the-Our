package com.bytheour.utils


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun CustomAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    yes:Boolean,
    no:Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    changeText:Boolean = false,
    changedText:String = "",
    yesText:String? = null,
    noText:String? = null,
    yesButtonColor:Color? = null,
    noButtonColor:Color? = null,

) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
//                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (title),
                        color = JTColors.enableButton,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = JtBtoFonts.getInterFont(),


                        )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box (contentAlignment = Alignment.Center){
                        Text(
                            text = (message),
                            color = Color.Black,
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            fontFamily = JtBtoFonts.getInterLightFont(),
                            style = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.Center
                    ) {      if (no) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = noButtonColor?:Color(0xFFEEECEF),
                            ),
                            border = BorderStroke(1.dp, Color(0xFFEEECEF))
                        ) {
                            Text(
                                text =noText?:"NO",
                                fontSize = 12.sp,
                                fontFamily = JtBtoFonts.getOpenSansBold(),
                                color = Color.Black,
                            )
                        }
                    }

                        if (yes) {
                            Spacer(modifier = Modifier.width(25.dp))
                            OutlinedButton(
                                onClick = onConfirm,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = yesButtonColor?:JTColors.enableButton,
                                ),
                                border = BorderStroke(1.dp, Color(0xFFEEECEF))
                            ) {
                                Text(
                                    text = yesText?:"Yes",
                                    fontSize = 12.sp,
                                    fontFamily = JtBtoFonts.getOpenSansBold(),
                                    color = Color.White,

                                )
                            }
                        }



                        if (changeText) {
                            Button(
                                onClick = { onConfirm() },
                                modifier = Modifier
                                    .height(45.dp)
                                    .padding(start = 6.dp)
                                    .wrapContentWidth(),
                                shape = RoundedCornerShape(7.dp),
                                colors = ButtonDefaults.outlinedButtonColors(JTColors.enableButton)
                            ) {

                                Text(
                                    text = changedText,
                                    color = Color.White,
                                    fontSize = 15.sp
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}