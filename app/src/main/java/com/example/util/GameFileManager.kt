package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log

data class GameFilesReport(
    val isFolderSelected: Boolean = false,
    val folderUriString: String? = null,
    val folderDisplayName: String = "No folder selected",
    val hasExecutable: Boolean = false,
    val executableName: String? = null,
    val hasDataFiles: Boolean = false,
    val hasDsfix: Boolean = false,
    val totalFilesFound: Int = 0,
    val detectedFiles: List<String> = emptyList(),
    val isValidGameReady: Boolean = false,
    val validationMessage: String = "Game files not selected. You must specify the folder containing DARKSOULS.exe."
)

object GameFileManager {
    private const val PREFS_NAME = "dark_souls_launcher_prefs"
    private const val KEY_FOLDER_URI = "key_game_folder_uri"
    private const val TAG = "GameFileManager"

    fun getSavedFolderUri(context: Context): Uri? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val uriStr = prefs.getString(KEY_FOLDER_URI, null) ?: return null
        return try {
            Uri.parse(uriStr)
        } catch (_: Exception) {
            null
        }
    }

    fun saveFolderUri(context: Context, uri: Uri) {
        try {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flags)
        } catch (e: Exception) {
            Log.w(TAG, "Could not take persistable permission: ${e.message}")
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FOLDER_URI, uri.toString()).apply()
    }

    fun clearSavedFolder(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_FOLDER_URI).apply()
    }

    fun inspectGameFolder(context: Context, uri: Uri?): GameFilesReport {
        if (uri == null) {
            return GameFilesReport(
                isFolderSelected = false,
                validationMessage = "No game folder selected. Please select your Dark Souls installation directory."
            )
        }

        val foundNames = mutableListOf<String>()
        var folderName = uri.lastPathSegment ?: "Dark Souls Directory"
        if (folderName.contains(":")) {
            folderName = folderName.substringAfterLast(":")
        }

        try {
            val treeDocId = DocumentsContract.getTreeDocumentId(uri)
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, treeDocId)
            val projection = arrayOf(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE
            )

            context.contentResolver.query(childrenUri, projection, null, null, null)?.use { cursor ->
                val nameIdx = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    if (nameIdx >= 0) {
                        val name = cursor.getString(nameIdx)
                        if (name != null) {
                            foundNames.add(name)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying folder tree: ${e.message}")
        }

        val exe = foundNames.find {
            it.equals("DARKSOULS.exe", ignoreCase = true) ||
            it.equals("DarkSoulsRemastered.exe", ignoreCase = true) ||
            it.endsWith(".exe", ignoreCase = true)
        }
        val hasExe = exe != null

        val hasData = foundNames.any {
            it.equals("DATA", ignoreCase = true) ||
            it.equals("dvdroot_clean", ignoreCase = true) ||
            it.equals("mov", ignoreCase = true) ||
            it.equals("sound", ignoreCase = true) ||
            it.equals("map", ignoreCase = true) ||
            it.equals("msg", ignoreCase = true) ||
            it.equals("param", ignoreCase = true)
        }

        val hasDsfix = foundNames.any {
            it.equals("DSfix.ini", ignoreCase = true) ||
            it.equals("dinput8.dll", ignoreCase = true) ||
            it.equals("DSfixKeys.ini", ignoreCase = true)
        }

        val isReady = hasExe

        val msg = when {
            isReady -> "Verified! Found executable ($exe) in $folderName. Ready to launch."
            foundNames.isNotEmpty() -> "Folder inspected ($folderName, ${foundNames.size} items), but DARKSOULS.exe was not found in root. Make sure to select the folder containing DARKSOULS.exe."
            else -> "Folder opened, but no files could be read. Ensure storage permission is allowed."
        }

        return GameFilesReport(
            isFolderSelected = true,
            folderUriString = uri.toString(),
            folderDisplayName = folderName,
            hasExecutable = hasExe,
            executableName = exe,
            hasDataFiles = hasData,
            hasDsfix = hasDsfix,
            totalFilesFound = foundNames.size,
            detectedFiles = foundNames,
            isValidGameReady = isReady,
            validationMessage = msg
        )
    }
}
