package com.astap.pexelsapp.data.mapper

import com.astap.pexelsapp.data.local.PhotoDbModel
import com.astap.pexelsapp.data.local.TopicDbModel
import com.astap.pexelsapp.data.remote.PhotoDto
import com.astap.pexelsapp.data.remote.PhotosResponseDto
import com.astap.pexelsapp.data.remote.PopularTopicsResponseDto
import com.astap.pexelsapp.domain.Photo

fun PopularTopicsResponseDto.toListString(): List<String> {
    return collections.map {
        it.title
    }
}

fun PopularTopicsResponseDto.toDbModelList(): List<TopicDbModel> {
    return collections.map {
        TopicDbModel(it.title)
    }
}

fun PhotoDto.toEntity(): Photo {
    return Photo(
        id = id,
        photographer = photographer,
        src = src.original
    )
}

fun PhotoDto.toDbModel(isFavourite: Boolean = false): PhotoDbModel {
    return PhotoDbModel(
        id = id,
        photographer = photographer,
        srcOriginal = src.original,
        isFavourite = isFavourite
    )
}

fun PhotosResponseDto.toListEntity(): List<Photo> {
    return photos.map {
        it.toEntity()
    }
}

fun PhotosResponseDto.toListDbModel(): List<PhotoDbModel> {
    return photos.map {
        it.toDbModel()
    }
}

fun PhotoDbModel.toEntity(): Photo {
    return Photo(
        id = id,
        photographer = photographer,
        src = srcOriginal
    )
}

fun List<PhotoDbModel>.toListEntity(): List<Photo> {
    return this.map {
        it.toEntity()
    }
}

fun List<TopicDbModel>.toListString(): List<String> {
    return this.map {
        it.topic
    }
}

fun Photo.toDbModel(isFavourite: Boolean = false): PhotoDbModel{
    return PhotoDbModel(
        id = id,
        photographer = photographer,
        srcOriginal = src,
        isFavourite = isFavourite
    )
}


