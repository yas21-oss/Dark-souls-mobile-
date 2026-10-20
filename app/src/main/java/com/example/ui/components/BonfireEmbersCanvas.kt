package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Ember(
    val initialX: Float,
    val speed: Float,
    val size: Float,
    val drift: Float,
    val color: Color,
    val phase: Float
)

@Composable
fun BonfireEmbersCanvas(
    modifier: Modifier = Modifier,
    emberCount: Int = 30
) {
    val infiniteTransition = rememberInfiniteTransition(label = "embers")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "emberProgress"
    )

    val embers = remember {
        val emberColors = listOf(
            Color(0xFFFF9E0B),
            Color(0xFFFF5722),
            Color(0xFFFBBF24),
            Color(0xFFFF7043),
            Color(0xFFFFD54F)
        )
        List(emberCount) {
            Ember(
                initialX = Random.nextFloat(),
                speed = 0.6f + Random.nextFloat() * 0.8f,
                size = 2f + Random.nextFloat() * 3.5f,
                drift = (Random.nextFloat() - 0.5f) * 60f,
                color = emberColors[Random.nextInt(emberColors.size)],
                phase = Random.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Subtle bottom fire glow gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0x33FF5722),
                    Color(0x66FF9E0B)
                ),
                startY = height * 0.6f,
                endY = height
            ),
            size = size
        )

        embers.forEach { ember ->
            val totalTime = (progress + ember.phase) % 1f
            val y = height - (totalTime * height * ember.speed)
            val x = (ember.initialX * width) + (kotlin.math.sin(totalTime * 6.28f) * ember.drift)

            val alpha = (1f - totalTime).coerceIn(0f, 1f) * 0.85f

            if (y in 0f..height) {
                drawCircle(
                    color = ember.color.copy(alpha = alpha),
                    radius = ember.size,
                    center = Offset(x, y)
                )
            }
        }
    }
}
