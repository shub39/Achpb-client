package com.shub39.achpb.browser.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.shub39.achpb.browser.domain.Image
import com.skydoves.landscapist.coil3.CoilImage
import org.koin.compose.koinInject

@Composable
fun ImageView(
    images: List<Image>,
    imageLoader: ImageLoader = koinInject()
) {
    val aspectRatios by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(images, key = { it.url }) {
            CoilImage(
                imageModel = { it.url },
//                imageLoader = { imageLoader },
//                success = { _, painter ->
//                    aspectRatios.plus(it.url to (painter.intrinsicSize.height / painter.intrinsicSize.width))
//                },
                modifier = Modifier
                    .aspectRatio(aspectRatios[it.url] ?: 1f)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}
