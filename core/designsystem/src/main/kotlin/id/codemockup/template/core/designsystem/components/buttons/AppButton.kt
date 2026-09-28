package id.codemockup.template.core.designsystem.components.buttons

import id.codemockup.template.core.designsystem.common.enums.AppButtonVariant
import id.codemockup.template.core.designsystem.common.enums.AppButtonSize
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.components.text.AppText

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.template.core.designsystem.common.action.actionColors
import id.codemockup.template.core.designsystem.theme.*


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    size: AppButtonSize = AppButtonSize.Medium,
    loadingLabel: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val focused by interactions.collectIsFocusedAsState()
    val active = enabled && !loading && (pressed || focused)
    val colors = actionColors(variant, !enabled, active)
    val background by animateColorAsState(
        colors.background,
        AppMotion.tween(AppMotion.fast),
        label = "button background"
    )
    val content by animateColorAsState(
        colors.content,
        AppMotion.tween(AppMotion.fast),
        label = "button content"
    )
    val height = when (size) {
        AppButtonSize.Small -> AppControlTokens.buttonSmall
        AppButtonSize.Medium -> AppControlTokens.buttonMedium
        AppButtonSize.Large -> AppControlTokens.buttonLarge
    }
    val radius = when (size) {
        AppButtonSize.Small -> AppRadius.controlSmall
        AppButtonSize.Medium -> AppRadius.control
        AppButtonSize.Large -> AppRadius.controlLarge
    }
    val padding = if (variant == AppButtonVariant.Text) AppSpacing.sm.sm8 else when (size) {
        AppButtonSize.Small -> AppSpacing.sm.sm12
        AppButtonSize.Medium -> AppSpacing.md.md16
        AppButtonSize.Large -> AppSpacing.md.md24
    }
    val typography = when (size) {
        AppButtonSize.Small -> AppControlTokens.smallLabel
        AppButtonSize.Medium -> AppTypography.label
        AppButtonSize.Large -> AppTypography.titleSmall
    }.let { if (variant == AppButtonVariant.Spark) it.copy(fontWeight = FontWeight.Bold) else it }
    val ring = active && variant in listOf(
        AppButtonVariant.Primary,
        AppButtonVariant.Spark,
        AppButtonVariant.Destructive
    )
    Button(
        onClick = onClick,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .heightIn(min = height)
            .semantics {
                if (loading) progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
        enabled = enabled && !loading,
        shape = RoundedCornerShape(radius),
        colors = ButtonDefaults.buttonColors(background, content, background, content),
        border = if (colors.border != Color.Transparent) BorderStroke(
            if (ring) AppControlTokens.focusRing else AppControlTokens.border, colors.border
        ) else null,
        contentPadding = PaddingValues(horizontal = padding, vertical = AppSpacing.sm.sm8),
        interactionSource = interactions,
    ) {
        if (loading) {
            CircularProgressIndicator(
                Modifier
                    .size(AppControlTokens.iconSmall)
                    .clearAndSetSemantics {},
                color = content, strokeWidth = AppControlTokens.activeBorder
            )
            Spacer(Modifier.width(AppSpacing.sm.sm8))
        } else if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(AppSpacing.sm.sm8))
        }
        AppText(if (loading) loadingLabel ?: text else text, style = AppTextStyle.Label, textStyle = typography)
        if (!loading && trailingIcon != null) {
            Spacer(Modifier.width(AppSpacing.sm.sm8))
            trailingIcon()
        }
    }
}

@Preview(showBackground = true, widthDp = 420, heightDp = 1250)
@Composable
private fun AppButtonPreview() {
    TemplateTheme {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
        ) {
            AppButtonVariant.entries.forEach { variant ->
                AppText(variant.name, style = AppTextStyle.TitleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)) {
                    AppButton("Continue", {}, variant = variant)
                    AppButton("Continue", {}, variant = variant, enabled = false)
                }
                AppButton("Continue", {}, variant = variant, loading = true, loadingLabel = "Working…")
            }
            AppButtonSize.entries.forEach { size -> AppButton(size.name, {}, size = size) }
        }
    }
}
