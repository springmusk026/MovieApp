package com.movie.app.best.data.remote

import com.movie.app.best.data.model.Collection
import com.movie.app.best.data.model.Credits
import com.movie.app.best.data.model.Genre
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.MovieDetails
import com.movie.app.best.data.model.PaginatedResponse
import com.movie.app.best.data.model.Review
import com.movie.app.best.data.model.SeasonDetails
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.data.model.TVShowDetails
import com.movie.app.best.data.model.Video
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    
    // Movie endpoints
    @GET("/api/movies/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int = 1
    ): PaginatedResponse<Movie>
    
    @GET("/api/movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int
    ): MovieDetails
    
    @GET("/api/movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int
    ): Credits
    
    @GET("/api/movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): PaginatedResponse<Video>
    
    @GET("/api/movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int
    ): PaginatedResponse<Review>
    
    @GET("/api/movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int
    ): PaginatedResponse<Movie>
    
    // Search
    @GET("/api/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): PaginatedResponse<Movie>
    
    // Genres
    @GET("/api/genres/movie/list")
    suspend fun getGenres(): List<Genre>
    
    @GET("/api/genres/{id}")
    suspend fun getGenreMovies(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): PaginatedResponse<Movie>
    
    // Trending
    @GET("/api/trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): PaginatedResponse<Any>

    @GET("/api/trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = "day"
    ): PaginatedResponse<Movie>

    @GET("/api/trending/tv/{time_window}")
    suspend fun getTrendingTVShows(
        @Path("time_window") timeWindow: String = "day"
    ): PaginatedResponse<TVShow>
    
    // TV Show endpoints
    @GET("/api/tv/{category}")
    suspend fun getTVShows(
        @Path("category") category: String,
        @Query("page") page: Int = 1
    ): PaginatedResponse<TVShow>
    
    @GET("/api/tv/{id}")
    suspend fun getTVShowDetails(
        @Path("id") id: Int
    ): TVShowDetails
    
    @GET("/api/tv/{id}/credits")
    suspend fun getTVShowCredits(
        @Path("id") id: Int
    ): Credits
    
    @GET("/api/tv/{id}/videos")
    suspend fun getTVShowVideos(
        @Path("id") id: Int
    ): PaginatedResponse<Video>
    
    @GET("/api/tv/{id}/similar")
    suspend fun getSimilarTVShows(
        @Path("id") id: Int
    ): PaginatedResponse<TVShow>

    @GET("/api/tv/{id}/season/{season_number}")
    suspend fun getTVShowSeasonDetails(
        @Path("id") id: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonDetails
} 