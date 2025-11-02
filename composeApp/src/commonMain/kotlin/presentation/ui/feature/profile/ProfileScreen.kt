package com.steamcompanion.presentation.ui.feature.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Game
import com.steamcompanion.presentation.ui.components.Avatar
import com.steamcompanion.presentation.ui.components.GameRow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    modifier: Modifier,
    steamId: String,
    repo: SteamRepositoryImpl,
    onOpenAchievements: (Game) -> Unit,
    onSignOut: () -> Unit,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("...") }
    var avatar by remember { mutableStateOf<String?>(null) }
    var level by remember { mutableStateOf<Int?>(null) }
    var games by remember { mutableStateOf<List<Game>>(emptyList()) }
    LaunchedEffect(steamId) {
        val p = repo.getProfile(steamId)
        name = p?.name ?: "Unknown"
        avatar = p?.avatarUrl
        level = p?.level
        games = repo.getRecentlyPlayed(steamId, 5)
    }

    BoxWithConstraints(modifier = modifier) {
        val width = constraints.maxWidth.toFloat()
        val scope = rememberCoroutineScope()
        val offsetX = remember { Animatable(0f) }
        val draggableState = rememberDraggableState { delta ->
            scope.launch {
                // Only allow dragging from left to right to go back
                val newOffset = (offsetX.value + delta).coerceAtLeast(0f)
                offsetX.snapTo(newOffset)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = 1f - ((offsetX.value / width) * 2).coerceIn(0f, 1f)
                }
                .offset {
                    IntOffset(
                        x = offsetX.value.roundToInt(),
                        y = 0
                    )
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = draggableState,
                    onDragStopped = {
                        scope.launch {
                            if (offsetX.value > width / 3) { // Threshold to trigger back
                                onGoBack()
                                offsetX.snapTo(0f)
                            } else {
                                offsetX.animateTo(0f, spring())
                            }
                        }
                    }
                )
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Button(onClick = onSignOut) {
                    Text("Sign Out")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Avatar(avatarUrl = avatar)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    level?.let { Text("Level $it", style = MaterialTheme.typography.bodyMedium) }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Recently Played")
            Spacer(Modifier.height(8.dp))
            games.forEach { g ->
                GameRow(g, onClick = { onOpenAchievements(g) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}