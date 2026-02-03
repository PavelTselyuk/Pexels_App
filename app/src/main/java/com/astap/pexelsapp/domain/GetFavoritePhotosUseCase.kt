package com.astap.pexelsapp.domain

import kotlinx.coroutines.flow.Flow

class GetFavoritePhotosUseCase(
    private val repository: PhotosRepository
) {

    operator fun invoke(): Flow<List<Photo>> {
        return repository.getFavoritePhotos()
    }
}