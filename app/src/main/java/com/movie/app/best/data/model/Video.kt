package com.movie.app.best.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val published_at: String,
    val iso6391: String,
    val iso31661: String
) : Parcelable

@Parcelize
data class Review(
    val id: String,
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val updated_at: String,
    val url: String
) : Parcelable

@Parcelize
data class AuthorDetails(
    val name: String,
    val username: String,
    val avatar_path: String?,
    val rating: Double?
) : Parcelable 