package com.steamcompanion.data.steam

import com.russhwolf.settings.Settings
import com.steamcompanion.domain.model.Achievement
import com.steamcompanion.domain.model.Game
import com.steamcompanion.domain.model.UserProfile

interface SteamRepository {
    suspend fun resolveSteamId(input: String): String?
    suspend fun getProfile(steamId: String): UserProfile?
    suspend fun getTopPlayed(steamId: String, limit: Int = 3): List<Game>
    suspend fun getRecentlyPlayed(steamId: String, limit: Int = 3): List<Game>
    suspend fun getAchievements(steamId: String, appId: Int): List<Achievement>
    fun clearCaches()
}

class SteamRepositoryImpl(private val api: SteamApi) : SteamRepository {
    private val settings = Settings()
    private var userName: String? = null
    private var userAvatar: String? = null
    private val recentlyPlayedCache = mutableMapOf<String, List<Game>>()

    companion object {
        private const val KEY_USER_LEVEL = "user_level"
    }

    override suspend fun resolveSteamId(input: String): String? {
        return if (input.all { it.isDigit() }) input else api.resolveVanity(input)
    }

    override suspend fun getProfile(steamId: String): UserProfile? {
        if (userName != null && userAvatar != null) {
            return UserProfile(steamId, userName!!, userAvatar!!, userLevel)
        }

        val p = api.getPlayerSummary(steamId) ?: return null
        val level = api.getPlayerLevel(steamId)

        userName = p.personaName
        userAvatar = p.avatarFull
        userLevel = level

        return UserProfile(p.steamid, p.personaName, p.avatarFull, level)
    }

    override suspend fun getTopPlayed(steamId: String, limit: Int): List<Game> {
        val owned = api.getOwnedGames(steamId).response.games
        val sorted = owned.sortedByDescending { it.playtimeMinutes }.take(limit)
        return sorted.map {
            Game(
                appId = it.appId,
                name = it.name ?: "App ${"$"}{it.appId}",
                playtimeMinutes = it.playtimeMinutes,
                iconUrl = it.iconHash?.let { hash -> steamCdnIcon(it.appId, hash) }
            )
        }
    }

    override suspend fun getRecentlyPlayed(steamId: String, limit: Int): List<Game> {
        // Check in-memory cache first
        if (recentlyPlayedCache.containsKey(steamId)) {
            return recentlyPlayedCache.getValue(steamId)
        }
        // Fetch from network if not in cache
        val recent = api.getRecentlyPlayedGames(steamId).response.games.take(limit)
        val games = recent.map {
            Game(
                appId = it.appId,
                name = it.name ?: $$"App ${it.appId}",
                playtimeMinutes = it.playtimeTwoWeeks ?: 0,
                iconUrl = it.iconHash?.let { hash -> steamCdnIcon(it.appId, hash) }
            )
        }
        recentlyPlayedCache[steamId] = games // Store in cache
        return games
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

    override fun clearCaches() {
        userName = null
        userAvatar = null
        recentlyPlayedCache.clear()

        settings.remove(KEY_USER_LEVEL)
    }

    private var userLevel: Int?
        get() = settings.getInt(KEY_USER_LEVEL, -1).takeIf { it != -1 }
        set(value) {
            settings.putInt(KEY_USER_LEVEL, value ?: -1)
        }
}
