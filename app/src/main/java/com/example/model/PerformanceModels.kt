package com.example.model

data class PerformancePreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetFps: Int,
    val resolutionWidth: Int,
    val resolutionHeight: Int,
    val d8vkAsync: Boolean,
    val smaaAntiAliasing: String,
    val shadowQuality: String,
    val isRecommendedForG99: Boolean,
    val isRecommendedForSnapdragon: Boolean = false,
    val engineTag: String = "Mali CoD4"
)

data class DsfixSettings(
    val unlockFps: Boolean = true,
    val fpsLimit: Int = 30,
    val renderWidth: Int = 1280,
    val renderHeight: Int = 720,
    val presentWidth: Int = 1920,
    val presentHeight: Int = 1080,
    val aaQuality: String = "SMAA (Low)",
    val ssaoEnabled: Boolean = false,
    val dofResolution: Int = 0,
    val textureOverride: Boolean = false,
    val hudScale: Float = 0.9f,
    // Dual Engine Optimizations
    val maliCoD4Pipeline: Boolean = true,
    val maliValhallCsf: Boolean = true,
    val snapdragonGmemDirect: Boolean = false,
    val d8vkAsyncPipeline: Boolean = true,
    val frameLatency: Int = 1
)

object PerformancePresets {
    val PRESETS = listOf(
        PerformancePreset(
            id = "g99_stable_30",
            title = "Mali CoD4-Engine (Helio G99 30 FPS Lock)",
            subtitle = "Exact CoD4 Android pipeline: PanVK Valhall CSF + D8VK async compiling at 720p with 540p sub-pixel upscale. 0 drops, ultra-smooth frame pacing on Mali-G57.",
            targetFps = 30,
            resolutionWidth = 1280,
            resolutionHeight = 720,
            d8vkAsync = true,
            smaaAntiAliasing = "SMAA Low",
            shadowQuality = "512 (Medium)",
            isRecommendedForG99 = true,
            isRecommendedForSnapdragon = false,
            engineTag = "Mali CoD4"
        ),
        PerformancePreset(
            id = "snapdragon_turnip_60",
            title = "Snapdragon Adreno (Turnip 60 FPS Ultra)",
            subtitle = "1080p native resolution paired with Mesa Turnip 26.2.4 freedreno Vulkan & GMEM direct tiling. Solid 60 FPS on Snapdragon 8 / 7 Gen devices.",
            targetFps = 60,
            resolutionWidth = 1920,
            resolutionHeight = 1080,
            d8vkAsync = true,
            smaaAntiAliasing = "SMAA High",
            shadowQuality = "1024 (High)",
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = true,
            engineTag = "Snapdragon"
        ),
        PerformancePreset(
            id = "g99_potato_60",
            title = "Mali High-Framerate (540p Scaled 45-60 FPS)",
            subtitle = "Dynamic sub-pixel render scale with low shadow overhead for responsive dodging and high evasion response on Helio G99 / Dimensity.",
            targetFps = 60,
            resolutionWidth = 960,
            resolutionHeight = 540,
            d8vkAsync = true,
            smaaAntiAliasing = "Disabled",
            shadowQuality = "256 (Low)",
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = false,
            engineTag = "Mali CoD4"
        ),
        PerformancePreset(
            id = "snapdragon_extreme_120",
            title = "Snapdragon Flagship (90-120 FPS High-Refresh)",
            subtitle = "Uncapped high refresh rate with full anisotropic filtering and SMAA High for Snapdragon 8 Gen 2 / 8 Gen 3 with active cooling.",
            targetFps = 90,
            resolutionWidth = 1920,
            resolutionHeight = 1080,
            d8vkAsync = true,
            smaaAntiAliasing = "SMAA High",
            shadowQuality = "1024 (Ultra)",
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = true,
            engineTag = "Snapdragon"
        )
    )
}
