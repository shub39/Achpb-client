package com.shub39.achpb.browser.presentation.settings

import com.materialkolor.PaletteStyle

sealed interface SettingsAction {
    data class OnSystemThemeChange(val pref: Boolean): SettingsAction
    data class OnIsDarkChange(val pref: Boolean): SettingsAction
    data class OnAmoledChange(val pref: Boolean): SettingsAction
    data class OnSeedChange(val seed: Int): SettingsAction
    data class OnStyleChange(val style: PaletteStyle): SettingsAction
}