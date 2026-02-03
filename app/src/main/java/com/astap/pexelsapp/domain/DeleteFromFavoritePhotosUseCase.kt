package com.astap.pexelsapp.domain

import javax.inject.Inject

class DeleteFromFavoritePhotosUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photo: Photo) {
        return repository.deleteFromFavoritePhotos(photo)
    }
}