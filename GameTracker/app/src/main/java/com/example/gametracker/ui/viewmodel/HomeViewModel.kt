package com.example.gametracker.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.repository.GameRepository
import com.example.gametracker.utils.GameFilterMaps
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GameRepository
) : ViewModel() {

    var games by mutableStateOf<List<Game>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set


    var searchQuery by mutableStateOf("")
    var selectedGenres by mutableStateOf(setOf<String>())
    var selectedPlatforms by mutableStateOf(setOf<String>())

    // Paginação
    private var currentPage = 1
    private var isLastPage = false
    private var searchJob: Job? = null

    init {
        loadGames(reset = true)
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(800)
            loadGames(reset = true)
        }
    }

    fun onFiltersChanged(genres: Set<String>, platforms: Set<String>) {
        selectedGenres = genres
        selectedPlatforms = platforms
        loadGames(reset = true)
    }

    // LazyList Scroll
    fun loadNextPage() {
        if (!isLoading && !isLastPage) {
            loadGames(reset = false)
        }
    }

    private fun loadGames(reset: Boolean) {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            if (reset) {
                currentPage = 1
                isLastPage = false
                games = emptyList()
            }

            val genreSlugs = selectedGenres.mapNotNull { GameFilterMaps.genres[it] }.joinToString(",")
            val platformIds = selectedPlatforms.mapNotNull { GameFilterMaps.platforms[it] }.joinToString(",")

            val finalGenres = genreSlugs.ifBlank { null }
            val finalPlatforms = platformIds.ifBlank { null }
            val finalQuery = searchQuery.ifBlank { null }

            val result = repository.searchRemoteGames(
                query = finalQuery,
                genres = finalGenres,
                platforms = finalPlatforms,
                page = currentPage
            )

            result.onSuccess { newGames ->
                if (newGames.isEmpty()) {
                    isLastPage = true
                } else {
                    games = if (reset) newGames else games + newGames
                    currentPage++
                }
            }.onFailure {
                errorMessage = "Erro ao carregar: ${it.message}"
            }

            isLoading = false
        }
    }
}