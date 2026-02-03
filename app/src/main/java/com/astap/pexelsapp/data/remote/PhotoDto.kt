package com.astap.pexelsapp.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("photographer")
    val photographer: String = "",
    @SerialName("src")
    val src: SrcDto = SrcDto(),
    @SerialName("url")
    val url: String = "",
)