package com.movie.app.best.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoviesUiState(
    // Popular Movies
    val popularMovies: List<Movie> = emptyList(),
    val isPopularMoviesLoading: Boolean = false,
    val popularMoviesError: String? = null,
    
    // Top Rated Movies
    val topRatedMovies: List<Movie> = emptyList(),
    val isTopRatedMoviesLoading: Boolean = false,
    val topRatedMoviesError: String? = null,
    
    // Upcoming Movies
    val upcomingMovies: List<Movie> = emptyList(),
    val isUpcomingMoviesLoading: Boolean = false,
    val upcomingMoviesError: String? = null,
    
    // Now Playing Movies
    val nowPlayingMovies: List<Movie> = emptyList(),
    val isNowPlayingMoviesLoading: Boolean = false,
    val nowPlayingMoviesError: String? = null,
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    init {
        loadAllMovies()
    }
    
    fun loadAllMovies() {
        loadPopularMovies()
        loadTopRatedMovies()
        loadUpcomingMovies()
        loadNowPlayingMovies()
    }
    
    fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPopularMoviesLoading = true) }
            
            repository.getMovies("popular").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                popularMovies = result.data?.results ?: emptyList(),
                                isPopularMoviesLoading = false,
                                popularMoviesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isPopularMoviesLoading = false,
                                popularMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTopRatedMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTopRatedMoviesLoading = true) }
            
            repository.getMovies("top_rated").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                topRatedMovies = result.data?.results ?: emptyList(),
                                isTopRatedMoviesLoading = false,
                                topRatedMoviesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTopRatedMoviesLoading = false,
                                topRatedMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadUpcomingMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpcomingMoviesLoading = true) }
            
            repository.getMovies("upcoming").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                upcomingMovies = result.data?.results ?: emptyList(),
                                isUpcomingMoviesLoading = false,
                                upcomingMoviesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isUpcomingMoviesLoading = false,
                                upcomingMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadNowPlayingMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isNowPlayingMoviesLoading = true) }
            
            repository.getMovies("now_playing").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                nowPlayingMovies = result.data?.results ?: emptyList(),
                                isNowPlayingMoviesLoading = false,
                                nowPlayingMoviesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isNowPlayingMoviesLoading = false,
                                nowPlayingMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
} 