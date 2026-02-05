package com.astap.pexelsapp.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astap.pexelsapp.domain.GetCuratedPhotosUseCase
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
    private val getCuratedPhotosUseCase: GetCuratedPhotosUseCase,
    private val getPhotosByTopicUseCase: GetPhotosByTopicUseCase,
    private val getPopularTopicsUseCase: GetPopularTopicsUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val popularTopics = getPopularTopicsUseCase() ?: listOf("")
            Log.d("Topics", "$popularTopics")
            _state.update {
                it.copy(topicsList = popularTopics.associateWith { false })
            }
        }
        query
            .onEach { input ->
                _state.update { oldState ->
                    oldState.copy(query = input)
                }
            }
            .debounce(700)
            .distinctUntilChanged()
            .onEach {
                _state.update { oldState ->
                    oldState.copy(
                        dataState = DataState.Loading
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
                    if (listPhoto == null) {
                        oldState.copy(
                            dataState = DataState.NoInternetConnection
                        )
                        
                    } else if (listPhoto.isNotEmpty()) {
                        oldState.copy(
                            dataState = DataState.ShowPhotos(photos = listPhoto)
                        )

                    } else {
                        oldState.copy(
                            dataState = DataState.NoData
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

        }
    }

}

sealed interface HomeScreenCommand {

    data class InputQuery(val query: String) : HomeScreenCommand

    data class ClickTopic(val topic: String) : HomeScreenCommand

}

data class HomeScreenState(
    val query: String = "",
    val topicsList: Map<String, Boolean> = mapOf(),
    val dataState: DataState = DataState.Loading
) {

    val selectedTopic: String?
        get() = topicsList.keys.firstOrNull {
            it == query
        }
}


sealed interface DataState {
    data object Loading : DataState

    data object NoData : DataState

    data class ShowPhotos(
        val photos: List<Photo>
    ) : DataState

    data object NoInternetConnection : DataState
}


