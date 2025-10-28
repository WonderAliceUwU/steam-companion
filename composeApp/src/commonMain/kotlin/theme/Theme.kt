package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.steamcompanion.composeapp.generated.resources.Res
import com.steamcompanion.composeapp.generated.resources.outfit_black
import com.steamcompanion.composeapp.generated.resources.outfit_bold
import com.steamcompanion.composeapp.generated.resources.outfit_extrabold
import com.steamcompanion.composeapp.generated.resources.outfit_extralight
import com.steamcompanion.composeapp.generated.resources.outfit_light
import com.steamcompanion.composeapp.generated.resources.outfit_medium
import com.steamcompanion.composeapp.generated.resources.outfit_regular
import com.steamcompanion.composeapp.generated.resources.outfit_semibold
import com.steamcompanion.composeapp.generated.resources.outfit_thin
import org.jetbrains.compose.resources.Font

@Composable
fun SteamCompanionTheme(content: @Composable () -> Unit) {
    val typography = Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = getOutfitFontFamily()),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = getOutfitFontFamily()),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = getOutfitFontFamily()),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = getOutfitFontFamily()),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = getOutfitFontFamily()),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = getOutfitFontFamily()),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = getOutfitFontFamily()),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = getOutfitFontFamily()),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = getOutfitFontFamily()),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = getOutfitFontFamily()),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = getOutfitFontFamily()),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = getOutfitFontFamily()),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = getOutfitFontFamily()),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = getOutfitFontFamily()),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = getOutfitFontFamily())
    )

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color.Transparent
        ),
        typography = typography,
        content = content
    )
}

@Composable
fun getOutfitFontFamily(): FontFamily = FontFamily(
    Font(Res.font.outfit_thin, FontWeight.Thin),
    Font(Res.font.outfit_extralight, FontWeight.ExtraLight),
    Font(Res.font.outfit_light, FontWeight.Light),
    Font(Res.font.outfit_regular, FontWeight.Normal),
    Font(Res.font.outfit_medium, FontWeight.Medium),
    Font(Res.font.outfit_semibold, FontWeight.SemiBold),
    Font(Res.font.outfit_bold, FontWeight.Bold),
    Font(Res.font.outfit_extrabold, FontWeight.ExtraBold),
    Font(Res.font.outfit_black, FontWeight.Black),
)