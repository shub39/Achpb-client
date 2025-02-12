package com.shub39.achpb.core.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.materialkolor.PaletteStyle
import com.shub39.achpb.core.domain.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreImpl(
    private val dataStore: DataStore<Preferences>
): AppDataStore {

    companion object {
        private val darkThemeKey = booleanPreferencesKey("dark_theme")
        private val seedColorKey = intPreferencesKey("seed_color")
        private val amoledKey = booleanPreferencesKey("amoled")
        private val paletteKey = stringPreferencesKey("palette")
        private val onSystemKey = booleanPreferencesKey("on_system")
    }

    override fun getDarkThemeFlow(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[darkThemeKey] ?: true
    }
    override suspend fun setDarkTheme(pref: Boolean) {
        dataStore.edit { prefs ->
            prefs[darkThemeKey] = pref
        }
    }

    override fun getSeedColorFlow(): Flow<Int> = dataStore.data.map { prefs ->
        prefs[seedColorKey] ?: Color.Blue.toArgb()
    }
    override suspend fun setSeedColor(color: Int) {
        dataStore.edit { prefs ->
            prefs[seedColorKey] = color
        }
    }

    override fun getAmoledFlow(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[amoledKey] ?: false
    }
    override suspend fun setAmoled(pref: Boolean) {
        dataStore.edit { prefs ->
            prefs[amoledKey] = pref
        }
    }

    override fun getPaletteStyleFlow(): Flow<PaletteStyle> = dataStore.data.map { prefs ->
        val style = prefs[paletteKey] ?: PaletteStyle.TonalSpot.name
        PaletteStyle.valueOf(style)
    }
    override suspend fun setPaletteStyle(style: PaletteStyle) {
        dataStore.edit { prefs ->
            prefs[paletteKey] = style.name
        }
    }

    override fun getIsSystemThemeFlow(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[onSystemKey] ?: false
    }
    override suspend fun setIsSystemTheme(pref: Boolean) {
        dataStore.edit { prefs ->
            prefs[onSystemKey] = pref
        }
    }

}