package com.astap.pexelsapp.data.remote

import com.astap.pexelsapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query


interface PexelsApiService {

    @GET("collections/featured?per_page=7&authorization=${BuildConfig.PEXELS_API_KEY}")
    suspend fun loadPopularTopics(): PopularTopicsResponseDto

    @GET("curated?per_page=30&authorization=${BuildConfig.PEXELS_API_KEY}")
    suspend fun loadCuratedPhotos(
        @Query("page") page: String = "1"
    ): PhotosResponseDto


    @GET("search?per_page=30&authorization=${BuildConfig.PEXELS_API_KEY}")
    suspend fun loadPhotosByTopic(
        @Query("page") page: String = "1",
        @Query("query") topic: String,
    ): PhotosResponseDto


}