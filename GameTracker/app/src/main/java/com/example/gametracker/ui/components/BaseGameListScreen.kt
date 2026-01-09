package com.example.gametracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseGameListScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    headerContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Pesquisar...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalIconButton(onClick = onFilterClick) {
                Icon(Icons.Default.List, contentDescription = "Filtros")
            }
        }

        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            headerContent()
        }

        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}