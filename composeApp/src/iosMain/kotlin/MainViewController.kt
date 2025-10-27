package com.steamcompanion

import androidx.compose.ui.window.ComposeUIViewController
import data.CredentialsStorage

fun MainViewController() = ComposeUIViewController {
    val credentialsStorage = CredentialsStorage()
    App(credentialsStorage)
}
