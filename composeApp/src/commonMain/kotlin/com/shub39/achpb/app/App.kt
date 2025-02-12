package com.shub39.achpb.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shub39.achpb.browser.presentation.BrowserVM
import com.shub39.achpb.core.domain.Routes
import com.shub39.achpb.core.presentation.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    bvm: BrowserVM = koinViewModel()
) {
    val navController = rememberNavController()

    val theme by bvm.theme.collectAsStateWithLifecycle()

    AppTheme(
        theme = theme
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.HomePage,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            composable<Routes.HomePage> {
                Text("HomePage")
            }

            composable<Routes.SettingsPage> {
                Text("Settings")
            }
        }
    }
}