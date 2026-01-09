package com.example.gametracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import com.example.gametracker.domain.repository.GameRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: Long,
    repository: GameRepository = koinInject(),
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var remoteGame by remember { mutableStateOf<Game?>(null) }
    LaunchedEffect(gameId) {
        remoteGame = repository.getGameDetailsRemote(gameId).getOrNull()
    }

    val localGame by repository.getGameByIdLocal(gameId).collectAsState(initial = null)
    val gameDisplay = localGame ?: remoteGame
    val isAddedToLibrary = localGame != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        if (gameDisplay != null) {
            val currentGame = gameDisplay!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentGame.coverUrl)
                            .crossfade(true)
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                    startY = 300f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = currentGame.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentGame.releaseDate ?: "Data desconhecida",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isAddedToLibrary) {
                            val statusColor = when(currentGame.status) {
                                GameStatus.PLAYING -> MaterialTheme.colorScheme.primary
                                GameStatus.COMPLETED -> Color(0xFF4CAF50)
                                GameStatus.DROPPED -> Color.Red
                                else -> MaterialTheme.colorScheme.secondary
                            }

                            AssistChip(
                                onClick = { /* Futuro: Abrir Dialog para mudar status */ },
                                label = { Text(currentGame.status.name) },
                                leadingIcon = {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = statusColor)
                                }
                            )
                        } else {
                            Text("Não está na sua lista", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }

                        if (currentGame.metacritic != null) {
                            MetacriticBadge(score = currentGame.metacritic)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        InfoItem(Icons.Default.DateRange, "${currentGame.averagePlaytime ?: "?"} h", "Tempo")
                        InfoItem(Icons.Default.Star, "${currentGame.userRating ?: "-"} / 5", "Nota")
                        InfoItem(Icons.Default.DateRange, currentGame.releaseDate?.take(4) ?: "N/A", "Ano")
                    }

                    HorizontalDivider(Modifier.padding(vertical = 16.dp))

                    Text("Gêneros", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(currentGame.genres) { genre ->
                            SuggestionChip(onClick = {}, label = { Text(genre) })
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text("Plataformas", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(currentGame.platforms) { platform ->
                            FilterChip(selected = true, onClick = {}, label = { Text(platform) })
                        }
                    }

                    HorizontalDivider(Modifier.padding(vertical = 16.dp))

                    Text("Sobre o Jogo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = currentGame.description ?: "Sem descrição disponível.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Spacer(Modifier.height(24.dp))

                    if (isAddedToLibrary && !currentGame.notes.isNullOrEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Suas Anotações", fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(currentGame.notes, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                if (!isAddedToLibrary) {
                                    repository.saveGameToLibrary(currentGame.copy(status = GameStatus.BACKLOG))
                                } else {
                                    // Futuro: Poderia abrir menu de edição. Por enquanto apenas re-salva.
                                    // repository.saveGameToLibrary(currentGame)
                                }
                                // onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAddedToLibrary) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            if (isAddedToLibrary) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isAddedToLibrary) "Salvo na Lista" else "Adicionar à Minha Lista")
                    }

                    if (isAddedToLibrary && currentGame.addedAt > 0) {
                        val date = Date(currentGame.addedAt)
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        Text(
                            text = "Adicionado em: ${format.format(date)}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun MetacriticBadge(score: Int) {
    val color = when {
        score >= 75 -> Color(0xFF66CC33)
        score >= 50 -> Color(0xFFFFCC33)
        else -> Color(0xFFFF0000)
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.size(width = 32.dp, height = 32.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = score.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}