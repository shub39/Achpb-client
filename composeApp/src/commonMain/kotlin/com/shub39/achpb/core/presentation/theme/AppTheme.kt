package com.shub39.achpb.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme

@Composable
fun AppTheme(
    theme: Theme = Theme(),
    content: @Composable () -> Unit
) {
    DynamicMaterialTheme(
        useDarkTheme = if (theme.onSystem) isSystemInDarkTheme() else theme.isDark,
        withAmoled = theme.withAmoled,
        style = theme.style,
        seedColor = Color(theme.seedColor),
        content = content
    )
}