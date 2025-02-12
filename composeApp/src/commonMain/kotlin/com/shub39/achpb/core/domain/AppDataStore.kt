package com.shub39.achpb.core.domain

import com.materialkolor.PaletteStyle
import kotlinx.coroutines.flow.Flow

interface AppDataStore {
    fun getDarkThemeFlow(): Flow<Boolean>
    suspend fun setDarkTheme(pref: Boolean)

    fun getSeedColorFlow(): Flow<Int>
    suspend fun setSeedColor(color: Int)

    fun getAmoledFlow(): Flow<Boolean>
    suspend fun setAmoled(pref: Boolean)

    fun getPaletteStyleFlow(): Flow<PaletteStyle>
    suspend fun setPaletteStyle(style: PaletteStyle)

    fun getIsSystemThemeFlow(): Flow<Boolean>
    suspend fun setIsSystemTheme(pref: Boolean)
}