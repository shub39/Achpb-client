package com.shub39.achpb.browser.presentation.home

import com.shub39.achpb.browser.domain.Image
import com.shub39.achpb.browser.domain.Language
import com.shub39.achpb.core.presentation.UiText

data class HomeState (
    val images: List<Image> = emptyList(),
    val language: Language? = null,
    val girlsRepoLangs: List<Language> = emptyList(),
    val boysRepoLangs: List<Language> = emptyList(),
    val selectedTab: Int = 0,
    val error: UiText? = null
)