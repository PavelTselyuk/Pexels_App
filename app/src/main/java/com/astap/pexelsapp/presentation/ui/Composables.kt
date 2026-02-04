package com.astap.pexelsapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.astap.pexelsapp.domain.Photo


@Composable
fun FavouriteNavigationTab(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onClick: () -> Unit,
) {

    IconToggleButton(
        modifier = modifier
            .height(24.dp)
            .clickable(
                onClick = onClick
            ),
        checked = isActive,
        onCheckedChange = {},
    ) {

        Icon(
            imageVector = if (isActive) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite Button",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun HomeNavigationTab(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onClick: () -> Unit,
) {

    IconToggleButton(
        modifier = modifier
            .height(24.dp)
            .clickable { onClick() },
        checked = isActive,
        onCheckedChange = { },
    ) {

        Icon(
            imageVector = if (isActive) Icons.Filled.Home else Icons.Outlined.Home,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = "Favorite Button",
        )
    }
}

@Composable
fun PhotoCollection(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    photos: List<Photo>,
) {
    val scrollState = rememberLazyStaggeredGridState()


    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columns = StaggeredGridCells.Fixed(2),
        state = scrollState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        content = {

            items(photos) { photo ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(200.dp)
                        .clickable(onClick = { onClick(photo.id) }),
                    model = photo.src,
                    contentDescription = "Photo",
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    )
}