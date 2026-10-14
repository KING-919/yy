package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BuildRepository
import com.example.model.AndroidConfig
import com.example.model.BuildItem
import com.example.model.BuildNotification
import com.example.model.Platform
import com.example.model.SourceType
import com.example.model.WindowsConfig
import com.example.model.WindowsEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class NavigationTab(val titleAr: String, val titleEn: String) {
  BUILDER("إنشاء تطبيق", "Builder"),
  HISTORY("سجل البناء", "History"),
  HOW_IT_WORKS("كيف يعمل", "How It Works"),
  CONSOLE("المحاكي المباشر", "Live Console")
}

class BuildViewModel : ViewModel() {

  val repository = BuildRepository(viewModelScope)

  val builds: StateFlow<List<BuildItem>> = repository.builds
  val activeBuildId: StateFlow<String?> = repository.activeBuildId
  val notifications: StateFlow<List<BuildNotification>> = repository.notifications
  val dailyQuotaUsed: StateFlow<Int> = repository.dailyQuotaUsed

  // Appearance & Localization
  private val _isDarkMode = MutableStateFlow(true)
  val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

  private val _isArabic = MutableStateFlow(true)
  val isArabic: StateFlow<Boolean> = _isArabic.asStateFlow()

  private val _currentTab = MutableStateFlow(NavigationTab.BUILDER)
  val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

  // Form State
  private val _sourceType = MutableStateFlow(SourceType.WEBSITE)
  val sourceType: StateFlow<SourceType> = _sourceType.asStateFlow()

  private val _appName = MutableStateFlow("تطبيقي الرائع")
  val appName: StateFlow<String> = _appName.asStateFlow()

  private val _selectedPlatform = MutableStateFlow(Platform.WINDOWS)
  val selectedPlatform: StateFlow<Platform> = _selectedPlatform.asStateFlow()

  private val _websiteUrl = MutableStateFlow("https://my-webapp.io")
  val websiteUrl: StateFlow<String> = _websiteUrl.asStateFlow()

  private val _uploadedFileName = MutableStateFlow("portfolio_bundle.zip")
  val uploadedFileName: StateFlow<String> = _uploadedFileName.asStateFlow()

  private val _windowsConfig = MutableStateFlow(WindowsConfig())
  val windowsConfig: StateFlow<WindowsConfig> = _windowsConfig.asStateFlow()

  private val _androidConfig = MutableStateFlow(AndroidConfig())
  val androidConfig: StateFlow<AndroidConfig> = _androidConfig.asStateFlow()

  private val _simulateWarning = MutableStateFlow(false)
  val simulateWarning: StateFlow<Boolean> = _simulateWarning.asStateFlow()

  // Dialogs & Sheets
  private val _showNotifications = MutableStateFlow(false)
  val showNotifications: StateFlow<Boolean> = _showNotifications.asStateFlow()

  private val _selectedArtifactForDetail = MutableStateFlow<BuildItem?>(null)
  val selectedArtifactForDetail: StateFlow<BuildItem?> = _selectedArtifactForDetail.asStateFlow()

  private val _downloadSuccessMessage = MutableStateFlow<String?>(null)
  val downloadSuccessMessage: StateFlow<String?> = _downloadSuccessMessage.asStateFlow()

  fun toggleDarkMode() {
    _isDarkMode.update { !it }
  }

  fun toggleLanguage() {
    _isArabic.update { !it }
  }

  fun setTab(tab: NavigationTab) {
    _currentTab.value = tab
  }

  fun setSourceType(type: SourceType) {
    _sourceType.value = type
  }

  fun setAppName(name: String) {
    _appName.value = name
  }

  fun setPlatform(platform: Platform) {
    _selectedPlatform.value = platform
  }

  fun setWebsiteUrl(url: String) {
    _websiteUrl.value = url
  }

  fun setUploadedFileName(name: String) {
    _uploadedFileName.value = name
  }

  fun updateWindowsConfig(transform: (WindowsConfig) -> WindowsConfig) {
    _windowsConfig.update(transform)
  }

  fun updateAndroidConfig(transform: (AndroidConfig) -> AndroidConfig) {
    _androidConfig.update(transform)
  }

  fun toggleSimulateWarning() {
    _simulateWarning.update { !it }
  }

  fun setShowNotifications(show: Boolean) {
    _showNotifications.value = show
    if (show) {
      repository.markAllNotificationsRead()
    }
  }

  fun setSelectedArtifact(item: BuildItem?) {
    _selectedArtifactForDetail.value = item
  }

  fun triggerBuild(): String {
    val currentPlatform = _selectedPlatform.value
    val currentSource = _sourceType.value
    val sourceVal = if (currentSource == SourceType.WEBSITE) _websiteUrl.value else _uploadedFileName.value

    val buildId = repository.startBuild(
      appName = _appName.value.ifBlank { "Untitled App" },
      platform = currentPlatform,
      sourceType = currentSource,
      sourceValue = sourceVal,
      windowsConfig = if (currentPlatform == Platform.WINDOWS) _windowsConfig.value else null,
      androidConfig = if (currentPlatform == Platform.ANDROID) _androidConfig.value else null,
      forceWarning = _simulateWarning.value
    )

    _currentTab.value = NavigationTab.CONSOLE
    return buildId
  }

  fun cancelBuild(id: String) {
    repository.cancelActiveBuild(id)
  }

  fun retryBuild(id: String) {
    repository.retryBuild(id)
    _currentTab.value = NavigationTab.CONSOLE
  }

  fun deleteBuild(id: String) {
    repository.deleteBuild(id)
  }

  fun simulateDownload(item: BuildItem) {
    _downloadSuccessMessage.value = if (_isArabic.value) {
      "تم تنزيل ${item.fileName} (${item.fileSize}) في مجلد التنزيلات بنجاح!"
    } else {
      "Downloaded ${item.fileName} (${item.fileSize}) to Downloads successfully!"
    }
  }

  fun clearDownloadSuccessMessage() {
    _downloadSuccessMessage.value = null
  }
}
