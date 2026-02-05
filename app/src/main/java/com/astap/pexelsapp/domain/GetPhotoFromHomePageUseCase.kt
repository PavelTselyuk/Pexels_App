package com.astap.pexelsapp.domain

import javax.inject.Inject

class GetPhotoFromHomePageUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photoId: Int): Photo? {
        return repository.getPhotoFromHomePage(photoId)
    }
}