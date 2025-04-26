package com.movie.app.best.ui.screens.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TVShowsUiState())
    val uiState: StateFlow<TVShowsUiState> = _uiState.asStateFlow()
    
    init {
        loadTVShows()
    }
    
    fun loadTVShows() {
        loadPopularTVShows()
        loadTopRatedTVShows()
        loadOnTheAirTVShows()
    }
    
    fun loadPopularTVShows() {
        viewModelScope.launch {
            repository.getTVShows("popular").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isPopularLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                popularTVShows = result.data?.results ?: emptyList(),
                                isPopularLoading = false,
                                popularError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isPopularLoading = false,
                                popularError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTopRatedTVShows() {
        viewModelScope.launch {
            repository.getTVShows("top_rated").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isTopRatedLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                topRatedTVShows = result.data?.results ?: emptyList(),
                                isTopRatedLoading = false,
                                topRatedError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTopRatedLoading = false,
                                topRatedError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadOnTheAirTVShows() {
        viewModelScope.launch {
            repository.getTVShows("on_the_air").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isOnTheAirLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                onTheAirTVShows = result.data?.results ?: emptyList(),
                                isOnTheAirLoading = false,
                                onTheAirError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isOnTheAirLoading = false,
                                onTheAirError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
}

data class TVShowsUiState(
    val popularTVShows: List<TVShow> = emptyList(),
    val isPopularLoading: Boolean = false,
    val popularError: String? = null,
    
    val topRatedTVShows: List<TVShow> = emptyList(),
    val isTopRatedLoading: Boolean = false,
    val topRatedError: String? = null,
    
    val onTheAirTVShows: List<TVShow> = emptyList(),
    val isOnTheAirLoading: Boolean = false,
    val onTheAirError: String? = null
) 