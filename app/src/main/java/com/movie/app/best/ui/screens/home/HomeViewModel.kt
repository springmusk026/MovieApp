package com.movie.app.best.ui.screens.home

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadAllContent()
    }
    
    fun loadAllContent() {
        loadMovies()
        loadTVShows()
    }
    
    fun loadMovies() {
        loadPopularMovies()
        loadTopRatedMovies()
        loadUpcomingMovies()
        loadTrendingMovies()
    }
    
    fun loadTVShows() {
        loadPopularTVShows()
        loadTopRatedTVShows()
        loadOnTheAirTVShows()
        loadTrendingTVShows()
    }
    
    fun loadPopularMovies() {
        viewModelScope.launch {
            repository.getMovies("popular").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isPopularLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                popularMovies = result.data?.results ?: emptyList(),
                                isPopularLoading = false,
                                popularError = null
                            )
                        }
                        // Update featured content items
                        updateFeaturedContent()
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
    
    fun loadTopRatedMovies() {
        viewModelScope.launch {
            repository.getMovies("top_rated").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isTopRatedLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                topRatedMovies = result.data?.results ?: emptyList(),
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
    
    fun loadUpcomingMovies() {
        viewModelScope.launch {
            repository.getMovies("upcoming").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isUpcomingLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                upcomingMovies = result.data?.results ?: emptyList(),
                                isUpcomingLoading = false,
                                upcomingError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isUpcomingLoading = false,
                                upcomingError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTrendingMovies() {
        viewModelScope.launch {
            repository.getTrendingMovies().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isTrendingMoviesLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                trendingMovies = result.data?.results ?: emptyList(),
                                isTrendingMoviesLoading = false,
                                trendingMoviesError = null
                            )
                        }
                        // Update featured content items
                        updateFeaturedContent()
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingMoviesLoading = false,
                                trendingMoviesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadPopularTVShows() {
        viewModelScope.launch {
            repository.getTVShows("popular").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isPopularTVShowsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                popularTVShows = result.data?.results ?: emptyList(),
                                isPopularTVShowsLoading = false,
                                popularTVShowsError = null
                            )
                        }
                        // Update featured content items
                        updateFeaturedContent()
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isPopularTVShowsLoading = false,
                                popularTVShowsError = result.error
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
                        _uiState.update { it.copy(isTopRatedTVShowsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                topRatedTVShows = result.data?.results ?: emptyList(),
                                isTopRatedTVShowsLoading = false,
                                topRatedTVShowsError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTopRatedTVShowsLoading = false,
                                topRatedTVShowsError = result.error
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
                        _uiState.update { it.copy(isOnTheAirTVShowsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                onTheAirTVShows = result.data?.results ?: emptyList(),
                                isOnTheAirTVShowsLoading = false,
                                onTheAirTVShowsError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isOnTheAirTVShowsLoading = false,
                                onTheAirTVShowsError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun loadTrendingTVShows() {
        viewModelScope.launch {
            repository.getTrendingTVShows().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isTrendingTVShowsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                trendingTVShows = result.data?.results ?: emptyList(),
                                isTrendingTVShowsLoading = false,
                                trendingTVShowsError = null
                            )
                        }
                        // Update featured content items
                        updateFeaturedContent()
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isTrendingTVShowsLoading = false,
                                trendingTVShowsError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    private fun updateFeaturedContent() {
        val featuredItems = mutableListOf<FeaturedItem>()
        
        // Add featured movies
        _uiState.value.trendingMovies.take(3).forEach { movie ->
            featuredItems.add(FeaturedItem.Movie(movie))
        }
        
        // Add featured TV shows
        _uiState.value.trendingTVShows.take(3).forEach { tvShow ->
            featuredItems.add(FeaturedItem.TVShow(tvShow))
        }
        
        // Add more from popular if needed
        if (featuredItems.size < 6) {
            _uiState.value.popularMovies.take(2).forEach { movie ->
                if (!featuredItems.any { (it as? FeaturedItem.Movie)?.movie?.id == movie.id }) {
                    featuredItems.add(FeaturedItem.Movie(movie))
                }
            }
        }
        
        if (featuredItems.size < 6) {
            _uiState.value.popularTVShows.take(2).forEach { tvShow ->
                if (!featuredItems.any { (it as? FeaturedItem.TVShow)?.tvShow?.id == tvShow.id }) {
                    featuredItems.add(FeaturedItem.TVShow(tvShow))
                }
            }
        }
        
        // Shuffle the items to mix movies and TV shows
        _uiState.update { it.copy(featuredItems = featuredItems.shuffled()) }
    }
}

data class HomeUiState(
    // Movies
    val popularMovies: List<Movie> = emptyList(),
    val isPopularLoading: Boolean = false,
    val popularError: String? = null,
    
    val topRatedMovies: List<Movie> = emptyList(),
    val isTopRatedLoading: Boolean = false,
    val topRatedError: String? = null,
    
    val upcomingMovies: List<Movie> = emptyList(),
    val isUpcomingLoading: Boolean = false,
    val upcomingError: String? = null,
    
    val trendingMovies: List<Movie> = emptyList(),
    val isTrendingMoviesLoading: Boolean = false,
    val trendingMoviesError: String? = null,
    
    // TV Shows
    val popularTVShows: List<TVShow> = emptyList(),
    val isPopularTVShowsLoading: Boolean = false,
    val popularTVShowsError: String? = null,
    
    val topRatedTVShows: List<TVShow> = emptyList(),
    val isTopRatedTVShowsLoading: Boolean = false,
    val topRatedTVShowsError: String? = null,
    
    val onTheAirTVShows: List<TVShow> = emptyList(),
    val isOnTheAirTVShowsLoading: Boolean = false,
    val onTheAirTVShowsError: String? = null,
    
    val trendingTVShows: List<TVShow> = emptyList(),
    val isTrendingTVShowsLoading: Boolean = false,
    val trendingTVShowsError: String? = null,
    
    // Featured content for hero carousel
    val featuredItems: List<FeaturedItem> = emptyList()
)

sealed class FeaturedItem {
    data class Movie(val movie: com.movie.app.best.data.model.Movie) : FeaturedItem()
    data class TVShow(val tvShow: com.movie.app.best.data.model.TVShow) : FeaturedItem()
} 