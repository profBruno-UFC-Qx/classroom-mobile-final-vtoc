package com.example.gametracker.domain.repository

import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    // (RAWG API)
    // Busca jogos na API para a tela de "Adicionar Novo"
    // Retorna uma lista simples, pois é uma ação pontual (Request/Response)
    suspend fun searchRemoteGames(query: String = ""): Result<List<Game>>

    // Usado quando clica num resultado da busca (RAWG API)
    suspend fun getGameDetailsRemote(gameId: Long): Result<Game>

    // (Room Database) ---
    // Usado quando clica num item da sua biblioteca
    fun getGameByIdLocal(gameId: Long): Flow<Game?>

    // Observa os jogos salvos.
    fun getSavedGames(): Flow<List<Game>>

    // Filtra jogos locais (Ex: só os que estou "Jogando")
    fun getGamesByStatus(status: GameStatus): Flow<List<Game>>

    // Salva um jogo da busca para o seu banco local
    suspend fun saveGameToLibrary(game: Game)

    // Atualiza o status (Ex: de Backlog -> Playing)
    suspend fun updateGameStatus(gameId: Long, newStatus: GameStatus)

    // Remove do backlog
    suspend fun deleteGame(gameId: Long)
}