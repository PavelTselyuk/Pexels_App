package com.astap.pexelsapp.domain

class AddToFavoritePhotosUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photo: Photo) {
        return repository.addToFavoritePhotos(photo)
    }
}