package com.example.gametracker.data.local

import androidx.room.*
import com.example.gametracker.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY addedAt DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Long): GameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM games WHERE status = :status ORDER BY addedAt DESC")
    fun getGamesByStatus(status: GameStatus): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameByIdFlow(id: Long): Flow<GameEntity?>

    @Query("UPDATE games SET status = :newStatus WHERE id = :gameId")
    suspend fun updateGameStatus(gameId: Long, newStatus: GameStatus)

    @Query("UPDATE games SET userRating = :rating, notes = :notes WHERE id = :gameId")
    suspend fun updateReview(gameId: Long, rating: Int?, notes: String?)
}