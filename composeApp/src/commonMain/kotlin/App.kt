package com.steamcompanion

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.steamcompanion.presentation.navigation.RootNav
import data.CredentialsStorage

val darkColorScheme = darkColorScheme(
    background = Color(0xFF1F1A30)
)

@Composable
fun App(credentialsStorage: CredentialsStorage) {
    MaterialTheme(
        colorScheme = darkColorScheme,
        typography = typography
    ) {
        Surface(Modifier.fillMaxSize()) {
            RootNav(credentialsStorage)
        }
    }
}