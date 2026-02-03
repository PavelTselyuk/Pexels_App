package com.astap.pexelsapp.domain

class GetCuratedPhotosUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(): List<Photo> {
        return repository.getCuratedPhotos()
    }
}