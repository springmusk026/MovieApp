package com.movie.app.best.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Credits(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
) : Parcelable

@Parcelize
data class Cast(
    val adult: Boolean,
    val gender: Int?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?,
    val castId: Int,
    val character: String,
    val creditId: String,
    val order: Int
) : Parcelable

@Parcelize
data class Crew(
    val adult: Boolean,
    val gender: Int?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?,
    val credit_id: String,
    val department: String,
    val job: String
) : Parcelable 