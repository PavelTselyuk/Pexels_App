package com.astap.pexelsapp.data.remote

import com.astap.pexelsapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface PexelsApiService {
    @Headers(
        "authorization: ${BuildConfig.PEXELS_API_KEY}"
    )

    @GET("collections/featured?per_page=7")
    suspend fun loadPopularTopics(
    ): PopularTopicsResponseDto

    @GET("curated?per_page=30")
    suspend fun loadCuratedPhotos(
        @Query("page") page: String = "1"
    ): PhotosResponseDto


    @GET("search?per_page=30")
    suspend fun loadPhotosByTopic(
        @Query("page") page: String = "1",
        @Query("query") topic: String,
    ): PhotosResponseDto

    @GET("photos/{id}")
    suspend fun loadPhotoById(
        @Path("id") userId: String
    ): PhotoDto


}