package com.astap.pexelsapp.domain

import javax.inject.Inject

class GetCuratedPhotosUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(): List<Photo> {
        return repository.getCuratedPhotos()
    }
}