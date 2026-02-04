package com.astap.pexelsapp.presentation.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astap.pexelsapp.domain.GetFavoritePhotosUseCase
import com.astap.pexelsapp.domain.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavoritePhotosUseCase: GetFavoritePhotosUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesScreenState())
    val state = _state.asStateFlow()

    init {
        getFavoritePhotosUseCase()
            .onEach { photoList ->
                _state.update { oldState ->
                    val emptyFavourites = photoList.isEmpty()
                    oldState.copy(
                        dataState = if (emptyFavourites) {
                            DataState.NoData
                        } else {
                            DataState.ShowPhotos(photoList)
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}

data class FavouritesScreenState(
    val dataState: DataState = DataState.Loading
)


sealed interface DataState {
    data object Loading : DataState

    data object NoData : DataState

    data class ShowPhotos(
        val photos: List<Photo>
    ) : DataState
}


