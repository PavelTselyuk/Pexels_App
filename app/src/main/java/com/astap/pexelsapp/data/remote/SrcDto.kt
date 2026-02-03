package com.astap.pexelsapp.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SrcDto(
    @SerialName("original")
    val original: String = "",
)