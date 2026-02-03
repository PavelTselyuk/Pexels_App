package com.astap.pexelsapp.domain

import javax.inject.Inject

class GetPopularTopicsUseCase @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.getPopularTopics()
    }
}