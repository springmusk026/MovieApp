package com.movie.app.best.ui.screens.tvshowdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Credits
import com.movie.app.best.data.model.Episode
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.data.model.TVShowDetails
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
class TVShowDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val tvShowId: String = checkNotNull(savedStateHandle["tvShowId"])
    
    private val _uiState = MutableStateFlow(TVShowDetailUiState())
    val uiState: StateFlow<TVShowDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadTVShowDetails()
        loadTVShowCredits()
        loadTVShowVideos()
        loadSimilarTVShows()
    }
    
    fun loadTVShowDetails() {
        viewModelScope.launch {
            repository.getTVShowDetails(tvShowId.toInt()).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                tvShow = result.data,
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
    
    fun loadTVShowCredits() {
        viewModelScope.launch {
            repository.getTVShowCredits(tvShowId.toInt()).collect { result ->
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
    
    fun loadTVShowVideos() {
        viewModelScope.launch {
            repository.getTVShowVideos(tvShowId.toInt()).collect { result ->
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
    
    fun loadSimilarTVShows() {
        viewModelScope.launch {
            repository.getSimilarTVShows(tvShowId.toInt()).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSimilarTVShowsLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                similarTVShows = result.data?.results ?: emptyList(),
                                isSimilarTVShowsLoading = false,
                                similarTVShowsError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isSimilarTVShowsLoading = false,
                                similarTVShowsError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load episodes for a specific season
     */
    fun loadSeasonEpisodes(tvShowId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                isEpisodesLoading = true,
                episodes = emptyList(), // Clear previous episodes
                episodesError = null
            )}
            
            repository.getSeasonEpisodes(tvShowId, seasonNumber).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isEpisodesLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                episodes = result.data?.episodes ?: emptyList(),
                                isEpisodesLoading = false,
                                episodesError = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isEpisodesLoading = false,
                                episodesError = result.error
                            )
                        }
                    }
                }
            }
        }
    }
}

data class TVShowDetailUiState(
    val tvShow: TVShowDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    
    val credits: Credits? = null,
    val isCreditsLoading: Boolean = false,
    val creditsError: String? = null,
    
    val videos: List<Video> = emptyList(),
    val isVideosLoading: Boolean = false,
    val videosError: String? = null,
    
    val similarTVShows: List<TVShow> = emptyList(),
    val isSimilarTVShowsLoading: Boolean = false,
    val similarTVShowsError: String? = null,
    
    val episodes: List<Episode> = emptyList(),
    val isEpisodesLoading: Boolean = false,
    val episodesError: String? = null
) 