package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import com.example.model.DriverEntry
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel

@Composable
fun DriversScreen(
    uiState: MainUiState,
    onSelectDriver: (String) -> Unit,
    onRunProbe: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "GPU DRIVER CATALOG",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Vulkan & PanVK drivers integrated from extracted package assets. Certified for ARM Mali (CoD4 pipeline) and Qualcomm Snapdragon (Turnip 60 FPS).",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Hardware Presentation Probe Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_probe_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = BonfireFlame,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HARDWARE PRESENTATION PROBE",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BonfireGlow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = uiState.gpuProbeReport.statusMessage,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = AncientParchment
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProbeMetric(title = "Estimated FPS", value = "${uiState.gpuProbeReport.estimatedFps} FPS")
                        ProbeMetric(title = "Presentation", value = uiState.gpuProbeReport.presentationMode)
                        ProbeMetric(title = "Memory Budget", value = "${uiState.gpuProbeReport.memoryBudgetMb} MB")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRunProbe,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("run_gpu_probe_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C241E),
                            contentColor = BonfireFlame
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.6f))
                    ) {
                        if (uiState.gpuProbeReport.isProbing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = BonfireFlame,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PROBING VULKAN SWAPCHAIN...",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RUN GPU PRESENTATION TEST",
                                fontFamily = CinzelFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Driver List Items
        items(uiState.drivers) { driver ->
            DriverItemCard(
                driver = driver,
                isSelected = driver.id == uiState.activeDriverId,
                onSelect = { onSelectDriver(driver.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ProbeMetric(title: String, value: String) {
    Column {
        Text(
            text = title,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 10.sp,
            color = WeatheredSteel
        )
        Text(
            text = value,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AncientParchment
        )
    }
}

@Composable
private fun DriverItemCard(
    driver: DriverEntry,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .testTag("driver_card_${driver.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF28211C) else StoneSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) BonfireFlame else StoneElevated
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isSelected,
                        onClick = onSelect,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BonfireFlame,
                            unselectedColor = WeatheredSteel
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = driver.name,
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isSelected) BonfireGlow else AncientParchment
                            )
                            if (driver.isCoD4Certified || driver.isRecommendedForG99) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = BonfireFlame.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.6.dp, BonfireFlame)
                                ) {
                                    Text(
                                        text = "CoD4 MALI CERTIFIED",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BonfireFlame,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (driver.isRecommendedForSnapdragon) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFF3B82F6).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(0xFF3B82F6))
                                ) {
                                    Text(
                                        text = "SNAPDRAGON 60FPS",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF60A5FA),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Family: ${driver.family} • HAL: ${driver.halBridgeName} • Tag: ${driver.tag}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            color = WeatheredSteel
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = driver.maturityNote,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                color = AncientParchment.copy(alpha = 0.85f),
                modifier = Modifier.padding(start = 12.dp)
            )

            if (driver.gpuModels.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    driver.gpuModels.take(4).forEach { model ->
                        Surface(
                            color = StoneElevated,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = model,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                color = WeatheredSteel,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
