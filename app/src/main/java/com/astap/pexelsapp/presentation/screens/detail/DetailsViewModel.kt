package com.astap.pexelsapp.presentation.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astap.pexelsapp.data.repository.PhotosRepositoryImpl
import com.astap.pexelsapp.domain.AddToFavoritePhotosUseCase
import com.astap.pexelsapp.domain.DeleteFromFavoritePhotosUseCase
import com.astap.pexelsapp.domain.DownloadPhotoUseCase
import com.astap.pexelsapp.domain.GetPhotoFromFavoritesUseCase
import com.astap.pexelsapp.domain.GetPhotoFromHomePageUseCase
import com.astap.pexelsapp.domain.Photo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted("photoId") private val photoId: Int,
    @Assisted("source") private val source: Int,
    private val addToFavoritePhotosUseCase: AddToFavoritePhotosUseCase,
    private val deleteFromFavouritePhotosUseCase: DeleteFromFavoritePhotosUseCase,
    private val getPhotoFromHomePageUseCase: GetPhotoFromHomePageUseCase,
    private val getPhotoFromFavoritesUseCase: GetPhotoFromFavoritesUseCase,
    private val downloadPhotoUseCase: DownloadPhotoUseCase,


    ) : ViewModel() {

    private val _state = MutableStateFlow(DetailScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val photo = when (source) {
                FROM_OTHERS -> getPhotoFromHomePageUseCase(photoId)
                FROM_FAVOURITES -> getPhotoFromFavoritesUseCase(photoId)
                else -> null
            }
            Log.d("DetailsViewModel", photo.toString())
            photo?.let { image ->
                val isInFavourites =
                    getPhotoFromFavoritesUseCase(image.id).id != PhotosRepositoryImpl.ID_OF_NON_EXISTED_PHOTO
                _state.update { oldState ->
                    oldState.copy(
                        dataState = DataState.ShowPhoto(image),
                        isInFavourites = isInFavourites
                    )
                }
            }
        }
    }

    fun processCommand(command: DetailScreenCommand) {
        when (command) {
            is DetailScreenCommand.AddToFavourites -> {
                viewModelScope.launch {
                    addToFavoritePhotosUseCase(command.photo)
                    val isInFavourites =
                        getPhotoFromFavoritesUseCase(command.photo.id).id != PhotosRepositoryImpl.ID_OF_NON_EXISTED_PHOTO

                    _state.update { oldState ->
                        oldState.copy(
                            isInFavourites = isInFavourites
                        )
                    }
                }
            }

            is DetailScreenCommand.DeleteFromFavourites -> {
                viewModelScope.launch {
                    deleteFromFavouritePhotosUseCase(command.photo)
                    val isInFavourites =
                        getPhotoFromFavoritesUseCase(command.photo.id).id != PhotosRepositoryImpl.ID_OF_NON_EXISTED_PHOTO


                    _state.update { oldState ->
                        oldState.copy(
                            isInFavourites = isInFavourites
                        )
                    }
                }
            }

            is DetailScreenCommand.DownloadPhoto -> {
                viewModelScope.launch {
                    viewModelScope.launch {
                        downloadPhotoUseCase(command.photo)
                    }
                }
            }
        }
    }

    companion object {
        const val FROM_FAVOURITES = 10
        const val FROM_OTHERS = 11
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("photoId") photoId: Int,
            @Assisted("source") source: Int
        ): DetailsViewModel
    }
}

sealed interface DetailScreenCommand {

    data class DownloadPhoto(val photo: Photo) : DetailScreenCommand

    data class AddToFavourites(val photo: Photo) : DetailScreenCommand

    data class DeleteFromFavourites(val photo: Photo) : DetailScreenCommand

}

data class DetailScreenState(
    val isInFavourites: Boolean = false,
    val dataState: DataState = DataState.Initial
) {

}


sealed interface DataState {
    data object Initial : DataState

    data object NoData : DataState

    data class ShowPhoto(
        val photo: Photo
    ) : DataState
}


