package com.shub39.achpb.browser.presentation.home

import com.shub39.achpb.browser.domain.Language

sealed interface HomeAction {
    data class OnLanguageChange(val language: Language): HomeAction
    data class OnTabChange(val tab: Int): HomeAction
}