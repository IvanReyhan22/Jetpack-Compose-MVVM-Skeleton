package id.codemockup.template.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = AppColors.primary.onyx, onPrimary = AppColors.neutral.surface,
    primaryContainer = AppColors.neutral.surfaceSubtle, onPrimaryContainer = AppColors.primary.onyx,
    inversePrimary = AppColors.secondary.spark,
    secondary = AppColors.secondary.spark, onSecondary = AppColors.primary.onyx,
    secondaryContainer = AppColors.secondary.sparkTint, onSecondaryContainer = AppColors.secondary.onSparkTint,
    tertiary = AppColors.primary.graphite, onTertiary = AppColors.neutral.surface,
    tertiaryContainer = AppColors.neutral.surfaceSubtle, onTertiaryContainer = AppColors.primary.graphite,
    background = AppColors.neutral.canvas, onBackground = AppColors.primary.onyx,
    surface = AppColors.neutral.surface, onSurface = AppColors.primary.onyx,
    surfaceVariant = AppColors.neutral.surfaceSubtle, onSurfaceVariant = AppColors.neutral.inkSecondary,
    surfaceTint = AppColors.neutral.surface,
    inverseSurface = AppColors.primary.graphite, inverseOnSurface = AppColors.neutral.surface,
    error = AppColors.negative.solid, onError = AppColors.neutral.surface,
    errorContainer = AppColors.negative.tint, onErrorContainer = AppColors.negative.solid,
    outline = AppColors.neutral.ash, outlineVariant = AppColors.neutral.platinum,
    scrim = AppColors.neutral.scrim,
    surfaceBright = AppColors.neutral.surface, surfaceDim = AppColors.neutral.surfaceSubtle,
    surfaceContainerLowest = AppColors.neutral.surface, surfaceContainerLow = AppColors.neutral.canvas,
    surfaceContainer = AppColors.neutral.surface, surfaceContainerHigh = AppColors.neutral.surfaceSubtle,
    surfaceContainerHighest = AppColors.neutral.surfaceSubtle,
)

@Composable
fun TemplateTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content,
    )
}
