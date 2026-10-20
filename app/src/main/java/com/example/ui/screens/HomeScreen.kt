package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AppTab
import com.example.MainUiState
import com.example.R
import com.example.util.EngineMode
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

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onLaunchGame: () -> Unit,
    onRestAtBonfire: () -> Unit,
    onNavigateTab: (AppTab) -> Unit,
    onSelectEngineMode: (EngineMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flameGlow")
    val flamePulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flamePulse"
    )

    val isReady = uiState.isCanLaunchGame

    // Landscape 2-Column Responsive Layout
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Column: Hero Banner + Launch Button
        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.4f)),
                colors = CardDefaults.cardColors(containerColor = ObsidianDark)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.darksouls_hero),
                        contentDescription = "Dark Souls Bonfire",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x30000000),
                                        Color(0x880D0C0B),
                                        Color(0xF00D0C0B)
                                    )
                                )
                            )
                    )

                    BonfireEmbersCanvas(modifier = Modifier.fillMaxSize(), emberCount = 18)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Surface(
                            color = Color(0x99181513),
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, BonfireFlame.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (isReady) Color(0xFF10B981) else HollowRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isReady) "FILES VERIFIED • READY TO LAUNCH" else "MISSING FILES • DIRECTORY REQUIRED",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isReady) Color(0xFF34D399) else HollowRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "DARK SOULS",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = AncientParchment,
                            letterSpacing = 1.5.sp
                        )

                        Text(
                            text = "Prepare to Die Edition • Helio G99 Mali-G57 PanVK",
                            fontFamily = CinzelFontFamily,
                            fontSize = 11.sp,
                            color = WeatheredSteel
                        )
                    }
                }
            }

            // Primary Launch & Rest Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLaunchGame,
                    modifier = Modifier
                        .weight(1.4f)
                        .height(48.dp)
                        .testTag("launch_game_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReady) BonfireFlame else Color(0xFF4A3428),
                        contentColor = if (isReady) AshBlack else BonfireGlow
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (uiState.isGameLaunching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = AshBlack,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "INITIALIZING...",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    } else {
                        Icon(
                            imageVector = if (isReady) Icons.Default.PlayArrow else Icons.Default.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isReady) "LAUNCH DARK SOULS" else "SELECT GAME FOLDER",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = onRestAtBonfire,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("rest_bonfire_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, BonfireFlame.copy(alpha = flamePulse * 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0x331F1C18),
                        contentColor = BonfireGlow
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = BonfireFlame,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uiState.isRestedAtBonfire) "RESTING..." else "REST BONFIRE",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Right Column: File Status & System Diagnostics (Scrollable)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Game Files Status Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("files_status_shortcut_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isReady) Color(0xFF1B241B) else Color(0xFF28201C)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isReady) Color(0xFF10B981) else BonfireFlame
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateTab(AppTab.FILES) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (isReady) Color(0xFF10B981) else BonfireFlame,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "GAME FILES PATH",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isReady) Color(0xFF34D399) else BonfireGlow
                                )
                                Text(
                                    text = if (uiState.gameFilesReport.isFolderSelected)
                                        uiState.gameFilesReport.folderDisplayName
                                    else
                                        "No folder selected — Click to set location",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    color = AncientParchment
                                )
                            }
                        }

                        if (!isReady) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { onNavigateTab(AppTab.DOWNLOAD) },
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(34.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BonfireFlame,
                                        contentColor = AshBlack
                                    )
                                ) {
                                    Text(
                                        text = "DOWNLOAD 7.46 GB",
                                        fontFamily = CinzelFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                                OutlinedButton(
                                    onClick = { onNavigateTab(AppTab.FILES) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WeatheredSteel.copy(alpha = 0.6f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AncientParchment)
                                ) {
                                    Text(
                                        text = "BROWSE FOLDER",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SoC Engine Architecture Selector Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("soc_engine_selector_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = StoneSurface),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4)
                            BonfireFlame.copy(alpha = 0.6f)
                        else
                            Color(0xFF3B82F6).copy(alpha = 0.6f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBolt,
                                    contentDescription = null,
                                    tint = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) BonfireFlame else Color(0xFF60A5FA),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SOC EXECUTION ENGINE",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = AncientParchment
                                )
                            }
                            Surface(
                                color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0x3310B981) else Color(0x333B82F6),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) "CoD4 Mali Mode" else "Adreno 60FPS",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0xFF34D399) else Color(0xFF60A5FA),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            EngineSelectButton(
                                title = "MALI (CoD4)",
                                subtitle = "PanVK CSF",
                                isSelected = uiState.selectedEngineMode == EngineMode.MALI_COD4,
                                activeColor = BonfireFlame,
                                modifier = Modifier.weight(1f),
                                onClick = { onSelectEngineMode(EngineMode.MALI_COD4) }
                            )
                            EngineSelectButton(
                                title = "SNAPDRAGON",
                                subtitle = "Turnip 60FPS",
                                isSelected = uiState.selectedEngineMode == EngineMode.SNAPDRAGON_TURNIP,
                                activeColor = Color(0xFF3B82F6),
                                modifier = Modifier.weight(1f),
                                onClick = { onSelectEngineMode(EngineMode.SNAPDRAGON_TURNIP) }
                            )
                            EngineSelectButton(
                                title = "AUTO",
                                subtitle = "Chip Match",
                                isSelected = uiState.selectedEngineMode == EngineMode.AUTO,
                                activeColor = EstusYellow,
                                modifier = Modifier.weight(0.85f),
                                onClick = { onSelectEngineMode(EngineMode.AUTO) }
                            )
                        }
                    }
                }
            }

            // Diagnostics Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = StoneSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SYSTEM DIAGNOSTICS",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AncientParchment
                            )
                            Surface(
                                color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0x3310B981) else Color(0x333B82F6),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) "HELIO G99 (MALI)" else "SNAPDRAGON (ADRENO)",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0xFF34D399) else Color(0xFF60A5FA),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        DiagnosticItem("Active Engine", if (uiState.selectedEngineMode == EngineMode.MALI_COD4) "Mali CoD4 Pipeline" else "Snapdragon Beast (60 FPS)")
                        DiagnosticItem("GPU Driver", uiState.activeDriver?.name ?: "System", isHighlight = true)
                        DiagnosticItem("HAL Bridge", uiState.activeDriver?.halBridgeName ?: "libc4vk.so")
                        DiagnosticItem("Target Profile", "${uiState.activePreset?.resolutionHeight}p @ ${uiState.activePreset?.targetFps} FPS")
                        DiagnosticItem("RAM / Temp", "${uiState.ramFreeMb} MB Free / ${uiState.batteryTempC}°C")
                    }
                }
            }

            // Module Quick Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickModuleTile(
                        title = "Drivers",
                        desc = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) "PanVK Mali" else "Turnip Adreno",
                        icon = Icons.Default.Speed,
                        tint = BonfireFlame,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTab(AppTab.DRIVERS) }
                    )
                    QuickModuleTile(
                        title = "SoC Tweaks",
                        desc = "Mali & SD",
                        icon = Icons.Default.Tune,
                        tint = EstusYellow,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTab(AppTab.PERFORMANCE) }
                    )
                    QuickModuleTile(
                        title = "Touch HUD",
                        desc = "Controls",
                        icon = Icons.Default.Gamepad,
                        tint = SoulCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTab(AppTab.CONTROLS) }
                    )
                    QuickModuleTile(
                        title = "Build",
                        desc = "Calculator",
                        icon = Icons.Default.Security,
                        tint = Color(0xFFA78BFA),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTab(AppTab.SAVES) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiagnosticItem(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 11.sp,
            color = WeatheredSteel
        )
        Text(
            text = value,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 11.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) BonfireGlow else AncientParchment
        )
    }
}

@Composable
private fun QuickModuleTile(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = StoneSurface),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, StoneElevated)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = AncientParchment
            )
            Text(
                text = desc,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.sp,
                color = WeatheredSteel
            )
        }
    }
}

@Composable
private fun EngineSelectButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag("engine_btn_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) activeColor.copy(alpha = 0.22f) else Color(0x331C1815)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 0.8.dp,
            color = if (isSelected) activeColor else StoneElevated
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isSelected) activeColor else AncientParchment
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 8.sp,
                color = if (isSelected) AncientParchment else WeatheredSteel
            )
        }
    }
}
