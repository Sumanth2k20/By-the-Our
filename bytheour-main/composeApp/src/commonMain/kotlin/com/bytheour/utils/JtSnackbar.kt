package com.bytheour.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SnackbarType { ERROR, SUCCESS, INFO }

@Composable
fun CommonSnackbar(
    message: String,
    type: SnackbarType = SnackbarType.INFO,
    actionLabel: String? = null,
    autoDismissMillis: Long = 2500L,
    onAction: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val customSnackbarHostState = remember { SnackbarHostState() }

    val (icon, bg) = when (type) {
        SnackbarType.ERROR   -> Icons.Default.Close       to Color(0xFFD32F2F)
        SnackbarType.SUCCESS -> Icons.Default.CheckCircle to Color(0xFF388E3C)
        SnackbarType.INFO    -> Icons.Default.Info        to Color(0xFF1976D2)
    }

    LaunchedEffect(message) {
        if (message.isBlank()) return@LaunchedEffect

        val resultJob = launch {
            val result = customSnackbarHostState.showSnackbar(
                message     = message,
                actionLabel = actionLabel,
                duration    = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) onAction?.invoke()
            onDismiss()
        }

        launch {
            delay(autoDismissMillis)
            customSnackbarHostState.currentSnackbarData?.dismiss()
            resultJob.cancel()
            onDismiss()
        }
    }

    Box(Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = customSnackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { data ->
                Snackbar(
                    containerColor = bg,
                    contentColor   = Color.White,
                    action = {
                        data.visuals.actionLabel?.let { label ->
                            TextButton(onClick = { data.performAction() }) {
                                Text(label, color = Color.White)
                            }
                        }
                    }
                ) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(Modifier.width(8.dp))
                        Text(data.visuals.message, fontFamily = JtBtoFonts.getInterFont())
                    }
                }
            }
        )
    }
}
