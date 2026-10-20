package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.ContainerViewportScreen
import com.example.ui.screens.ControlsScreen
import com.example.ui.screens.DownloadScreen
import com.example.ui.screens.DriversScreen
import com.example.ui.screens.FilesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PerformanceScreen
import com.example.ui.screens.SavesScreen
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.DarkSoulsTheme
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.WeatheredSteel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DarkSoulsTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // Storage Access Framework folder picker
                val folderPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocumentTree()
                ) { uri: Uri? ->
                    if (uri != null) {
                        viewModel.onGameFolderSelected(uri)
                    }
                }

                BackHandler(enabled = uiState.isContainerViewportActive) {
                    viewModel.setContainerViewportActive(false)
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AshBlack)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Left Landscape Navigation Rail (Gamer Ergonomics)
                        NavigationRail(
                            containerColor = ObsidianDark,
                            contentColor = AncientParchment,
                            header = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = BonfireFlame,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Text(
                                        text = "SOULS",
                                        fontFamily = CinzelFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = BonfireGlow
                                    )
                                }
                            },
                            modifier = Modifier.testTag("landscape_nav_rail")
                        ) {
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.LAUNCHER,
                                onClick = { viewModel.selectTab(AppTab.LAUNCHER) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = "Bonfire"
                                    )
                                },
                                label = {
                                    Text("Bonfire", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.DOWNLOAD,
                                onClick = { viewModel.selectTab(AppTab.DOWNLOAD) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = "Download"
                                    )
                                },
                                label = {
                                    Text("Download", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.FILES,
                                onClick = { viewModel.selectTab(AppTab.FILES) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.FolderOpen,
                                        contentDescription = "Files"
                                    )
                                },
                                label = {
                                    Text("Files", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.DRIVERS,
                                onClick = { viewModel.selectTab(AppTab.DRIVERS) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = "Drivers"
                                    )
                                },
                                label = {
                                    Text("Drivers", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.PERFORMANCE,
                                onClick = { viewModel.selectTab(AppTab.PERFORMANCE) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Tweaks"
                                    )
                                },
                                label = {
                                    Text("SoC", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.CONTROLS,
                                onClick = { viewModel.selectTab(AppTab.CONTROLS) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Gamepad,
                                        contentDescription = "Controls"
                                    )
                                },
                                label = {
                                    Text("Controls", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == AppTab.SAVES,
                                onClick = { viewModel.selectTab(AppTab.SAVES) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = "Build"
                                    )
                                },
                                label = {
                                    Text("Build", fontFamily = CinzelFontFamily, fontSize = 9.sp)
                                },
                                colors = railItemColors()
                            )
                        }

                        // Main Content Area
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(top = 6.dp)
                        ) {
                            when (uiState.currentTab) {
                                AppTab.LAUNCHER -> HomeScreen(
                                    uiState = uiState,
                                    onLaunchGame = {
                                        viewModel.launchGame(
                                            onRequestPickFolder = { folderPickerLauncher.launch(null) }
                                        )
                                    },
                                    onRestAtBonfire = { viewModel.restAtBonfire() },
                                    onNavigateTab = { tab -> viewModel.selectTab(tab) },
                                    onSelectEngineMode = { mode -> viewModel.selectEngineMode(mode) }
                                )
                                AppTab.DOWNLOAD -> DownloadScreen(
                                    uiState = uiState,
                                    onStartDirectDownload = { pkg -> viewModel.startRealDirectDownload(pkg) },
                                    onStartSystemDownload = { pkg -> viewModel.startSystemDownloadWithRealTracking(pkg) },
                                    onOpenExternal = { url -> viewModel.openPackageInExternalDownloader(url) },
                                    onCancelDownload = { viewModel.cancelActiveDownload() }
                                )
                                AppTab.FILES -> FilesScreen(
                                    uiState = uiState,
                                    onOpenFolderPicker = { folderPickerLauncher.launch(null) },
                                    onClearFolder = { viewModel.clearGameFolder() },
                                    onToggleDemoMode = { chk -> viewModel.setDemoModeUnlocked(chk) }
                                )
                                AppTab.DRIVERS -> DriversScreen(
                                    uiState = uiState,
                                    onSelectDriver = { id -> viewModel.selectDriver(id) },
                                    onRunProbe = { viewModel.runGpuProbe() }
                                )
                                AppTab.PERFORMANCE -> PerformanceScreen(
                                    uiState = uiState,
                                    onSelectPreset = { id -> viewModel.selectPerformancePreset(id) },
                                    onUpdateDsfix = { update -> viewModel.updateDsfix(update) },
                                    onSelectEngineMode = { mode -> viewModel.selectEngineMode(mode) }
                                )
                                AppTab.CONTROLS -> ControlsScreen(
                                    uiState = uiState,
                                    onUpdatePreferences = { update -> viewModel.updateHudPreferences(update) },
                                    onOpenSimulator = { viewModel.setContainerViewportActive(true) }
                                )
                                AppTab.SAVES -> SavesScreen(
                                    uiState = uiState,
                                    onSelectCharacter = { id -> viewModel.selectCharacter(id) },
                                    onUpdateStat = { stat, delta -> viewModel.updateCalcStat(stat, delta) }
                                )
                            }
                        }
                    }

                    // Top Notification Banner in Root Box Scope
                    androidx.compose.animation.AnimatedVisibility(
                        visible = uiState.launchBannerMessage != null,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(10.dp)
                    ) {
                        uiState.launchBannerMessage?.let { msg ->
                            Surface(
                                color = Color(0xF2181513),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame),
                                shadowElevation = 8.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = BonfireFlame,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = msg,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BonfireGlow
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { viewModel.clearBannerMessage() },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = WeatheredSteel,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Fullscreen Real Container Game Viewport Overlay
                    androidx.compose.animation.AnimatedVisibility(
                        visible = uiState.isContainerViewportActive,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ContainerViewportScreen(
                            containerState = uiState.containerState,
                            hudPreferences = uiState.hudPreferences,
                            onInputEvent = { btnId, pressed -> viewModel.handleInputEvent(btnId, pressed) },
                            onRestAtBonfire = { viewModel.restAtBonfire() },
                            onExportProfile = { viewModel.exportContainerProfile() },
                            onClose = { viewModel.setContainerViewportActive(false) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun railItemColors() = NavigationRailItemDefaults.colors(
        selectedIconColor = BonfireFlame,
        selectedTextColor = BonfireGlow,
        indicatorColor = StoneElevated,
        unselectedIconColor = WeatheredSteel,
        unselectedTextColor = WeatheredSteel
    )
}
