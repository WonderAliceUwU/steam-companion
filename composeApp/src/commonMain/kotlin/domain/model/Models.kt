package com.steamcompanion.domain.model

data class UserProfile(
    val steamId: String,
    val name: String,
    val avatarUrl: String?,
    val level: Int?
)

data class Game(
    val appId: Int,
    val name: String,
    val playtimeMinutes: Int,
    val iconUrl: String?
) {
    val coverUrl: String
        get() = "https://cdn.akamai.steamstatic.com/steam/apps/$appId/library_600x900.jpg"
}

data class Achievement(
    val apiName: String,
    val title: String,
    val description: String?,
    val iconUrl: String?,
    val achieved: Boolean
)
