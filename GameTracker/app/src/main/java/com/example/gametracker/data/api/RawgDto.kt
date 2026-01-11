package com.example.gametracker.data.api

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    val results: List<GameDto>
)

data class GameDto(
    val id: Long,
    val name: String,
    @SerializedName("background_image") val backgroundImage: String?,
    @SerializedName("released") val releasedDate: String?,
    val metacritic: Int?,
    val playtime: Int?,

    // Details
    val description: String?,
    val genres: List<GenreDto>?,
    val platforms: List<PlatformWrapperDto>?
)

data class GenreDto(val name: String)
data class PlatformWrapperDto(val platform: PlatformDto)
data class PlatformDto(val name: String)