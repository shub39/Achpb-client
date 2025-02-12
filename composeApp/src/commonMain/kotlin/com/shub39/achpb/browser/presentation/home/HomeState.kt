package com.shub39.achpb.browser.presentation.home

import com.shub39.achpb.core.presentation.UiText

data class HomeState (
    val images: List<String> = emptyList(),
    val language: String = "",
    val error: UiText? = null
)