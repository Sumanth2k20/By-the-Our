package com.bytheour.presentations.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.permission
import com.bytheour.utils.PermissionType
import com.bytheour.utils.usePermission
import org.jetbrains.compose.resources.stringResource

@Composable
fun JTAskPermission() {
    val permission = usePermission(
        permissionType = PermissionType.RECORD_AUDIO,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    permission()
                }
            ) {
                Text(stringResource(Res.string.permission))
            }
        }
    }

}