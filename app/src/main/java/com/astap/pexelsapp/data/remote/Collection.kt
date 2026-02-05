package com.astap.pexelsapp.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    @SerialName("id")
    val id: String = "",
    @SerialName("title")
    val title: String = "",
)