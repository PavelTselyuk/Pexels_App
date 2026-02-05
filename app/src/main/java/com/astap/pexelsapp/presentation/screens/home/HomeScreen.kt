package com.astap.pexelsapp.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.astap.pexelsapp.presentation.ui.FavouriteNavigationTab
import com.astap.pexelsapp.presentation.ui.HomeNavigationTab
import com.astap.pexelsapp.presentation.ui.PhotoCollection


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onFavouritesClick: () -> Unit,
    onPhotoClick: (Int) -> Unit
) {

    val state by viewModel.state.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Searchbar(
                modifier = Modifier
                    .padding(top= 44.dp)
                    .padding(horizontal = 24.dp),
                query = state.query,
                onValueChange = {
                    viewModel.processCommand(HomeScreenCommand.InputQuery(it))
                }
            )

            if (state.topicsList.keys.size > 1) {
                LazyRow(
                    modifier = modifier,
                    contentPadding = PaddingValues(16.dp),

                    ) {
                    state.selectedTopic?.let { topic ->
                        item {
                            TopicButton(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                isSelected = true,
                                onClick = {
                                    viewModel.processCommand(HomeScreenCommand.ClickTopic(it))
                                },
                                topic = topic
                            )
                        }
                    }

                    val unselectedTopics =
                        state.topicsList.keys.toList().filter { it != state.selectedTopic }
                    items(unselectedTopics) { topic ->
                        TopicButton(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            isSelected = topic == state.selectedTopic,
                            onClick = {
                                viewModel.processCommand(HomeScreenCommand.ClickTopic(it))
                            },
                            topic = topic
                        )
                    }
                }
            }
        }

        when (val dataState = state.dataState) {
            DataState.Loading -> {
                Button(
                    onClick = { }

                ) {
                    Text(
                        text = "Load smth"
                    )
                }
            }

            DataState.NoData -> {

                Button(
                    onClick = { }

                ) {
                    Text(
                        text = "No Data"
                    )
                }
            }

            DataState.NoInternetConnection -> {
                Button(
                    onClick = { }

                ) {
                    Text(
                        text = "No Internet Connection"
                    )
                }

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
                isActive = true,
                onClick = {
                }
            )
            FavouriteNavigationTab(
                modifier = Modifier,
                isActive = false,
                onClick = {
                    onFavouritesClick()
                }
            )
        }

    }
}


@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    query: String,
    onValueChange: (String) -> Unit
) {
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                onValueChange("")
            },
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear query",
                tint = Color.Black
            )
        }
    }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        value = query,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = "Search",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = if (query.isNotEmpty()) trailingIconView else null,
        shape = RoundedCornerShape(50.dp),
        textStyle = TextStyle(
            fontStyle = FontStyle.Normal,
            fontSize = 15.sp,
        )

    )
}

@Composable
fun TopicButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    topic: String
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = { onClick(topic) },
        label = {
            Text(
                text = topic
            )
        }
    )
}

@Preview
@Composable
fun HomeNavigationTabPreview(
) {
    IconToggleButton(
        modifier = Modifier
            .height(24.dp),
        checked = true,
        onCheckedChange = {},
    ) {
        Icon(
            imageVector = if (true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite Button",
        )
    }
}