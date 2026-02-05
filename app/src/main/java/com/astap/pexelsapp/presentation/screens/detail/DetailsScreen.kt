package com.astap.pexelsapp.presentation.screens.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage


@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    photoId: Int,
    source: Int,
    viewModel: DetailsViewModel = hiltViewModel(
        creationCallback = { factory: DetailsViewModel.Factory ->
            factory.create(
                photoId = photoId,
                source = source
            )
        }
    ),
    onBackClick: () -> Unit,
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

            DataState.Initial -> {}

            DataState.NoData -> {
//               todo: Button "Explore"
            }

            is DataState.ShowPhoto -> {
                val photo = dataState.photo
                Log.d("DetailsScreen", photo.toString())
                AsyncImage(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .heightIn(200.dp),
                    model = photo.src,
                    contentDescription = "Photo",
                    contentScale = ContentScale.FillWidth,
                )
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
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(180.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            percent = 50
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .clickable {
                        viewModel.processCommand(
                            DetailScreenCommand.DownloadPhoto(
                                photo = (state.dataState as DataState.ShowPhoto).photo
                            )
                        )
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Download"
                )
                Icon(
                    modifier = Modifier.padding(start = 12.dp),
                    imageVector = Icons.Filled.Download,
                    contentDescription = "Download Button",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }


            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isInFavourites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    Log.d("DetailsScreen", "Button onClick")
                    if (state.isInFavourites) {
                        Log.d("DetailsScreen", "DELETE FromFavourites")

                        viewModel.processCommand(DetailScreenCommand.DeleteFromFavourites((state.dataState as DataState.ShowPhoto).photo))
                    } else {
                        Log.d("DetailsScreen", "ADD ToFavourites")

                        viewModel.processCommand(DetailScreenCommand.AddToFavourites((state.dataState as DataState.ShowPhoto).photo))
                    }
                }
            ) {
                Text(
                    text = "Fav"
                )
            }
        }
    }
}

