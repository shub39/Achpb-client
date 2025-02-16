package com.shub39.achpb.browser.presentation

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.shub39.achpb.browser.domain.AnimeBoysRepo
import com.shub39.achpb.browser.domain.AnimeGirlsRepo
import com.shub39.achpb.browser.presentation.home.HomeAction
import com.shub39.achpb.browser.presentation.home.HomeState
import com.shub39.achpb.browser.presentation.home.HomeStateDef
import com.shub39.achpb.browser.presentation.settings.SettingsAction
import com.shub39.achpb.core.domain.AppDataStore
import com.shub39.achpb.core.domain.Result
import com.shub39.achpb.core.domain.onError
import com.shub39.achpb.core.domain.onSuccess
import com.shub39.achpb.core.presentation.theme.Theme
import com.shub39.achpb.core.presentation.toUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class BrowserVM(
    private val dataStore: AppDataStore,
    private val animeBoysRepo: AnimeBoysRepo,
    private val animeGirlsRepo: AnimeGirlsRepo,
    private val imageLoader: ImageLoader
) : ViewModel() {

    private var observeThemeJob: Job? = null

    private val _theme = MutableStateFlow(Theme())
    val theme = _theme.asStateFlow()
        .onStart { observeTheme() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _theme.value
        )

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
        .onStart { initialLoad() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _homeState.value
        )

    fun onHomeAction(action: HomeAction) = viewModelScope.launch {
        when (action) {
            is HomeAction.OnLanguageChange -> {
                _homeState.update {
                    it.copy(
                        language = action.language,
                        homeStateDef = HomeStateDef.Loading
                    )
                }

                val imageReq = if (_homeState.value.selectedTab == 0) {
                    animeGirlsRepo.getImagesForLang(action.language)
                } else {
                    animeBoysRepo.getImagesForLang(action.language)
                }

                when (imageReq) {
                    is Result.Error -> _homeState.update {
                        it.copy(
                            error = imageReq.error.toUiText(),
                            homeStateDef = HomeStateDef.Error
                        )
                    }

                    is Result.Success -> _homeState.update {
                        it.copy(
                            images = imageReq.data,
                            homeStateDef = HomeStateDef.Idle
                        )
                    }
                }
            }

            is HomeAction.OnTabChange -> {
                _homeState.update {
                    it.copy(
                        language = if (action.tab != it.selectedTab) null else it.language,
                        selectedTab = action.tab
                    )
                }
            }

            is HomeAction.OnImageDownload -> {
                _homeState.value.snackBarHost.currentSnackbarData?.dismiss()
                downloadImage(action.context, action.url, action.name)
            }

            HomeAction.OnReferesh -> {
                _homeState.update {
                    it.copy(
                        homeStateDef = HomeStateDef.Loading
                    )
                }

                initialLoad()
            }
        }
    }

    private suspend fun downloadImage(context: Context, url: String, name: String) = withContext(Dispatchers.IO) {
        _homeState.value.snackBarHost.showSnackbar("Downloading...")

        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()

        val result = (imageLoader.execute(request) as? SuccessResult)?.image?.toBitmap()

        if (result != null) {
            val directory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Achpb")

            if (!directory.exists() || !directory.isDirectory) directory.mkdirs()

            val file = File(directory, name)

            try {
                val outputStream = FileOutputStream(file)
                result.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                _homeState.value.snackBarHost.showSnackbar("Saved to Donwloads/Achpb/$name")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error Saving Image: ${e.printStackTrace()}")
                _homeState.value.snackBarHost.showSnackbar("Failed to save Image :(")
            }
        } else {
            _homeState.value.snackBarHost.showSnackbar("Download Failed :(")
        }
    }

    fun onSettingsAction(action: SettingsAction) = viewModelScope.launch {
        when (action) {
            is SettingsAction.OnSystemThemeChange -> dataStore.setIsSystemTheme(action.pref)
            is SettingsAction.OnIsDarkChange -> dataStore.setDarkTheme(action.pref)
            is SettingsAction.OnAmoledChange -> dataStore.setAmoled(action.pref)
            is SettingsAction.OnSeedChange -> dataStore.setSeedColor(action.seed)
            is SettingsAction.OnStyleChange -> dataStore.setPaletteStyle(action.style)
        }
    }

    private suspend fun initialLoad() {
        animeBoysRepo.getLanguages()
            .onSuccess { result ->
                _homeState.update {
                    it.copy(
                        boysRepoLangs = result
                    )
                }
            }
            .onError { error ->
                _homeState.update {
                    it.copy(
                        error = error.toUiText(),
                        homeStateDef = HomeStateDef.Error
                    )
                }
            }

        animeGirlsRepo.getLanguages()
            .onSuccess { result ->
                _homeState.update {
                    it.copy(
                        girlsRepoLangs = result,
                        language = result[13]
                    )
                }

                when (val imageReq = animeGirlsRepo.getImagesForLang(result[13])) {
                    is Result.Error -> _homeState.update {
                        it.copy(
                            error = imageReq.error.toUiText(),
                            homeStateDef = HomeStateDef.Error
                        )
                    }

                    is Result.Success -> _homeState.update {
                        it.copy(
                            images = imageReq.data,
                            homeStateDef = HomeStateDef.Idle
                        )
                    }
                }
            }
            .onError { error ->
                _homeState.update {
                    it.copy(
                        error = error.toUiText(),
                        homeStateDef = HomeStateDef.Error
                    )
                }
            }
    }

    private fun observeTheme() = viewModelScope.launch {
        observeThemeJob?.cancel()
        observeThemeJob = launch {
            combine(
                dataStore.getDarkThemeFlow(),
                dataStore.getIsSystemThemeFlow(),
                dataStore.getSeedColorFlow(),
                dataStore.getPaletteStyleFlow(),
                dataStore.getAmoledFlow()
            ) { isDark, onSystem, seedColor, style, withAmoled ->
                _theme.update {
                    it.copy(
                        isDark = isDark,
                        onSystem = onSystem,
                        seedColor = seedColor,
                        style = style,
                        withAmoled = withAmoled
                    )
                }
            }.launchIn(this)
        }
    }
}