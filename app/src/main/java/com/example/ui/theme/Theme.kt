package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = ImperialRedLight,
  onPrimary = Color.White,
  primaryContainer = ImperialRedDark,
  onPrimaryContainer = ImperialRedContainer,
  secondary = JadeBambooLight,
  onSecondary = Color.White,
  secondaryContainer = JadeBambooSecondary,
  onSecondaryContainer = JadeBambooContainer,
  tertiary = ImperialGoldLight,
  onTertiary = Color.Black,
  tertiaryContainer = ImperialGoldTertiary,
  onTertiaryContainer = ImperialGoldContainer,
  background = InkDarkBackground,
  onBackground = OnInkDarkText,
  surface = InkDarkSurface,
  onSurface = OnInkDarkText,
  surfaceVariant = InkDarkSurfaceVariant,
  onSurfaceVariant = OnInkDarkMuted
)

private val LightColorScheme = lightColorScheme(
  primary = ImperialRedPrimary,
  onPrimary = Color.White,
  primaryContainer = ImperialRedContainer,
  onPrimaryContainer = OnImperialRedContainer,
  secondary = JadeBambooSecondary,
  onSecondary = Color.White,
  secondaryContainer = JadeBambooContainer,
  onSecondaryContainer = OnJadeBambooContainer,
  tertiary = ImperialGoldTertiary,
  onTertiary = Color.White,
  tertiaryContainer = ImperialGoldContainer,
  onTertiaryContainer = OnImperialGoldContainer,
  background = WarmParchmentBackground,
  onBackground = OnWarmParchmentText,
  surface = WarmParchmentSurface,
  onSurface = OnWarmParchmentText,
  surfaceVariant = WarmParchmentSurfaceVariant,
  onSurfaceVariant = OnWarmParchmentMuted
)

@Composable
fun HanyuMateTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted Chinese aesthetics by default
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
