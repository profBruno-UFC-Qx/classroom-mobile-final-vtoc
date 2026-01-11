package com.example.gametracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.gametracker.ui.components.*
import com.example.gametracker.ui.viewmodel.HomeViewModel
import com.example.gametracker.utils.GameFilterMaps

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onGameClick: (Long) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    val games = viewModel.games
    val isLoading = viewModel.isLoading
    val searchText = viewModel.searchQuery

    BaseGameListScreen(
        searchQuery = searchText,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        onFilterClick = { showFilterDialog = true },
        headerContent = { },
        content = {
            if (games.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum jogo encontrado.", color = Color.Gray)
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(games) { index, game ->
                    if (index >= games.lastIndex - 1 && !isLoading) {
                        LaunchedEffect(Unit) {
                            viewModel.loadNextPage()
                        }
                    }

                    Box(Modifier.clickable { onGameClick(game.id) }) {
                        GameItemCard(game = game, showStatus = false)
                    }
                }

                if (isLoading && games.isNotEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (isLoading && games.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    )

    if (showFilterDialog) {
        GameFilterDialog(
            onDismiss = { showFilterDialog = false },
            onClear = {
                viewModel.onFiltersChanged(emptySet(), emptySet())
            },
            sections = listOf(
                FilterSectionModel(
                    "Gênero",
                    GameFilterMaps.genres.keys.toList().sorted(),
                    viewModel.selectedGenres.toList()
                ) { clickedGenre ->
                    val current = viewModel.selectedGenres
                    val newSet = if (clickedGenre in current) current - clickedGenre else current + clickedGenre
                    viewModel.onFiltersChanged(newSet, viewModel.selectedPlatforms)
                },

                FilterSectionModel(
                    "Plataforma",
                    GameFilterMaps.platforms.keys.toList(),
                    viewModel.selectedPlatforms.toList()
                ) { clickedPlatform ->
                    val current = viewModel.selectedPlatforms
                    val newSet = if (clickedPlatform in current) current - clickedPlatform else current + clickedPlatform
                    viewModel.onFiltersChanged(viewModel.selectedGenres, newSet)
                }
            )
        )
    }
}