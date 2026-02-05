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

    @GET("collections/featured?per_page=$POPULAR_TOPICS_AMOUNT")
    suspend fun loadPopularTopics(
    ): PopularTopicsResponseDto

    @GET("curated?per_page=$PHOTOS_AMOUNT_PER_PAGE")
    suspend fun loadCuratedPhotos(
        @Query("page") page: String = "1"
    ): PhotosResponseDto


    @GET("search?per_page=$PHOTOS_AMOUNT_PER_PAGE")
    suspend fun loadPhotosByTopic(
        @Query("page") page: String = "1",
        @Query("query") topic: String,
    ): PhotosResponseDto

    @GET("photos/{id}")
    suspend fun loadPhotoById(
        @Path("id") userId: String
    ): PhotoDto


    companion object{

        const val POPULAR_TOPICS_AMOUNT = 7
        const val PHOTOS_AMOUNT_PER_PAGE = 30
    }
}