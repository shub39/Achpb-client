package com.shub39.achpb.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory(private val context: Context) {
    actual fun getPreferencesDataStore(): DataStore<Preferences> = createDataStore(
        producePath = { context.filesDir.resolve(preferencesFileName).absolutePath }
    )
}