package com.astap.pexelsapp.presentation.screens.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.astap.pexelsapp.presentation.ui.FavouriteNavigationTab
import com.astap.pexelsapp.presentation.ui.HomeNavigationTab
import com.astap.pexelsapp.presentation.ui.PhotoCollection


@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel = hiltViewModel(),
    onHomePageClick: () -> Unit,
    onPhotoClick: (Int) -> Unit,
    onExploreClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            // todo: Title
        }

        when (val dataState = state.dataState) {
            DataState.Loading -> {}

            DataState.NoData -> {
//               todo: Button "Explore"
            }

            is DataState.ShowPhotos -> {
                val photos = dataState.photos
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    PhotoCollection(
                        modifier = Modifier,
                        onClick = { photoId ->
                            onPhotoClick(photoId)
                        },
                        photos = photos
                    )
                }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .heightIn(min = 48.dp),
            horizontalArrangement = Arrangement.SpaceAround

        )
        {
            HomeNavigationTab(
                modifier = Modifier,
                isActive = false,
                onClick = {
                    onHomePageClick()
                }
            )
            FavouriteNavigationTab(
                modifier = Modifier,
                isActive = true,
                onClick = { }
            )
        }

    }
}

