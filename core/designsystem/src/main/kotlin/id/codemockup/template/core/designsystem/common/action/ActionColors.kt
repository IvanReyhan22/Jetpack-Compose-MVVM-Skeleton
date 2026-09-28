package id.codemockup.template.core.designsystem.common.action

import androidx.compose.ui.graphics.Color
import id.codemockup.template.core.designsystem.common.enums.AppButtonVariant
import id.codemockup.template.core.designsystem.theme.AppColors

internal data class ActionColors(val background: Color, val content: Color, val border: Color = Color.Transparent)

internal fun actionColors(variant: AppButtonVariant, disabled: Boolean, active: Boolean): ActionColors {
    val white = AppColors.neutral.surface
    val ink = AppColors.primary.onyx
    val ash = AppColors.neutral.ash
    return when (variant) {
        AppButtonVariant.Primary -> ActionColors(
            if (disabled) AppColors.neutral.platinum else if (active) AppColors.primary.pressed else ink,
            if (disabled) AppColors.neutral.inkDisabled else white,
            if (active) ash else Color.Transparent)
        AppButtonVariant.Spark -> ActionColors(
            if (disabled) AppColors.secondary.disabled else if (active) AppColors.secondary.pressed else AppColors.secondary.spark,
            if (disabled) AppColors.secondary.onDisabled else ink,
            if (active) AppColors.secondary.sparkTint else Color.Transparent)
        AppButtonVariant.Secondary -> ActionColors(
            if (active) AppColors.neutral.surfaceSubtle else white,
            if (disabled) ash else ink, if (disabled) AppColors.neutral.platinum else ink)
        AppButtonVariant.Tonal -> ActionColors(
            if (disabled) AppColors.neutral.surfaceDisabled else if (active) AppColors.neutral.platinum else AppColors.neutral.surfaceSubtle,
            if (disabled) ash else ink)
        AppButtonVariant.Text -> ActionColors(
            if (active) AppColors.neutral.surfaceSubtle else Color.Transparent, if (disabled) ash else ink)
        AppButtonVariant.Destructive -> ActionColors(
            if (disabled) AppColors.negative.tint else if (active) AppColors.negative.pressed else AppColors.negative.solid,
            if (disabled) AppColors.negative.onDisabled else white,
            if (active) AppColors.negative.tint else Color.Transparent)
        AppButtonVariant.DestructiveOutline -> ActionColors(
            if (active) AppColors.negative.tint else white,
            if (disabled) AppColors.negative.onDisabled else AppColors.negative.solid,
            if (disabled) AppColors.negative.tint else AppColors.negative.solid)
    }
}
