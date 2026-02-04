package com.astap.pexelsapp.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astap.pexelsapp.domain.AddToFavoritePhotosUseCase
import com.astap.pexelsapp.domain.DeleteFromFavoritePhotosUseCase
import com.astap.pexelsapp.domain.GetCuratedPhotosUseCase
import com.astap.pexelsapp.domain.GetFavoritePhotosUseCase
import com.astap.pexelsapp.domain.GetPhotoFromFavoritesUseCase
import com.astap.pexelsapp.domain.GetPhotoFromHomePageUseCase
import com.astap.pexelsapp.domain.GetPhotosByTopicUseCase
import com.astap.pexelsapp.domain.GetPopularTopicsUseCase
import com.astap.pexelsapp.domain.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val addToFavoritePhotosUseCase: AddToFavoritePhotosUseCase,
    private val deleteFromFavoritePhotosUseCase: DeleteFromFavoritePhotosUseCase,
    private val getCuratedPhotosUseCase: GetCuratedPhotosUseCase,
    private val getFavoritePhotosUseCase: GetFavoritePhotosUseCase,
    private val getPhotoFromFavoritesUseCase: GetPhotoFromFavoritesUseCase,
    private val getPhotoFromHomePageUseCase: GetPhotoFromHomePageUseCase,
    private val getPhotosByTopicUseCase: GetPhotosByTopicUseCase,
    private val getPopularTopicsUseCase: GetPopularTopicsUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading(query = ""))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val popularTopics = getPopularTopicsUseCase() ?: listOf("")
            HomeScreenState.topicsList = popularTopics.associateWith { false }
            _state.update {
                it
            }
        }
        query
            .onEach { input ->
                _state.update { oldState ->
                    when (oldState) {
                        is HomeScreenState.Loading -> {
                            oldState.copy(query = input)
                        }

                        is HomeScreenState.NoData -> {
                            oldState.copy(query = input)
                        }

                        is HomeScreenState.NoInternetConnection -> {
                            oldState.copy(query = input)
                        }

                        is HomeScreenState.ShowPhotos -> {
                            oldState.copy(query = input)
                        }
                    }
                }
            }
            .debounce(700)
            .distinctUntilChanged()
            .onEach { input ->
                _state.update { oldState ->
                    HomeScreenState.Loading(
                        query = input
                    )
                }
            }
            .map { input ->
                if (input.isBlank()) {
                    getCuratedPhotosUseCase()
                } else {
                    getPhotosByTopicUseCase(input)
                }
            }
            .onEach { listPhoto ->
                _state.update { oldState ->
                    val query = (oldState as HomeScreenState.Loading).query
                    if (listPhoto == null) {
                        HomeScreenState.NoInternetConnection(query = query)

                    } else if (listPhoto.isNotEmpty()) {
                        HomeScreenState.ShowPhotos(
                            query = query,
                            photos = listPhoto
                        )

                    } else {
                        HomeScreenState.NoData(
                            query = query
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: HomeScreenCommand) {
        when (command) {
            is HomeScreenCommand.ClickTopic -> {
                query.update { command.topic }
            }

            is HomeScreenCommand.InputQuery -> {
                query.update { command.query }
            }

            //todo: don't forget to delete

            is HomeScreenCommand.AddPhotoToFavourites -> {
                viewModelScope.launch {
                    addToFavoritePhotosUseCase(command.photo)
                }
            }

            is HomeScreenCommand.ClickPhotoFromFavourites -> {
                viewModelScope.launch {
                    val photo = getPhotoFromFavoritesUseCase(command.id)
                    Log.d("HomeScreenViewModel", "Photo from favourites: $photo")
                }
            }

            is HomeScreenCommand.ClickPhotoFromOthers -> {
                viewModelScope.launch {
                    val photo = getPhotoFromHomePageUseCase(command.id)
                    Log.d("HomeScreenViewModel", "Photo from others: $photo")
                }
            }

            is HomeScreenCommand.DeletePhotoFromFavourites -> {
                viewModelScope.launch {
                    deleteFromFavoritePhotosUseCase(command.photo)
                }
            }

            HomeScreenCommand.GetFavouritesPhotos -> {
                viewModelScope.launch {
                    val photos = getFavoritePhotosUseCase()
                    Log.d("HomeScreenViewModel", "Photo from others: $photos")
                }
            }
        }
    }

}

sealed interface HomeScreenCommand {

    data class InputQuery(val query: String) : HomeScreenCommand

    data class ClickTopic(val topic: String) : HomeScreenCommand

    //todo: Temporary commands

    data class ClickPhotoFromOthers(val id: Int) : HomeScreenCommand

    data class ClickPhotoFromFavourites(val id: Int) : HomeScreenCommand

    data object GetFavouritesPhotos : HomeScreenCommand

    data class AddPhotoToFavourites(val photo: Photo) : HomeScreenCommand

    data class DeletePhotoFromFavourites(val photo: Photo) : HomeScreenCommand

}

sealed interface HomeScreenState {


    data class Loading(
        val query: String,
    ) : HomeScreenState {

        val clearQueryIconEnable: Boolean
            get() = query.isNotEmpty()

        val selectedTopic: String?
            get() = topicsList.keys.firstOrNull {
                it == query
            }
    }

    data class NoData(
        val query: String,
    ) : HomeScreenState {

        val clearQueryIconEnable: Boolean
            get() = query.isNotEmpty()


        val selectedTopic: String?
            get() = topicsList.keys.firstOrNull {
                it == query
            }
    }

    data class ShowPhotos(
        val query: String,
        val photos: List<Photo> = listOf()
    ) : HomeScreenState {

        val clearQueryIconEnable: Boolean
            get() = query.isNotEmpty()


        val selectedTopic: String?
            get() = topicsList.keys.firstOrNull {
                it == query
            }
    }

    data class NoInternetConnection(
        val query: String,
    ) : HomeScreenState {

        val clearQueryIconEnable: Boolean
            get() = query.isNotEmpty()


        val selectedTopic: String?
            get() = topicsList.keys.firstOrNull {
                it == query
            }
    }

    companion object {
        var topicsList: Map<String, Boolean> = mapOf()
    }

}
