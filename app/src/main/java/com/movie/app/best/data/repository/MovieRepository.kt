package com.movie.app.best.data.repository

import com.movie.app.best.data.model.Credits
import com.movie.app.best.data.model.Genre
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.MovieDetails
import com.movie.app.best.data.model.PaginatedResponse
import com.movie.app.best.data.model.Resource
import com.movie.app.best.data.model.Review
import com.movie.app.best.data.model.SeasonDetails
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.data.model.TVShowDetails
import com.movie.app.best.data.model.Video
import com.movie.app.best.data.remote.MovieApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieApiService: MovieApiService
) {
    
    fun getMovies(category: String, page: Int = 1): Flow<Resource<PaginatedResponse<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getMovies(category, page)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getMovieDetails(id: Int): Flow<Resource<MovieDetails>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getMovieDetails(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getMovieCredits(id: Int): Flow<Resource<Credits>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getMovieCredits(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getMovieVideos(id: Int): Flow<Resource<PaginatedResponse<Video>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getMovieVideos(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getMovieReviews(id: Int): Flow<Resource<PaginatedResponse<Review>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getMovieReviews(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getSimilarMovies(id: Int): Flow<Resource<PaginatedResponse<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getSimilarMovies(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun searchMovies(query: String, page: Int = 1): Flow<Resource<PaginatedResponse<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.searchMovies(query, page)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getGenres()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getGenreMovies(id: Int, page: Int = 1): Flow<Resource<PaginatedResponse<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getGenreMovies(id, page)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTrendingMovies(timeWindow: String = "day"): Flow<Resource<PaginatedResponse<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTrendingMovies(timeWindow)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTrendingTVShows(timeWindow: String = "day"): Flow<Resource<PaginatedResponse<TVShow>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTrendingTVShows(timeWindow)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTrending(mediaType: String, timeWindow: String): Flow<Resource<PaginatedResponse<Any>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTrending(mediaType, timeWindow)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTVShows(category: String, page: Int = 1): Flow<Resource<PaginatedResponse<TVShow>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTVShows(category, page)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTVShowDetails(id: Int): Flow<Resource<TVShowDetails>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTVShowDetails(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTVShowCredits(id: Int): Flow<Resource<Credits>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTVShowCredits(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getTVShowVideos(id: Int): Flow<Resource<PaginatedResponse<Video>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTVShowVideos(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getSimilarTVShows(id: Int): Flow<Resource<PaginatedResponse<TVShow>>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getSimilarTVShows(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
    
    fun getSeasonEpisodes(tvShowId: Int, seasonNumber: Int): Flow<Resource<SeasonDetails>> = flow {
        emit(Resource.Loading())
        try {
            val response = movieApiService.getTVShowSeasonDetails(tvShowId, seasonNumber)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }
} 