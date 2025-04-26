package com.movie.app.best.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVShow(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val first_air_date: String?,
    val original_language: String,
    val original_name: String,
    val genre_ids: List<Int>,
    val popularity: Double,
    val origin_country: List<String>
) : Parcelable

@Parcelize
data class TVShowDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val first_air_date: String?,
    val last_air_date: String?,
    val original_language: String,
    val original_name: String,
    val popularity: Double,
    val genres: List<Genre>,
    val homepage: String?,
    val in_production: Boolean,
    val languages: List<String>,
    val last_episode_to_air: Episode?,
    val next_episode_to_air: Episode?,
    val networks: List<Network>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val type: String
) : Parcelable

@Parcelize
data class Episode(
    val id: Int,
    val name: String,
    val overview: String?,
    val air_date: String?,
    val episode_number: Int,
    val season_number: Int,
    val still_path: String?,
    val runtime: Int? = null,
    val vote_average: Double? = 0.0
) : Parcelable

@Parcelize
data class Network(
    val id: Int,
    val name: String,
    val logo_path: String?,
    val origin_country: String
) : Parcelable

@Parcelize
data class Season(
    val id: Int,
    val name: String,
    val overview: String,
    val air_date: String?,
    val episode_count: Int,
    val poster_path: String?,
    val season_number: Int
) : Parcelable

@Parcelize
data class SeasonDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val air_date: String?,
    val episodes: List<Episode>,
    val poster_path: String?,
    val season_number: Int,
    val _id: String
) : Parcelable 