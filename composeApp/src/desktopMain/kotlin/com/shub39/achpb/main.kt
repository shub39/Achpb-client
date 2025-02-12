package com.shub39.achpb

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.shub39.achpb.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Template",
    ) {
        App()
    }
}