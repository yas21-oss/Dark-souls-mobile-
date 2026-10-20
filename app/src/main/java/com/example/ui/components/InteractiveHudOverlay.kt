package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DefaultHudLayout
import com.example.model.HudPreferences
import com.example.model.TouchHudButton
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SoulCyan
import com.example.ui.theme.StoneElevated
import kotlin.math.roundToInt

@Composable
fun InteractiveHudOverlay(
    preferences: HudPreferences,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastActionText by remember { mutableStateOf("Touch controls active - Tap any button to test") }
    var activePressedId by remember { mutableStateOf<String?>(null) }

    fun triggerVibration() {
        if (!preferences.hapticFeedbackEnabled) return
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(25)
                }
            }
        } catch (_: Exception) {}
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xE60D0C0B))
    ) {
        // Subtle background grid simulating game viewport
        BonfireEmbersCanvas(modifier = Modifier.fillMaxSize(), emberCount = 15)

        // Top Header with HUD info and close button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = Color(0xCC1F1C18),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = BonfireFlame,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "DARK SOULS TOUCH SIMULATOR",
                        fontFamily = CinzelFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BonfireGlow
                    )
                    Text(
                        text = lastActionText,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Simulator",
                        tint = Color.White
                    )
                }
            }
        }

        // Joystick Visual Placeholders
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            // Left Joystick (Movement)
            if (preferences.showJoystickLeft) {
                Box(
                    modifier = Modifier
                        .offset(x = 36.dp, y = screenHeight - 160.dp)
                        .size(110.dp)
                        .alpha(preferences.opacity)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .border(1.5.dp, Color(0x66FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0x88F59E0B), Color(0x44F59E0B))
                                )
                            )
                            .border(1.dp, BonfireFlame, CircleShape)
                    )
                    Text(
                        text = "MOVE",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        color = Color.White
                    )
                }
            }

            // Right Joystick (Look & Camera)
            if (preferences.showJoystickRight) {
                Box(
                    modifier = Modifier
                        .offset(x = screenWidth - 146.dp, y = screenHeight - 160.dp)
                        .size(110.dp)
                        .alpha(preferences.opacity)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .border(1.5.dp, Color(0x66FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0x8838BDF8), Color(0x4438BDF8))
                                )
                            )
                            .border(1.dp, SoulCyan, CircleShape)
                    )
                    Text(
                        text = "CAMERA",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        color = Color.White
                    )
                }
            }

            // Render all touch buttons positioned according to layout
            DefaultHudLayout.BUTTONS.forEach { btn ->
                val baseSize = (btn.diameterDp * preferences.scaleMultiplier).dp
                val posX = (screenWidth.value * (btn.offsetXPercent / 100f)) - (baseSize.value / 2f)
                val posY = (screenHeight.value * (btn.offsetYPercent / 100f)) - (baseSize.value / 2f)
                val isPressed = activePressedId == btn.id

                HudButtonElement(
                    button = btn,
                    sizeDp = baseSize.value.roundToInt(),
                    opacity = preferences.opacity,
                    isPressed = isPressed,
                    modifier = Modifier.offset {
                        IntOffset(
                            (posX * density).roundToInt(),
                            (posY * density).roundToInt()
                        )
                    },
                    onTap = {
                        activePressedId = btn.id
                        lastActionText = "${btn.label} [${btn.shortcutKey}]: ${btn.actionDescription}"
                        triggerVibration()
                    },
                    onRelease = {
                        activePressedId = null
                    }
                )
            }
        }
    }
}

@Composable
private fun HudButtonElement(
    button: TouchHudButton,
    sizeDp: Int,
    opacity: Float,
    isPressed: Boolean,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
    onRelease: () -> Unit
) {
    val borderColor = when (button.category) {
        "Attack" -> if (isPressed) BonfireCore else BonfireFlame
        "Trigger" -> if (isPressed) SoulCyan else Color(0xFF60A5FA)
        "D-Pad" -> if (isPressed) EstusYellow else Color(0xFF94A3B8)
        else -> if (isPressed) BonfireGlow else Color(0xFFE2E8F0)
    }

    val bgGradient = if (isPressed) {
        Brush.radialGradient(
            listOf(borderColor.copy(alpha = 0.8f), Color(0xCC000000))
        )
    } else {
        Brush.radialGradient(
            listOf(StoneElevated.copy(alpha = opacity), AshBlack.copy(alpha = opacity))
        )
    }

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .alpha(opacity)
            .shadow(if (isPressed) 8.dp else 2.dp, CircleShape)
            .clip(CircleShape)
            .background(bgGradient)
            .border(if (isPressed) 2.5.dp else 1.5.dp, borderColor, CircleShape)
            .pointerInput(button.id) {
                detectTapGestures(
                    onPress = {
                        onTap()
                        tryAwaitRelease()
                        onRelease()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = button.label,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = if (button.diameterDp > 50) 18.sp else 13.sp,
                color = if (isPressed) Color.White else borderColor
            )
            if (button.category == "Action" || button.category == "Attack") {
                Text(
                    text = button.shortcutKey,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 8.sp,
                    color = Color.LightGray.copy(alpha = 0.7f)
                )
            }
        }
    }
}
