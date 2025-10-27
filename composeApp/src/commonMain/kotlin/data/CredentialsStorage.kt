package data

expect class CredentialsStorage {
    fun saveCredentials(username: String, apiKey: String)
    fun getUsername(): String?
    fun getApiKey(): String?
    fun clearCredentials()
}
