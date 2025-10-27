package com.steamcompanion.presentation.ui.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Game
import com.steamcompanion.presentation.ui.feature.home.HomeScreen
import com.steamcompanion.presentation.ui.feature.profile.ProfileScreen

@Composable
fun MainShell(
    steamId: String,
    repo: SteamRepositoryImpl,
    onOpenAchievements: (Game) -> Unit,
    onSignOut: () -> Unit
) {
    var tab by remember { mutableStateOf(0) } // 0=Home, 1=Profile
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                0 -> HomeScreen(steamId = steamId, repo = repo)
                else -> ProfileScreen(
                    steamId = steamId,
                    repo = repo,
                    onOpenAchievements = onOpenAchievements,
                    onSignOut = onSignOut
                )
            }
        }
    }
}
