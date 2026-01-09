package com.example.gametracker.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApi {

    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String,
        @Query("search") query: String,
        @Query("page_size") pageSize: Int = 20
    ): GamesResponse

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Long,
        @Query("key") apiKey: String
    ): GameDto
}