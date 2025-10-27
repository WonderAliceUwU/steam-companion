package com.steamcompanion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.steamcompanion.data.steam.SteamApi
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Game
import com.steamcompanion.presentation.ui.feature.achievements.AchievementsScreen
import com.steamcompanion.presentation.ui.feature.auth.LoginScreen
import com.steamcompanion.presentation.ui.feature.main.MainShell
import data.CredentialsStorage

sealed interface Screen {
    object Login : Screen
    data class Main(val steamId: String) : Screen
    data class Achievements(val steamId: String, val game: Game) : Screen
}

@Composable
fun RootNav(credentialsStorage: CredentialsStorage) {
    var apiKey by remember { mutableStateOf(credentialsStorage.getApiKey()) }
    var steamId by remember { mutableStateOf(credentialsStorage.getUsername()) }

    val initialScreen = if (apiKey != null && steamId != null) {
        Screen.Main(steamId!!)
    } else {
        Screen.Login
    }

    var screen by remember { mutableStateOf(initialScreen) }

    val repo = remember(apiKey) {
        SteamRepositoryImpl(SteamApi(keyProvider = { apiKey ?: "" }))
    }

    when (val s = screen) {
        Screen.Login -> LoginScreen(
            onLogin = { key, id ->
                credentialsStorage.saveCredentials(apiKey = key, username = id)
                apiKey = key
                steamId = id
                screen = Screen.Main(id)
            },
            repo = repo
        )
        is Screen.Main -> MainShell(
            steamId = s.steamId,
            repo = repo,
            onOpenAchievements = { game -> screen = Screen.Achievements(s.steamId, game) },
            onSignOut = {
                credentialsStorage.clearCredentials()
                apiKey = null
                steamId = null
                screen = Screen.Login
            }
        )
        is Screen.Achievements -> AchievementsScreen(
            steamId = s.steamId,
            game = s.game,
            repo = repo,
            onBack = { screen = Screen.Main(s.steamId) }
        )
    }
}
