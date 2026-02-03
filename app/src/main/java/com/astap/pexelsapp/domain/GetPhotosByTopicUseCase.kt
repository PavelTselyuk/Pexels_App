package com.astap.pexelsapp.domain

class GetPhotosByTopicUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(topic: String): List<Photo> {
        return repository.getPhotosByTopic(topic)
    }
}