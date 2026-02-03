package com.astap.pexelsapp.domain

import javax.inject.Inject

class GetPhotoFromFavoritesUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photoId: Int): Photo {
        return repository.getPhotoFromFavorites(photoId)
    }
}