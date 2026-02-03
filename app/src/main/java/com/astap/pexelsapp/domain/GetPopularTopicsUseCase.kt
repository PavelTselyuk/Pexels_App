package com.astap.pexelsapp.domain

class GetPopularTopicsUseCase(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.getPopularTopics()
    }
}