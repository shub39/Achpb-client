package com.shub39.achpb.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shub39.achpb.browser.presentation.BrowserVM
import com.shub39.achpb.browser.presentation.home.HomePage
import com.shub39.achpb.browser.presentation.settings.SettingsPage
import com.shub39.achpb.core.domain.Routes
import com.shub39.achpb.core.presentation.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    bvm: BrowserVM = koinViewModel()
) {
    val navController = rememberNavController()

    val theme by bvm.theme.collectAsStateWithLifecycle()
    val homeState by bvm.homeState.collectAsStateWithLifecycle()

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
                HomePage(
                    state = homeState,
                    action = bvm::onHomeAction,
                    onSettingsNav = { navController.navigate(Routes.SettingsPage) }
                )
            }

            composable<Routes.SettingsPage> {
                SettingsPage(
                    theme = theme,
                    action = bvm::onSettingsAction,
                    onBack = { navController.navigateUp() }
                )
            }
        }
    }
}