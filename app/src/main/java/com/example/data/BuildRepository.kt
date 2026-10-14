package com.example.data

import com.example.model.AndroidConfig
import com.example.model.BuildItem
import com.example.model.BuildNotification
import com.example.model.BuildStatus
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.model.NotificationType
import com.example.model.Platform
import com.example.model.SourceType
import com.example.model.WindowsConfig
import com.example.model.WindowsEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BuildRepository(private val coroutineScope: CoroutineScope) {

  private val _builds = MutableStateFlow<List<BuildItem>>(emptyList())
  val builds: StateFlow<List<BuildItem>> = _builds.asStateFlow()

  private val _activeBuildId = MutableStateFlow<String?>(null)
  val activeBuildId: StateFlow<String?> = _activeBuildId.asStateFlow()

  private val _notifications = MutableStateFlow<List<BuildNotification>>(emptyList())
  val notifications: StateFlow<List<BuildNotification>> = _notifications.asStateFlow()

  private val _dailyQuotaUsed = MutableStateFlow(3)
  val dailyQuotaUsed: StateFlow<Int> = _dailyQuotaUsed.asStateFlow()
  val dailyQuotaTotal = 25

  private var activeBuildJob: Job? = null

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    val initialBuilds = listOf(
      BuildItem(
        id = "bld-001",
        appName = "CryptoWatch Desktop",
        platform = Platform.WINDOWS,
        sourceType = SourceType.WEBSITE,
        sourceValue = "https://cryptowatch-demo.io",
        status = BuildStatus.COMPLETED,
        progress = 1.0f,
        currentStep = "تم حزم الملف التنفيذي بنجاح",
        fileSize = "4.2 MB",
        fileName = "CryptoWatch-Setup-x64.exe",
        checksumSha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
        createdAt = System.currentTimeMillis() - 7200000,
        windowsConfig = WindowsConfig(
          windowWidth = 1280,
          windowHeight = 720,
          isResizable = true,
          hasSystemTray = true,
          engine = WindowsEngine.TAURI
        ),
        logs = listOf(
          LogEntry("14:20:01", LogLevel.INFO, "Starting Windows compilation pipeline for 'CryptoWatch'"),
          LogEntry("14:20:03", LogLevel.CMD, "cargo tauri build --target x86_64-pc-windows-msvc"),
          LogEntry("14:20:07", LogLevel.INFO, "Compiling Rust WebView2 runner & Tray icon hook"),
          LogEntry("14:20:12", LogLevel.WARN, "High DPI scaling enabled for 4K displays automatically"),
          LogEntry("14:20:18", LogLevel.SUCCESS, "Embedded application icon: icon.ico (256x256)"),
          LogEntry("14:20:25", LogLevel.SUCCESS, "Binary generated: target/release/CryptoWatch-Setup-x64.exe (4.2 MB)")
        )
      ),
      BuildItem(
        id = "bld-002",
        appName = "FreshMart Express",
        platform = Platform.ANDROID,
        sourceType = SourceType.UPLOAD,
        sourceValue = "freshmart_pwa_bundle.zip",
        status = BuildStatus.COMPLETED,
        progress = 1.0f,
        currentStep = "تم توقيع ملف APK بنجاح",
        fileSize = "16.8 MB",
        fileName = "FreshMart-v1.0.0-universal.apk",
        checksumSha256 = "8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4",
        createdAt = System.currentTimeMillis() - 18000000,
        androidConfig = AndroidConfig(
          outputType = "APK (Universal)",
          packageName = "com.freshmart.express"
        ),
        logs = listOf(
          LogEntry("11:15:10", LogLevel.INFO, "Unpacking HTML5 bundle from freshmart_pwa_bundle.zip"),
          LogEntry("11:15:12", LogLevel.INFO, "Generating AndroidManifest.xml and hardware acceleration config"),
          LogEntry("11:15:16", LogLevel.CMD, "./gradlew assembleRelease --no-daemon"),
          LogEntry("11:15:22", LogLevel.SUCCESS, "Signed with production keystore -> FreshMart-v1.0.0-universal.apk")
        )
      )
    )
    _builds.value = initialBuilds

    _notifications.value = listOf(
      BuildNotification(
        id = "notif-1",
        title = "اكتمل بناء ويندوز (.exe)",
        message = "تم إنشاء ملف CryptoWatch-Setup-x64.exe بنجاح وجاهز للتنزيل.",
        type = NotificationType.SUCCESS,
        timestamp = System.currentTimeMillis() - 7200000,
        isRead = false
      ),
      BuildNotification(
        id = "notif-2",
        title = "تنبيه الحصة اليومية",
        message = "تم استهلاك 3 من أصل 25 عملية بناء مجانية لهذا اليوم.",
        type = NotificationType.INFO,
        timestamp = System.currentTimeMillis() - 14400000,
        isRead = true
      )
    )
  }

  fun startBuild(
    appName: String,
    platform: Platform,
    sourceType: SourceType,
    sourceValue: String,
    windowsConfig: WindowsConfig? = null,
    androidConfig: AndroidConfig? = null,
    forceWarning: Boolean = false
  ): String {
    val buildId = "bld-" + UUID.randomUUID().toString().take(6)
    val sanitizedName = appName.replace(" ", "_")
    val extension = platform.extension
    val fileName = when (platform) {
      Platform.WINDOWS -> "$sanitizedName-x64$extension"
      Platform.ANDROID -> "$sanitizedName-release$extension"
      Platform.FLUTTER -> "$sanitizedName-project$extension"
    }

    val newBuild = BuildItem(
      id = buildId,
      appName = appName,
      platform = platform,
      sourceType = sourceType,
      sourceValue = sourceValue,
      status = BuildStatus.QUEUED,
      progress = 0.05f,
      currentStep = "تهيئة بيئة التجميع والحزم...",
      fileSize = when (platform) {
        Platform.WINDOWS -> if (windowsConfig?.engine == WindowsEngine.ELECTRON) "64.8 MB" else "4.4 MB"
        Platform.ANDROID -> "15.2 MB"
        Platform.FLUTTER -> "8.6 MB"
      },
      fileName = fileName,
      checksumSha256 = generateRandomChecksum(),
      createdAt = System.currentTimeMillis(),
      logs = listOf(
        LogEntry(currentTimeStr(), LogLevel.INFO, "Build requested for '$appName' [$platform]")
      ),
      windowsConfig = windowsConfig,
      androidConfig = androidConfig
    )

    _builds.update { listOf(newBuild) + it }
    _activeBuildId.value = buildId
    _dailyQuotaUsed.update { (it + 1).coerceAtMost(dailyQuotaTotal) }

    executePipeline(buildId, forceWarning)
    return buildId
  }

  private fun executePipeline(buildId: String, forceWarning: Boolean) {
    activeBuildJob?.cancel()
    activeBuildJob = coroutineScope.launch(Dispatchers.Default) {
      val build = _builds.value.firstOrNull { it.id == buildId } ?: return@launch

      updateBuild(buildId) {
        it.copy(
          status = BuildStatus.COMPILING,
          progress = 0.15f,
          currentStep = "فحص المصدر والتحقق من التكوين..."
        )
      }
      appendLog(buildId, LogLevel.INFO, "Validating source: ${build.sourceValue} (${build.sourceType.name})")
      delay(1100)

      if (build.platform == Platform.WINDOWS) {
        val winCfg = build.windowsConfig ?: WindowsConfig()
        updateBuild(buildId) {
          it.copy(
            progress = 0.32f,
            currentStep = "تجهيز غلاف سطح المكتب (${winCfg.engine.title})..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "init_desktop_wrapper --engine=${winCfg.engine.name} --width=${winCfg.windowWidth} --height=${winCfg.windowHeight}")
        delay(1200)

        appendLog(buildId, LogLevel.INFO, "Generating multi-resolution Windows ICO icon (16x16 -> 256x256)")
        if (winCfg.hasSystemTray) {
          appendLog(buildId, LogLevel.SUCCESS, "System Tray hook injected: minimize-to-tray & context menu enabled")
        }
        delay(1000)

        updateBuild(buildId) {
          it.copy(
            status = BuildStatus.PACKAGING,
            progress = 0.65f,
            currentStep = "تجميع الكود الثنائي لنظام ويندوز x64..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "cargo build --release --target x86_64-pc-windows-msvc")
        delay(1400)

        if (forceWarning) {
          appendLog(buildId, LogLevel.WARN, "تحذير: شهادة التوقيع الرقمي ذاتية التوقيع (Self-Signed Code Signing). يُنصح بإضافة شهادة EV معتمدة للمتاجر الرسمية.")
          postNotification(
            title = "تحذير أثناء تجميع ${build.appName}",
            message = "تم البناء بنجاح ولكن باستخدام شهادة رقمية تجريبية.",
            type = NotificationType.WARNING
          )
        } else {
          appendLog(buildId, LogLevel.INFO, "Embedding Windows PE manifest, DPI-aware level: PerMonitorV2")
        }
        delay(1100)

        updateBuild(buildId) {
          it.copy(
            progress = 0.90f,
            currentStep = "حزم الملف التنفيذي المحمول وتطبيق الضغط..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "makensis -DPRODUCT_NAME=\"${build.appName}\" -DEXE_NAME=\"${build.fileName}\" installer.nsi")
        delay(1000)

      } else if (build.platform == Platform.ANDROID) {
        updateBuild(buildId) {
          it.copy(
            progress = 0.35f,
            currentStep = "إنشاء هيكل مشروع أندرويد و AndroidManifest..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "aapt2 compile --dir res/ -o compiled_res.zip")
        delay(1200)

        updateBuild(buildId) {
          it.copy(
            status = BuildStatus.PACKAGING,
            progress = 0.70f,
            currentStep = "توليد ملفات DEX وتوقيع ملف الحزمة..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "d8 --release --min-api 24 --output . classes.jar")
        delay(1300)

        appendLog(buildId, LogLevel.SUCCESS, "Zipalign 4-byte boundary alignment completed")
        appendLog(buildId, LogLevel.SUCCESS, "Signed with V2/V3 APK Signature Scheme")
        delay(1000)

      } else { // Flutter
        updateBuild(buildId) {
          it.copy(
            progress = 0.40f,
            currentStep = "توليد كود دارت ومشروع فلاتر متعدد المنصات..."
          )
        }
        appendLog(buildId, LogLevel.CMD, "flutter create --platforms=android,windows,web ${build.appName}")
        delay(1400)

        updateBuild(buildId) {
          it.copy(
            status = BuildStatus.PACKAGING,
            progress = 0.75f,
            currentStep = "حزم الكود المصدري في ملف ZIP..."
          )
        }
        appendLog(buildId, LogLevel.INFO, "Compressing multi-platform workspace into zip package")
        delay(1200)
      }

      // Final step
      val finalStatus = if (forceWarning) BuildStatus.WARNING else BuildStatus.COMPLETED
      updateBuild(buildId) {
        it.copy(
          status = finalStatus,
          progress = 1.0f,
          currentStep = if (forceWarning) "اكتمل البناء مع وجود تحذيرات" else "اكتمل البناء بنجاح! جاهز للتنزيل"
        )
      }

      appendLog(buildId, LogLevel.SUCCESS, "Build pipeline completed successfully! Output: ${build.fileName} (${build.fileSize})")
      appendLog(buildId, LogLevel.INFO, "SHA-256: ${build.checksumSha256}")

      postNotification(
        title = "اكتمل البناء: ${build.appName}",
        message = "ملف ${build.fileName} (${build.fileSize}) لمنصة ${build.platform.titleAr} جاهز للاستخدام.",
        type = if (forceWarning) NotificationType.WARNING else NotificationType.SUCCESS
      )
    }
  }

  fun cancelActiveBuild(buildId: String) {
    activeBuildJob?.cancel()
    updateBuild(buildId) {
      it.copy(
        status = BuildStatus.FAILED,
        progress = 1.0f,
        currentStep = "تم إلغاء العملية بواسطة المستخدم"
      )
    }
    appendLog(buildId, LogLevel.ERROR, "Build cancelled by user.")
    postNotification(
      title = "تم إلغاء البناء",
      message = "تم إيقاف عملية البناء بنجاح.",
      type = NotificationType.INFO
    )
  }

  fun retryBuild(buildId: String) {
    val build = _builds.value.firstOrNull { it.id == buildId } ?: return
    startBuild(
      appName = build.appName,
      platform = build.platform,
      sourceType = build.sourceType,
      sourceValue = build.sourceValue,
      windowsConfig = build.windowsConfig,
      androidConfig = build.androidConfig
    )
  }

  fun deleteBuild(buildId: String) {
    _builds.update { list -> list.filter { it.id != buildId } }
    if (_activeBuildId.value == buildId) {
      _activeBuildId.value = null
    }
  }

  fun markAllNotificationsRead() {
    _notifications.update { list -> list.map { it.copy(isRead = true) } }
  }

  fun clearNotifications() {
    _notifications.value = emptyList()
  }

  private fun updateBuild(buildId: String, transform: (BuildItem) -> BuildItem) {
    _builds.update { list ->
      list.map { if (it.id == buildId) transform(it) else it }
    }
  }

  private fun appendLog(buildId: String, level: LogLevel, message: String) {
    val entry = LogEntry(currentTimeStr(), level, message)
    _builds.update { list ->
      list.map {
        if (it.id == buildId) it.copy(logs = it.logs + entry) else it
      }
    }
  }

  private fun postNotification(title: String, message: String, type: NotificationType) {
    val notif = BuildNotification(
      id = "notif-" + UUID.randomUUID().toString().take(6),
      title = title,
      message = message,
      type = type,
      timestamp = System.currentTimeMillis(),
      isRead = false
    )
    _notifications.update { listOf(notif) + it }
  }

  private fun currentTimeStr(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
  }

  private fun generateRandomChecksum(): String {
    val chars = "0123456789abcdef"
    return (1..64).map { chars.random() }.joinToString("")
  }
}
