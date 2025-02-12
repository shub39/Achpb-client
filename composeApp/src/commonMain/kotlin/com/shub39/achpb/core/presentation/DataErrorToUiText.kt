package com.shub39.achpb.core.presentation

import achpb.composeapp.generated.resources.Res
import achpb.composeapp.generated.resources.no_internet
import achpb.composeapp.generated.resources.no_results
import achpb.composeapp.generated.resources.server_error
import achpb.composeapp.generated.resources.too_many_requests
import achpb.composeapp.generated.resources.unknown
import com.shub39.achpb.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.too_many_requests
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.no_internet
        DataError.Remote.SERVER -> Res.string.server_error
        DataError.Remote.SERIALIZATION -> Res.string.unknown
        DataError.Remote.UNKNOWN -> Res.string.unknown
        DataError.Remote.NO_RESULTS -> Res.string.no_results
    }

    return UiText.StringResourceId(stringRes)
}