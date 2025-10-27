package com.steamcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import data.CredentialsStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val credentialsStorage = CredentialsStorage(applicationContext)

        setContent { App(credentialsStorage) }
    }
}
