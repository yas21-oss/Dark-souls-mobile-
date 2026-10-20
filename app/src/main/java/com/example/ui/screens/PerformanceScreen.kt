package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainUiState
import com.example.model.DsfixSettings
import com.example.model.PerformancePreset
import com.example.model.PerformancePresets
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel
import com.example.util.EngineMode
import kotlin.math.roundToInt

@Composable
fun PerformanceScreen(
    uiState: MainUiState,
    onSelectPreset: (String) -> Unit,
    onUpdateDsfix: ((DsfixSettings) -> DsfixSettings) -> Unit,
    onSelectEngineMode: (EngineMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun copyConfigToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("DSfix.ini", uiState.generatedConfigText)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "DSfix.ini copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "SOC ARCHITECTURE & GRAPHICS TWEAKS",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Engineered for ARM Mali (Helio G99 CoD4 smoothness) and Qualcomm Snapdragon (Adreno Turnip 60 FPS).",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Dedicated SoC Engine Selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("perf_soc_engine_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) BonfireFlame else Color(0xFF3B82F6)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SELECT HARDWARE ENGINE",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BonfireGlow
                            )
                        }

                        Surface(
                            color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0x3310B981) else Color(0x333B82F6),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = uiState.selectedEngineMode.badge,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.selectedEngineMode == EngineMode.MALI_COD4) Color(0xFF34D399) else Color(0xFF60A5FA),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EngineModeCard(
                            mode = EngineMode.MALI_COD4,
                            isSelected = uiState.selectedEngineMode == EngineMode.MALI_COD4,
                            badgeColor = BonfireFlame,
                            modifier = Modifier.weight(1f),
                            onClick = { onSelectEngineMode(EngineMode.MALI_COD4) }
                        )

                        EngineModeCard(
                            mode = EngineMode.SNAPDRAGON_TURNIP,
                            isSelected = uiState.selectedEngineMode == EngineMode.SNAPDRAGON_TURNIP,
                            badgeColor = Color(0xFF3B82F6),
                            modifier = Modifier.weight(1f),
                            onClick = { onSelectEngineMode(EngineMode.SNAPDRAGON_TURNIP) }
                        )

                        EngineModeCard(
                            mode = EngineMode.AUTO,
                            isSelected = uiState.selectedEngineMode == EngineMode.AUTO,
                            badgeColor = EstusYellow,
                            modifier = Modifier.weight(0.85f),
                            onClick = { onSelectEngineMode(EngineMode.AUTO) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.selectedEngineMode.description,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = AncientParchment.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Preset Selector
        item {
            Text(
                text = "PERFORMANCE PRESETS",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BonfireGlow
            )
        }

        items(PerformancePresets.PRESETS.size) { index ->
            val preset = PerformancePresets.PRESETS[index]
            val isSelected = preset.id == uiState.selectedPresetId

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectPreset(preset.id) }
                    .testTag("preset_card_${preset.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF28211C) else StoneSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) BonfireFlame else StoneElevated
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectPreset(preset.id) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BonfireFlame,
                            unselectedColor = WeatheredSteel
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = preset.title,
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) BonfireGlow else AncientParchment
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (preset.isRecommendedForG99) BonfireFlame.copy(alpha = 0.2f) else Color(0xFF3B82F6).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(0.6.dp, if (preset.isRecommendedForG99) BonfireFlame else Color(0xFF3B82F6))
                            ) {
                                Text(
                                    text = preset.engineTag,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (preset.isRecommendedForG99) BonfireFlame else Color(0xFF60A5FA),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = preset.subtitle,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            color = WeatheredSteel
                        )
                    }
                }
            }
        }

        // Granular Tweaks Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("granular_tweaks_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "MANUAL PIPELINE & DSFIX PARAMETERS",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BonfireGlow
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // CoD4 Mali Stutter Guard Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mali CoD4 Stutter Guard (Valhall CSF)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BonfireFlame
                            )
                            Text(
                                text = "Replicates the exact CoD4 Mali command buffer pooling to eliminate frametime spikes on Helio G99 / Dimensity",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = WeatheredSteel
                            )
                        }
                        Switch(
                            checked = uiState.dsfixSettings.maliCoD4Pipeline,
                            onCheckedChange = { chk ->
                                onUpdateDsfix { it.copy(maliCoD4Pipeline = chk) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BonfireFlame,
                                checkedTrackColor = BonfireFlame.copy(alpha = 0.5f),
                                uncheckedThumbColor = WeatheredSteel,
                                uncheckedTrackColor = StoneElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // D8VK Async Pipeline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "D8VK Async Shader Pipeline",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = AncientParchment
                            )
                            Text(
                                text = "Compiles shaders on background CPU threads; prevents combat freeze during boss encounters",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = WeatheredSteel
                            )
                        }
                        Switch(
                            checked = uiState.dsfixSettings.d8vkAsyncPipeline,
                            onCheckedChange = { chk ->
                                onUpdateDsfix { it.copy(d8vkAsyncPipeline = chk) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BonfireFlame,
                                checkedTrackColor = BonfireFlame.copy(alpha = 0.5f),
                                uncheckedThumbColor = WeatheredSteel,
                                uncheckedTrackColor = StoneElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Snapdragon GMEM Direct Tiling
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Snapdragon Direct GMEM Tiling",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = Color(0xFF60A5FA)
                            )
                            Text(
                                text = "Direct on-chip memory tiling for Adreno GPUs via Mesa Turnip; unlocks 60 FPS",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = WeatheredSteel
                            )
                        }
                        Switch(
                            checked = uiState.dsfixSettings.snapdragonGmemDirect,
                            onCheckedChange = { chk ->
                                onUpdateDsfix { it.copy(snapdragonGmemDirect = chk) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF3B82F6),
                                checkedTrackColor = Color(0xFF3B82F6).copy(alpha = 0.5f),
                                uncheckedThumbColor = WeatheredSteel,
                                uncheckedTrackColor = StoneElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // FPS Limit Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Framerate Lock Limit",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            color = AncientParchment
                        )
                        Text(
                            text = "${uiState.dsfixSettings.fpsLimit} FPS",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BonfireFlame
                        )
                    }

                    Slider(
                        value = uiState.dsfixSettings.fpsLimit.toFloat(),
                        onValueChange = { newFps ->
                            onUpdateDsfix { it.copy(fpsLimit = newFps.roundToInt()) }
                        },
                        valueRange = 24f..60f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = BonfireFlame,
                            activeTrackColor = BonfireFlame,
                            inactiveTrackColor = StoneElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // SSAO Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Screen-Space Ambient Occlusion",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = AncientParchment
                            )
                            Text(
                                text = "Disable on Helio G99 to eliminate shadow shader load; enable on Snapdragon 8 Gen",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = WeatheredSteel
                            )
                        }
                        Switch(
                            checked = uiState.dsfixSettings.ssaoEnabled,
                            onCheckedChange = { chk ->
                                onUpdateDsfix { it.copy(ssaoEnabled = chk) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BonfireFlame,
                                checkedTrackColor = BonfireFlame.copy(alpha = 0.5f),
                                uncheckedThumbColor = WeatheredSteel,
                                uncheckedTrackColor = StoneElevated
                            )
                        )
                    }
                }
            }
        }

        // Generated Config Viewer
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generated_config_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GENERATED CONFIGURATION & HAL BRIDGE",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BonfireGlow
                        )
                        OutlinedButton(
                            onClick = { copyConfigToClipboard() },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BonfireFlame)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "COPY",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF0F0E0D),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF22201D))
                    ) {
                        Text(
                            text = uiState.generatedConfigText,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun EngineModeCard(
    mode: EngineMode,
    isSelected: Boolean,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag("engine_mode_${mode.id}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) badgeColor.copy(alpha = 0.2f) else Color(0x331C1815)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 0.8.dp,
            color = if (isSelected) badgeColor else StoneElevated
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mode.title,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (isSelected) badgeColor else AncientParchment
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = mode.badge,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.sp,
                color = if (isSelected) AncientParchment else WeatheredSteel
            )
        }
    }
}
