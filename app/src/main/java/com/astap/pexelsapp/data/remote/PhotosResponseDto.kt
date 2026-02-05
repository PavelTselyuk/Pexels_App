package com.astap.pexelsapp.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponseDto(
    @SerialName("page")
    val page: Int = 0,
    @SerialName("photos")
    val photos: List<PhotoDto> = listOf(),
    @SerialName("prev_page")
    val prevPage: String? = "",
    @SerialName("next_page")
    val nextPage: String? = ""
)