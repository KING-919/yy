package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
  primary = EmeraldLight,
  onPrimary = Color.Black,
  primaryContainer = EmeraldContainerDark,
  onPrimaryContainer = EmeraldGlow,
  secondary = TealAccent,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF134E48),
  onSecondaryContainer = Color(0xFF99F6E4),
  tertiary = EmeraldGlow,
  onTertiary = Color.Black,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  surfaceContainer = DarkSurfaceCard,
  outline = DarkCardBorder,
  outlineVariant = Color(0xFF32363F),
  error = CrimsonError,
  onError = Color.White
)

val LightColorScheme = lightColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.White,
  primaryContainer = EmeraldContainerLight,
  onPrimaryContainer = Color(0xFF064E3B),
  secondary = TealAccent,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFCCFBF1),
  onSecondaryContainer = Color(0xFF115E59),
  tertiary = EmeraldLight,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightTextSecondary,
  surfaceContainer = LightSurfaceCard,
  outline = LightCardBorder,
  outlineVariant = Color(0xFFE5E7EB),
  error = CrimsonError,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}
