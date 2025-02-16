package com.shub39.achpb.browser.presentation.home

import android.content.Context
import com.shub39.achpb.browser.domain.Language

sealed interface HomeAction {
    data class OnLanguageChange(val language: Language): HomeAction
    data object OnReferesh: HomeAction
    data class OnImageDownload(
        val context: Context,
        val url: String,
        val name: String
    ): HomeAction
    data class OnTabChange(val tab: Int): HomeAction
}