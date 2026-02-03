package com.astap.pexelsapp.data.repository

import com.astap.pexelsapp.domain.Photo
import com.astap.pexelsapp.domain.PhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.ArrayList
import javax.inject.Inject

class TestPhotosRepositoryImpl @Inject constructor() : PhotosRepository {
    override suspend fun getPopularTopics(): List<String> {
        return ArrayList<String>()
    }

    override suspend fun getCuratedPhotos(): List<Photo> {
        return ArrayList<Photo>()
    }

    override suspend fun getPhotosByTopic(topic: String): List<Photo> {
        return ArrayList<Photo>()
    }

    override suspend fun getPhotoFromFavorites(photoId: Int): Photo {
        return Photo()
    }

    override suspend fun getPhotoFromHomePage(photoId: Int): Photo {
        return Photo()
    }

    override fun getFavoritePhotos(): Flow<List<Photo>> {
        return flowOf( ArrayList<Photo>())

    }

    override suspend fun addToFavoritePhotos(photo: Photo) {

    }

    override suspend fun deleteFromFavoritePhotos(photo: Photo) {

    }
}