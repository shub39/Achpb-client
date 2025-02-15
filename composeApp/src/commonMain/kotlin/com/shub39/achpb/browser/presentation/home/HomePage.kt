package com.shub39.achpb.browser.presentation.home

import achpb.composeapp.generated.resources.Res
import achpb.composeapp.generated.resources.anime_boys
import achpb.composeapp.generated.resources.anime_girls
import achpb.composeapp.generated.resources.download
import achpb.composeapp.generated.resources.home_title
import achpb.composeapp.generated.resources.unknown
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shub39.achpb.browser.presentation.ImageView
import com.shub39.achpb.core.presentation.PageFill
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    state: HomeState,
    action: (HomeAction) -> Unit,
    onSettingsNav: () -> Unit
) = PageFill {
    var showLangSelect by remember { mutableStateOf(false) }
    var showFullImage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.widthIn(max = 700.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.home_title)
                    )
                },
                actions = {
                    IconButton(
                        onClick = onSettingsNav
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showLangSelect = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = state.homeStateDef
            ) { homeStateDef ->
                when (homeStateDef) {
                    HomeStateDef.Loading -> CircularProgressIndicator()

                    HomeStateDef.Idle -> ImageView(state.images)

                    HomeStateDef.Error -> Column {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            modifier = Modifier.size(128.dp)
                        )

                        Text(
                            text = state.error?.asString() ?: stringResource(Res.string.unknown),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    if (showFullImage != null) {
        val uriHandler = LocalUriHandler.current

        Dialog(
            onDismissRequest = { showFullImage = null }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CoilImage(
                    imageModel = { showFullImage },
                    imageOptions = ImageOptions(
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit
                    )
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            showFullImage = null
                        },
                        colors = IconButtonDefaults.filledIconButtonColors()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Button(
                        onClick = {
                            uriHandler.openUri(showFullImage!!.replace(" ", "%20"))
                            showFullImage = null
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.download)
                        )
                    }
                }
            }
        }
    }

    if (showLangSelect) {
        val color = ListItemDefaults.colors(
            containerColor = BottomSheetDefaults.ContainerColor
        )

        ModalBottomSheet(
            onDismissRequest = { showLangSelect = false }
        ) {
            TabRow(
                containerColor = color.containerColor,
                selectedTabIndex = state.selectedTab,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { action(HomeAction.OnTabChange(0)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(Res.string.anime_girls),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                Tab(
                    selected = state.selectedTab == 2,
                    onClick = { action(HomeAction.OnTabChange(1)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(Res.string.anime_boys),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = if (state.selectedTab == 0) {
                        state.girlsRepoLangs
                    } else {
                        state.boysRepoLangs
                    }
                ) { language ->
                    ListItem(
                        colors = color,
                        headlineContent = {
                            Text(
                                text = language.name
                            )
                        },
                        trailingContent = {
                            Checkbox(
                                checked = state.language == language,
                                onCheckedChange = {
                                    action(HomeAction.OnLanguageChange(language))
                                    showLangSelect = false
                                }
                            )
                        }
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}