package com.astap.pexelsapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Home : Route, NavKey

    @Serializable
    data object Favourites : Route, NavKey

    @Serializable
    data class Detail(
        val photoId: Int,
        val source: Int,
    ) : Route, NavKey
}