package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkSoulsColorScheme = darkColorScheme(
    primary = BonfireFlame,
    onPrimary = AshBlack,
    primaryContainer = StoneElevated,
    onPrimaryContainer = BonfireGlow,
    secondary = EstusYellow,
    onSecondary = AshBlack,
    secondaryContainer = StoneElevated,
    onSecondaryContainer = AncientParchment,
    tertiary = SoulCyan,
    onTertiary = AshBlack,
    background = AshBlack,
    onBackground = AncientParchment,
    surface = ObsidianDark,
    onSurface = AncientParchment,
    surfaceVariant = StoneSurface,
    onSurfaceVariant = WeatheredSteel,
    outline = StoneElevated,
    error = HollowRed
)

@Composable
fun DarkSoulsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkSoulsColorScheme,
        typography = DarkSoulsTypography,
        content = content
    )
}
