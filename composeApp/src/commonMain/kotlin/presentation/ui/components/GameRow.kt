package com.steamcompanion.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
            Box(
                modifier = Modifier
                    .width(134.dp)
                    .height(200.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xff4a4358),
                                Color(0xff2c2834)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(1.5.dp)
            ) {
                KamelImage(
                    { asyncPainterResource(data = game.coverUrl) },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    onFailure = { e -> println("Failed to load image: ${e.message}") },
                    contentScale = ContentScale.Crop,
                    contentAlignment = Alignment.Center
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