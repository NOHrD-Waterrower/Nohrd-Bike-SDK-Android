package com.nohrd.bike.sdkv1.ble.sample.ui.theming

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = lightColors(
            primary = AppColors.nohrdBlue
        ),
        content = content
    )
}
