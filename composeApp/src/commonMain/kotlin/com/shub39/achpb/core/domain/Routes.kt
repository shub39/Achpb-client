package com.shub39.achpb.core.domain

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object HomePage: Routes

    @Serializable
    data object SettingsPage: Routes
}