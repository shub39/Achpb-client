package com.shub39.achpb.core.presentation.theme

import achpb.composeapp.generated.resources.Res
import achpb.composeapp.generated.resources.poppins_regular
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.materialkolor.PaletteStyle
import org.jetbrains.compose.resources.FontResource

data class Theme(
    val isDark: Boolean = true,
    val onSystem: Boolean = false,
    val withAmoled: Boolean = false,
    val seedColor: Int = Color.Blue.toArgb(),
    val style: PaletteStyle = PaletteStyle.TonalSpot,
    val fontResource: FontResource = Res.font.poppins_regular
)
