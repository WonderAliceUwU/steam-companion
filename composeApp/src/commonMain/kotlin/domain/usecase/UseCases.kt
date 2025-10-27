package com.steamcompanion.domain.usecase

import com.steamcompanion.data.steam.SteamRepository
import com.steamcompanion.domain.model.Achievement
import com.steamcompanion.domain.model.Game
import com.steamcompanion.domain.model.UserProfile

class FetchProfile(private val repo: SteamRepository) {
    suspend operator fun invoke(steamId: String): UserProfile? = repo.getProfile(steamId)
}

class FetchTopPlayed(private val repo: SteamRepository) {
    suspend operator fun invoke(steamId: String, limit: Int = 3): List<Game> = repo.getTopPlayed(steamId, limit)
}

class FetchRecentlyPlayed(private val repo: SteamRepository) {
    suspend operator fun invoke(steamId: String, limit: Int = 3): List<Game> = repo.getRecentlyPlayed(steamId, limit)
}

class FetchAchievements(private val repo: SteamRepository) {
    suspend operator fun invoke(steamId: String, appId: Int): List<Achievement> = repo.getAchievements(steamId, appId)
}

class ResolveSteamId(private val repo: SteamRepository) {
    suspend operator fun invoke(input: String): String? = repo.resolveSteamId(input)
}
