package com.steamcompanion.data.steam

import com.steamcompanion.domain.model.Achievement
import com.steamcompanion.domain.model.Game
import com.steamcompanion.domain.model.UserProfile

interface SteamRepository {
    suspend fun resolveSteamId(input: String): String? // steamid64 or vanity
    suspend fun getProfile(steamId: String): UserProfile?
    suspend fun getTopPlayed(steamId: String, limit: Int = 3): List<Game>
    suspend fun getRecentlyPlayed(steamId: String, limit: Int = 3): List<Game>
    suspend fun getAchievements(steamId: String, appId: Int): List<Achievement>
}

class SteamRepositoryImpl(private val api: SteamApi): SteamRepository {
    override suspend fun resolveSteamId(input: String): String? {
        return if (input.all { it.isDigit() }) input else api.resolveVanity(input)
    }

    override suspend fun getProfile(steamId: String): UserProfile? {
        val p = api.getPlayerSummary(steamId) ?: return null
        val level = api.getPlayerLevel(steamId)
        return UserProfile(p.steamid, p.personaName, p.avatarFull, level)
    }

    override suspend fun getTopPlayed(steamId: String, limit: Int): List<Game> {
        val owned = api.getOwnedGames(steamId).response.games
        val sorted = owned.sortedByDescending { it.playtimeMinutes }.take(limit)
        return sorted.map {
            Game(
                appId = it.appId,
                name = it.name ?: "App ${'$'}{it.appId}",
                playtimeMinutes = it.playtimeMinutes,
                iconUrl = it.iconHash?.let { hash -> steamCdnIcon(it.appId, hash) }
            )
        }
    }

    override suspend fun getRecentlyPlayed(steamId: String, limit: Int): List<Game> {
        val recent = api.getRecentlyPlayedGames(steamId).response.games.take(limit)
        return recent.map {
            Game(
                appId = it.appId,
                name = it.name ?: "App ${'$'}{it.appId}",
                playtimeMinutes = it.playtimeTwoWeeks ?: 0,
                iconUrl = it.iconHash?.let { hash -> steamCdnIcon(it.appId, hash) }
            )
        }
    }

    override suspend fun getAchievements(steamId: String, appId: Int): List<Achievement> {
        val schema = api.getSchema(appId)?.game?.availableGameStats?.achievements.orEmpty()
        val player = api.getPlayerAchievements(steamId, appId)?.playerstats?.achievements.orEmpty()
        val achievedSet = player.filter { it.achieved == 1 }.associateBy { it.apiname }
        return schema.map { s ->
            Achievement(
                apiName = s.name,
                title = s.displayName ?: s.name,
                description = s.description,
                iconUrl = s.icon,
                achieved = achievedSet.containsKey(s.name)
            )
        }
    }
}
