package com.movie.app.best.ui.screens.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Credits
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.MovieDetails
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.model.Video
import com.movie.app.best.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])
    
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadMovieDetails()
        loadMovieCredits()
        loadMovieVideos()
        loadSimilarMovies()
    }
    
    fun loadMovieDetails() {
        viewModelScope.launch {
            repository.getMovieDetails(movieId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                movie = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadMovieCredits() {
        viewModelScope.launch {
            repository.getMovieCredits(movieId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isCreditsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                credits = result.data,
                                isCreditsLoading = false,
                                creditsError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isCreditsLoading = false,
                                creditsError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadMovieVideos() {
        viewModelScope.launch {
            repository.getMovieVideos(movieId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isVideosLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                videos = result.data?.results ?: emptyList(),
                                isVideosLoading = false,
                                videosError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isVideosLoading = false,
                                videosError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadSimilarMovies() {
        viewModelScope.launch {
            repository.getSimilarMovies(movieId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSimilarMoviesLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                similarMovies = result.data?.results ?: emptyList(),
                                isSimilarMoviesLoading = false,
                                similarMoviesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isSimilarMoviesLoading = false,
                                similarMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MovieDetailUiState(
    val movie: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    
    val credits: Credits? = null,
    val isCreditsLoading: Boolean = false,
    val creditsError: String? = null,
    
    val videos: List<Video> = emptyList(),
    val isVideosLoading: Boolean = false,
    val videosError: String? = null,
    
    val similarMovies: List<Movie> = emptyList(),
    val isSimilarMoviesLoading: Boolean = false,
    val similarMoviesError: String? = null
) 