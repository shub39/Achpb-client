package com.shub39.achpb.browser.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shub39.achpb.browser.presentation.home.HomeAction
import com.shub39.achpb.browser.presentation.home.HomeState
import com.shub39.achpb.browser.presentation.settings.SettingsAction
import com.shub39.achpb.core.domain.AppDataStore
import com.shub39.achpb.core.presentation.theme.Theme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowserVM(
    private val dataStore: AppDataStore
): ViewModel() {

    private var observeThemeJob: Job? = null

    private val _theme = MutableStateFlow(Theme())
    val theme = _theme.asStateFlow()
        .onStart { observeTheme() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _theme.value
        )

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _homeState.value
        )

    fun onHomeAction(action: HomeAction) = viewModelScope.launch {
        when (action) {
            is HomeAction.OnLanguageChange -> {
                _homeState.update {
                    it.copy(
                        language = action.language
                    )
                }
                // TODO: Update Images
            }
        }
    }

    fun onSettingsAction(action: SettingsAction) = viewModelScope.launch {
        when (action) {
            is SettingsAction.OnSystemThemeChange -> dataStore.setIsSystemTheme(action.pref)
            is SettingsAction.OnIsDarkChange -> dataStore.setDarkTheme(action.pref)
            is SettingsAction.OnAmoledChange -> dataStore.setAmoled(action.pref)
            is SettingsAction.OnSeedChange -> dataStore.setSeedColor(action.seed)
            is SettingsAction.OnStyleChange -> dataStore.setPaletteStyle(action.style)
        }
    }

    private fun observeTheme() = viewModelScope.launch {
        observeThemeJob?.cancel()
        observeThemeJob = launch {
            combine(
                dataStore.getDarkThemeFlow(),
                dataStore.getIsSystemThemeFlow(),
                dataStore.getSeedColorFlow(),
                dataStore.getPaletteStyleFlow(),
                dataStore.getAmoledFlow()
            ) { isDark, onSystem, seedColor, style, withAmoled ->
                _theme.update {
                    it.copy(
                        isDark = isDark,
                        onSystem = onSystem,
                        seedColor = seedColor,
                        style = style,
                        withAmoled = withAmoled
                    )
                }
            }.launchIn(this)
        }
    }
}