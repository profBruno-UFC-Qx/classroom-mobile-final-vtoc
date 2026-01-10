package com.example.gametracker.domain.repository

import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import com.example.gametracker.domain.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeGameRepository : GameRepository {

    private val _localLibrary = MutableStateFlow<List<Game>>(
        listOf(
            Game(1, "The Legend of Zelda: TOTK", "Aventura épica...", "https://upload.wikimedia.org/wikipedia/en/f/fb/The_Legend_of_Zelda_Tears_of_the_Kingdom_cover.jpg", "2023", 96, listOf("Adventure"), listOf("Switch"), 100, GameStatus.PLAYING),
            Game(2, "God of War Ragnarök", "Kratos e Atreus...", "https://upload.wikimedia.org/wikipedia/en/e/ee/God_of_War_Ragnar%C3%B6k_cover.jpg", "2022", 94, listOf("Action"), listOf("PS5"), 40, GameStatus.COMPLETED)
        )
    )

    private val remoteApiGames = listOf(
        Game(1, "The Legend of Zelda: TOTK", "Aventura épica...", "https://upload.wikimedia.org/wikipedia/en/f/fb/The_Legend_of_Zelda_Tears_of_the_Kingdom_cover.jpg", "2023", 96, listOf("Adventure"), listOf("Switch"), 100),
        Game(2, "God of War Ragnarök", "Kratos e Atreus...", "https://upload.wikimedia.org/wikipedia/en/e/ee/God_of_War_Ragnar%C3%B6k_cover.jpg", "2022", 94, listOf("Action"), listOf("PS5"), 40),
        Game(3, "Hollow Knight", "Um metroidvania sombrio.", "https://upload.wikimedia.org/wikipedia/en/0/04/Hollow_Knight_first_cover_art.webp", "2017", 90, listOf("Indie", "Platformer"), listOf("PC", "Switch"), 30),
        Game(4, "Cyberpunk 2077", "RPG futurista.", "https://upload.wikimedia.org/wikipedia/en/9/9f/Cyberpunk_2077_box_art.jpg", "2020", 86, listOf("RPG", "FPS"), listOf("PC", "PS5"), 60),
        Game(5, "Elden Ring", "Mundo aberto Soulslike.", "https://upload.wikimedia.org/wikipedia/pt/0/0d/Elden_Ring_capa.jpg", "2022", 96, listOf("RPG"), listOf("PC", "Xbox"), 100),
        Game(6, "Stardew Valley", "Fazendinha relaxante.", "https://upload.wikimedia.org/wikipedia/en/f/fd/Logo_of_Stardew_Valley.png", "2016", 89, listOf("Indie", "Sim"), listOf("Switch", "PC"), 500)
    )

    suspend fun getPopularGames(): Result<List<Game>> {
        delay(500)
        return Result.success(remoteApiGames)
    }

    override suspend fun searchRemoteGames(query: String): Result<List<Game>> {
        delay(300)
        val results = remoteApiGames.filter { it.title.contains(query, ignoreCase = true) }
        return Result.success(results)
    }

    override suspend fun getGameDetailsRemote(gameId: Long): Result<Game> {
        delay(100)
        val game = remoteApiGames.find { it.id == gameId } ?: _localLibrary.value.find { it.id == gameId }
        return if (game != null) Result.success(game) else Result.failure(Exception("Not found"))
    }

    override fun getGameByIdLocal(gameId: Long): Flow<Game?> {
        return _localLibrary.map { list -> list.find { it.id == gameId } }
    }

    override fun getSavedGames(): Flow<List<Game>> = _localLibrary

    override fun getGamesByStatus(status: GameStatus): Flow<List<Game>> {
        return _localLibrary.map { list -> list.filter { it.status == status } }
    }

    override suspend fun saveGameToLibrary(game: Game) {
        _localLibrary.update { currentList ->
            if (currentList.any { it.id == game.id }) {
                currentList.map { if (it.id == game.id) game else it }
            } else {
                currentList + game
            }
        }
    }

    override suspend fun updateGameStatus(gameId: Long, newStatus: GameStatus) {
        _localLibrary.update { list ->
            list.map { if (it.id == gameId) it.copy(status = newStatus) else it }
        }
    }

    override suspend fun deleteGame(gameId: Long) {
        _localLibrary.update { list -> list.filterNot { it.id == gameId } }
    }

    override suspend fun updateReview(gameId: Long, rating: Int?, notes: String?) {
        _localLibrary.update { list ->
            list.map {
                if (it.id == gameId) it.copy(userRating = rating, notes = notes) else it
            }
        }
    }
}