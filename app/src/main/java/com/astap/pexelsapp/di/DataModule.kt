package com.astap.pexelsapp.di

import android.content.Context
import androidx.room.Room
import com.astap.pexelsapp.data.local.PexelsDao
import com.astap.pexelsapp.data.local.PexelsDatabase
import com.astap.pexelsapp.data.remote.PexelsApiService
import com.astap.pexelsapp.data.repository.PhotosRepositoryImpl
import com.astap.pexelsapp.domain.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindPhotoRepository(
        impl: PhotosRepositoryImpl
    ): PhotosRepository

    companion object {

        @Provides
        @Singleton
        fun providePexelsDatabase(
            @ApplicationContext context: Context
        ): PexelsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = PexelsDatabase::class.java,
                name = "pexels.db"
            ).build()
        }

        @Provides
        @Singleton
        fun providePexelsDao(
            database: PexelsDatabase
        ): PexelsDao = database.pexelsDao()


        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Provides
        @Singleton
        fun provideConverterFactory(
            json: Json
        ): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            converter: Converter.Factory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.pexels.com/v1/")
                .addConverterFactory(converter)
                .build()
        }

        @Provides
        @Singleton
        fun provideApiService(
            retrofit: Retrofit
        ): PexelsApiService {
            return retrofit.create()
        }
    }
}