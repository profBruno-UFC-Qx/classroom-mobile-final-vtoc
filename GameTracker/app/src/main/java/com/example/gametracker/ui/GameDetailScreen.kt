package com.example.gametracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.compose.koinInject
import com.example.gametracker.data.repository.FakeGameRepository
import com.example.gametracker.domain.model.Game
import com.example.gametracker.domain.model.GameStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: Long,
    repository: FakeGameRepository = koinInject(),
    onBack: () -> Unit
) {

    val scope = rememberCoroutineScope()
    var game by remember { mutableStateOf<Game?>(null) }

    LaunchedEffect(gameId) {
        game = repository.getGameDetailsRemote(gameId).getOrNull()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game?.title ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { p ->
        if (game != null) {
            Column(
                modifier = Modifier
                    .padding(p)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = game!!.coverUrl,
                    contentDescription = null,
                    modifier = Modifier.height(250.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(game!!.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    AssistChip(onClick = {}, label = { Text(game!!.status.name) })
                    Spacer(Modifier.height(16.dp))
                    Text(game!!.description ?: "Sem descrição disponível.")
                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                repository.saveGameToLibrary(game!!.copy(status = GameStatus.BACKLOG))
                                onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Adicionar à Minha Lista")
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}