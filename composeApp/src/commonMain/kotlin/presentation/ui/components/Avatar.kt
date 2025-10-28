package com.steamcompanion.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun Avatar(avatarUrl: String?, size: Dp = 64.dp) {
    if (avatarUrl != null) {
        GameCoverFrame{
            KamelImage(
                resource = { asyncPainterResource(data = avatarUrl) },
                contentDescription = null,
                modifier = Modifier.size(size).clip(RoundedCornerShape(size * 0.2f)),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Box(
            Modifier.size(size).clip(RoundedCornerShape(size / 5))
        )
    }
}
