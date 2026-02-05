package com.astap.pexelsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.astap.pexelsapp.presentation.screens.detail.DetailsScreen
import com.astap.pexelsapp.presentation.screens.detail.DetailsViewModel
import com.astap.pexelsapp.presentation.screens.favourites.FavouritesScreen
import com.astap.pexelsapp.presentation.screens.home.HomeScreen

@Composable
fun NavigationRoot(

) {
    val backStack = rememberNavBackStack(Route.Home)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreen(
                            onFavouritesClick = {
                                backStack.add(Route.Favourites)
                                backStack.remove(it)
                            },
                            onPhotoClick = { photoId ->
                                backStack.add(
                                    Route.Detail(
                                        photoId = photoId,
                                        source = DetailsViewModel.FROM_OTHERS
                                    )
                                )
                            },
                        )
                    }
                }

                is Route.Favourites -> {
                    NavEntry(key) {
                        FavouritesScreen(
                            onHomePageClick = {
                                backStack.add(Route.Home)
                                backStack.remove(it)
                            },
                            onPhotoClick = { photoId ->
                                backStack.add(
                                    Route.Detail(
                                        photoId = photoId,
                                        source = DetailsViewModel.FROM_FAVOURITES
                                    )
                                )
                            },
                            onExploreClick = {
                                backStack.add(Route.Home)
                                backStack.remove(it)
                            }
                        )
                    }
                }

                is Route.Detail -> {
                    NavEntry(key) {
                        DetailsScreen(
                            photoId = key.photoId,
                            source = key.source,
                            onBackClick = {
                                backStack.remove(it)
                            },
                            onExploreClick = {
                                backStack.remove(it)
                            }
                        )
                    }
                }

                else -> error("Unknown NavKey $key")
            }
        }
    )
}