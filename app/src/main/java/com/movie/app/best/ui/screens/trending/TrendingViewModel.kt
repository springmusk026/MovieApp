package com.movie.app.best.ui.screens.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Movie
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

data class TrendingUiState(
    // Trending Movies Today
    val trendingMoviesToday: List<Movie> = emptyList(),
    val isTrendingMoviesTodayLoading: Boolean = false,
    val trendingMoviesTodayError: String? = null,
    
    // Trending Movies Week
    val trendingMoviesWeek: List<Movie> = emptyList(),
    val isTrendingMoviesWeekLoading: Boolean = false,
    val trendingMoviesWeekError: String? = null,
    
    // Trending TV Shows Today
    val trendingTVShowsToday: List<TVShow> = emptyList(),
    val isTrendingTVShowsTodayLoading: Boolean = false,
    val trendingTVShowsTodayError: String? = null,
    
    // Trending TV Shows Week
    val trendingTVShowsWeek: List<TVShow> = emptyList(),
    val isTrendingTVShowsWeekLoading: Boolean = false,
    val trendingTVShowsWeekError: String? = null
)

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState> = _uiState.asStateFlow()
    
    init {
        loadTrendingToday()
    }
    
    fun loadTrendingToday() {
        loadTrendingMoviesToday()
        loadTrendingTVShowsToday()
    }
    
    fun loadTrendingThisWeek() {
        loadTrendingMoviesThisWeek()
        loadTrendingTVShowsThisWeek()
    }
    
    fun loadTrendingMoviesToday() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTrendingMoviesTodayLoading = true) }
            
            repository.getTrending("movie", "day").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        val movies = result.data?.results?.mapNotNull { item ->
                            try {
                                // Convert the map to a Movie object
                                com.google.gson.Gson().fromJson(
                                    com.google.gson.Gson().toJson(item),
                                    Movie::class.java
                                )
                            } catch (e: Exception) {
                                null
                            }
                        } ?: emptyList()
                        
                        _uiState.update { 
                            it.copy(
                                trendingMoviesToday = movies,
                                isTrendingMoviesTodayLoading = false,
                                trendingMoviesTodayError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingMoviesTodayLoading = false,
                                trendingMoviesTodayError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTrendingMoviesThisWeek() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTrendingMoviesWeekLoading = true) }
            
            repository.getTrending("movie", "week").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        val movies = result.data?.results?.mapNotNull { item ->
                            try {
                                // Convert the map to a Movie object
                                com.google.gson.Gson().fromJson(
                                    com.google.gson.Gson().toJson(item),
                                    Movie::class.java
                                )
                            } catch (e: Exception) {
                                null
                            }
                        } ?: emptyList()
                        
                        _uiState.update { 
                            it.copy(
                                trendingMoviesWeek = movies,
                                isTrendingMoviesWeekLoading = false,
                                trendingMoviesWeekError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingMoviesWeekLoading = false,
                                trendingMoviesWeekError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTrendingTVShowsToday() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTrendingTVShowsTodayLoading = true) }
            
            repository.getTrending("tv", "day").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        val tvShows = result.data?.results?.mapNotNull { item ->
                            try {
                                // Convert the map to a TVShow object
                                com.google.gson.Gson().fromJson(
                                    com.google.gson.Gson().toJson(item),
                                    TVShow::class.java
                                )
                            } catch (e: Exception) {
                                null
                            }
                        } ?: emptyList()
                        
                        _uiState.update { 
                            it.copy(
                                trendingTVShowsToday = tvShows,
                                isTrendingTVShowsTodayLoading = false,
                                trendingTVShowsTodayError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingTVShowsTodayLoading = false,
                                trendingTVShowsTodayError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTrendingTVShowsThisWeek() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTrendingTVShowsWeekLoading = true) }
            
            repository.getTrending("tv", "week").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading above
                    }
                    is Resource.Success -> {
                        val tvShows = result.data?.results?.mapNotNull { item ->
                            try {
                                // Convert the map to a TVShow object
                                com.google.gson.Gson().fromJson(
                                    com.google.gson.Gson().toJson(item),
                                    TVShow::class.java
                                )
                            } catch (e: Exception) {
                                null
                            }
                        } ?: emptyList()
                        
                        _uiState.update { 
                            it.copy(
                                trendingTVShowsWeek = tvShows,
                                isTrendingTVShowsWeekLoading = false,
                                trendingTVShowsWeekError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingTVShowsWeekLoading = false,
                                trendingTVShowsWeekError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
} 