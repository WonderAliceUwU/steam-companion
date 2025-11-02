package com.steamcompanion.presentation.ui.feature.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.steamcompanion.data.steam.SteamRepositoryImpl
import com.steamcompanion.domain.model.Game
import com.steamcompanion.presentation.ui.feature.home.HomeScreen
import com.steamcompanion.presentation.ui.feature.profile.ProfileScreen
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainShell(
    steamId: String,
    repo: SteamRepositoryImpl,
    onOpenAchievements: (Game) -> Unit,
    onSignOut: () -> Unit,
    tab: Int,
    onTabChange: (Int) -> Unit
) {
    val hazeState = rememberHazeState()
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Person)
    val barShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(barShape)
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF5b677a), Color.Transparent)
                        ),
                        shape = barShape
                    )
                    .pointerInput(Unit) {}

            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .hazeEffect(
                            state = hazeState,
                            style = HazeStyle(
                                backgroundColor = Color.Transparent,
                                tints = listOf(HazeTint(Color(0xFF192736).copy(alpha = 0.5f))),
                                noiseFactor = 0.1f,
                                fallbackTint = HazeTint(Color(0xFF192736).copy(alpha = 0.5f))
                            )
                        )
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val iconSize = 48.dp
                    val space = (maxWidth - (icons.size * iconSize)) / (icons.size + 1)
                    val targetOffset = space + (space + iconSize) * tab

                    val animatedIndicatorOffset by animateDpAsState(
                        targetValue = targetOffset,
                        animationSpec = tween(durationMillis = 300)
                    )

                    Box(
                        modifier = Modifier
                            .offset(x = animatedIndicatorOffset)
                            .size(iconSize)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        Color(0xFF7B6BFF).copy(alpha = 0.35f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        icons.forEachIndexed { index, icon ->
                            val selected = tab == index
                            val tint by animateColorAsState(
                                targetValue = if (selected) Color(0xFFa9c6ff)
                                else Color(0xFF9E9E9E).copy(alpha = 0.7f),
                                animationSpec = tween(300)
                            )

                            Box(
                                modifier = Modifier
                                    .size(iconSize)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onTabChange(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { _ ->
        val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding, bottom = 0.dp)
        ) {
            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500)) + fadeOut()
                    } else {
                        slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500)) + fadeOut()
                    }
                }
            ) { currentTab ->
                when (currentTab) {
                    0 -> HomeScreen(
                        modifier = Modifier.hazeSource(state = hazeState),
                        steamId = steamId,
                        repo = repo,
                    )

                    else -> ProfileScreen(
                        modifier = Modifier.hazeSource(state = hazeState),
                        steamId = steamId,
                        repo = repo,
                        onOpenAchievements = onOpenAchievements,
                        onSignOut = onSignOut,
                        onGoBack = { onTabChange(0) }
                    )
                }
            }
        }
    }
}
