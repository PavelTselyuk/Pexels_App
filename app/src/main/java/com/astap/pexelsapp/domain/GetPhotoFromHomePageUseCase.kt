package com.astap.pexelsapp.domain

class GetPhotoFromHomePageUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photoId: Int): Photo {
        return repository.getPhotoFromHomePage(photoId)
    }
}