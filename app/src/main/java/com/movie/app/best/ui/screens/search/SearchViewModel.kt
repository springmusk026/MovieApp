package com.movie.app.best.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null
    
    fun searchMovies(query: String) {
        if (query.isEmpty()) {
            _uiState.update { 
                it.copy(
                    searchResults = emptyList(),
                    isLoading = false,
                    error = null,
                    searchQuery = query
                )
            }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, searchQuery = query) }
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            repository.searchMovies(query).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                searchResults = result.data?.results ?: emptyList(),
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
    
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        // Cancel previous search job if it exists
        searchJob?.cancel()
        
        // Only start a new search if the query is at least 3 characters long
        if (query.length >= 3) {
            searchJob = viewModelScope.launch {
                // Add a 500ms delay before executing the search
                delay(500)
                searchMovies(query)
            }
        } else if (query.isEmpty()) {
            // Clear results immediately if query is empty
            _uiState.update { 
                it.copy(
                    searchResults = emptyList(),
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 