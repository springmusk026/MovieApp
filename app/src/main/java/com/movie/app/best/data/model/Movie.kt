package com.movie.app.best.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String?,
    val original_language: String,
    val original_title: String,
    val genreIds: List<Int>,
    val adult: Boolean,
    val popularity: Double,
    val video: Boolean
) : Parcelable

@Parcelize
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String?,
    val original_language: String,
    val original_title: String,
    val adult: Boolean,
    val popularity: Double,
    val video: Boolean,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String?,
    val imdbId: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val revenue: Long,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val belongs_to_collection: Collection?
) : Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
) : Parcelable

@Parcelize
data class ProductionCountry(
    val iso31661: String,
    val name: String
) : Parcelable

@Parcelize
data class SpokenLanguage(
    val english_name: String,
    val iso6391: String,
    val name: String
) : Parcelable

@Parcelize
data class Collection(
    val id: Int,
    val name: String,
    val poster_path: String?,
    val backdrop_path: String?
) : Parcelable 