package com.example.gametracker.data.repository

import com.example.gametracker.data.api.RawgApi
import com.example.gametracker.data.local.GameDao
import com.example.gametracker.data.local.toDomain
import com.example.gametracker.data.mapper.toDomain
import com.example.gametracker.data.mapper.toEntity
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import com.example.gametracker.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConcreteGameRepository(
    private val api: RawgApi,
    private val dao: GameDao,
    private val apiKey: String = "b9e45507eea746a9a1f6291707df5c8c" // Injetar via BuildConfig dps
) : GameRepository {

    // API
    override suspend fun searchRemoteGames(query: String): Result<List<Game>> {
        return try {
            val response = api.searchGames(apiKey, query)
            val games = response.results.map { it.toDomain() }
            Result.success(games)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGameDetailsRemote(gameId: Long): Result<Game> {
        return try {
            val dto = api.getGameDetails(id = gameId, apiKey = apiKey)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ROOM
    override fun getSavedGames(): Flow<List<Game>> {
        return dao.getAllGames().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getGameByIdLocal(gameId: Long): Flow<Game?> {
        return dao.getGameByIdFlow(gameId).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getGamesByStatus(status: GameStatus): Flow<List<Game>> {
        return dao.getGamesByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveGameToLibrary(game: Game) {
        dao.insertGame(game.toEntity())
    }

    override suspend fun updateGameStatus(
        gameId: Long,
        newStatus: GameStatus
    ) {
        dao.updateGameStatus(gameId, newStatus)
    }

    override suspend fun updateReview(gameId: Long, rating: Int?, notes: String?) {
        dao.updateReview(gameId, rating, notes)
    }

    override suspend fun deleteGame(gameId: Long) {
        val entity = dao.getGameById(gameId)
        if (entity != null) {
            dao.deleteGame(entity)
        }
    }

}