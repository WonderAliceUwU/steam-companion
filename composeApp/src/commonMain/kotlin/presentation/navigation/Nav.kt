package com.steamcompanion.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNav(credentialsStorage: CredentialsStorage) {
    var apiKey by remember { mutableStateOf(credentialsStorage.getApiKey()) }
    var steamId by remember { mutableStateOf(credentialsStorage.getUsername()) }

    val initialScreen = if (apiKey != null && steamId != null) {
        Screen.Main(steamId!!)
    } else {
        Screen.Login
    }

    var screenStack by remember { mutableStateOf(listOf(initialScreen)) }
    val currentScreen = screenStack.last()

    fun goBack() {
        if (screenStack.size > 1) {
            screenStack = screenStack.dropLast(1)
        }
    }

    fun navigateTo(screen: Screen, replace: Boolean = false) {
        screenStack = if (replace) {
            listOf(screen)
        } else {
            screenStack + screen
        }
    }

    val repo = remember(apiKey) {
        SteamRepositoryImpl(SteamApi(keyProvider = { apiKey ?: "" }))
    }
    var tab by rememberSaveable { mutableStateOf(0) }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            val isAchievements = targetState is Screen.Achievements
            val initialIsAchievements = initialState is Screen.Achievements
            if (isAchievements && !initialIsAchievements) {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500)) + fadeOut()
            } else if (!isAchievements && initialIsAchievements) {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500)) + fadeOut()
            } else {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            }
        }
    ) { currentScreen ->
        when (val s = currentScreen) {
            Screen.Login -> LoginScreen(
                onLogin = { key, id ->
                    credentialsStorage.saveCredentials(apiKey = key, username = id)
                    apiKey = key
                    steamId = id
                    navigateTo(Screen.Main(id), replace = true)
                },
                repo = repo
            )
            is Screen.Main -> MainShell(
                steamId = s.steamId,
                repo = repo,
                onOpenAchievements = { game -> navigateTo(Screen.Achievements(s.steamId, game)) },
                onSignOut = {
                    credentialsStorage.clearCredentials()
                    apiKey = null
                    steamId = null
                    navigateTo(Screen.Login, replace = true)
                },
                tab = tab,
                onTabChange = { tab = it }
            )
            is Screen.Achievements -> AchievementsScreen(
                steamId = s.steamId,
                game = s.game,
                repo = repo,
                onBack = { goBack() }
            )
        }
    }
}
