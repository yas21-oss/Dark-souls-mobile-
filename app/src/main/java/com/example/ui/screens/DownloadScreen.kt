package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainUiState
import com.example.model.DownloadPackage
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AshBlack
import com.example.ui.theme.BonfireCore
import com.example.ui.theme.BonfireFlame
import com.example.ui.theme.BonfireGlow
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EstusYellow
import com.example.ui.theme.HollowRed
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SoulCyan
import com.example.ui.theme.StoneElevated
import com.example.ui.theme.StoneSurface
import com.example.ui.theme.WeatheredSteel
import kotlin.math.roundToInt

@Composable
fun DownloadScreen(
    uiState: MainUiState,
    onStartDirectDownload: (DownloadPackage) -> Unit,
    onStartSystemDownload: (DownloadPackage) -> Unit,
    onOpenExternal: (String) -> Unit,
    onCancelDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val downloadState = uiState.activeDownloadState

    fun copyUrlToClipboard(url: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Download Link", url)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Direct link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

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
                    text = "REAL GAME DOWNLOAD CENTER",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AncientParchment
                )
                Text(
                    text = "Real HTTP network streaming & background DownloadManager to phone storage. Zero simulated data.",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    color = WeatheredSteel
                )
            }
        }

        // Active Real Download Progress Card
        item {
            AnimatedVisibility(visible = downloadState.isDownloading || downloadState.isCompleted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_download_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF28211C)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, BonfireFlame)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (downloadState.isCompleted) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    tint = if (downloadState.isCompleted) Color(0xFF10B981) else BonfireFlame,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (downloadState.isCompleted) "REAL DOWNLOAD COMPLETE" else "REAL NETWORK STREAM ACTIVE",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (downloadState.isCompleted) Color(0xFF34D399) else BonfireGlow
                                )
                            }
                            if (downloadState.isDownloading) {
                                OutlinedButton(
                                    onClick = onCancelDownload,
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, HollowRed.copy(alpha = 0.7f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HollowRed)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "STOP",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Real Progress Bar
                        LinearProgressIndicator(
                            progress = { (downloadState.progressPercent / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = BonfireFlame,
                            trackColor = StoneElevated
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${String.format("%.1f", downloadState.progressPercent)}% (${downloadState.downloadedBytes / (1024 * 1024)} MB)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AncientParchment
                            )
                            if (downloadState.isDownloading && downloadState.speedKbps > 0) {
                                Text(
                                    text = "Real Speed: ${(downloadState.speedKbps / 1024f).toInt()} MB/s",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    color = EstusYellow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = downloadState.statusText,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            color = WeatheredSteel
                        )
                    }
                }
            }
        }

        // Available Packages
        item {
            Text(
                text = "AVAILABLE PACKAGES & RELEASES",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BonfireGlow
            )
        }

        items(uiState.downloadPackages) { pkg ->
            val isDownloadingThis = downloadState.isDownloading && downloadState.activePackageId == pkg.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("download_pkg_${pkg.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = StoneSurface),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (pkg.isRecommendedForG99) 1.5.dp else 1.dp,
                    color = if (pkg.isRecommendedForG99) BonfireFlame else StoneElevated
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = pkg.title,
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AncientParchment
                                )
                                if (pkg.isRecommendedForG99) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = BonfireFlame.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(0.6.dp, BonfireFlame)
                                    ) {
                                        Text(
                                            text = "HELIO G99 TOP PICK",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BonfireFlame,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Size: ${pkg.sizeText} • Format: ${pkg.format} • ${pkg.tag}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = EstusYellow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = pkg.description,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = WeatheredSteel
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Three Real Actions: Direct Stream, Background Notification, External Downloader
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onStartDirectDownload(pkg) },
                            enabled = !isDownloadingThis,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BonfireFlame,
                                contentColor = AshBlack
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isDownloadingThis) "STREAMING..." else "STREAM TO DISK",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }

                        Button(
                            onClick = { onStartSystemDownload(pkg) },
                            enabled = !isDownloadingThis,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF382F27),
                                contentColor = BonfireGlow
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BonfireFlame.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "DOWNLOAD MANAGER",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { onOpenExternal(pkg.url) },
                            modifier = Modifier.height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BonfireGlow.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BonfireGlow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = "1DM / External",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = { copyUrlToClipboard(pkg.url) },
                            modifier = Modifier.height(42.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WeatheredSteel.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WeatheredSteel)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Link",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Instructions Card
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
                            text = "خيارات التنزيل الحقيقي:",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BonfireGlow
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• STREAM TO DISK: تحميل حقيقي عبر اتصال شبكي يكتب البيانات الفعلية فوراً إلى مجلد التحميلات بالهاتف مع قياس السرعة اللحظية بدقة.\n" +
                               "• DOWNLOAD MANAGER: يرسل طلب التحميل الحقيقي لمدير تحميلات أندرويد ليعمل في الخلفية وشريط الإشعارات حتى لو أغلقت التطبيق.\n" +
                               "• 1DM / BROWSER: فتح الرابط المباشر في برنامج تنزيل خارجي مثل 1DM لتنزيل ملفات 7.5GB بسرعات مضاعفة واستئناف التحميل في أي وقت.",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        lineHeight = 18.sp,
                        color = AncientParchment
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
