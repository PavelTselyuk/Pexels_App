package com.astap.pexelsapp.domain

import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(photo: Photo) {
        repository.downloadImageToDevice(photo)
    }
}