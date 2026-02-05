package com.astap.pexelsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhotoDbModel::class, TopicDbModel::class],
    version = 1
)
abstract class PexelsDatabase: RoomDatabase() {

    abstract fun pexelsDao(): PexelsDao
}