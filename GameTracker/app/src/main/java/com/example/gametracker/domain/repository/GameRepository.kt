package com.example.gametracker.domain.repository

import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    // RAWG API
    // Busca jogos na API
    suspend fun searchRemoteGames(
        query: String?,
        genres: String?,
        platforms: String?,
        page: Int
    ): Result<List<Game>>

    // Usado quando clica num resultado da HomePage
    suspend fun getGameDetailsRemote(gameId: Long): Result<Game>

    // Room
    // Usado quando clica num item da sua biblioteca
    fun getGameByIdLocal(gameId: Long): Flow<Game?>

    // Observa os jogos salvos.
    fun getSavedGames(): Flow<List<Game>>

    // Filtra jogos locais
    fun getGamesByStatus(status: GameStatus): Flow<List<Game>>

    // Salva um jogo da busca para o banco local (biblioteca)
    suspend fun saveGameToLibrary(game: Game)

    // Atualiza o status
    suspend fun updateGameStatus(gameId: Long, newStatus: GameStatus)

    // Atualiza o rating e review
    suspend fun updateReview(gameId: Long, rating: Int?, notes: String?)

    // Remove do backlog
    suspend fun deleteGame(gameId: Long)
}