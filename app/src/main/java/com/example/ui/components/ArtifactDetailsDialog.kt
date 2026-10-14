package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BuildItem
import com.example.model.Platform
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.RobotoMonoFontFamily
import com.example.ui.theme.TealAccent

@Composable
fun ArtifactDetailsDialog(
  build: BuildItem,
  isArabic: Boolean,
  onDismiss: () -> Unit,
  onDownload: (BuildItem) -> Unit
) {
  val clipboardManager: ClipboardManager = LocalClipboardManager.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("artifact_details_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
      ),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Computer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = build.appName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isArabic) "تفاصيل الحزمة التنفيذية" else "Binary Package Details",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // File & Platform specs
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          DetailRow(
            label = if (isArabic) "اسم الملف:" else "File Name:",
            value = build.fileName,
            isMonospace = true
          )
          DetailRow(
            label = if (isArabic) "الحجم النهائي:" else "Package Size:",
            value = build.fileSize,
            isHighlight = true
          )
          DetailRow(
            label = if (isArabic) "المنصة المستهدفة:" else "Target Platform:",
            value = build.platform.titleAr
          )

          if (build.platform == Platform.WINDOWS && build.windowsConfig != null) {
            val win = build.windowsConfig
            DetailRow(
              label = if (isArabic) "محرك الغلاف:" else "Wrapper Engine:",
              value = win.engine.title
            )
            DetailRow(
              label = if (isArabic) "أبعاد النافذة:" else "Window Dimensions:",
              value = "${win.windowWidth} × ${win.windowHeight} px"
            )
            DetailRow(
              label = if (isArabic) "دعم شريط المهام:" else "System Tray:",
              value = if (win.hasSystemTray) (if (isArabic) "مفعل (Minimize to Tray)" else "Enabled") else (if (isArabic) "معطل" else "Disabled")
            )
            DetailRow(
              label = if (isArabic) "تنسيق الأيقونة:" else "Icon Format:",
              value = win.iconFormat
            )
          }
        }

        // Checksum Box with Copy
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "SHA-256 Checksum:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = build.checksumSha256.take(24) + "...",
              fontFamily = RobotoMonoFontFamily,
              style = MaterialTheme.typography.labelSmall,
              color = EmeraldGlow,
              modifier = Modifier.weight(1f)
            )

            IconButton(
              onClick = {
                clipboardManager.setText(AnnotatedString(build.checksumSha256))
              },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Checksum",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(if (isArabic) "إغلاق" else "Close")
          }

          Button(
            onClick = {
              onDownload(build)
              onDismiss()
            },
            modifier = Modifier.weight(1.5f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
          ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isArabic) "تنزيل الحزمة" else "Download File")
          }
        }
      }
    }
  }
}

@Composable
private fun DetailRow(
  label: String,
  value: String,
  isMonospace: Boolean = false,
  isHighlight: Boolean = false
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      fontFamily = if (isMonospace) RobotoMonoFontFamily else null,
      style = MaterialTheme.typography.bodySmall,
      fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
      color = if (isHighlight) EmeraldLight else MaterialTheme.colorScheme.onSurface
    )
  }
}
