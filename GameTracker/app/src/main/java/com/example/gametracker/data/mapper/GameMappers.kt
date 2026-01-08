package com.example.gametracker.data.mapper

import com.example.gametracker.data.api.GameDto
import com.example.gametracker.data.local.GameEntity
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus

fun GameDto.toDomain(): Game {
    return Game(
        id = this.id,
        title = this.name,
        coverUrl = this.backgroundImage,
        description = this.description,
        releaseDate = this.releasedDate,
        metacritic = this.metacritic,
        averagePlaytime = this.playtime,

        genres = this.genres?.map { it.name } ?: emptyList(),
        platforms = this.platforms?.map { it.platform.name } ?: emptyList(),
        status = GameStatus.BACKLOG
    )
}

fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = this.id,
        title = this.title,
        coverUrl = this.coverUrl,
        description = this.description,
        status = this.status,
        genres = this.genres,
        platforms = this.platforms,
        addedAt = this.addedAt
    )
}