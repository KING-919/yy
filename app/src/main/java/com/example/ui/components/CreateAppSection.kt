package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AndroidConfig
import com.example.model.Platform
import com.example.model.SourceType
import com.example.model.WindowsConfig
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldContainerDark
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TealAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateAppSection(
  isArabic: Boolean,
  sourceType: SourceType,
  appName: String,
  selectedPlatform: Platform,
  websiteUrl: String,
  uploadedFileName: String,
  windowsConfig: WindowsConfig,
  androidConfig: AndroidConfig,
  simulateWarning: Boolean,
  onSourceTypeChange: (SourceType) -> Unit,
  onAppNameChange: (String) -> Unit,
  onPlatformChange: (Platform) -> Unit,
  onWebsiteUrlChange: (String) -> Unit,
  onUploadedFileNameChange: (String) -> Unit,
  onWindowsConfigChange: ((WindowsConfig) -> WindowsConfig) -> Unit,
  onAndroidConfigChange: ((AndroidConfig) -> AndroidConfig) -> Unit,
  onToggleSimulateWarning: () -> Unit,
  onSubmitBuild: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isPlatformDropdownOpen by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
        Image(
          painter = painterResource(id = R.drawable.builder_hero_banner_1791995134265),
          contentDescription = "Builder Hero Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.matchParentSize()
        )
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0xDD121212))
              )
            )
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(EmeraldPrimary)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = if (isArabic) "محرك البناء الشامل" else "Universal Compiler Engine",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isArabic) "حوّل أي موقع ويب أو مشروع HTML5 إلى تطبيق تنفيذي أو جوال" else "Turn Any Web App or Code into Native Windows & Android Apps",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }

    // 1. Top Toggle Tabs: "موقع ويب" and "رفع"
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
      )
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = if (isArabic) "طريقة إدخال المشروع:" else "Project Source Mode:",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Custom Sleek Segmented Switch for tabs
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
        ) {
          // Tab 1: Website URL ("موقع ويب")
          val isWeb = sourceType == SourceType.WEBSITE
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isWeb) MaterialTheme.colorScheme.primary else Color.Transparent
              )
              .clickable { onSourceTypeChange(SourceType.WEBSITE) }
              .padding(vertical = 10.dp)
              .testTag("tab_website"),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = if (isWeb) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isArabic) SourceType.WEBSITE.titleAr else SourceType.WEBSITE.titleEn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isWeb) FontWeight.Bold else FontWeight.Medium,
                color = if (isWeb) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Tab 2: Upload ("رفع")
          val isUpload = sourceType == SourceType.UPLOAD
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isUpload) MaterialTheme.colorScheme.primary else Color.Transparent
              )
              .clickable { onSourceTypeChange(SourceType.UPLOAD) }
              .padding(vertical = 10.dp)
              .testTag("tab_upload"),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = if (isUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isArabic) SourceType.UPLOAD.titleAr else SourceType.UPLOAD.titleEn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isUpload) FontWeight.Bold else FontWeight.Medium,
                color = if (isUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // 2. Input Fields: App Name ("اسم التطبيق")
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // App Name
        Column {
          Text(
            text = if (isArabic) "اسم التطبيق *" else "App Name *",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = appName,
            onValueChange = onAppNameChange,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("app_name_input"),
            placeholder = {
              Text(if (isArabic) "مثال: منصة التجارة الذكية" else "e.g. Smart Retail Hub")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
          )
        }

        // 3. Platform Selector Dropdown ("المنصة" / What to build)
        Column {
          Text(
            text = if (isArabic) "المنصة المستهدفة (Target Platform) *" else "Target Platform *",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))

          // Platform Selector Display & Dropdown trigger
          Box(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { isPlatformDropdownOpen = true }
                .padding(14.dp)
                .testTag("platform_selector_dropdown"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = when (selectedPlatform) {
                    Platform.ANDROID -> Icons.Default.Android
                    Platform.WINDOWS -> Icons.Default.Computer
                    Platform.FLUTTER -> Icons.Default.Code
                  },
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = if (isArabic) selectedPlatform.titleAr else selectedPlatform.titleEn,
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )

                    // Small subtle tag or badge indicating desktop support for Windows!
                    if (selectedPlatform == Platform.WINDOWS) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(TealAccent.copy(alpha = 0.2f))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = if (isArabic) "دعم سطح المكتب" else "Desktop Support",
                          style = MaterialTheme.typography.labelSmall,
                          color = TealAccent,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }

                  Text(
                    text = if (selectedPlatform == Platform.WINDOWS) {
                      if (isArabic) "توليد ملف تنفيذي .exe مع غلاف خفيف" else "Generates standalone .exe binary with native wrapper"
                    } else if (selectedPlatform == Platform.ANDROID) {
                      if (isArabic) "توليد ملف APK جاهز للتثبيت على الهواتف والأجهزة اللوحية" else "Generates installable APK/AAB for Android devices"
                    } else {
                      if (isArabic) "توليد حزمة مشروع كود فلاتر متكامل مع دعم متعدد المنصات" else "Full Dart & Flutter codebase with cross-platform bindings"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            DropdownMenu(
              expanded = isPlatformDropdownOpen,
              onDismissRequest = { isPlatformDropdownOpen = false },
              modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            ) {
              Platform.entries.forEach { platform ->
                DropdownMenuItem(
                  text = {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                          imageVector = when (platform) {
                            Platform.ANDROID -> Icons.Default.Android
                            Platform.WINDOWS -> Icons.Default.Computer
                            Platform.FLUTTER -> Icons.Default.Code
                          },
                          contentDescription = null,
                          tint = if (selectedPlatform == platform) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                          modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                          text = if (isArabic) platform.titleAr else platform.titleEn,
                          fontWeight = if (selectedPlatform == platform) FontWeight.Bold else FontWeight.Normal,
                          color = MaterialTheme.colorScheme.onSurface
                        )
                      }

                      if (platform == Platform.WINDOWS) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TealAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                          Text(
                            text = if (isArabic) platform.badgeAr else platform.badgeEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = TealAccent,
                            fontWeight = FontWeight.Bold
                          )
                        }
                      }
                    }
                  },
                  onClick = {
                    onPlatformChange(platform)
                    isPlatformDropdownOpen = false
                  }
                )
              }
            }
          }
        }

        // 4. Dynamic Input Field Based On Selected Source
        AnimatedContent(
          targetState = sourceType,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "DynamicSourceInput"
        ) { currentSource ->
          if (currentSource == SourceType.WEBSITE) {
            // URL Input for Websites
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = if (isArabic) "رابط الموقع الإلكتروني (URL) *" else "Website URL *",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              OutlinedTextField(
                value = websiteUrl,
                onValueChange = onWebsiteUrlChange,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("website_url_input"),
                leadingIcon = {
                  Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                  )
                },
                trailingIcon = {
                  if (websiteUrl.startsWith("https://")) {
                    Icon(
                      imageVector = Icons.Default.Security,
                      contentDescription = "SSL Verified",
                      tint = EmeraldBright
                    )
                  }
                },
                placeholder = {
                  Text("https://example.com")
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = MaterialTheme.colorScheme.primary,
                  unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
              )

              // Quick fill chips
              Text(
                text = if (isArabic) "نماذج سريعة للتجربة:" else "Quick demo presets:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf(
                  "https://cryptowatch-demo.io" to "Crypto Tracker",
                  "https://my-dashboard.io" to "Admin Dashboard",
                  "https://freshmart-store.app" to "E-Commerce PWA"
                ).forEach { (url, label) ->
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(MaterialTheme.colorScheme.surfaceVariant)
                      .clickable { onWebsiteUrlChange(url) }
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = label,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                }
              }
            }
          } else {
            // File Picker for HTML/ZIP Files ("رفع")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(
                text = if (isArabic) "ملف المشروع (HTML / ZIP / Web Assets) *" else "Project Files (HTML / ZIP / Assets) *",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                  )
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .clickable {
                    // Quick simulated file select
                    onUploadedFileNameChange("web_app_dist_v2.zip")
                  }
                  .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
              ) {
                Column(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(46.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.FolderZip,
                      contentDescription = "Upload ZIP",
                      tint = MaterialTheme.colorScheme.onPrimaryContainer,
                      modifier = Modifier.size(24.dp)
                    )
                  }

                  Text(
                    text = uploadedFileName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )

                  Text(
                    text = if (isArabic) "الحجم: 14.8 ميجابايت • تم التحقق من وجود index.html" else "Size: 14.8 MB • index.html verified",
                    style = MaterialTheme.typography.bodySmall,
                    color = EmeraldGlow
                  )

                  OutlinedButton(
                    onClick = {
                      onUploadedFileNameChange("custom_react_build.zip")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("file_picker_button")
                  ) {
                    Icon(
                      imageVector = Icons.Default.AttachFile,
                      contentDescription = null,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isArabic) "اختر ملفاً آخر (Browse)" else "Browse Different File")
                  }
                }
              }
            }
          }
        }
      }
    }

    // 5. Windows (.exe) Specific Configuration Card
    AnimatedVisibility(visible = selectedPlatform == Platform.WINDOWS) {
      WindowsConfigCard(
        config = windowsConfig,
        isArabic = isArabic,
        onConfigChange = onWindowsConfigChange
      )
    }

    // 6. Android Specific Options (when Android selected)
    AnimatedVisibility(visible = selectedPlatform == Platform.ANDROID) {
      Card(
        modifier = Modifier.fillMaxWidth(),
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Android,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isArabic) "خيارات حزمة أندرويد:" else "Android Package Settings:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
          }

          OutlinedTextField(
            value = androidConfig.packageName,
            onValueChange = { pkg -> onAndroidConfigChange { it.copy(packageName = pkg) } },
            label = { Text(if (isArabic) "معرّف الحزمة (Package Name)" else "Package Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("APK (Universal)", "AAB (Play Store)").forEach { type ->
              val isSel = androidConfig.outputType == type
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                  .clickable { onAndroidConfigChange { it.copy(outputType = type) } }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = type,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    // Build Simulator Options / Warning toggle
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        .clickable { onToggleSimulateWarning() }
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = if (isArabic) "محاكاة تحذير أثناء البناء (اختبار إدارة الأخطاء)" else "Simulate build warning (test error handling)",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(if (simulateWarning) EmeraldPrimary else MaterialTheme.colorScheme.surface)
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Text(
          text = if (simulateWarning) "ON" else "OFF",
          style = MaterialTheme.typography.labelSmall,
          color = if (simulateWarning) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // 7. Primary Action Button: "متابعة" (Continue / Create)
    Button(
      onClick = onSubmitBuild,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("continue_button"),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
      ),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.RocketLaunch,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isArabic) "متابعة وبدء البناء" else "Continue & Build",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}
