package com.steamcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.steamcompanion.presentation.navigation.RootNav
import data.CredentialsStorage

val steamStoreGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF080608),
        Color(0xFF1B1730),
        Color(0xFF2E2650),
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

@Composable
fun App(credentialsStorage: CredentialsStorage) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color.Transparent
        ),
        typography = typography
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(steamStoreGradient)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x332F1E66),
                                Color.Transparent
                            ),
                            center = Offset(200f, 200f),
                            radius = 600f
                        )
                    )
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {
                RootNav(credentialsStorage)
            }
        }
    }
}