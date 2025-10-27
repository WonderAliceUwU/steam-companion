package data

import platform.Foundation.NSUserDefaults

actual class CredentialsStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun saveCredentials(username: String, apiKey: String) {
        userDefaults.setObject(username, "username")
        userDefaults.setObject(apiKey, "apiKey")
    }

    actual fun getUsername(): String? {
        return userDefaults.stringForKey("username")
    }

    actual fun getApiKey(): String? {
        return userDefaults.stringForKey("apiKey")
    }

    actual fun clearCredentials() {
        userDefaults.removeObjectForKey("username")
        userDefaults.removeObjectForKey("apiKey")
    }
}