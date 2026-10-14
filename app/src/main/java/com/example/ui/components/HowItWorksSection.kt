package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TealAccent

data class WorkflowStep(
  val stepNumber: Int,
  val titleAr: String,
  val titleEn: String,
  val descAr: String,
  val descEn: String,
  val icon: ImageVector,
  val tipAr: String,
  val tipEn: String,
  val techAr: String,
  val techEn: String
)

@Composable
fun HowItWorksSection(
  isArabic: Boolean,
  onGoToBuilder: () -> Unit,
  modifier: Modifier = Modifier
) {
  val steps = listOf(
    WorkflowStep(
      stepNumber = 1,
      titleAr = "1. أضف مشروعك (رابط موقع أو ملف مضغوط ZIP)",
      titleEn = "1. Add your project (URL or ZIP)",
      descAr = "اختر بين إدخال رابط موقع ويب حي يعمل بتقنيات الويب الحديثة (PWA, React, Vue, Next.js) أو ارفع ملف مضغوط ZIP يحتوي على ملفات HTML و CSS و JavaScript.",
      descEn = "Choose between entering a live web URL or uploading an HTML/ZIP bundle containing index.html and all required assets.",
      icon = Icons.Default.FolderZip,
      tipAr = "نصيحة: تأكد من أن الموقع يدعم بروتوكول HTTPS إذا كنت تبني تطبيق ويب مباشر.",
      tipEn = "Tip: Ensure your live site supports HTTPS for secure mobile and desktop packaging.",
      techAr = "يدعم: HTML5, PWA, Webpack, Vite, React, Vue, Svelte",
      techEn = "Supports: HTML5, PWA, React, Vue, Vite, Static Bundles"
    ),
    WorkflowStep(
      stepNumber = 2,
      titleAr = "2. اضبط الإعدادات والمنصة (أندرويد أو ويندوز .exe)",
      titleEn = "2. Adjust options (App name, icon, platform target like Android or Windows .exe)",
      descAr = "حدد اسم التطبيق، والمنصة المستهدفة. في حال اختيار ويندوز (.exe)، يمكنك تخصيص أبعاد النافذة (1280x720)، تفعيل دعم شريط المهام (Tray Icon)، توليد أيقونات .ico متعددة الدقات، واختيار محرك الغلاف (Tauri أو Electron).",
      descEn = "Set your app name and select your target platform. For Windows (.exe), configure window size, system tray minimize support, multi-resolution .ico icon generation, and runtime engine (Tauri/Electron).",
      icon = Icons.Default.Tune,
      tipAr = "نصيحة: محرك Tauri يولد ملفاً تنفيذياً خفيفاً للغاية (~3 ميجابايت) مقارنة بـ Electron (~65 ميجابايت).",
      tipEn = "Tip: Tauri wrapper yields an ultra-compact binary (~3.2 MB) with minimal RAM footprint.",
      techAr = "تخصيص: أبعاد النافذة • Tray Hook • ملفات ICO • محرك Tauri/Rust",
      techEn = "Options: Window Size • System Tray • ICO Multi-Res • Tauri Engine"
    ),
    WorkflowStep(
      stepNumber = 3,
      titleAr = "3. ابدأ البناء وتابع التقدم الفوري",
      titleEn = "3. Build and track live progress",
      descAr = "انقر على 'متابعة' لبدء خط أنابيب التجميع الآلي. راقب شاشة الأوامر الحية ومراحل بناء الحزمة (تجهيز الغلاف، توليد الأيقونة، تجميع الكود الثنائي x64 أو تحزيم APK، والتوقيع الرقمي).",
      descEn = "Click Continue to run the automated compilation pipeline. Follow live terminal stdout logs and stage milestones as binary compilation, resource embedding, and digital signing take place.",
      icon = Icons.Default.Build,
      tipAr = "نصيحة: يتم فحص الحصة اليومية والشهادات الرقمية تلقائياً مع تنبيهات فورية بأي تحذيرات.",
      tipEn = "Tip: Quota checks and signing certificate warnings are monitored in real-time.",
      techAr = "المحاكي: سطر أوامر تفاعلي • مراقبة تقدم 0% - 100% • إشعارات فورية",
      techEn = "Pipeline: Interactive Console • 0%-100% Progress • Real-time Alerts"
    ),
    WorkflowStep(
      stepNumber = 4,
      titleAr = "4. حمّل ملف APK أو EXE من سجل البناء",
      titleEn = "4. Download the generated APK or EXE file from history",
      descAr = "بمجرد اكتمال البناء، يصبح الملف التنفيذي (.exe لويندوز أو .apk لأندرويد) جاهزاً للتحميل مباشرة مع توفير حجم الملف، تجزئة الأمان SHA-256، وأوامر التشغيل السريعة.",
      descEn = "Once completed, your standalone executable (.exe for Windows or .apk for Android) is ready to download from the History tab with SHA-256 checksums and deployment details.",
      icon = Icons.Default.CloudDownload,
      tipAr = "نصيحة: يمكنك العودة لسجل البناء في أي وقت لإعادة تحميل أي حزمة أو مشاركتها.",
      tipEn = "Tip: All generated builds are saved to history for quick re-download and verification.",
      techAr = "المخرجات: ملف .exe مستقل لويندوز • حزمة APK للأندرويد • ملف ZIP لفلاتر",
      techEn = "Artifacts: Standalone .exe • Universal APK • Flutter Source ZIP"
    )
  )

  var selectedStepIndex by remember { mutableStateOf(0) }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Hero Guide
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isArabic) "كيف يعمل صانع التطبيقات؟" else "How It Works: 4 Easy Steps",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isArabic) "دليل إرشادي لبناء تطبيقات الجوال وسطح المكتب بنقرة واحدة" else "Step-by-step workflow for mobile & desktop output",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // Step Cards
    items(steps.size) { index ->
      val step = steps[index]
      val isSelected = selectedStepIndex == index

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { selectedStepIndex = index }
          .testTag("step_card_${step.stepNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceContainer
          else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
          width = if (isSelected) 1.5.dp else 1.dp,
          color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${step.stepNumber}",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Text(
                text = if (isArabic) step.titleAr else step.titleEn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )
            }

            Icon(
              imageVector = step.icon,
              contentDescription = null,
              tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(24.dp)
            )
          }

          Text(
            text = if (isArabic) step.descAr else step.descEn,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          // Tech badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = if (isArabic) step.techAr else step.techEn,
              style = MaterialTheme.typography.labelSmall,
              color = EmeraldGlow,
              fontWeight = FontWeight.SemiBold
            )
          }

          // Tip Box
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(EmeraldPrimary.copy(alpha = 0.12f))
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = EmeraldLight,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) step.tipAr else step.tipEn,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // Call to action button at bottom of How It Works
    item {
      Button(
        onClick = onGoToBuilder,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("start_first_build_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = Color.White
        )
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isArabic) "ابدأ أول عملية بناء الآن" else "Start Your First Build Now",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
