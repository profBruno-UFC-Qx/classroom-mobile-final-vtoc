package com.example.gametracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditGameDialog(
    currentRating: Int?,
    currentNotes: String?,
    onDismiss: () -> Unit,
    onSave: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(currentRating ?: 0) }
    var notes by remember { mutableStateOf(currentNotes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Avaliar Jogo", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sua Nota", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        val isSelected = star <= rating
                        val starColor = if (isSelected) Color(0xFFFFC107) else Color.LightGray.copy(alpha = 0.5f)

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Estrela $star",
                            tint = starColor,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { rating = star }
                        )
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("O que você achou?") },
                    placeholder = { Text("Gráficos, jogabilidade, história...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(rating, notes) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}