package com.astap.pexelsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicDbModel(@PrimaryKey val topic: String)
