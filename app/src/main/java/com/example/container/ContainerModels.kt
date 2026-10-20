package com.example.container

data class PeSection(
    val name: String,
    val virtualSize: Long,
    val virtualAddress: Long,
    val rawDataSize: Long,
    val characteristicsHex: String
)

data class ExecutableBinaryInfo(
    val isLoaded: Boolean = false,
    val fileName: String = "DARKSOULS.exe",
    val fileSizeBytes: Long = 0L,
    val dosMagic: String = "MZ (0x5A4D)",
    val peSignature: String = "PE\\0\\0 (0x00004550)",
    val machineType: String = "x86 32-bit (IMAGE_FILE_MACHINE_I386)",
    val subsystem: String = "Win32 GUI (Direct3D 9)",
    val entryPointAddress: String = "0x00401000",
    val imageBase: String = "0x00400000",
    val numberOfSections: Int = 5,
    val sections: List<PeSection> = emptyList(),
    val sha256Checksum: String = "",
    val detectedGameEdition: String = "Dark Souls: Prepare to Die Edition (Steam Build 1.0.2.0)"
)

enum class ContainerLifecycleStage(val displayName: String) {
    STOPPED("Container Offline"),
    BOOTING_PREFIX("Initializing Wineprefix & Rootfs"),
    PREPARING_DRIVERS("Linking GPU Driver & Hal Bridge"),
    INJECTING_DSFIX("Configuring DSfix.ini & d8vk.conf"),
    PARSING_BINARY("Inspecting DARKSOULS.exe Binary"),
    BOOTING_AUDIO("Starting OpenSL ES Audio Stream"),
    INITIALIZING_VULKAN("Allocating Vulkan Swapchain Buffer"),
    RUNNING("Container Active — Executing Game Process"),
    PAUSED("Container Paused"),
    TERMINATED("Container Terminated")
}

data class ContainerLogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String,
    val message: String,
    val level: String = "INFO" // INFO, WARN, SUCCESS, DEBUG
)

data class XInputPacket(
    val buttons: Int = 0,
    val leftTrigger: Int = 0,
    val rightTrigger: Int = 0,
    val thumbLX: Short = 0,
    val thumbLY: Short = 0,
    val thumbRX: Short = 0,
    val thumbRY: Short = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class InGameRuntimeState(
    val playerName: String = "Chosen Undead",
    val playerClass: String = "Knight",
    val currentHp: Float = 780f,
    val maxHp: Float = 780f,
    val currentStamina: Float = 120f,
    val maxStamina: Float = 120f,
    val estusFlaskCount: Int = 5,
    val maxEstusFlasks: Int = 5,
    val soulsCount: Long = 14250L,
    val humanityCount: Int = 2,
    val activeWeaponRight: String = "Claymore +15",
    val activeWeaponLeft: String = "Grass Crest Shield",
    val isTwoHanding: Boolean = false,
    val isTargetLocked: Boolean = false,
    val currentActionText: String = "Container engine ready — Lordran awaits",
    val activeAnimation: String = "Idle",
    val fpsCurrent: Float = 30f,
    val frameTimeMs: Float = 33.3f,
    val gpuUsagePercent: Int = 78,
    val vramAllocatedMb: Int = 620,
    val drawCalls: Int = 1480,
    val packetsProcessed: Long = 0L,
    val lastPacketSummary: String = "XInput [0x0000]"
)

data class ContainerState(
    val stage: ContainerLifecycleStage = ContainerLifecycleStage.STOPPED,
    val isRunning: Boolean = false,
    val pid: Int = 0,
    val prefixDirectoryPath: String = "",
    val activeDriverName: String = "PanVK Mali Valhall CSF",
    val binaryInfo: ExecutableBinaryInfo = ExecutableBinaryInfo(),
    val logs: List<ContainerLogEntry> = emptyList(),
    val runtime: InGameRuntimeState = InGameRuntimeState(),
    val externalContainerAppsDetected: List<String> = emptyList(),
    val isExternalLauncherAvailable: Boolean = false,
    val containerProfilePath: String = ""
)
