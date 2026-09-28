package id.codemockup.template.core.designsystem.components.previews

import id.codemockup.template.core.designsystem.common.enums.AppBackgroundVariant
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle

import id.codemockup.template.core.designsystem.components.text.*
import id.codemockup.template.core.designsystem.components.backgrounds.*

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppRadius
import id.codemockup.template.core.designsystem.theme.AppSpacing
import id.codemockup.template.core.designsystem.theme.TemplateTheme

class BackgroundPreviewProvider : PreviewParameterProvider<AppBackgroundVariant> {
    override val values = AppBackgroundVariant.entries.asSequence()
}

@Preview(name = "Background fields", widthDp = 360, heightDp = 640)
@Composable
private fun BackgroundPreview(@PreviewParameter(BackgroundPreviewProvider::class) variant: AppBackgroundVariant) {
    TemplateTheme {
        AppBackground(Modifier.fillMaxSize(), variant) {
            Column(Modifier.padding(AppSpacing.md.md16), verticalArrangement = Arrangement.spacedBy(AppSpacing.md.md16)) {
                AppText(variant.name, style = AppTextStyle.Headline)
                AppText("Background fades to canvas halfway down.", style = AppTextStyle.BodySmall)
            }
        }
    }
}

@Preview(name = "Onest", widthDp = 360, heightDp = 720)
@Preview(name = "Onest large text", widthDp = 360, heightDp = 920, fontScale = 1.5f)
@Composable
private fun TypographyPreview() {
    TemplateTheme {
        Column(
            Modifier.fillMaxSize().background(AppColors.neutral.canvas).verticalScroll(rememberScrollState()).padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md.md16),
        ) {
            AppTextStyle.entries.forEach { style ->
                AppText("${style.name}: Make room for ideas.", style = style)
            }
        }
    }
}

@Preview(name = "Color palette", widthDp = 360, heightDp = 1000)
@Composable
private fun ColorsPreview() {
    val colors = listOf(
        "primary.onyx" to AppColors.primary.onyx,
        "primary.graphite" to AppColors.primary.graphite,
        "neutral.canvas" to AppColors.neutral.canvas,
        "neutral.surface" to AppColors.neutral.surface,
        "neutral.surfaceSubtle" to AppColors.neutral.surfaceSubtle,
        "neutral.inkSecondary" to AppColors.neutral.inkSecondary,
        "neutral.platinum" to AppColors.neutral.platinum,
        "neutral.ash" to AppColors.neutral.ash,
        "neutral.darkField" to AppColors.neutral.darkField,
        "neutral.darkTrack" to AppColors.neutral.darkTrack,
        "neutral.darkMuted" to AppColors.neutral.darkMuted,
        "neutral.darkSoft" to AppColors.neutral.darkSoft,
        "neutral.glowDot" to AppColors.neutral.glowDot,
        "neutral.fieldDot" to AppColors.neutral.fieldDot,
        "neutral.fieldLine" to AppColors.neutral.fieldLine,
        "neutral.scrim" to AppColors.neutral.scrim,
        "secondary.spark" to AppColors.secondary.spark,
        "secondary.sparkTint" to AppColors.secondary.sparkTint,
        "secondary.onSparkTint" to AppColors.secondary.onSparkTint,
        "warning.solid" to AppColors.warning.solid,
        "warning.tint" to AppColors.warning.tint,
        "negative.solid" to AppColors.negative.solid,
        "negative.tint" to AppColors.negative.tint,
        "success.solid" to AppColors.success.solid,
        "success.tint" to AppColors.success.tint,
    )
    TemplateTheme {
        Column(
            Modifier.fillMaxSize().background(AppColors.neutral.canvas).verticalScroll(rememberScrollState()).padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8),
        ) {
            colors.forEach { (name, color) ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm12)) {
                    Box(Modifier.size(AppSpacing.md.md24).background(color).border(1.dp, AppColors.neutral.platinum))
                    AppText(name, style = AppTextStyle.Label)
                }
            }
        }
    }
}

@Preview(name = "Spacing and radius", widthDp = 360, heightDp = 680)
@Composable
private fun DimensionsPreview() {
    TemplateTheme {
        Column(
            Modifier.fillMaxSize().background(AppColors.neutral.canvas).padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm12),
        ) {
            AppText("Spacing", style = AppTextStyle.SectionTitle)
            listOf(
                AppSpacing.sm.sm2, AppSpacing.sm.sm4, AppSpacing.sm.sm8, AppSpacing.sm.sm12,
                AppSpacing.md.md16, AppSpacing.md.md24,
                AppSpacing.lg.lg32, AppSpacing.lg.lg48, AppSpacing.lg.lg64,
            ).forEach { space ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md.md16)) {
                    AppText(space.toString(), Modifier.width(64.dp), style = AppTextStyle.Label)
                    Box(Modifier.width(space).height(12.dp).background(AppColors.primary.graphite))
                }
            }
            AppText("Radius", style = AppTextStyle.SectionTitle)
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)) {
                listOf(AppRadius.small, AppRadius.control, AppRadius.card, AppRadius.feature).forEach { radius ->
                    Box(Modifier.size(64.dp).background(AppColors.neutral.surfaceSubtle, RoundedCornerShape(radius)).border(1.dp, AppColors.neutral.platinum, RoundedCornerShape(radius))) {
                        AppText(radius.value.toInt().toString(), Modifier.padding(12.dp), style = AppTextStyle.Caption)
                    }
                }
            }
            Box(Modifier.width(96.dp).height(40.dp).background(AppColors.secondary.spark, AppRadius.pill)) {
                AppText("Pill", Modifier.padding(12.dp), style = AppTextStyle.Label)
            }
        }
    }
}
