package com.astap.pexelsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.astap.pexelsapp.domain.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PexelsDao {

    @Query("SELECT * FROM topics")
    fun getTopics(): Flow<List<TopicDbModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTopics(topics: List<TopicDbModel>)

    @Query("DELETE FROM topics")
    suspend fun deleteTopics()

    @Transaction
    suspend fun changeTopics(topics: List<TopicDbModel>){
        deleteTopics()
        addTopics(topics)
    }

    @Query("SELECT * FROM photos WHERE isFavorite == 1")
    fun getFavoritePhotos(): Flow<List<PhotoDbModel>>

    @Query("SELECT * FROM photos WHERE id == :id")
    suspend fun getPhotoFromFavorites(id: Int): PhotoDbModel

    @Delete
    suspend fun deletePhotoFromFavorites(photoDbModel: PhotoDbModel)
}