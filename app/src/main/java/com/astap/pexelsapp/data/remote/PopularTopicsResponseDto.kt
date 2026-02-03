package com.astap.pexelsapp.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularTopicsResponseDto(
    @SerialName("collections")
    val collections: List<Collection> = listOf(),
)