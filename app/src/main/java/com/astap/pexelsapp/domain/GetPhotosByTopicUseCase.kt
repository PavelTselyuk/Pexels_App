package com.astap.pexelsapp.domain

import javax.inject.Inject

class GetPhotosByTopicUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(topic: String): List<Photo>? {
        return repository.getPhotosByTopic(topic)
    }
}