package com.astap.pexelsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDbModel(
    @PrimaryKey val id: Int = 0,
    val photographer: String = "",
    val srcOriginal: String = "",
    val isFavourite: Boolean = false
)
