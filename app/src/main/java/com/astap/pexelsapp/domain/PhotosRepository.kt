package com.astap.pexelsapp.domain

import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    suspend fun getPopularTopics(): List<String>

    suspend fun getCuratedPhotos(): List<Photo>

    suspend fun getPhotosByTopic(topic: String): List<Photo>

    suspend fun getPhotoFromFavorites(photoId: Int): Photo

    suspend fun getPhotoFromHomePage(photoId: Int): Photo

    fun getFavoritePhotos(): Flow<List<Photo>>

    suspend fun addToFavoritePhotos(photo: Photo)

    suspend fun deleteFromFavoritePhotos(photo: Photo)

}