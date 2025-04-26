package com.movie.app.best.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.model.Season
import com.movie.app.best.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle

data class VideoPlayerUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSeason: Int = 1,
    val selectedEpisode: Int = 1,
    val seasons: List<Season> = emptyList(),
    val videoKey: String = ""
)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()
    
    // Extract videoKey from saved state handle
    init {
        savedStateHandle.get<String>("videoKey")?.let { videoKey ->
            if (videoKey.isNotEmpty()) {
                _uiState.update { it.copy(videoKey = videoKey) }
            }
        }
    }

    fun initializeWithMedia(mediaId: String, mediaType: String) {
        if (mediaType == "tv") {
            loadTVShowSeasons(mediaId)
        }
    }

    private fun loadTVShowSeasons(tvShowId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getTVShowDetails(tvShowId.toInt()).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading state above
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                seasons = result.data?.seasons ?: emptyList(),
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = result.error ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadVideoTrailer(mediaId: String, mediaType: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val videosFlow = if (mediaType == "movie") {
                repository.getMovieVideos(mediaId.toInt())
            } else {
                repository.getTVShowVideos(mediaId.toInt())
            }
            
            videosFlow.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already set loading state above
                    }
                    is Resource.Success -> {
                        val videos = result.data?.results ?: emptyList()
                        
                        // Find a trailer (prioritize official trailers)
                        val trailer = videos.find { 
                            it.type.equals("Trailer", ignoreCase = true) && 
                            it.official && 
                            it.site.equals("YouTube", ignoreCase = true) 
                        } ?: videos.find { 
                            it.type.equals("Trailer", ignoreCase = true) && 
                            it.site.equals("YouTube", ignoreCase = true)
                        } ?: videos.firstOrNull { 
                            it.site.equals("YouTube", ignoreCase = true) 
                        }
                        
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                videoKey = trailer?.key ?: "",
                                error = if (trailer == null) "No trailer found" else null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = result.error ?: "Failed to load video"
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateSeason(seasonNumber: Int) {
        _uiState.update { 
            it.copy(
                selectedSeason = seasonNumber,
                selectedEpisode = 1 // Reset episode when season changes
            )
        }
    }

    fun updateEpisode(episodeNumber: Int) {
        _uiState.update { it.copy(selectedEpisode = episodeNumber) }
    }
} 