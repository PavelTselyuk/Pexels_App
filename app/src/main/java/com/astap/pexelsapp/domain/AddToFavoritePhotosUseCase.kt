package com.astap.pexelsapp.domain

import javax.inject.Inject

class AddToFavoritePhotosUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photo: Photo) {
        return repository.addToFavoritePhotos(photo)
    }
}