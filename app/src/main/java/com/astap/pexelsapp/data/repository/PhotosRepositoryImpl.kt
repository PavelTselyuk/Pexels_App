package com.astap.pexelsapp.data.repository

import com.astap.pexelsapp.data.local.PexelsDao
import com.astap.pexelsapp.data.mapper.toDbModel
import com.astap.pexelsapp.data.mapper.toDbModelList
import com.astap.pexelsapp.data.mapper.toEntity
import com.astap.pexelsapp.data.mapper.toListDbModel
import com.astap.pexelsapp.data.mapper.toListEntity
import com.astap.pexelsapp.data.mapper.toListString
import com.astap.pexelsapp.data.remote.PexelsApiService
import com.astap.pexelsapp.domain.Photo
import com.astap.pexelsapp.domain.PhotosRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val pexelsDao: PexelsDao,
    private val pexelsApiService: PexelsApiService
) : PhotosRepository {
    override suspend fun getPopularTopics(): List<String> {
        val topicsResponseDto = pexelsApiService.loadPopularTopics()
        coroutineScope {
            pexelsDao.changeTopics(topicsResponseDto.toDbModelList())
        }
        return topicsResponseDto.toListString()
    }

    override suspend fun getCuratedPhotos(): List<Photo> {
        val photosResponseDto = pexelsApiService.loadCuratedPhotos()
        coroutineScope {
            pexelsDao.changeCuratePhotos(photosResponseDto.toListDbModel())
        }
        return pexelsApiService.loadCuratedPhotos().toListEntity()
    }

    override suspend fun getPhotosByTopic(topic: String): List<Photo> {
        return pexelsApiService.loadPhotosByTopic(topic = topic).toListEntity()

    }

    override suspend fun getPhotoFromFavorites(photoId: Int): Photo {
        return pexelsDao.getPhotoById(photoId).toEntity()
    }

    override suspend fun getPhotoFromHomePage(photoId: Int): Photo {
        return pexelsApiService.loadPhotoById(photoId.toString()).toEntity()
    }

    override fun getFavoritePhotos(): Flow<List<Photo>> {
        return pexelsDao.getFavoritePhotos().map {
            it.toListEntity()
        }
    }

    override suspend fun addToFavoritePhotos(photo: Photo) {
        pexelsDao.addPhotoToFavourites(photo.toDbModel(isFavourite = true))
    }

    override suspend fun deleteFromFavoritePhotos(photo: Photo) {
        pexelsDao.deletePhotoFromFavorites(photo.id)
    }
}