package com.steamcompanion.data.steam

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SteamApi(
    private val keyProvider: () -> String,
    private val client: HttpClient = defaultClient()
) {
    companion object {
        private fun defaultClient() = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    suspend fun resolveVanity(vanity: String): String? {
        val resp: VanityResponse = client.get("https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/") {
            parameter("key", keyProvider())
            parameter("vanityurl", vanity)
        }.body()
        return if (resp.response.success == 1) resp.response.steamid else null
    }

    suspend fun getPlayerSummary(steamId: String): PlayerSummariesResponse.Player? {
        val resp: PlayerSummariesResponse = client.get("https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/") {
            parameter("key", keyProvider())
            parameter("steamids", steamId)
        }.body()
        return resp.response.players.firstOrNull()
    }

    suspend fun getPlayerLevel(steamId: String): Int? = try {
        val resp: BadgesResponse = client.get("https://api.steampowered.com/IPlayerService/GetBadges/v1/") {
            parameter("key", keyProvider())
            parameter("steamid", steamId)
        }.body()
        resp.response.player_level
    } catch (e: Exception) { null }

    suspend fun getRecentlyPlayedGames(steamId: String): RecentlyPlayedResponse {
        return client.get("https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/") {
            parameter("key", keyProvider())
            parameter("steamid", steamId)
        }.body()
    }

    suspend fun getOwnedGames(steamId: String): OwnedGamesResponse {
        return client.get("https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/") {
            parameter("key", keyProvider())
            parameter("steamid", steamId)
            parameter("include_appinfo", 1)
            parameter("include_played_free_games", 1)
        }.body()
    }

    suspend fun getPlayerAchievements(steamId: String, appId: Int): PlayerAchievementsResponse? {
        return try {
            client.get("https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/") {
                parameter("key", keyProvider())
                parameter("steamid", steamId)
                parameter("appid", appId)
            }.body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSchema(appId: Int): GameSchemaResponse? = try {
        client.get("https://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/") {
            parameter("key", keyProvider())
            parameter("appid", appId)
        }.body()
    } catch (e: Exception) { null }
}

@Serializable
data class BadgesResponse(@SerialName("response") val response: BadgesInner) {
    @Serializable
    data class BadgesInner(@SerialName("player_level") val player_level: Int? = null)
}

@Serializable
data class RecentlyPlayedResponse(@SerialName("response") val response: RecentlyInner) {
    @Serializable
    data class RecentlyInner(
        @SerialName("total_count") val total: Int? = null,
        val games: List<Game> = emptyList()
    )
    @Serializable
    data class Game(
        @SerialName("appid") val appId: Int,
        @SerialName("name") val name: String? = null,
        @SerialName("playtime_2weeks") val playtimeTwoWeeks: Int? = null,
        @SerialName("img_icon_url") val iconHash: String? = null
    )
}

@Serializable
private data class VanityResponse(val response: VanityInner) {
    @Serializable
    data class VanityInner(val success: Int, val steamid: String? = null)
}

@Serializable
data class PlayerSummariesResponse(val response: Players) {
    @Serializable
    data class Players(val players: List<Player>)
    @Serializable
    data class Player(
        val steamid: String,
        @SerialName("personaname") val personaName: String,
        @SerialName("avatarfull") val avatarFull: String? = null
    )
}

@Serializable
data class OwnedGamesResponse(@SerialName("response") val response: OwnedGamesInner) {
    @Serializable
    data class OwnedGamesInner(
        @SerialName("game_count") val count: Int? = null,
        val games: List<Game> = emptyList()
    )
    @Serializable
    data class Game(
        @SerialName("appid") val appId: Int,
        @SerialName("name") val name: String? = null,
        @SerialName("playtime_forever") val playtimeMinutes: Int = 0,
        @SerialName("img_icon_url") val iconHash: String? = null
    )
}

@Serializable
data class PlayerAchievementsResponse(val playerstats: PlayerStats? = null) {
    @Serializable
    data class PlayerStats(
        val steamID: String? = null,
        val gameName: String? = null,
        val achievements: List<Achievement>? = null
    )
    @Serializable
    data class Achievement(
        val apiname: String,
        val achieved: Int,
        val unlocktime: Long? = null
    )
}

@Serializable
data class GameSchemaResponse(val game: Game? = null) {
    @Serializable
    data class Game(val gameName: String? = null, val availableGameStats: Stats? = null)
    @Serializable
    data class Stats(val achievements: List<SchemaAchievement>? = null)
    @Serializable
    data class SchemaAchievement(
        val name: String,
        val displayName: String? = null,
        val description: String? = null,
        val icon: String? = null,
        val icongray: String? = null
    )
}

fun steamCdnHeaderImage(appId: Int): String = "https://cdn.cloudflare.steamstatic.com/steam/apps/${'$'}appId/header.jpg"
fun steamCdnIcon(appId: Int, iconHash: String): String = "https://media.steampowered.com/steamcommunity/public/images/apps/${'$'}appId/${'$'}iconHash.jpg"
