package com.example.model

data class DriverEntry(
    val id: String,
    val name: String,
    val family: String,
    val tag: String,
    val libraryName: String,
    val assetZip: String,
    val gpuModels: List<String>,
    val isRecommendedForG99: Boolean,
    val isRecommendedForSnapdragon: Boolean = false,
    val experimental: Boolean,
    val maturityNote: String,
    val active: Boolean = false,
    val isCoD4Certified: Boolean = false,
    val halBridgeName: String = "libc4vk.so"
)

data class GpuProbeReport(
    val isProbing: Boolean = false,
    val completed: Boolean = false,
    val gpuName: String = "ARM Mali-G57 MC2 (MediaTek Helio G99)",
    val driverVersion: String = "PanVK Kbase v0.1.0-rc3 (CoD4 Smooth Pipeline)",
    val vulkanVersion: String = "Vulkan 1.3.268",
    val presentationMode: String = "VK_PRESENT_MODE_FIFO_KHR (V-Sync)",
    val maxImageExtent: String = "4096 x 4096",
    val memoryBudgetMb: Int = 3840,
    val shaderExtensionsSupported: Boolean = true,
    val estimatedFps: Int = 32,
    val statusMessage: String = "PanVK CSF driver validated on Mali-G57 MC2! CoD4 smooth pipeline active.",
    val architectureTag: String = "MALI_COD4"
)

object DriverCatalog {
    val BUILT_IN_DRIVERS = listOf(
        DriverEntry(
            id = "panvk-kbase-rc3",
            name = "PanVK Kbase RC3 (CoD4 Verified)",
            family = "PanVK",
            tag = "panvk-kbase-v0.1.0-rc3",
            libraryName = "libvulkan_panfrost.so",
            assetZip = "PanVK-Kbase-Android-g615-v11-csf-v0.1.0-rc3-5a07217f.adpkg.zip",
            gpuModels = listOf("G57 (Helio G99)", "G68", "G77", "G78", "G610", "G710", "G615"),
            isRecommendedForG99 = true,
            isRecommendedForSnapdragon = false,
            experimental = true,
            maturityNote = "Exact CoD4 graphics driver: Command Stream Frontend (CSF) enabled for smooth stutter-free 30-45 FPS on Mali-G57 & Dimensity.",
            active = true,
            isCoD4Certified = true,
            halBridgeName = "libc4vk.so & libcod4first.so"
        ),
        DriverEntry(
            id = "turnip-2624",
            name = "Mesa Turnip 26.2.4 (Snapdragon Beast)",
            family = "Turnip",
            tag = "26.2.4",
            libraryName = "libvulkan_freedreno.so",
            assetZip = "mesa-turnip-android-26.2.4.zip",
            gpuModels = listOf("Adreno 6xx (Snapdragon 845-870)", "Adreno 7xx (8 Gen 1/2/3)", "Adreno 8xx (8 Elite)"),
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = true,
            experimental = true,
            maturityNote = "Optimized for Qualcomm Snapdragon Adreno. Direct GMEM tiling delivers solid 60 FPS ultra.",
            active = false,
            isCoD4Certified = false,
            halBridgeName = "libgsl_alloc_hook.so"
        ),
        DriverEntry(
            id = "panvk-bifrost-130",
            name = "PanVK Bifrost 1.3.0",
            family = "PanVK",
            tag = "v1.3.0",
            libraryName = "libvulkan_panfrost.so",
            assetZip = "mali-panvk-26.3.0-devel-r4.zip",
            gpuModels = listOf("G52", "G72", "G76 (Older Mali chips)"),
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = false,
            experimental = true,
            maturityNote = "Bifrost Job Manager architecture for previous generation MediaTek / Exynos Mali processors.",
            active = false,
            isCoD4Certified = false,
            halBridgeName = "libc4vk.so"
        ),
        DriverEntry(
            id = "radv-xclipse",
            name = "RADV Xclipse 1.5.0",
            family = "RADV/Xclipse",
            tag = "v1.5.0",
            libraryName = "vulkan.radeon.so",
            assetZip = "radv-xclipse-26.3.0-devel-313870e.zip",
            gpuModels = listOf("Samsung Xclipse 920", "Xclipse 940 (Exynos RDNA)"),
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = false,
            experimental = true,
            maturityNote = "Exynos RDNA architecture driver for AMD-based Samsung Galaxy devices.",
            active = false,
            isCoD4Certified = false,
            halBridgeName = "libc4vk.so"
        ),
        DriverEntry(
            id = "system-vulkan",
            name = "Android System Vendor Driver",
            family = "System",
            tag = "Android HAL",
            libraryName = "vulkan.mali.so",
            assetZip = "",
            gpuModels = listOf("All Stock Android GPUs (Default Factory HAL)"),
            isRecommendedForG99 = false,
            isRecommendedForSnapdragon = false,
            experimental = false,
            maturityNote = "Stock vendor driver provided by device manufacturer.",
            active = false,
            isCoD4Certified = false,
            halBridgeName = "System HAL"
        )
    )
}
