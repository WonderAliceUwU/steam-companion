package com.steamcompanion.presentation.ui.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.steamcompanion.data.steam.SteamRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, repo: SteamRepositoryImpl) {
    var key by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var resolving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("Steam Companion", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = key, onValueChange = { key = it }, label = { Text("Steam API Key") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = idInput, onValueChange = { idInput = it }, label = { Text("SteamID64 or Vanity URL") }, singleLine = true)
        Spacer(Modifier.height(16.dp))
        Button(enabled = key.isNotBlank() && idInput.isNotBlank() && !resolving, onClick = {
            resolving = true
            error = null
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                val steamId = repo.resolveSteamId(idInput)
                if (steamId != null) onLogin(key, steamId) else error = "Could not resolve Steam ID"
                resolving = false
            }
        }) { Text(if (resolving) "Resolving..." else "Login with API Key") }
        error?.let { Spacer(Modifier.height(8.dp)); Text(it, color = MaterialTheme.colorScheme.error) }
    }
}
