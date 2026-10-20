package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.HollowRed
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel

@Composable
fun FilesScreen(
    uiState: MainUiState,
    onOpenFolderPicker: () -> Unit,
    onClearFolder: () -> Unit,
    onToggleDemoMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val report = uiState.gameFilesReport

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
                    text = "GAME FILES & DIRECTORY",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Dark Souls PC files directory selector (SAF Storage Access Framework). Game execution is strictly gated by files presence.",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Folder Selector Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game_folder_selector_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (report.isValidGameReady) Color(0xFF1E261E) else Color(0xFF28211C)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = if (report.isValidGameReady) Color(0xFF10B981) else BonfireFlame
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (report.isValidGameReady) Icons.Default.CheckCircle else Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = if (report.isValidGameReady) Color(0xFF10B981) else BonfireFlame,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (report.isValidGameReady) "GAME FILES VERIFIED" else "SELECT GAME DIRECTORY",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (report.isValidGameReady) Color(0xFF34D399) else BonfireGlow
                            )
                        }

                        if (report.isFolderSelected) {
                            OutlinedButton(
                                onClick = onClearFolder,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, HollowRed.copy(alpha = 0.7f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = HollowRed)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "RESET",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = report.validationMessage,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = AncientParchment
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF141210),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, StoneElevated)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Current Directory Path:",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                color = WeatheredSteel
                            )
                            Text(
                                text = report.folderDisplayName,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (report.isFolderSelected) BonfireGlow else WeatheredSteel
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onOpenFolderPicker,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("choose_game_folder_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (report.isValidGameReady) Color(0xFF10B981) else BonfireFlame,
                            contentColor = AshBlack
                        )
                    ) {
                        if (uiState.isCheckingFiles) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AshBlack,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SCANNING DIRECTORY...",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (report.isFolderSelected) "CHANGE GAME FOLDER" else "BROWSE & SELECT GAME FOLDER",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Checklist of required files
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("files_checklist_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "FILE INTEGRITY CHECKLIST",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BonfireGlow
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FileCheckRow(
                        name = "DARKSOULS.exe",
                        description = "Main Game Executable (Required)",
                        isFound = report.hasExecutable,
                        isRequired = true
                    )
                    FileCheckRow(
                        name = "DATA / sound / mov",
                        description = "Core Game Resources & Cutscenes",
                        isFound = report.hasDataFiles,
                        isRequired = false
                    )
                    FileCheckRow(
                        name = "DSfix.ini / dinput8.dll",
                        description = "Framerate & Resolution Fix",
                        isFound = report.hasDsfix,
                        isRequired = false
                    )

                    if (report.detectedFiles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Detected Items (${report.detectedFiles.size}):",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            color = WeatheredSteel
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(report.detectedFiles.take(15)) { f ->
                                Surface(
                                    color = Color(0xFF1E1B18),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.6.dp, StoneElevated)
                                ) {
                                    Text(
                                        text = f,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        color = if (f.endsWith(".exe", ignoreCase = true)) BonfireGlow else AncientParchment,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Instructions Card in Arabic
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StoneElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = BonfireFlame,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "أين تضع ملفات اللعبة في هاتفك؟",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BonfireGlow
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "1. انقل مجلد لعبة Dark Souls المجهزة من الكمبيوتر إلى ذاكرة هاتفك (مثلاً داخل مجلد Download أو Documents).\n" +
                               "2. تأكد أن المجلد يحتوي مباشرة على ملف DARKSOULS.exe ومجلدات البيانات (sound, mov, DATA).\n" +
                               "3. اضغط زر 'BROWSE & SELECT GAME FOLDER' أعلاه، ثم اختر المجلد واضغط 'استخدام هذا المجلد' (Use this folder) للموافقة على الصلاحية.\n" +
                               "4. سيتعرف التطبيق فوراً على ملفات اللعبة وسيتم فتح زر تشغيل اللعبة بأمان.",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        lineHeight = 18.sp,
                        color = AncientParchment
                    )
                }
            }
        }

        // Developer Simulator Mode (Bypass)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A17)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38302B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Simulator Test Mode (Demo Bypass)",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AncientParchment
                        )
                        Text(
                            text = "Allows opening the HUD overlay & runtime simulator without physical PC game files on device.",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = WeatheredSteel
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Switch(
                        checked = uiState.demoModeUnlocked,
                        onCheckedChange = onToggleDemoMode,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BonfireFlame,
                            checkedTrackColor = BonfireFlame.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun FileCheckRow(
    name: String,
    description: String,
    isFound: Boolean,
    isRequired: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFound) Color(0xFF34D399) else AncientParchment
                )
                if (isRequired) {
                    Text(
                        text = " *",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HollowRed
                    )
                }
            }
            Text(
                text = description,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 10.sp,
                color = WeatheredSteel
            )
        }
        Icon(
            imageVector = if (isFound) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isFound) Color(0xFF10B981) else if (isRequired) HollowRed else WeatheredSteel,
            modifier = Modifier.size(20.dp)
        )
    }
}
