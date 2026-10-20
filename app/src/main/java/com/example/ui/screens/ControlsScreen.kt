package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainUiState
import com.example.model.DefaultHudLayout
import com.example.model.HudPreferences
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SoulCyan
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel
import kotlin.math.roundToInt

@Composable
fun ControlsScreen(
    uiState: MainUiState,
    onUpdatePreferences: ((HudPreferences) -> HudPreferences) -> Unit,
    onOpenSimulator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = uiState.hudPreferences

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "DARK SOULS TOUCH HUD",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Adapted on-screen touch controller with custom combat layout for Dark Souls roll, parry, attacks and Estus.",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Live Simulator Button Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simulator_trigger_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF28211C)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = BonfireFlame,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TEST INTERACTIVE HUD",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = BonfireGlow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Opens the full-screen touch overlay simulator. Tap buttons to verify response and tactile haptics.",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = AncientParchment
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onOpenSimulator,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_hud_simulator_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BonfireFlame,
                            contentColor = AshBlack
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OPEN TOUCH SIMULATOR",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // HUD Preferences
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hud_preferences_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CONTROLLER PREFERENCES",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BonfireGlow
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scale
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Button Scale Multiplier",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            color = AncientParchment
                        )
                        Text(
                            text = "${(prefs.scaleMultiplier * 100).roundToInt()}%",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BonfireFlame
                        )
                    }

                    Slider(
                        value = prefs.scaleMultiplier,
                        onValueChange = { s -> onUpdatePreferences { it.copy(scaleMultiplier = s) } },
                        valueRange = 0.75f..1.35f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = BonfireFlame,
                            activeTrackColor = BonfireFlame,
                            inactiveTrackColor = StoneElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Opacity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "On-Screen Opacity",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            color = AncientParchment
                        )
                        Text(
                            text = "${(prefs.opacity * 100).roundToInt()}%",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BonfireFlame
                        )
                    }

                    Slider(
                        value = prefs.opacity,
                        onValueChange = { o -> onUpdatePreferences { it.copy(opacity = o) } },
                        valueRange = 0.3f..1.0f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = BonfireFlame,
                            activeTrackColor = BonfireFlame,
                            inactiveTrackColor = StoneElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vibration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Haptic Vibration Feedback",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = AncientParchment
                            )
                            Text(
                                text = "Vibrate on roll, light attack, and parry inputs",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = WeatheredSteel
                            )
                        }
                        Switch(
                            checked = prefs.hapticFeedbackEnabled,
                            onCheckedChange = { v -> onUpdatePreferences { it.copy(hapticFeedbackEnabled = v) } },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BonfireFlame,
                                checkedTrackColor = BonfireFlame.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }

        // Button Mapping Reference
        item {
            Text(
                text = "BUTTON MAPPINGS",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BonfireGlow
            )
        }

        items(DefaultHudLayout.BUTTONS) { btn ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF28211C))
                            .border(1.2.dp, BonfireFlame, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = btn.label,
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BonfireGlow
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = btn.actionDescription,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = AncientParchment
                        )
                        Text(
                            text = "Gamepad: ${btn.shortcutKey} • Type: ${btn.category}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = WeatheredSteel
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
