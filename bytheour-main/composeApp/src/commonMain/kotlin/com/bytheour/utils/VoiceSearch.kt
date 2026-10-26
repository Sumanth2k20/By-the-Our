package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
expect fun VoiceSearch(onResult: (String?) -> Unit, onComplete: () -> Unit,isListening:(Boolean) -> Unit)