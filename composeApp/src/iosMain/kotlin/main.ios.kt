import androidx.compose.ui.window.ComposeUIViewController
import com.steamcompanion.App
import data.CredentialsStorage

fun MainViewController() = ComposeUIViewController { App(CredentialsStorage()) }
