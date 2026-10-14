package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary

@Composable
fun AppTopBar(
  isDarkMode: Boolean,
  isArabic: Boolean,
  unreadNotificationsCount: Int,
  dailyQuotaUsed: Int,
  dailyQuotaTotal: Int,
  onToggleDarkMode: () -> Unit,
  onToggleLanguage: () -> Unit,
  onOpenNotifications: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // App Logo & Title
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { /* Brand click */ }
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(EmeraldPrimary, EmeraldGlow)
                )
              )
              .padding(2.dp),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.app_launcher_icon_1791995002770),
              contentDescription = "OmniBuild Logo",
              modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "OmniBuild",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "v2.5",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = if (isArabic) "صانع تطبيقات الويب وسطح المكتب" else "Multi-Platform App & Desktop Builder",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Action Icons
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Language Switcher
          IconButton(
            onClick = onToggleLanguage,
            modifier = Modifier
              .size(38.dp)
              .testTag("language_toggle_button")
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 6.dp, vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (isArabic) "EN" else "عربي",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          // Dark Mode Toggle
          IconButton(
            onClick = onToggleDarkMode,
            modifier = Modifier
              .size(38.dp)
              .testTag("theme_toggle_button")
          ) {
            Icon(
              imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
              contentDescription = if (isDarkMode) "Light Mode" else "Dark Mode",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }

          // Notifications Bell
          IconButton(
            onClick = onOpenNotifications,
            modifier = Modifier
              .size(38.dp)
              .testTag("notifications_button")
          ) {
            BadgedBox(
              badge = {
                if (unreadNotificationsCount > 0) {
                  Badge(
                    containerColor = EmeraldLight,
                    contentColor = Color.Black
                  ) {
                    Text(
                      text = unreadNotificationsCount.toString(),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // Quota pill
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(EmeraldLight)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isArabic) "خادم التجميع متصل (Docker + MSVC x64)" else "Build Cluster Online (MSVC x64 & AAPT2)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = if (isArabic) "الحصة اليومية: $dailyQuotaUsed/$dailyQuotaTotal" else "Quota: $dailyQuotaUsed/$dailyQuotaTotal",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
