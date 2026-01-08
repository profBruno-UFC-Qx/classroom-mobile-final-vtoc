package com.example.gametracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gametracker.data.repository.FakeGameRepository
import com.example.gametracker.domain.model.Game
import com.example.gametracker.ui.components.*

@Composable
fun HomeScreen(onGameClick: (Long) -> Unit) {
    val repository = remember { FakeGameRepository() }
    var allGames by remember { mutableStateOf(emptyList<Game>()) }
    var searchText by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedGenres by remember { mutableStateOf(setOf<String>()) }
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(Unit) {
        allGames = repository.getPopularGames().getOrDefault(emptyList())
    }

    val filteredGames = allGames.filter { game ->
        (searchText.isEmpty() || game.title.contains(searchText, ignoreCase = true)) &&
                (selectedGenres.isEmpty() || game.genres.any { it in selectedGenres }) &&
                (selectedPlatforms.isEmpty() || game.platforms.any { it in selectedPlatforms })
    }

    BaseGameListScreen(
        searchQuery = searchText,
        onSearchQueryChange = { searchText = it },
        onFilterClick = { showFilterDialog = true },
        headerContent = { }, // SEM ABAS AQUI
        content = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredGames) { game ->
                    Box(Modifier.clickable { onGameClick(game.id) }) {
                        GameItemCard(game)
                    }
                }
            }
        }
    )

    if (showFilterDialog) {
        GameFilterDialog(
            onDismiss = { showFilterDialog = false },
            onClear = { selectedGenres = emptySet(); selectedPlatforms = emptySet() },
            sections = listOf(
                FilterSectionModel("Gênero", listOf("RPG", "Action", "Adventure", "Indie"), selectedGenres.toList()) {
                    selectedGenres = if (it in selectedGenres) selectedGenres - it else selectedGenres + it
                },
                FilterSectionModel("Plataforma", listOf("PC", "PS5", "Switch", "Xbox"), selectedPlatforms.toList()) {
                    selectedPlatforms = if (it in selectedPlatforms) selectedPlatforms - it else selectedPlatforms + it
                }
            )
        )
    }
}