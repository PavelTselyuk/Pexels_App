package com.astap.pexelsapp.data.repository

import android.util.Log
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
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val pexelsDao: PexelsDao,
    private val pexelsApiService: PexelsApiService
) : PhotosRepository {
    override suspend fun getPopularTopics(): List<String>? {
        val topicsResponseDto = try {
            pexelsApiService.loadPopularTopics()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("PhotosRepository", e.stackTraceToString())
            if (e is UnknownHostException) {
                return null
            }
            null
        }

        coroutineScope {
            launch {
                topicsResponseDto?.let {
                    pexelsDao.changeTopics(topicsResponseDto.toDbModelList())
                }
            }
        }

        return topicsResponseDto?.toListString() ?: listOf()
    }

    override suspend fun getCuratedPhotos(): List<Photo>? {

        val photosResponseDto = try {
            pexelsApiService.loadCuratedPhotos()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("PhotosRepository", e.stackTraceToString())
            if (e is UnknownHostException) {
                return null
            }

            null
        }

        coroutineScope {
            launch {
                photosResponseDto?.let {
                    pexelsDao.changeCuratePhotos(photosResponseDto.toListDbModel())
                }
            }
        }

        return photosResponseDto?.toListEntity() ?: listOf()

    }

    override suspend fun getPhotosByTopic(topic: String): List<Photo>? {

        return try {
            pexelsApiService.loadPhotosByTopic(topic = topic).toListEntity()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("PhotosRepository", e.stackTraceToString())
            if (e is UnknownHostException) {
                return null
            }
            listOf()
        }

    }

    override suspend fun getPhotoFromFavorites(photoId: Int): Photo {
        return pexelsDao.getPhotoById(photoId).toEntity()
    }

    override suspend fun getPhotoFromHomePage(photoId: Int): Photo? {
        return try {
            pexelsApiService.loadPhotoById(photoId.toString()).toEntity()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("PhotosRepository", e.stackTraceToString())
            if (e is UnknownHostException) {
                return null
            }
            return Photo(id = ID_OF_NON_EXISTED_PHOTO)
        }
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

    companion object {
        const val ID_OF_NON_EXISTED_PHOTO = -1
    }
}