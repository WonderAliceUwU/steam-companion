package com.steamcompanion.presentation.ui.feature.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Achievement
import com.steamcompanion.domain.model.Game
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    steamId: String,
    game: Game,
    repo: SteamRepositoryImpl,
    onBack: () -> Unit
) {
    var achievements by remember { mutableStateOf(emptyList<Achievement>()) }
    LaunchedEffect(game.appId) {
        achievements = repo.getAchievements(steamId, game.appId)
    }
    val completed = achievements.count { it.achieved }
    val pct = if (achievements.isNotEmpty()) (completed * 100) / achievements.size else 0
    Scaffold(topBar = { TopAppBar(title = { Text(game.name) }, navigationIcon = {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
    }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Completion ${pct}% (${completed}/${achievements.size})", fontWeight = FontWeight.Bold)
            Spacer(Modifier.size(8.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(achievements) { a ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (a.achieved && a.iconUrl != null) {
                            // ✅ show actual unlocked icon
                            KamelImage(
                                resource = { asyncPainterResource(a.iconUrl) },
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        } else {
                            // ❓ show placeholder for locked achievements
                            Box(
                                Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(Modifier.size(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                a.title,
                                fontWeight = if (a.achieved) FontWeight.SemiBold else FontWeight.Normal
                            )
                            a.description?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        if (a.achieved) {
                            Text("✓", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
