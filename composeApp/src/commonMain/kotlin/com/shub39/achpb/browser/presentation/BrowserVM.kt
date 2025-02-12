package com.shub39.achpb.browser.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shub39.achpb.core.domain.AppDataStore
import com.shub39.achpb.core.presentation.theme.Theme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowserVM(
    private val dataStore: AppDataStore
): ViewModel() {

    private var observeTheme: Job? = null

    private val _theme = MutableStateFlow(Theme())
    val theme = _theme.asStateFlow()
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _theme.value
        )

    private fun observeTheme() = viewModelScope.launch {
        observeTheme?.cancel()
        observeTheme = launch {
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
            }
        }
    }
}