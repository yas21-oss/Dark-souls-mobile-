package com.example.model

data class DownloadPackage(
    val id: String,
    val title: String,
    val description: String,
    val sizeText: String,
    val sizeBytes: Long,
    val url: String,
    val tag: String,
    val isRecommendedForG99: Boolean,
    val format: String = "RAR"
)

data class ActiveDownloadState(
    val isDownloading: Boolean = false,
    val activePackageId: String? = null,
    val progressPercent: Float = 0f,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 7457235345L,
    val speedKbps: Float = 0f,
    val etaText: String = "",
    val statusText: String = "Ready to download real archive (7.46 GB)",
    val systemDownloadId: Long? = null,
    val isCompleted: Boolean = false
)

object DownloadCatalog {
    val OFFICIAL_RELEASE = DownloadPackage(
        id = "ds_remastered_steamrip",
        title = "Dark Souls Remastered (SteamRIP Archive)",
        description = "Official complete Dark Souls Remastered archive from your direct link. Contains DarkSoulsRemastered.exe and full game asset archives.",
        sizeText = "7.46 GB",
        sizeBytes = 7457235345L,
        url = "https://ts.bzzhr.to/d/n0y5bc5kze2n?v=Cc1n60XN73Yc60jtECGt2fM9VDOUX9AvSrXSoDWzbLC3zCBw8uxZMtc5aNQV-698T8fpBIotsMd9u90kAc_3uU9Ll5S3xl-r9o8lB3atpvPAlbCW649pWtt9JFRAQUDGkB-y6CVA60CESLPNus4hm2WL5eHYNuyIzMczZTDb-oO5FiFOtWZ7qpPyTqyB-SEbDgBuPh5ka20wAJWu0laCmrY",
        tag = "Direct Server Release (SteamRIP)",
        isRecommendedForG99 = true,
        format = "RAR Archive"
    )

    val PACKAGES = listOf(
        OFFICIAL_RELEASE,
        DownloadPackage(
            id = "panvk_ds_tweaks",
            title = "PanVK & Mali-G57 Direct Configuration Pack",
            description = "High-efficiency PanVK driver configuration and D8VK/DXVK async profile to pair with your game download.",
            sizeText = "12.4 MB",
            sizeBytes = 13002342L,
            url = "https://github.com/zenithblue-oss/panvk-kbase-android/releases/download/v1.0/dsfix-panvk-patch.zip",
            tag = "Mali Optimization",
            isRecommendedForG99 = true,
            format = "ZIP Patch"
        )
    )
}
