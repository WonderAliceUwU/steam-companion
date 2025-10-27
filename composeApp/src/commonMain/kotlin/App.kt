package com.steamcompanion

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.steamcompanion.presentation.navigation.RootNav
import data.CredentialsStorage

@Composable
fun App(credentialsStorage: CredentialsStorage) {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            RootNav(credentialsStorage)
        }
    }
}
