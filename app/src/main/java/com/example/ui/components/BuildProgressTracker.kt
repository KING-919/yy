package com.example.ui.components

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.LogLevel
import com.example.model.Platform
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.DarkTerminalBg
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.RobotoMonoFontFamily
import com.example.ui.theme.TealAccent

@Composable
fun BuildProgressTracker(
  build: BuildItem?,
  isArabic: Boolean,
  onCancelBuild: (String) -> Unit,
  onRetryBuild: (String) -> Unit,
  onDownloadArtifact: (BuildItem) -> Unit,
  modifier: Modifier = Modifier
) {
  if (build == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Terminal,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(54.dp)
        )
        Text(
          text = if (isArabic) "لا توجد عملية بناء نشطة حالياً" else "No active build currently running",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = if (isArabic) "ابدأ بناء تطبيق جديد من تبويب 'إنشاء تطبيق'" else "Start a new build from the 'Builder' tab",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    return
  }

  val listState = rememberLazyListState()

  // Auto scroll to latest log
  LaunchedEffect(build.logs.size) {
    if (build.logs.isNotEmpty()) {
      listState.animateScrollToItem(build.logs.size - 1)
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Current Status Header Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = build.appName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(8.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(
                  text = build.platform.extension.uppercase(),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Text(
              text = "${build.platform.titleAr} • ${build.fileName}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Status Badge
          val (badgeBg, badgeTextColor, badgeIcon) = when (build.status) {
            BuildStatus.COMPLETED -> Triple(EmeraldLight.copy(alpha = 0.2f), EmeraldLight, Icons.Default.CheckCircle)
            BuildStatus.WARNING -> Triple(AmberWarning.copy(alpha = 0.2f), AmberWarning, Icons.Default.Warning)
            BuildStatus.FAILED -> Triple(CrimsonError.copy(alpha = 0.2f), CrimsonError, Icons.Default.Error)
            else -> Triple(TealAccent.copy(alpha = 0.2f), TealAccent, Icons.Default.Refresh)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(badgeBg)
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = badgeIcon,
                contentDescription = null,
                tint = badgeTextColor,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isArabic) build.status.labelAr else build.status.labelEn,
                style = MaterialTheme.typography.labelMedium,
                color = badgeTextColor,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Progress Bar
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = build.currentStep,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "${(build.progress * 100).toInt()}%",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          LinearProgressIndicator(
            progress = { build.progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }

        // Action Buttons Row (Cancel, Retry, Download)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (build.status == BuildStatus.COMPILING || build.status == BuildStatus.PACKAGING || build.status == BuildStatus.QUEUED) {
            OutlinedButton(
              onClick = { onCancelBuild(build.id) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("cancel_build_button"),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (isArabic) "إلغاء البناء" else "Cancel Build")
            }
          } else if (build.status == BuildStatus.COMPLETED || build.status == BuildStatus.WARNING) {
            Button(
              onClick = { onDownloadArtifact(build) },
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("download_artifact_button"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
              )
            ) {
              Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isArabic) "تحميل ${build.fileName} (${build.fileSize})" else "Download ${build.fileName}",
                fontWeight = FontWeight.Bold
              )
            }
          } else {
            Button(
              onClick = { onRetryBuild(build.id) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("retry_build_button"),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (isArabic) "إعادة المحاولة" else "Retry Build")
            }
          }
        }
      }
    }

    // 2. Real-Time Terminal Logs Viewer
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = DarkTerminalBg
      ),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Terminal Window Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF161B22))
            .padding(horizontal = 14.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF27C93F)))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "OmniBuild-Terminal (x64 MSVC Clang)",
              style = MaterialTheme.typography.labelSmall,
              fontFamily = RobotoMonoFontFamily,
              color = Color(0xFF8B949E)
            )
          }

          Text(
            text = "${build.logs.size} lines",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = RobotoMonoFontFamily,
            color = Color(0xFF8B949E)
          )
        }

        HorizontalDivider(color = Color(0xFF30363D))

        // Logs Stream
        LazyColumn(
          state = listState,
          modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(build.logs) { log ->
            val textColor = when (log.level) {
              LogLevel.CMD -> TealAccent
              LogLevel.SUCCESS -> EmeraldBright
              LogLevel.WARN -> AmberWarning
              LogLevel.ERROR -> CrimsonError
              LogLevel.INFO -> Color(0xFFC9D1D9)
            }

            val prefix = when (log.level) {
              LogLevel.CMD -> "$"
              LogLevel.SUCCESS -> "✔"
              LogLevel.WARN -> "⚠"
              LogLevel.ERROR -> "✖"
              LogLevel.INFO -> "ℹ"
            }

            Row(verticalAlignment = Alignment.Top) {
              Text(
                text = log.timestamp,
                fontFamily = RobotoMonoFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6E7681),
                modifier = Modifier.width(64.dp)
              )
              Text(
                text = prefix,
                fontFamily = RobotoMonoFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                modifier = Modifier.width(20.dp),
                fontWeight = FontWeight.Bold
              )
              Text(
                text = log.message,
                fontFamily = RobotoMonoFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }
  }
}
