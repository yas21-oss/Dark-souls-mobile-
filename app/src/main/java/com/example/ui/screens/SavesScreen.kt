package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.model.SaveCharacter
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.SoulCyan
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel

@Composable
fun SavesScreen(
    uiState: MainUiState,
    onSelectCharacter: (String) -> Unit,
    onUpdateStat: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                    text = "SAVE SLOTS & STAT CALCULATOR",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Manage DRAKS0005.sl2 save slots, backup game progress, and calculate Soul Level requirements.",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Save Slot Cards
        item {
            Text(
                text = "ACTIVE SAVE CHARACTERS",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BonfireGlow
            )
        }

        items(uiState.saveCharacters) { char ->
            val isSelected = char.id == uiState.selectedCharacterId
            SaveSlotCard(
                character = char,
                isSelected = isSelected,
                onSelect = { onSelectCharacter(char.id) },
                onBackup = {
                    Toast.makeText(context, "Backup created for ${char.name}!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Build & Level Up Calculator
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("build_calculator_card"),
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
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = BonfireFlame,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BONFIRE STAT CALCULATOR",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = BonfireGlow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Summary Stats Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = ObsidianDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, StoneElevated)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "SOUL LEVEL",
                                    fontFamily = CinzelFontFamily,
                                    fontSize = 11.sp,
                                    color = WeatheredSteel
                                )
                                Text(
                                    text = "${uiState.calcTotalLevel}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BonfireFlame
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "SOUL REQUIRED",
                                    fontFamily = CinzelFontFamily,
                                    fontSize = 11.sp,
                                    color = WeatheredSteel
                                )
                                Text(
                                    text = "${uiState.calcSoulsForNextLevel}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoulCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stat Rows
                    StatRow(label = "Vitality (HP)", value = uiState.calcVitality, onDelta = { onUpdateStat("VIT", it) })
                    StatRow(label = "Endurance (Stamina)", value = uiState.calcEndurance, onDelta = { onUpdateStat("END", it) })
                    StatRow(label = "Strength (Physical)", value = uiState.calcStrength, onDelta = { onUpdateStat("STR", it) })
                    StatRow(label = "Dexterity (Speed/Scaling)", value = uiState.calcDexterity, onDelta = { onUpdateStat("DEX", it) })
                    StatRow(label = "Attunement (Spell Slots)", value = uiState.calcAttunement, onDelta = { onUpdateStat("ATT", it) })
                    StatRow(label = "Intelligence (Sorcery)", value = uiState.calcIntelligence, onDelta = { onUpdateStat("INT", it) })
                    StatRow(label = "Faith (Miracles)", value = uiState.calcFaith, onDelta = { onUpdateStat("FTH", it) })
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: Int,
    onDelta: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 12.sp,
            color = AncientParchment
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onDelta(-1) },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = BonfireFlame,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$value",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BonfireGlow,
                modifier = Modifier.width(32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(
                onClick = { onDelta(1) },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = BonfireFlame,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SaveSlotCard(
    character: SaveCharacter,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onBackup: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .testTag("save_slot_${character.id}"),
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
                Column {
                    Text(
                        text = character.name,
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isSelected) BonfireGlow else AncientParchment
                    )
                    Text(
                        text = "Level ${character.soulLevel} ${character.className} • ${character.playtimeHours}h",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = WeatheredSteel
                    )
                }
                OutlinedButton(
                    onClick = onBackup,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, BonfireFlame.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BonfireFlame)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BACKUP",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Location: ${character.currentArea}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                color = AncientParchment.copy(alpha = 0.85f)
            )
            Text(
                text = "Loadout: ${character.primaryWeapon} | ${character.shield} | ${character.rollType}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                color = EstusYellow
            )
        }
    }
}
