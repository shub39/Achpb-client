package com.shub39.achpb.browser.data.repository

import com.shub39.achpb.browser.data.dto.RandomDto
import com.shub39.achpb.browser.data.dto.toRandom
import com.shub39.achpb.browser.domain.AnimeBoysRepo
import com.shub39.achpb.browser.domain.Image
import com.shub39.achpb.browser.domain.Language
import com.shub39.achpb.browser.domain.Random
import com.shub39.achpb.core.data.safeCall
import com.shub39.achpb.core.domain.DataError
import com.shub39.achpb.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class AnimeBoysRepoImpl(
    private val httpClient: HttpClient
): AnimeBoysRepo {

    companion object {
        private const val BASE_URL = "https://api.senpy.club/v2/boys"
    }

    override suspend fun getLanguages(): Result<List<Language>, DataError> {
        val request = safeCall<List<String>> {
            httpClient.get(
                urlString = "$BASE_URL/languages"
            )
        }

        return when (request) {
            is Result.Error -> request
            is Result.Success -> Result.Success(request.data.map { Language(it) })
        }
    }

    override suspend fun getImagesForLang(language: Language): Result<List<Image>, DataError> {
        val request = safeCall<List<String>> {
            httpClient.get(
                urlString = "$BASE_URL/language/${language.name}"
            )
        }

        return when (request) {
            is Result.Error -> request
            is Result.Success -> Result.Success(request.data.map { Image(it) })
        }
    }

    override suspend fun getRandom(): Result<Random, DataError> {
        val request = safeCall<RandomDto> {
            httpClient.get(
                urlString = "$BASE_URL/random"
            )
        }

        return when (request) {
            is Result.Error -> request
            is Result.Success -> Result.Success(request.data.toRandom())
        }
    }
}