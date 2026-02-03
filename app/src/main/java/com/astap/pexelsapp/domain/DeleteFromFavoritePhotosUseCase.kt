package com.astap.pexelsapp.domain

class DeleteFromFavoritePhotosUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photo: Photo) {
        return repository.deleteFromFavoritePhotos(photo)
    }
}