package com.example.gametracker.domain.model

enum class GameStatus {
    BACKLOG, PLAYING, COMPLETED, DROPPED
}

data class Game(
    // API (RAWG)
    val id: Long,
    val title: String,
    val description: String? = null, // Vem apenas no endpoint de detalhes, não na busca
    val coverUrl: String?,
    val releaseDate: String?,
    val metacritic: Int?,
    val genres: List<String>,
    val platforms: List<String>,
    val averagePlaytime: Int?,

    // (Local)
    val status: GameStatus = GameStatus.BACKLOG,
    val userRating: Int? = null,     // Nota do usuário (estrelinhas)
    val notes: String? = null,       // Anotações pessoais (ex: "Pegar troféu X")
    val addedAt: Long = System.currentTimeMillis() // Para ordenar por "Adicionados recentemente"
)