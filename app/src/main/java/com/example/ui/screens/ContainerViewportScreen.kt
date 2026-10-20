package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.container.ContainerLifecycleStage
import com.example.container.ContainerLogEntry
import com.example.container.ContainerState
import com.example.model.DefaultHudLayout
import com.example.model.HudPreferences
import com.example.model.TouchHudButton
import com.example.ui.components.BonfireEmbersCanvas
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.GoldenSunlight
import com.example.ui.theme.HollowRed
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.SoulCyan
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel
import kotlin.math.roundToInt

@Composable
fun ContainerViewportScreen(
    containerState: ContainerState,
    hudPreferences: HudPreferences,
    onInputEvent: (buttonId: String, pressed: Boolean) -> Unit,
    onRestAtBonfire: () -> Unit,
    onExportProfile: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showConsoleDrawer by remember { mutableStateOf(false) }
    var activePressedId by remember { mutableStateOf<String?>(null) }
    val runtime = containerState.runtime

    fun triggerVibration() {
        if (!hudPreferences.hapticFeedbackEnabled) return
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(20)
                }
            }
        } catch (_: Exception) {}
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0908))
    ) {
        // --- 1. Game Render Viewport Canvas ---
        GameViewportCanvas(
            runtime = runtime,
            containerStage = containerState.stage,
            modifier = Modifier.fillMaxSize()
        )

        // --- 2. Top Bar: Telemetry, In-Game Location & Control Toggles ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color(0xCC141210),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(0.8.dp, BonfireFlame.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Location & Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = BonfireFlame,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "DARK SOULS • FIRELINK SHRINE",
                            fontFamily = CinzelFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BonfireGlow
                        )
                        Text(
                            text = runtime.currentActionText,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            color = AncientParchment
                        )
                    }
                }

                // Middle: Real Performance Telemetry (FPS, FrameTime, Driver)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TelemetryPill(
                        label = "FPS",
                        value = "%.1f".format(runtime.fpsCurrent),
                        color = if (runtime.fpsCurrent >= 28f) Color(0xFF10B981) else HollowRed
                    )
                    TelemetryPill(
                        label = "TIME",
                        value = "%.1f ms".format(runtime.frameTimeMs),
                        color = Color(0xFF38BDF8)
                    )
                    TelemetryPill(
                        label = "GPU",
                        value = "${runtime.gpuUsagePercent}%",
                        color = EstusYellow
                    )
                    TelemetryPill(
                        label = "VRAM",
                        value = "${runtime.vramAllocatedMb}MB",
                        color = Color(0xFFA78BFA)
                    )
                }

                // Right: Action Buttons (Console, Rest Bonfire, Close)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onRestAtBonfire,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Rest at Bonfire",
                            tint = BonfireFlame,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { showConsoleDrawer = !showConsoleDrawer },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Toggle Container Console",
                            tint = if (showConsoleDrawer) BonfireFlame else AncientParchment,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Game Container",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // --- 3. Authentic In-Game HUD Overlays ---
        // Top-Left: Health & Stamina Bars
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 54.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Health Bar (Red)
            StatProgressBar(
                current = runtime.currentHp,
                max = runtime.maxHp,
                barColor = HollowRed,
                label = "HP ${runtime.currentHp.toInt()} / ${runtime.maxHp.toInt()}"
            )
            // Stamina Bar (Green)
            StatProgressBar(
                current = runtime.currentStamina,
                max = runtime.maxStamina,
                barColor = Color(0xFF10B981),
                label = "STAMINA ${runtime.currentStamina.toInt()}"
            )
        }

        // Bottom-Left: Equipment Diamonds (Active Weapon, Shield, Estus Flask)
        EquipmentDiamonds(
            weaponRight = runtime.activeWeaponRight,
            weaponLeft = runtime.activeWeaponLeft,
            estusCount = runtime.estusFlaskCount,
            maxEstus = runtime.maxEstusFlasks,
            isTwoHanding = runtime.isTwoHanding,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 12.dp)
        )

        // Bottom-Right: Souls Counter
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 12.dp),
            color = Color(0xCC141210),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(0.8.dp, SoulCyan.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SOULS",
                    fontFamily = CinzelFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WeatheredSteel
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${runtime.soulsCount}",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoulCyan
                )
            }
        }

        // --- 4. Interactive Touch Controls Overlay ---
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            // Left Joystick (Movement)
            if (hudPreferences.showJoystickLeft) {
                Box(
                    modifier = Modifier
                        .offset(x = 36.dp, y = screenHeight - 150.dp)
                        .size(105.dp)
                        .alpha(hudPreferences.opacity)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .border(1.5.dp, Color(0x66FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
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
            if (hudPreferences.showJoystickRight) {
                Box(
                    modifier = Modifier
                        .offset(x = screenWidth - 146.dp, y = screenHeight - 150.dp)
                        .size(105.dp)
                        .alpha(hudPreferences.opacity)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .border(1.5.dp, Color(0x66FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0x8838BDF8), Color(0x4438BDF8))
                                )
                            )
                            .border(1.dp, SoulCyan, CircleShape)
                    )
                    Text(
                        text = "LOOK",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        color = Color.White
                    )
                }
            }

            // Render all touch buttons mapped to real game actions
            DefaultHudLayout.BUTTONS.forEach { btn ->
                val baseSize = (btn.diameterDp * hudPreferences.scaleMultiplier).dp
                val posX = (screenWidth.value * (btn.offsetXPercent / 100f)) - (baseSize.value / 2f)
                val posY = (screenHeight.value * (btn.offsetYPercent / 100f)) - (baseSize.value / 2f)
                val isPressed = activePressedId == btn.id

                ContainerButtonElement(
                    button = btn,
                    sizeDp = baseSize.value.roundToInt(),
                    opacity = hudPreferences.opacity,
                    isPressed = isPressed,
                    modifier = Modifier.offset {
                        IntOffset(
                            (posX * density).roundToInt(),
                            (posY * density).roundToInt()
                        )
                    },
                    onTap = {
                        activePressedId = btn.id
                        triggerVibration()
                        onInputEvent(btn.id, true)
                    },
                    onRelease = {
                        activePressedId = null
                        onInputEvent(btn.id, false)
                    }
                )
            }
        }

        // --- 5. Real-Time Container Diagnostics & Terminal Drawer ---
        AnimatedVisibility(
            visible = showConsoleDrawer,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(360.dp)
        ) {
            ContainerConsoleDrawer(
                containerState = containerState,
                onExportProfile = onExportProfile,
                onClose = { showConsoleDrawer = false }
            )
        }
    }
}

@Composable
private fun StatProgressBar(
    current: Float,
    max: Float,
    barColor: Color,
    label: String
) {
    val pct = (current / max).coerceIn(0f, 1f)
    Column {
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = AncientParchment
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(9.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x66000000))
                .border(0.8.dp, Color(0x88FFFFFF), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(pct)
                    .background(barColor)
            )
        }
    }
}

@Composable
private fun EquipmentDiamonds(
    weaponRight: String,
    weaponLeft: String,
    estusCount: Int,
    maxEstus: Int,
    isTwoHanding: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xB3181513),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Estus Flask Quick Item
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ESTUS [X]",
                    fontFamily = CinzelFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = EstusYellow
                )
                Text(
                    text = "$estusCount / $maxEstus",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (estusCount > 0) BonfireFlame else HollowRed
                )
            }

            // Right Weapon
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isTwoHanding) "RIGHT (2H)" else "RIGHT (1H)",
                    fontFamily = CinzelFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = AncientParchment
                )
                Text(
                    text = weaponRight,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BonfireGlow
                )
            }

            // Left Shield
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEFT",
                    fontFamily = CinzelFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = AncientParchment
                )
                Text(
                    text = weaponLeft,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SoulCyan
                )
            }
        }
    }
}

@Composable
private fun TelemetryPill(
    label: String,
    value: String,
    color: Color
) {
    Surface(
        color = Color(0x33000000),
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(0.6.dp, color.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.sp,
                color = WeatheredSteel
            )
            Text(
                text = value,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun GameViewportCanvas(
    runtime: com.example.container.InGameRuntimeState,
    containerStage: ContainerLifecycleStage,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bonfireGlow")
    val flamePulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Atmospheric Vignette Gradient
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1E1612),
                        Color(0xFF0F0D0B),
                        Color(0xFF050404)
                    ),
                    center = Offset(width * 0.5f, height * 0.55f),
                    radius = width * 0.7f
                )
            )

            // Ancient Stone Floor perspective lines
            val horizonY = height * 0.45f
            val floorColor = Color(0x22FFFFFF)
            for (i in -4..4) {
                val startX = (width * 0.5f) + (i * 90f)
                drawLine(
                    color = floorColor,
                    start = Offset(startX, horizonY),
                    end = Offset((width * 0.5f) + (i * 320f), height),
                    strokeWidth = 1.2f
                )
            }

            // Central Bonfire Core Visual
            val bonfireCenter = Offset(width * 0.5f, height * 0.65f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BonfireFlame.copy(alpha = 0.45f * flamePulse),
                        BonfireCore.copy(alpha = 0.15f * flamePulse),
                        Color.Transparent
                    )
                ),
                radius = 160f * flamePulse,
                center = bonfireCenter
            )

            // Sword in Bonfire (Coiled Sword silhouette)
            val swordPath = Path().apply {
                moveTo(bonfireCenter.x, bonfireCenter.y - 70f)
                lineTo(bonfireCenter.x - 6f, bonfireCenter.y + 15f)
                lineTo(bonfireCenter.x + 6f, bonfireCenter.y + 15f)
                close()
            }
            drawPath(path = swordPath, color = WeatheredSteel)

            // Target Lock-On Reticle if active
            if (runtime.isTargetLocked) {
                val targetCenter = Offset(width * 0.62f, height * 0.48f)
                drawCircle(
                    color = HollowRed,
                    radius = 14f,
                    center = targetCenter,
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.5f,
                    center = targetCenter
                )
            }
        }

        // Ambient Fire Embers
        BonfireEmbersCanvas(modifier = Modifier.fillMaxSize(), emberCount = 20)
    }
}

@Composable
private fun ContainerButtonElement(
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
            listOf(borderColor.copy(alpha = 0.85f), Color(0xEE000000))
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
            .shadow(if (isPressed) 10.dp else 2.dp, CircleShape)
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
                fontSize = if (button.diameterDp > 50) 17.sp else 12.sp,
                color = if (isPressed) Color.White else borderColor
            )
            if (button.category == "Action" || button.category == "Attack") {
                Text(
                    text = button.shortcutKey,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 8.sp,
                    color = Color.LightGray.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun ContainerConsoleDrawer(
    containerState: ContainerState,
    onExportProfile: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(containerState.logs.size) {
        if (containerState.logs.isNotEmpty()) {
            listState.animateScrollToItem(containerState.logs.size - 1)
        }
    }

    Surface(
        modifier = modifier.fillMaxHeight(),
        color = Color(0xF20F0D0B),
        border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = BonfireFlame,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CONTAINER TERMINAL",
                        fontFamily = CinzelFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BonfireGlow
                    )
                }
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AncientParchment,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subsystem Status Pill
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF191613),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(0.6.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Binary: ${containerState.binaryInfo.fileName} (${containerState.binaryInfo.machineType})",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = AncientParchment
                    )
                    Text(
                        text = "Driver: ${containerState.activeDriverName}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = BonfireFlame
                    )
                    Text(
                        text = "PID: ${containerState.pid} • Packets: ${containerState.runtime.packetsProcessed}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = SoulCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Live Log Console Output
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF070605), RoundedCornerShape(6.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(containerState.logs) { log ->
                    ConsoleLogItem(log)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Export Actions
            Button(
                onClick = onExportProfile,
                modifier = Modifier.fillMaxWidth().height(38.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BonfireFlame, contentColor = AshBlack)
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "EXPORT RUNNER SCRIPT",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun ConsoleLogItem(log: ContainerLogEntry) {
    val tagColor = when (log.tag) {
        "CONTAINER" -> BonfireGlow
        "PANVK-CSF" -> BonfireFlame
        "TURNIP" -> Color(0xFF60A5FA)
        "D8VK", "DSFIX" -> SoulCyan
        "GAMEPLAY" -> Color(0xFF34D399)
        "INPUT" -> EstusYellow
        else -> WeatheredSteel
    }

    val msgColor = when (log.level) {
        "SUCCESS" -> Color(0xFF10B981)
        "WARN" -> Color(0xFFF59E0B)
        else -> AncientParchment
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "[${log.tag}] ",
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = tagColor
        )
        Text(
            text = log.message,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.sp,
            color = msgColor
        )
    }
}
