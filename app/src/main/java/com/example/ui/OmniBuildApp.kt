package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.BuildStatus
import com.example.ui.components.AppTopBar
import com.example.ui.components.ArtifactDetailsDialog
import com.example.ui.components.BuildHistorySection
import com.example.ui.components.BuildProgressTracker
import com.example.ui.components.CreateAppSection
import com.example.ui.components.HowItWorksSection
import com.example.ui.components.NotificationsDialog
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MyApplicationTheme

@Composable
fun OmniBuildApp(
  viewModel: BuildViewModel = viewModel()
) {
  val isDarkMode by viewModel.isDarkMode.collectAsState()
  val isArabic by viewModel.isArabic.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()

  val builds by viewModel.builds.collectAsState()
  val activeBuildId by viewModel.activeBuildId.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val dailyQuotaUsed by viewModel.dailyQuotaUsed.collectAsState()

  val sourceType by viewModel.sourceType.collectAsState()
  val appName by viewModel.appName.collectAsState()
  val selectedPlatform by viewModel.selectedPlatform.collectAsState()
  val websiteUrl by viewModel.websiteUrl.collectAsState()
  val uploadedFileName by viewModel.uploadedFileName.collectAsState()
  val windowsConfig by viewModel.windowsConfig.collectAsState()
  val androidConfig by viewModel.androidConfig.collectAsState()
  val simulateWarning by viewModel.simulateWarning.collectAsState()

  val showNotifications by viewModel.showNotifications.collectAsState()
  val selectedArtifact by viewModel.selectedArtifactForDetail.collectAsState()
  val downloadSuccessMessage by viewModel.downloadSuccessMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  // Handle download toast message
  LaunchedEffect(downloadSuccessMessage) {
    downloadSuccessMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearDownloadSuccessMessage()
    }
  }

  // BackHandler: if not on BUILDER tab, go back to BUILDER
  BackHandler(enabled = currentTab != NavigationTab.BUILDER) {
    viewModel.setTab(NavigationTab.BUILDER)
  }

  val activeBuild = builds.firstOrNull { it.id == activeBuildId } ?: builds.firstOrNull()

  val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    MyApplicationTheme(darkTheme = isDarkMode) {
      Scaffold(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
          .statusBarsPadding(),
        topBar = {
          AppTopBar(
            isDarkMode = isDarkMode,
            isArabic = isArabic,
            unreadNotificationsCount = notifications.count { !it.isRead },
            dailyQuotaUsed = dailyQuotaUsed,
            dailyQuotaTotal = viewModel.repository.dailyQuotaTotal,
            onToggleDarkMode = { viewModel.toggleDarkMode() },
            onToggleLanguage = { viewModel.toggleLanguage() },
            onOpenNotifications = { viewModel.setShowNotifications(true) }
          )
        },
        bottomBar = {
          NavigationBar(
            modifier = Modifier
              .fillMaxWidth()
              .navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
          ) {
            val tabs = listOf(
              NavigationTab.BUILDER to Icons.Default.Build,
              NavigationTab.CONSOLE to Icons.Default.Terminal,
              NavigationTab.HISTORY to Icons.Default.History,
              NavigationTab.HOW_IT_WORKS to Icons.Default.Lightbulb
            )

            tabs.forEach { (tab, icon) ->
              val isSelected = currentTab == tab
              val isBuilding = tab == NavigationTab.CONSOLE &&
                builds.any { it.status == BuildStatus.COMPILING || it.status == BuildStatus.PACKAGING }

              NavigationBarItem(
                selected = isSelected,
                onClick = { viewModel.setTab(tab) },
                icon = {
                  BadgedBox(
                    badge = {
                      if (isBuilding) {
                        Badge(containerColor = EmeraldLight)
                      }
                    }
                  ) {
                    Icon(
                      imageVector = icon,
                      contentDescription = if (isArabic) tab.titleAr else tab.titleEn,
                      modifier = Modifier.size(22.dp)
                    )
                  }
                },
                label = {
                  Text(
                    text = if (isArabic) tab.titleAr else tab.titleEn,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.labelSmall
                  )
                },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = MaterialTheme.colorScheme.primary,
                  selectedTextColor = MaterialTheme.colorScheme.primary,
                  unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
              )
            }
          }
        },
        snackbarHost = {
          SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
              snackbarData = data,
              shape = RoundedCornerShape(12.dp),
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
        ) {
          AnimatedContent(
            targetState = currentTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "TabContent"
          ) { tab ->
            when (tab) {
              NavigationTab.BUILDER -> {
                CreateAppSection(
                  isArabic = isArabic,
                  sourceType = sourceType,
                  appName = appName,
                  selectedPlatform = selectedPlatform,
                  websiteUrl = websiteUrl,
                  uploadedFileName = uploadedFileName,
                  windowsConfig = windowsConfig,
                  androidConfig = androidConfig,
                  simulateWarning = simulateWarning,
                  onSourceTypeChange = { viewModel.setSourceType(it) },
                  onAppNameChange = { viewModel.setAppName(it) },
                  onPlatformChange = { viewModel.setPlatform(it) },
                  onWebsiteUrlChange = { viewModel.setWebsiteUrl(it) },
                  onUploadedFileNameChange = { viewModel.setUploadedFileName(it) },
                  onWindowsConfigChange = { viewModel.updateWindowsConfig(it) },
                  onAndroidConfigChange = { viewModel.updateAndroidConfig(it) },
                  onToggleSimulateWarning = { viewModel.toggleSimulateWarning() },
                  onSubmitBuild = { viewModel.triggerBuild() }
                )
              }

              NavigationTab.CONSOLE -> {
                BuildProgressTracker(
                  build = activeBuild,
                  isArabic = isArabic,
                  onCancelBuild = { viewModel.cancelBuild(it) },
                  onRetryBuild = { viewModel.retryBuild(it) },
                  onDownloadArtifact = { viewModel.simulateDownload(it) }
                )
              }

              NavigationTab.HISTORY -> {
                BuildHistorySection(
                  builds = builds,
                  isArabic = isArabic,
                  onViewDetails = { viewModel.setSelectedArtifact(it) },
                  onDownload = { viewModel.simulateDownload(it) },
                  onViewLogs = { item ->
                    viewModel.setSelectedArtifact(null)
                    viewModel.setTab(NavigationTab.CONSOLE)
                  },
                  onDelete = { viewModel.deleteBuild(it) },
                  onGoToBuilder = { viewModel.setTab(NavigationTab.BUILDER) }
                )
              }

              NavigationTab.HOW_IT_WORKS -> {
                HowItWorksSection(
                  isArabic = isArabic,
                  onGoToBuilder = { viewModel.setTab(NavigationTab.BUILDER) }
                )
              }
            }
          }
        }
      }

      // Dialogs
      if (showNotifications) {
        NotificationsDialog(
          notifications = notifications,
          isArabic = isArabic,
          onDismiss = { viewModel.setShowNotifications(false) },
          onClearAll = { viewModel.repository.clearNotifications() }
        )
      }

      selectedArtifact?.let { item ->
        ArtifactDetailsDialog(
          build = item,
          isArabic = isArabic,
          onDismiss = { viewModel.setSelectedArtifact(null) },
          onDownload = { viewModel.simulateDownload(it) }
        )
      }
    }
  }
}
