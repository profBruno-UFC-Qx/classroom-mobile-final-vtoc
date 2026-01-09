package com.example.gametracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import com.example.gametracker.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<GameStatus?>(null)
    private val _games = repository.getSavedGames()

    val uiState = combine(_searchText, _selectedStatus, _games) { text, status, games ->
        var filtered = games
        if (text.isNotBlank()) {
            filtered = filtered.filter { it.title.contains(text, ignoreCase = true) }
        }
        if (status != null) {
            filtered = filtered.filter { it.status == status }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchTextChange(text: String) { _searchText.value = text }
    fun onTabSelected(status: GameStatus?) { _selectedStatus.value = status }

    fun addGame(game: Game) {
        viewModelScope.launch { repository.saveGameToLibrary(game) }
    }
}