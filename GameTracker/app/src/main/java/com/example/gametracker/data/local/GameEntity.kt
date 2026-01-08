package com.example.gametracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gametracker.domain.model.GameStatus

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val coverUrl: String?,
    val description: String?,
    val status: GameStatus,
    val genres: List<String>,
    val platforms: List<String>,
    val addedAt: Long = System.currentTimeMillis()
)

fun GameEntity.toDomain() = com.example.gametracker.domain.model.Game(
    id = id, title = title, coverUrl = coverUrl, description = description,
    status = status, genres = genres, platforms = platforms, addedAt = addedAt,
    metacritic = null, releaseDate = null, averagePlaytime = null
)