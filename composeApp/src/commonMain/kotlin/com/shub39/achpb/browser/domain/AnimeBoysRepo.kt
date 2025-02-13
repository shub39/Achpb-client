package com.shub39.achpb.browser.domain

import com.shub39.achpb.core.domain.DataError
import com.shub39.achpb.core.domain.Result

interface AnimeBoysRepo {
    suspend fun getLanguages(): Result<List<Language>, DataError>
    suspend fun getImagesForLang(language: Language): Result<List<Image>, DataError>
    suspend fun getRandom(): Result<Random, DataError>
}