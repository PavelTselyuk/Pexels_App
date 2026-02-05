package com.astap.pexelsapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePhotosUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    operator fun invoke(): Flow<List<Photo>> {
        return repository.getFavoritePhotos()
    }
}