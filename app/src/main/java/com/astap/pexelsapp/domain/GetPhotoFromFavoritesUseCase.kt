package com.astap.pexelsapp.domain

class GetPhotoFromFavoritesUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photoId: Int): Photo {
        return repository.getPhotoFromFavorites(photoId)
    }
}