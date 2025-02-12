package com.shub39.achpb.browser.presentation.home

sealed interface HomeAction {
    data class OnLanguageChange(val language: String): HomeAction
}