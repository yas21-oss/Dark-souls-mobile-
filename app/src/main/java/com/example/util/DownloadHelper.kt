package com.example.util

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast

object DownloadHelper {

    fun startSystemDownload(
        context: Context,
        url: String,
        fileName: String,
        title: String
    ): Long? {
        return try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                ?: return null

            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle(title)
                setDescription("Downloading Dark Souls archive to Downloads/DarkSouls")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "DarkSouls/$fileName"
                )
            }

            val id = downloadManager.enqueue(request)
            Toast.makeText(
                context,
                "Download queued in notification bar: $title",
                Toast.LENGTH_LONG
            ).show()
            id
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Could not enqueue download: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    fun openInBrowserOrDownloader(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
