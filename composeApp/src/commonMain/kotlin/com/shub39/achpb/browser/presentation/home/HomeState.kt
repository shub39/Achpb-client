package com.shub39.achpb.browser.presentation.home

import androidx.compose.material3.SnackbarHostState
import com.shub39.achpb.browser.domain.Image
import com.shub39.achpb.browser.domain.Language
import com.shub39.achpb.core.presentation.UiText

data class HomeState (
    val homeStateDef: HomeStateDef = HomeStateDef.Loading,
    val images: List<Image> = emptyList(),
    val language: Language? = null,
    val girlsRepoLangs: List<Language> = emptyList(),
    val boysRepoLangs: List<Language> = emptyList(),
    val selectedTab: Int = 0,
    val error: UiText? = null,
    val snackBarHost: SnackbarHostState = SnackbarHostState()
)