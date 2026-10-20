package com.example.util

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

data class RealDownloadProgress(
    val isRunning: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val bytesDownloaded: Long = 0L,
    val totalBytes: Long = 0L,
    val progressPercent: Float = 0f,
    val speedBytesPerSec: Long = 0L,
    val statusMessage: String = "Idle",
    val downloadedFile: File? = null
)

object RealDownloader {
    private const val TAG = "RealDownloader"
    private const val MAX_RETRIES = 10

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    /**
     * Real resilient streaming network download with automatic Range resume on connection aborts.
     */
    suspend fun downloadDirectToFile(
        context: Context,
        url: String,
        targetFileName: String,
        onProgress: (RealDownloadProgress) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val destinationDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: context.filesDir
        val targetFile = File(destinationDir, targetFileName)

        var retryCount = 0
        var totalBytes = 0L
        var isCompleted = false

        while (coroutineContext.isActive && !isCompleted && retryCount < MAX_RETRIES) {
            val existingLength = if (targetFile.exists()) targetFile.length() else 0L

            val statusMsg = if (existingLength > 0L) {
                "Resuming from ${(existingLength / (1024 * 1024))} MB (Attempt ${retryCount + 1}/$MAX_RETRIES)..."
            } else {
                "Connecting to server..."
            }

            onProgress(
                RealDownloadProgress(
                    isRunning = true,
                    bytesDownloaded = existingLength,
                    totalBytes = totalBytes,
                    progressPercent = if (totalBytes > 0) (existingLength.toFloat() / totalBytes) * 100f else 0f,
                    statusMessage = statusMsg,
                    downloadedFile = targetFile
                )
            )

            try {
                val requestBuilder = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36")
                    .header("Accept", "*/*")
                    .header("Connection", "keep-alive")

                if (existingLength > 0L) {
                    requestBuilder.header("Range", "bytes=$existingLength-")
                }

                val response = okHttpClient.newCall(requestBuilder.build()).execute()
                val code = response.code

                if (!response.isSuccessful && code != 206) {
                    val errorMsg = "HTTP $code: ${response.message}"
                    response.close()
                    if (code in 500..599 || code == 429) {
                        retryCount++
                        delay(2000L * retryCount)
                        continue
                    } else {
                        onProgress(
                            RealDownloadProgress(
                                isRunning = false,
                                isFailed = true,
                                statusMessage = "Server error: $errorMsg"
                            )
                        )
                        return@withContext Result.failure(IOException(errorMsg))
                    }
                }

                val body = response.body ?: throw IOException("Empty body from server")
                val isPartial = code == 206
                val append = isPartial && existingLength > 0L

                val contentLen = body.contentLength()
                if (totalBytes <= 0L) {
                    totalBytes = if (isPartial) existingLength + contentLen else contentLen
                }

                var downloadedBytes = if (append) existingLength else 0L
                var lastUpdateTime = System.currentTimeMillis()
                var bytesSinceLastUpdate = 0L
                var currentSpeed = 0L

                val buffer = ByteArray(64 * 1024)
                val inputStream: InputStream = body.byteStream()
                val outputStream = FileOutputStream(targetFile, append)

                try {
                    outputStream.use { out ->
                        inputStream.use { input ->
                            while (coroutineContext.isActive) {
                                val read = input.read(buffer)
                                if (read == -1) {
                                    isCompleted = true
                                    break
                                }

                                out.write(buffer, 0, read)
                                downloadedBytes += read
                                bytesSinceLastUpdate += read

                                val now = System.currentTimeMillis()
                                val elapsed = now - lastUpdateTime
                                if (elapsed >= 500) {
                                    currentSpeed = (bytesSinceLastUpdate * 1000) / elapsed
                                    val percent = if (totalBytes > 0) {
                                        (downloadedBytes.toFloat() / totalBytes) * 100f
                                    } else {
                                        0f
                                    }

                                    onProgress(
                                        RealDownloadProgress(
                                            isRunning = true,
                                            bytesDownloaded = downloadedBytes,
                                            totalBytes = totalBytes,
                                            progressPercent = percent.coerceIn(0f, 100f),
                                            speedBytesPerSec = currentSpeed,
                                            statusMessage = "Transferring real bytes: ${(downloadedBytes / (1024 * 1024))} MB / ${(totalBytes / (1024 * 1024))} MB",
                                            downloadedFile = targetFile
                                        )
                                    )

                                    lastUpdateTime = now
                                    bytesSinceLastUpdate = 0L
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is SocketException, is SocketTimeoutException, is IOException -> {
                            Log.w(TAG, "Socket connection interrupted (${e.message}). Auto-resuming...")
                            retryCount++
                            delay(1500L)
                            // continue loop to auto-resume from last byte on disk
                        }
                        else -> throw e
                    }
                } finally {
                    response.close()
                }

            } catch (e: Exception) {
                Log.w(TAG, "Connection attempt failed: ${e.message}")
                retryCount++
                delay(2000L)
            }
        }

        if (!coroutineContext.isActive) {
            onProgress(
                RealDownloadProgress(
                    isRunning = false,
                    statusMessage = "Download paused / cancelled."
                )
            )
            return@withContext Result.failure(IOException("Cancelled"))
        }

        if (isCompleted) {
            onProgress(
                RealDownloadProgress(
                    isRunning = false,
                    isSuccess = true,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = totalBytes,
                    progressPercent = 100f,
                    statusMessage = "Download completed! File saved: ${targetFile.name} (${targetFile.length() / (1024 * 1024)} MB)",
                    downloadedFile = targetFile
                )
            )
            Result.success(targetFile)
        } else {
            val failureMsg = "Connection interrupted after $retryCount attempts. Use DOWNLOAD MANAGER (notification) or 1DM for multi-thread download."
            onProgress(
                RealDownloadProgress(
                    isRunning = false,
                    isFailed = true,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = totalBytes,
                    statusMessage = failureMsg,
                    downloadedFile = targetFile
                )
            )
            Result.failure(IOException(failureMsg))
        }
    }

    /**
     * Polls the real Android DownloadManager database cursor to get actual downloaded bytes.
     */
    fun queryDownloadManagerProgress(context: Context, downloadId: Long): RealDownloadProgress {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
            ?: return RealDownloadProgress(isFailed = true, statusMessage = "DownloadManager unavailable")

        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor? = downloadManager.query(query)

        cursor?.use {
            if (it.moveToFirst()) {
                val bytesIdx = it.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalIdx = it.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                val statusIdx = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val reasonIdx = it.getColumnIndex(DownloadManager.COLUMN_REASON)

                val downloaded = if (bytesIdx >= 0) it.getLong(bytesIdx) else 0L
                val total = if (totalIdx >= 0) it.getLong(totalIdx) else 0L
                val status = if (statusIdx >= 0) it.getInt(statusIdx) else 0

                val percent = if (total > 0) (downloaded.toFloat() / total) * 100f else 0f

                return when (status) {
                    DownloadManager.STATUS_RUNNING -> RealDownloadProgress(
                        isRunning = true,
                        bytesDownloaded = downloaded,
                        totalBytes = total,
                        progressPercent = percent,
                        statusMessage = "Downloading via Android DownloadManager: ${(downloaded / (1024 * 1024))} MB"
                    )
                    DownloadManager.STATUS_SUCCESSFUL -> RealDownloadProgress(
                        isRunning = false,
                        isSuccess = true,
                        bytesDownloaded = downloaded,
                        totalBytes = total,
                        progressPercent = 100f,
                        statusMessage = "Download successful in Downloads/DarkSouls!"
                    )
                    DownloadManager.STATUS_FAILED -> {
                        val reason = if (reasonIdx >= 0) it.getInt(reasonIdx) else 0
                        RealDownloadProgress(
                            isRunning = false,
                            isFailed = true,
                            statusMessage = "Download failed (code: $reason)"
                        )
                    }
                    DownloadManager.STATUS_PAUSED -> RealDownloadProgress(
                        isRunning = true,
                        bytesDownloaded = downloaded,
                        totalBytes = total,
                        progressPercent = percent,
                        statusMessage = "Download paused by network condition"
                    )
                    else -> RealDownloadProgress(
                        isRunning = true,
                        statusMessage = "Waiting for network..."
                    )
                }
            }
        }

        return RealDownloadProgress(statusMessage = "Initializing download query...")
    }
}
