package com.example.model

enum class Platform(
  val id: String,
  val titleAr: String,
  val titleEn: String,
  val extension: String,
  val isDesktop: Boolean,
  val badgeAr: String,
  val badgeEn: String
) {
  ANDROID(
    id = "android",
    titleAr = "أندرويد (APK / AAB)",
    titleEn = "Android (APK / AAB)",
    extension = ".apk",
    isDesktop = false,
    badgeAr = "جوال",
    badgeEn = "Mobile"
  ),
  WINDOWS(
    id = "windows",
    titleAr = "ويندوز (.exe)",
    titleEn = "Windows (.exe)",
    extension = ".exe",
    isDesktop = true,
    badgeAr = "دعم سطح المكتب",
    badgeEn = "Desktop Support"
  ),
  FLUTTER(
    id = "flutter",
    titleAr = "فلاتر (ZIP)",
    titleEn = "Flutter (ZIP)",
    extension = ".zip",
    isDesktop = false,
    badgeAr = "متعدد المنصات",
    badgeEn = "Cross-Platform"
  )
}

enum class SourceType(val titleAr: String, val titleEn: String) {
  WEBSITE("موقع ويب", "Website URL"),
  UPLOAD("رفع", "Upload Project / ZIP")
}

enum class WindowsEngine(
  val title: String,
  val description: String,
  val typicalSize: String
) {
  TAURI("Tauri v2 (Rust + WebView2)", "الأخف وزناً وسرعة فائقة في استهلاك الذاكرة", "~3.2 MB"),
  ELECTRON("Electron 30 (Node.js + Chromium)", "دعم شامل لجميع ميزات متصفح كروميوم ومكتبات npm", "~64.5 MB"),
  PYTHON("PyInstaller (Python + WebView)", "بيئة بايثون متكاملة مدمجة مع واجهة ويب", "~18.4 MB")
}

data class WindowsConfig(
  val windowWidth: Int = 1280,
  val windowHeight: Int = 720,
  val isResizable: Boolean = true,
  val hasSystemTray: Boolean = true,
  val iconFormat: String = ".ico (Multi-size 256x256)",
  val engine: WindowsEngine = WindowsEngine.TAURI,
  val singleExecutable: Boolean = true,
  val startMaximized: Boolean = false,
  val frameless: Boolean = false
)

data class AndroidConfig(
  val outputType: String = "APK (Universal)",
  val packageName: String = "com.omnibuild.app",
  val minSdk: Int = 24,
  val enableHardwareAcceleration: Boolean = true
)

enum class BuildStatus(val labelAr: String, val labelEn: String) {
  QUEUED("في الانتظار", "Queued"),
  COMPILING("جاري التجميع", "Compiling"),
  PACKAGING("جاري الحزم", "Packaging"),
  COMPLETED("اكتمل بنجاح", "Completed"),
  FAILED("فشل البناء", "Failed"),
  WARNING("مكتمل مع تحذيرات", "Warning")
}

data class LogEntry(
  val timestamp: String,
  val level: LogLevel,
  val message: String
)

enum class LogLevel {
  INFO, SUCCESS, WARN, ERROR, CMD
}

data class BuildItem(
  val id: String,
  val appName: String,
  val platform: Platform,
  val sourceType: SourceType,
  val sourceValue: String,
  val status: BuildStatus,
  val progress: Float,
  val currentStep: String,
  val fileSize: String,
  val fileName: String,
  val checksumSha256: String,
  val createdAt: Long,
  val logs: List<LogEntry> = emptyList(),
  val windowsConfig: WindowsConfig? = null,
  val androidConfig: AndroidConfig? = null
)

data class BuildNotification(
  val id: String,
  val title: String,
  val message: String,
  val type: NotificationType,
  val timestamp: Long,
  val isRead: Boolean = false
)

enum class NotificationType {
  SUCCESS, WARNING, ERROR, INFO
}
