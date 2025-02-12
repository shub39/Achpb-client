package com.shub39.achpb.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual class DatastoreFactory {
    actual fun getPreferencesDataStore(): DataStore<Preferences> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Achpb")
            os.contains("mac") -> File(userHome, "Library/Application Support/Achpb")
            else -> File(userHome, ".local/share/Achpb")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, preferencesFileName)
        return createDataStore(
            producePath = { dbFile.absolutePath }
        )
    }
}