package com.example.gametracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus

@Composable
fun GameItemCard(game: Game) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(100.dp)) {
            AsyncImage(
                model = game.coverUrl,
                contentDescription = null,
                modifier = Modifier.width(80.dp).fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Surface(
                    color = getStatusColor(game.status),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = game.status.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun getStatusColor(status: GameStatus): Color {
    return when (status) {
        GameStatus.PLAYING -> Color(0xFF2E7D32)
        GameStatus.COMPLETED -> Color(0xFF1565C0)
        GameStatus.BACKLOG -> Color(0xFFEF6C00)
        GameStatus.DROPPED -> Color(0xFFC62828)
    }
}