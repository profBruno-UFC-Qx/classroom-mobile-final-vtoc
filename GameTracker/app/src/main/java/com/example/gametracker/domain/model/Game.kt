package com.example.gametracker.domain.model

enum class GameStatus {
    BACKLOG, PLAYING, COMPLETED, DROPPED
}

data class Game(
    val id: Long,               // ID do RAWG
    val title: String,
    val coverUrl: String?,
    val platforms: List<String>,
    val status: GameStatus = GameStatus.BACKLOG,
    val userRating: Int? = null
)