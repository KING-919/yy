package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuildItem
import com.example.model.BuildStatus
import com.example.model.Platform
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.RobotoMonoFontFamily
import com.example.ui.theme.TealAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BuildHistorySection(
  builds: List<BuildItem>,
  isArabic: Boolean,
  onViewDetails: (BuildItem) -> Unit,
  onDownload: (BuildItem) -> Unit,
  onViewLogs: (BuildItem) -> Unit,
  onDelete: (String) -> Unit,
  onGoToBuilder: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (builds.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.History,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(56.dp)
        )
        Text(
          text = if (isArabic) "سجل البناء فارغ" else "No builds in history yet",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(
          onClick = onGoToBuilder,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text(if (isArabic) "إنشاء أول تطبيق" else "Build Your First App")
        }
      }
    }
    return
  }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isArabic) "سجل الحزم والملفات التنفيذية" else "Build History & Artifacts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (isArabic) "جميع التطبيقات التي تم تجميعها وجاهزة للتنزيل والاستخدام" else "Compiled applications ready for deployment",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${builds.size} " + if (isArabic) "تطبيقات" else "Builds",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Build Cards
    items(builds, key = { it.id }) { item ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onViewDetails(item) }
          .testTag("build_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Top Row: Platform Icon, App Name & Status
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(
                    when (item.platform) {
                      Platform.WINDOWS -> MaterialTheme.colorScheme.primaryContainer
                      Platform.ANDROID -> Color(0xFF0F382A)
                      Platform.FLUTTER -> Color(0xFF0D2833)
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (item.platform) {
                    Platform.WINDOWS -> Icons.Default.Computer
                    Platform.ANDROID -> Icons.Default.Android
                    Platform.FLUTTER -> Icons.Default.Code
                  },
                  contentDescription = null,
                  tint = when (item.platform) {
                    Platform.WINDOWS -> MaterialTheme.colorScheme.primary
                    Platform.ANDROID -> EmeraldLight
                    Platform.FLUTTER -> TealAccent
                  },
                  modifier = Modifier.size(22.dp)
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = item.appName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )

                  if (item.platform == Platform.WINDOWS) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TealAccent.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = if (isArabic) "سطح المكتب" else "Desktop",
                        style = MaterialTheme.typography.labelSmall,
                        color = TealAccent,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }

                Text(
                  text = formatTimestamp(item.createdAt),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            // Status Badge
            val (badgeBg, badgeColor, badgeIcon) = when (item.status) {
              BuildStatus.COMPLETED -> Triple(EmeraldLight.copy(alpha = 0.15f), EmeraldLight, Icons.Default.CheckCircle)
              BuildStatus.WARNING -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, Icons.Default.Warning)
              BuildStatus.FAILED -> Triple(CrimsonError.copy(alpha = 0.15f), CrimsonError, Icons.Default.Error)
              else -> Triple(TealAccent.copy(alpha = 0.15f), TealAccent, Icons.Default.Refresh)
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(badgeBg)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = badgeIcon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isArabic) item.status.labelAr else item.status.labelEn,
                  style = MaterialTheme.typography.labelSmall,
                  color = badgeColor,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          // File details info box
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = item.fileName,
                fontFamily = RobotoMonoFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (item.platform == Platform.WINDOWS) {
                  "Engine: ${item.windowsConfig?.engine?.name ?: "TAURI"} • Size: ${item.fileSize}"
                } else {
                  "Format: ${item.platform.extension.uppercase()} • Size: ${item.fileSize}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = item.fileSize,
                style = MaterialTheme.typography.labelSmall,
                color = EmeraldGlow,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Action buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = { onDownload(item) },
              modifier = Modifier
                .weight(1f)
                .height(38.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isArabic) "تحميل" else "Download",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
              )
            }

            OutlinedButton(
              onClick = { onViewDetails(item) },
              modifier = Modifier.height(38.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isArabic) "تفاصيل" else "Details",
                style = MaterialTheme.typography.labelMedium
              )
            }

            OutlinedButton(
              onClick = { onViewLogs(item) },
              modifier = Modifier.height(38.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(imageVector = Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isArabic) "الأوامر" else "Logs",
                style = MaterialTheme.typography.labelMedium
              )
            }

            IconButton(
              onClick = { onDelete(item.id) },
              modifier = Modifier.size(38.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

private fun formatTimestamp(timestamp: Long): String {
  val sdf = SimpleDateFormat("yyyy/MM/dd • HH:mm", Locale.getDefault())
  return sdf.format(Date(timestamp))
}
