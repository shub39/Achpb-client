package com.shub39.achpb.browser.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.shub39.achpb.browser.presentation.home.HomeAction
import com.shub39.achpb.browser.presentation.home.HomeState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Download
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.koin.compose.koinInject

@Composable
fun ImageView(
    state: HomeState,
    action: (HomeAction) -> Unit,
    imageLoader: ImageLoader = koinInject()
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState { state.images.size }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            pageSpacing = 8.dp
        ) { page ->
            CoilImage(
                imageModel = { state.images[page].url },
                imageOptions = ImageOptions(
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit
                ),
                imageLoader = { imageLoader },
                loading = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                    }
                },
                failure = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                },
                modifier = Modifier
                    .zoomable(rememberZoomState())
                    .fillMaxSize()
            )
        }

        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            shape = MaterialTheme.shapes.extraLarge
        ) {
           Text(
               text = "${pagerState.currentPage + 1} / ${state.images.size}",
               modifier = Modifier.padding(16.dp)
           )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            onClick = { action(HomeAction.OnImageDownload(
                context = context,
                url = state.images[pagerState.currentPage].url,
                name = "${state.language?.name}_${pagerState.currentPage}.jpeg"
            )) }
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Download,
                contentDescription = "Download",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
