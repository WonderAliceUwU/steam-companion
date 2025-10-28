package com.steamcompanion.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.steamcompanion.domain.model.Game
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun GameRow(game: Game, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { onClick() },
    ) {
        Row( verticalAlignment = Alignment.CenterVertically) {
            GameCoverFrame(
                modifier = Modifier
                    .width(134.dp)
                    .height(200.dp)
            ) {
                KamelImage(
                    { asyncPainterResource(data = game.coverUrl) },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onFailure = { e -> println("Failed to load image: ${e.message}") }
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(game.name, fontWeight = FontWeight.SemiBold)
                Text(
                    "Playtime: ${game.playtimeMinutes / 60}h",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}


@Composable
fun GameCoverFrame(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    glowColors: List<Color> = listOf(
        Color(0xFF6A5B7A), // top subtle violet highlight
        Color.Transparent,
    ),
    overlayGradient: List<Color> = listOf(
        Color(0x33000000), // soft dim top
        Color.Transparent,
        Color(0x66000000)  // stronger dark bottom shadow
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.Transparent) // dark neutral base background
            .shadow(8.dp, RoundedCornerShape(cornerRadius))
    ) {
        // Main content (your image, etc.)
        content()

        // Lighting gradient overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(overlayGradient)
                )
        )

        // Outer gradient border
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(glowColors),
                    shape = RoundedCornerShape(cornerRadius)
                )
        )
    }
}