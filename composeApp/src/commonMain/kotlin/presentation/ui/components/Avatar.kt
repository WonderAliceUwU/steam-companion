package com.steamcompanion.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun Avatar(avatarUrl: String?, size: Dp = 64.dp) {
    if (avatarUrl != null) {
        KamelImage(
            resource = { asyncPainterResource(data = avatarUrl) },
            contentDescription = null,
            modifier = Modifier.size(size)
                .clip(RoundedCornerShape(size * 0.2f))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF5b677a),
                            Color.Transparent,
                        ),
                    ),
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )

    } else {
        Box(
            Modifier.size(size).clip(RoundedCornerShape(size / 5))
        )
    }
}
