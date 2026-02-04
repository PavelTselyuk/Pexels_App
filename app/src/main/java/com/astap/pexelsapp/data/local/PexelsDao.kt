package com.astap.pexelsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT * FROM photos WHERE isFavourite == 1")
    fun getFavoritePhotos(): Flow<List<PhotoDbModel>>

    @Query("SELECT * FROM photos WHERE id == :id")
    suspend fun getPhotoById(id: Int): PhotoDbModel

    @Query("DELETE FROM photos WHERE id ==:photoId")
    suspend fun deletePhotoFromFavorites(photoId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCuratedPhotos(curatedPhotos: List<PhotoDbModel>)

    @Query("DELETE FROM photos WHERE isFavourite == 0")
    suspend fun deleteCuratePhotos()

    @Transaction
    suspend fun changeCuratePhotos(curatedPhotos: List<PhotoDbModel>){
        deleteCuratePhotos()
        addCuratedPhotos(curatedPhotos)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotoToFavourites(photoDbModel: PhotoDbModel)
}