package data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

actual class CredentialsStorage(private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    actual fun saveCredentials(username: String, apiKey: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("api_key", apiKey)
            apply()
        }
    }

    actual fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    actual fun getApiKey(): String? {
        return sharedPreferences.getString("api_key", null)
    }

    actual fun clearCredentials() {
        with(sharedPreferences.edit()) {
            remove("username")
            remove("api_key")
            apply()
        }
    }
}
