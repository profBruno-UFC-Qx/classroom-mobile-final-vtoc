package com.example.gametracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.gametracker.domain.model.GameStatus
import com.example.gametracker.ui.components.*

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onGameClick: (Long) -> Unit
) {
    val games by viewModel.uiState.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf<GameStatus?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    var selectedGenres by remember { mutableStateOf(setOf<String>()) }
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }

    val finalGames = games.filter { game ->
        (selectedGenres.isEmpty() || game.genres.any { it in selectedGenres }) &&
                (selectedPlatforms.isEmpty() || game.platforms.any { it in selectedPlatforms })
    }

    BaseGameListScreen(
        searchQuery = searchText,
        onSearchQueryChange = {
            searchText = it
            viewModel.onSearchTextChange(it)
        },
        onFilterClick = { showFilterDialog = true },

        headerContent = {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedTab == null,
                        onClick = {
                            selectedTab = null
                            viewModel.onTabSelected(null)
                        },
                        label = { Text("Todos") }
                    )
                }
                items(GameStatus.entries) { status ->
                    FilterChip(
                        selected = selectedTab == status,
                        onClick = {
                            selectedTab = status
                            viewModel.onTabSelected(status)
                        },
                        label = { Text(status.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },

        content = {
            if (finalGames.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum jogo encontrado nesta lista.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(finalGames) { game ->
                        Box(modifier = Modifier.clickable { onGameClick(game.id) }) {
                            GameItemCard(game)
                        }
                    }
                }
            }
        }
    )

    if (showFilterDialog) {
        val allGenres = listOf("RPG", "Action", "Adventure", "Indie", "Strategy")
        val allPlatforms = listOf("PC", "PS5", "Xbox", "Switch")

        GameFilterDialog(
            onDismiss = { showFilterDialog = false },
            onClear = {
                selectedGenres = emptySet()
                selectedPlatforms = emptySet()
            },
            sections = listOf(
                FilterSectionModel(
                    title = "Gênero",
                    options = allGenres,
                    selectedOptions = selectedGenres.toList(),
                    onOptionSelected = { genre ->
                        selectedGenres = if (selectedGenres.contains(genre)) {
                            selectedGenres - genre
                        } else {
                            selectedGenres + genre
                        }
                    }
                ),
                FilterSectionModel(
                    title = "Plataforma",
                    options = allPlatforms,
                    selectedOptions = selectedPlatforms.toList(),
                    onOptionSelected = { platform ->
                        selectedPlatforms = if (selectedPlatforms.contains(platform)) {
                            selectedPlatforms - platform
                        } else {
                            selectedPlatforms + platform
                        }
                    }
                )
            )
        )
    }
}