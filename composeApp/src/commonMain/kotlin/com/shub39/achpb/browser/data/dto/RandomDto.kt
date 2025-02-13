package com.shub39.achpb.browser.data.dto

import com.shub39.achpb.browser.domain.Image
import com.shub39.achpb.browser.domain.Language
import com.shub39.achpb.browser.domain.Random
import kotlinx.serialization.Serializable

@Serializable
data class RandomDto(
    val language: String,
    val image: String
)

fun RandomDto.toRandom(): Random {
    return Random(
        language = Language(language),
        image = Image(image)
    )
}