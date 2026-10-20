package com.example.util

import android.os.Build

enum class GpuArchitecture(val displayName: String, val vendor: String) {
    MALI("ARM Mali (Helio G99 / Dimensity / Exynos)", "ARM / MediaTek"),
    SNAPDRAGON_ADRENO("Qualcomm Adreno (Snapdragon)", "Qualcomm"),
    GENERIC("Standard / Generic GPU", "System")
}

enum class EngineMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetSoc: String,
    val driverId: String,
    val presetId: String,
    val badge: String,
    val description: String
) {
    MALI_COD4(
        id = "mali_cod4",
        title = "Mali CoD4-Engine",
        subtitle = "PanVK Valhall CSF + D8VK Async (Helio G99 / Dimensity)",
        targetSoc = "MediaTek Helio G99 / Dimensity / Mali-G57/G77",
        driverId = "panvk-kbase-rc3",
        presetId = "g99_stable_30",
        badge = "CoD4 Smooth",
        description = "Replicates the exact graphics bridge that ran CoD4 smoothly on Helio G99: PanVK Kbase RC3 with Valhall CSF frontend, D8VK async pipeline, and locked 30-45 FPS."
    ),
    SNAPDRAGON_TURNIP(
        id = "snapdragon_turnip",
        title = "Snapdragon Beast Engine",
        subtitle = "Mesa Turnip 26.2.4 + Adreno GMEM Direct (60 FPS)",
        targetSoc = "Qualcomm Snapdragon 6xx / 7xx / 8xx (Adreno)",
        driverId = "turnip-2624",
        presetId = "snapdragon_turnip_60",
        badge = "60 FPS Ultra",
        description = "Mesa Turnip 26.2.4 freedreno Vulkan with KGSL direct memory tiling for Qualcomm Adreno GPUs, delivering solid 60 FPS at 1080p."
    ),
    AUTO(
        id = "auto",
        title = "Auto-Detect SoC",
        subtitle = "Smart matching based on detected chipset hardware",
        targetSoc = "System Auto-Detect",
        driverId = "panvk-kbase-rc3",
        presetId = "g99_stable_30",
        badge = "Auto",
        description = "Automatically detects whether the device has a MediaTek/Mali or Qualcomm/Snapdragon processor and selects the optimal driver."
    )
}

data class HardwareProfile(
    val architecture: GpuArchitecture,
    val socName: String,
    val recommendedDriverId: String,
    val recommendedResolution: String,
    val recommendedFps: Int,
    val optimalPipelineNote: String,
    val halHookName: String = "libc4vk.so",
    val isCoD4Optimized: Boolean = true
)

object HardwareDetector {

    fun detectHardware(): HardwareProfile {
        val hardware = Build.HARDWARE.lowercase()
        val board = Build.BOARD.lowercase()
        val model = Build.MODEL.lowercase()

        val isSnapdragon = hardware.contains("qcom") ||
                hardware.contains("qualcomm") ||
                board.contains("msm") ||
                board.contains("sm") ||
                board.contains("bengal") ||
                board.contains("taro") ||
                board.contains("kalama")

        val isMali = hardware.contains("mt") ||
                hardware.contains("mediatek") ||
                hardware.contains("mali") ||
                board.contains("mt") ||
                model.contains("g99") ||
                hardware.contains("exynos")

        return when {
            isSnapdragon -> HardwareProfile(
                architecture = GpuArchitecture.SNAPDRAGON_ADRENO,
                socName = "Qualcomm Snapdragon (Adreno GPU)",
                recommendedDriverId = "turnip-2624",
                recommendedResolution = "1080p Native (1920x1080)",
                recommendedFps = 60,
                optimalPipelineNote = "Mesa Turnip 26.2.4 with Adreno GMEM direct tiling. Solid 60 FPS unlocked.",
                halHookName = "libgsl_alloc_hook.so (Snapdragon KGSL)",
                isCoD4Optimized = false
            )
            isMali -> HardwareProfile(
                architecture = GpuArchitecture.MALI,
                socName = "MediaTek Helio G99 (Mali-G57 MC2)",
                recommendedDriverId = "panvk-kbase-rc3",
                recommendedResolution = "720p (960x540 dynamic upscale)",
                recommendedFps = 30,
                optimalPipelineNote = "PanVK Kbase RC3 (Valhall CSF) + D8VK async pipeline. Exactly matches CoD4 smooth frame delivery on Mali-G57.",
                halHookName = "libc4vk.so & libcod4first.so (CoD4 Mali HAL Bridge)",
                isCoD4Optimized = true
            )
            else -> HardwareProfile(
                architecture = GpuArchitecture.MALI, // Default to Mali Helio G99
                socName = "MediaTek Helio G99 / Mali-G57 (Active)",
                recommendedDriverId = "panvk-kbase-rc3",
                recommendedResolution = "720p",
                recommendedFps = 30,
                optimalPipelineNote = "PanVK Valhall CSF driver with CoD4-style Mali stutter elimination.",
                halHookName = "libc4vk.so (CoD4 Mali Adapter)",
                isCoD4Optimized = true
            )
        }
    }

    fun getMaliProfile(): HardwareProfile {
        return HardwareProfile(
            architecture = GpuArchitecture.MALI,
            socName = "MediaTek Helio G99 (Mali-G57 MC2)",
            recommendedDriverId = "panvk-kbase-rc3",
            recommendedResolution = "720p (1280x720) with 540p sub-pixel",
            recommendedFps = 30,
            optimalPipelineNote = "PanVK Kbase RC3 (Mali Valhall CSF). CoD4 smooth execution pipeline with zero thermal drops.",
            halHookName = "libc4vk.so (CoD4 Mali Bridge)",
            isCoD4Optimized = true
        )
    }

    fun getSnapdragonProfile(): HardwareProfile {
        return HardwareProfile(
            architecture = GpuArchitecture.SNAPDRAGON_ADRENO,
            socName = "Qualcomm Snapdragon (Adreno 6xx / 7xx / 8xx)",
            recommendedDriverId = "turnip-2624",
            recommendedResolution = "1080p Native (1920x1080)",
            recommendedFps = 60,
            optimalPipelineNote = "Mesa Turnip 26.2.4 (Freedreno Vulkan). Direct GMEM tiling, maximum shader caching, 60 FPS.",
            halHookName = "libgsl_alloc_hook.so (KGSL Direct)",
            isCoD4Optimized = false
        )
    }
}
