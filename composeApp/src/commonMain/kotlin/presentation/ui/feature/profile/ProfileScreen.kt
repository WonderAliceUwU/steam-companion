package com.steamcompanion.presentation.ui.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Game
import com.steamcompanion.presentation.ui.components.Avatar
import com.steamcompanion.presentation.ui.components.GameRow

@Composable
fun ProfileScreen(
    steamId: String,
    repo: SteamRepositoryImpl,
    onOpenAchievements: (Game) -> Unit,
    onSignOut: () -> Unit
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
    Column(
        Modifier.fillMaxSize().padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profile", style = MaterialTheme.typography.titleLarge)
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
